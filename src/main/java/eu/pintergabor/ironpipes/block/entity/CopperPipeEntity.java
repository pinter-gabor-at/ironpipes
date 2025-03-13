package eu.pintergabor.ironpipes.block.entity;

import java.util.ArrayList;

import eu.pintergabor.ironpipes.block.CopperFitting;
import eu.pintergabor.ironpipes.block.CopperPipe;
import eu.pintergabor.ironpipes.block.entity.base.BaseBlockEntity;
import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeManager;
import eu.pintergabor.ironpipes.block.entity.nbt.MoveablePipeDataHandler;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.config.ModConfig;
import eu.pintergabor.ironpipes.registry.CopperPipeDispenseBehaviors;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import eu.pintergabor.ironpipes.registry.ModBlockStateProperties;
import eu.pintergabor.ironpipes.registry.ModSoundEvents;
import eu.pintergabor.ironpipes.tag.ModBlockTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;


public class CopperPipeEntity extends BaseBlockEntity {
    private static final int MAX_TRANSFER_AMOUNT = 1;
    public int transferCooldown;
    public int dispenseCooldown;
    public boolean canDispense;
    public boolean shootsControlled;
    public boolean shootsSpecial;
    public boolean canAccept;

    public CopperPipeEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.COPPER_PIPE_ENTITY, blockPos, blockState, MoveType.FROM_PIPE);
    }

    public static boolean canTransfer(
        World world, BlockPos pos, boolean to, @NotNull CopperPipeEntity copperPipe,
        @Nullable Storage<ItemVariant> inventory, @Nullable Storage<ItemVariant> pipeInventory) {
        if (0 < copperPipe.transferCooldown) {
            return false;
        }
        boolean transferApiCheck = true;
        boolean usingTransferApi = false;
        if (inventory != null) {
            usingTransferApi = true;
            transferApiCheck = to
                ? inventory.supportsInsertion() &&
                (pipeInventory == null || pipeInventory.supportsExtraction())
                : inventory.supportsExtraction() &&
                (pipeInventory == null || pipeInventory.supportsInsertion());
        }
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity != null) {
            if (entity instanceof CopperPipeEntity pipe) {
                return (to || pipe.transferCooldown <= 0) && transferApiCheck;
            }
            if (entity instanceof CopperFittingEntity) {
                return false;
            }
        }
        return usingTransferApi && transferApiCheck;
    }

    public static long addItem(ItemVariant resource, @NotNull Storage<ItemVariant> inventory, Transaction transaction) {
        if (inventory.supportsInsertion()) {
            return inventory.insert(resource, MAX_TRANSFER_AMOUNT, transaction);
        }
        return 0L;
    }

    public static void spawnItem(
        World world, ItemStack itemStack, int shotLength,
        @NotNull Direction direction, @NotNull Vec3d vec3, Direction facing) { //Simply Spawn An Item
        // Position.
        double x = vec3.getX();
        double y = vec3.getY() -
            (direction.getAxis() == Direction.Axis.Y ? 0.125 : 0.15625);
        double z = vec3.getZ();
        // Speed.
        Direction.Axis axis = facing.getAxis();
        double vx = axis == Direction.Axis.X ? (shotLength * facing.getOffsetX()) * 0.1 : 0;
        double vy = axis == Direction.Axis.Y ? (shotLength * facing.getOffsetY()) * 0.1 : 0;
        double vz = axis == Direction.Axis.Z ? (shotLength * facing.getOffsetZ()) * 0.1 : 0;
        // Spawn it.
        ItemEntity itemEntity = new ItemEntity(world, x, y, z, itemStack);
        itemEntity.setVelocity(vx, vy, vz);
        // Notify the world.
        world.spawnEntity(itemEntity);
    }

    public static Storage<ItemVariant> getStorageAt(World world, BlockPos blockPos, Direction direction) {
        return ItemStorage.SIDED.find(
            world, blockPos, world.getBlockState(blockPos), world.getBlockEntity(blockPos), direction);
    }

    @Override
    public void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState) {
        super.serverTick(world, blockPos, blockState);
        if (!world.isClient()) {
            // Dispensing.
            if (0 < dispenseCooldown) {
                dispenseCooldown--;
            } else {
                dispense((ServerWorld) world, blockPos, blockState);
                int i = 0;
                if (world.getBlockState(blockPos.offset(blockState.get(Properties.FACING)
                    .getOpposite())).getBlock() instanceof CopperFitting fitting) {
                    i = fitting.cooldown;
                } else {
                    if (blockState.getBlock() instanceof CopperPipe pipe) {
                        i = MathHelper.floor(pipe.cooldown * 0.5);
                    }
                }
                dispenseCooldown = i;
            }
            // Transfering.
            if (0 < transferCooldown) {
                transferCooldown--;
            } else {
                pipeMove(world, blockPos, blockState);
            }
            if (blockState.get(ModBlockStateProperties.FLUID) == PipeFluid.WATER &&
                blockState.get(Properties.FACING) != Direction.UP) {
                LeakingPipeManager.addPos(world, blockPos);
            }
        }
    }

//    @Override
//    public void updateBlockEntityValues(World level, BlockPos pos, @NotNull BlockState state) {
//        if (state.getBlock() instanceof CopperPipe) {
//            Direction direction = state.get(Properties.FACING);
//            Direction directionOpp = direction.getOpposite();
//            Block dirBlock = level.getBlockState(pos.offset(direction)).getBlock();
//            BlockState oppState = level.getBlockState(pos.offset(directionOpp));
//            Block oppBlock = oppState.getBlock();
//            this.canDispense = (dirBlock == Blocks.AIR || dirBlock == Blocks.WATER) &&
//                (oppBlock != Blocks.AIR && oppBlock != Blocks.WATER);
//            this.shootsControlled = oppBlock == Blocks.DROPPER;
//            this.shootsSpecial = oppBlock == Blocks.DISPENSER;
//            this.canAccept = !(
//                oppBlock instanceof CopperPipe ||
//                    oppBlock instanceof CopperFitting ||
//                    oppState.isSolidBlock(level, pos));
//            this.canWater = (oppBlock == Blocks.WATER || state.get(Properties.WATERLOGGED) ||
//                (oppState.contains(Properties.WATERLOGGED) ? oppState.get(Properties.WATERLOGGED) : false)) &&
//                SimpleCopperPipesConfig.get().carryWater;
//            this.canLava =
//                oppBlock == Blocks.LAVA &&
//                    SimpleCopperPipesConfig.get().carryLava;
//            if (this.canWater && this.canLava) {
//                this.canWater = false;
//                this.canLava = false;
//            }
//        }
//    }

    public void pipeMove(World world, BlockPos blockPos, @NotNull BlockState blockState) {
        Direction facing = blockState.get(Properties.FACING);
        boolean bl1 = this.moveOut(world, blockPos, facing);
        int bl2 = this.moveIn(world, blockPos, blockState, facing);
        if (bl1 || bl2 >= 2) {
            setCooldown(blockState);
            markDirty(world, blockPos, blockState);
            if (bl2 == 3) {
                if (ModConfig.get().suctionSounds) {
                    world.playSound(
                        null, blockPos, ModSoundEvents.ITEM_IN,
                        SoundCategory.BLOCKS, 0.2F, (world.random.nextFloat() * 0.25F) + 0.8F);
                }
            }
        }
    }

    /**
     * Pull item(s) from the inventory at the back of the pipe.
     *
     * @return 0=failed, 2=pulled from another pipe, 3=pulled from an inventory
     */
    private int moveIn(World world, @NotNull BlockPos pipePos, BlockState pipeState, @NotNull Direction facing) {
        int result = 0;
        Direction opposite = facing.getOpposite();
        BlockPos sourcePos = pipePos.offset(opposite);
        Storage<ItemVariant> sourceInventory = getStorageAt(world, sourcePos, facing);
        Storage<ItemVariant> pipeInventory = getStorageAt(world, pipePos, opposite);
        if (sourceInventory != null && pipeInventory != null &&
            canTransfer(world, sourcePos, false, this, sourceInventory, pipeInventory)) {
            for (StorageView<ItemVariant> storageView : sourceInventory) {
                if (!storageView.isResourceBlank() && 0 < storageView.getAmount()) {
                    Transaction transaction = Transaction.openOuter();
                    ItemVariant resource = storageView.getResource();
                    long extracted = sourceInventory.extract(resource, MAX_TRANSFER_AMOUNT, transaction);
                    // If successfully extracted.
                    if (0 < extracted) {
                        long inserted = addItem(resource, pipeInventory, transaction);
                        // And if successfully inserted.
                        if (0 < inserted) {
                            // Then apply changes.
                            transaction.commit();
                            if (pipeState.isIn(ModBlockTags.SILENT_PIPES)) {
                                result = 2;
                            } else {
                                Block block = world.getBlockState(sourcePos).getBlock();
                                if (block instanceof CopperPipe || block instanceof CopperFitting) {
                                    result = 2;
                                } else {
                                    result = 3;
                                }
                            }
                        }
                    }
                    // Close transaction.
                    transaction.close();
                    // One type of item at a time.
                    if (result != 0) break;
                }
            }
        }
        return result;
    }

    /**
     * Push item(s) to the inventory in front of the pipe.
     *
     * @return false=failed, true=success
     */
    private boolean moveOut(World world, @NotNull BlockPos blockPos, Direction facing) {
        boolean result = false;
        BlockPos offsetPos = blockPos.offset(facing);
        Storage<ItemVariant> inventory = getStorageAt(world, offsetPos, facing.getOpposite());
        Storage<ItemVariant> pipeInventory = getStorageAt(world, blockPos, facing);
        if (inventory != null && pipeInventory != null &&
            canTransfer(world, offsetPos, true, this, inventory, pipeInventory)) {
            boolean canMove = true;
            BlockState state = world.getBlockState(offsetPos);
            if (state.getBlock() instanceof CopperPipe) {
                canMove = state.get(Properties.FACING) != facing;
            }
            if (canMove) {
                for (StorageView<ItemVariant> storageView : pipeInventory) {
                    if (!storageView.isResourceBlank() && 0 < storageView.getAmount()) {
                        Transaction transaction = Transaction.openOuter();
                        var resource = storageView.getResource();
                        long inserted = inventory.insert(resource, MAX_TRANSFER_AMOUNT, transaction);
                        // If sucessfully inserted the item.
                        if (0 < inserted) {
                            long extracted = pipeInventory.extract(resource, MAX_TRANSFER_AMOUNT, transaction);
                            // And if successfully extracted the item.
                            if (0 < extracted) {
                                // Apply changes.
                                transaction.commit();
                                result = true;
                            }
                        }
                        // Close the transaction.
                        transaction.close();
                        // One type of item at a time.
                        if (result) break;
                    }
                }
            }
        }
        return result;
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean dispense(ServerWorld serverWorld, BlockPos blockPos, @NotNull BlockState blockState) {
        Direction direction = blockState.get(Properties.FACING);
        Direction directionOpp = direction.getOpposite();
        //boolean powered = blockState.get(CopperPipe.POWERED);
        if (canDispense) {
            int slot = chooseNonEmptySlot(serverWorld.random);
            if (0 <= slot) {
                ItemStack itemStack = getStack(slot);
                if (!itemStack.isEmpty()) {
                    ItemStack itemStack2;
                    int shotLength = 4;
                    if (shootsControlled) { //If Dropper
                        shotLength = 10;
                        ModSoundEvents.playDispenseSound(serverWorld, blockPos);
                    } else if (shootsSpecial) { //If Dispenser, Use Pipe-Specific Launch Length
                        if (blockState.getBlock() instanceof CopperPipe pipe) {
                            shotLength = pipe.dispenseShotLength;
                            ModSoundEvents.playDispenseSound(serverWorld, blockPos);
                        } else {
                            shotLength = 12;
                        }
                    }
                    boolean silent = blockState.isIn(ModBlockTags.SILENT_PIPES);
                    if (serverWorld.getBlockState(blockPos.offset(directionOpp)).getBlock() instanceof CopperFitting) {
                        itemStack2 = canonShoot(serverWorld, blockPos, itemStack, blockState, shotLength, true, silent);
                    } else {
                        itemStack2 = canonShoot(serverWorld, blockPos, itemStack, blockState, shotLength, false, silent);
                        serverWorld.syncWorldEvent(WorldEvents.CRAFTER_SHOOTS, blockPos, direction.getId());
                    }
                    setStack(slot, itemStack2);
                    return true;
                }
            }
        }
        return false;
    }

    private ItemStack canonShoot(
        ServerWorld serverWorld, @NotNull BlockPos pos, ItemStack itemStack, @NotNull
        BlockState state, int shotLength, boolean fitting, boolean silent) {
        Direction direction = state.get(Properties.FACING);
        Vec3d vec3 = CopperPipe.getOutputLocation(pos, direction);
        ItemStack itemStack2 = itemStack;
        if (ModConfig.get().dispensing) {
            itemStack2 = itemStack.split(1);
            serverWorld.syncWorldEvent(WorldEvents.DISPENSER_ACTIVATED, pos, direction.getId());
            spawnItem(serverWorld, itemStack2, shotLength, direction, vec3, direction);
            if (!silent) {
                serverWorld.emitGameEvent(null, GameEvent.ENTITY_PLACE, pos);
                if (ModConfig.get().dispenseSounds) {
                    serverWorld.playSound(
                        null, pos, ModSoundEvents.ITEM_OUT,
                        SoundCategory.BLOCKS, 0.2F, (serverWorld.random.nextFloat() * 0.25F) + 0.8F);
                }
            }
        }
        return itemStack;
    }

    public void setCooldown(@NotNull BlockState state) {
        int i = 2;
        if (state.getBlock() instanceof CopperPipe pipe) {
            i = pipe.cooldown;
        }
        transferCooldown = i;
    }

    @Override
    public void readNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookupProvider) {
        super.readNbt(nbtCompound, lookupProvider);
        transferCooldown = nbtCompound.getInt("transferCooldown");
        dispenseCooldown = nbtCompound.getInt("dispenseCooldown");
        canDispense = nbtCompound.getBoolean("canDispense");
        shootsControlled = nbtCompound.getBoolean("shootsControlled");
        shootsSpecial = nbtCompound.getBoolean("shootsSpecial");
        canAccept = nbtCompound.getBoolean("canAccept");
    }

    @Override
    protected void writeNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookupProvider) {
        super.writeNbt(nbtCompound, lookupProvider);
        nbtCompound.putInt("transferCooldown", transferCooldown);
        nbtCompound.putInt("dispenseCooldown", dispenseCooldown);
        nbtCompound.putBoolean("canDispense", canDispense);
        nbtCompound.putBoolean("shootsControlled", shootsControlled);
        nbtCompound.putBoolean("shootsSpecial", shootsSpecial);
        nbtCompound.putBoolean("canAccept", canAccept);
    }

    @Override
    public boolean canAcceptMoveableNbt(MoveType moveType, Direction moveDirection, BlockState fromState) {
        if (moveType == MoveType.FROM_FITTING) {
            return getCachedState().get(Properties.FACING) == moveDirection;
        }
        return getCachedState().get(Properties.FACING) == moveDirection ||
            moveDirection == fromState.get(Properties.FACING);
    }

    @Override
    public boolean canMoveNbtInDirection(Direction direction, @NotNull BlockState state) {
        return direction != state.get(Properties.FACING).getOpposite();
    }

    @Override
    public void dispenseMoveableNbt(ServerWorld serverWorld, BlockPos blockPos, BlockState blockState) {
        if (canDispense) {
            ArrayList<MoveablePipeDataHandler.SaveableMovablePipeNbt> nbtList =
                moveablePipeDataHandler.getSavedNbtList();
            if (!nbtList.isEmpty()) {
                for (MoveablePipeDataHandler.SaveableMovablePipeNbt nbt : nbtList) {
                    if (nbt.getShouldMove()) {
                        nbt.dispense(serverWorld, blockPos, blockState, this);
                    }
                }
                moveMoveableNbt(serverWorld, blockPos, blockState);
            }
        }
    }
}
