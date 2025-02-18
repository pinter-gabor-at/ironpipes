package eu.pintergabor.ironpipes.block.entity;

import java.util.ArrayList;

import eu.pintergabor.ironpipes.block.CopperFitting;
import eu.pintergabor.ironpipes.block.CopperPipe;
import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeManager;
import eu.pintergabor.ironpipes.block.entity.nbt.MoveablePipeDataHandler;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.config.SimpleCopperPipesConfig;
import eu.pintergabor.ironpipes.registry.CopperPipeDispenseBehaviors;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import eu.pintergabor.ironpipes.registry.ModBlockStateProperties;
import eu.pintergabor.ironpipes.registry.ModSoundEvents;
import eu.pintergabor.ironpipes.registry.PipeMovementRestrictions;
import eu.pintergabor.ironpipes.tag.ModBlockTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CampfireBlock;
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
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

public class CopperPipeEntity extends AbstractModBlockEntity {
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
        if (copperPipe.transferCooldown > 0) {
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
            if (!world.isClient()) {
                ServerWorld serverWorld = (ServerWorld) world;
                if (to) {
                    PipeMovementRestrictions.CanTransferTo<BlockEntity> canTransfer =
                        PipeMovementRestrictions.getCanTransferTo(entity);
                    if (canTransfer != null) {
                        return canTransfer.canTransfer(
                            serverWorld, pos, world.getBlockState(pos), copperPipe, entity) &&
                            transferApiCheck;
                    }
                } else {
                    PipeMovementRestrictions.CanTakeFrom<BlockEntity> canTake =
                        PipeMovementRestrictions.getCanTakeFrom(entity);
                    if (canTake != null) {
                        return canTake.canTake(
                            serverWorld, pos, world.getBlockState(pos), copperPipe, entity) &&
                            transferApiCheck;
                    }
                }
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
        World level, ItemStack itemStack, int shotLength,
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
        ItemEntity itemEntity = new ItemEntity(level, x, y, z, itemStack);
        itemEntity.setVelocity(vx, vy, vz);
        // Notify the world.
        level.spawnEntity(itemEntity);
    }

    public static Storage<ItemVariant> getStorageAt(World level, BlockPos blockPos, Direction direction) {
        return ItemStorage.SIDED.find(level, blockPos, level.getBlockState(blockPos), level.getBlockEntity(blockPos), direction);
    }

    @Override
    public void setStack(int i, ItemStack itemStack) {
        this.generateLoot(null);
        if (itemStack != null) {
            this.getHeldStacks().set(i, itemStack);
            if (itemStack.getCount() > this.getMaxCountPerStack()) {
                itemStack.setCount(this.getMaxCountPerStack());
            }
        }
    }

    @Override
    public void serverTick(@NotNull World level, BlockPos blockPos, BlockState blockState) {
        super.serverTick(level, blockPos, blockState);
        if (!level.isClient()) {
            if (this.dispenseCooldown > 0) {
                --this.dispenseCooldown;
            } else {
                this.dispense((ServerWorld) level, blockPos, blockState);
                int i = 0;
                if (level.getBlockState(blockPos.offset(blockState.get(Properties.FACING)
                    .getOpposite())).getBlock() instanceof CopperFitting fitting) {
                    i = fitting.cooldown;
                } else {
                    if (blockState.getBlock() instanceof CopperPipe pipe) {
                        i = MathHelper.floor(pipe.cooldown * 0.5);
                    }
                }
                this.dispenseCooldown = i;
            }

            if (this.transferCooldown > 0) {
                --this.transferCooldown;
            } else {
                this.pipeMove(level, blockPos, blockState);
            }
            if (blockState.get(ModBlockStateProperties.FLUID) == PipeFluid.WATER &&
                blockState.get(Properties.FACING) != Direction.UP) {
                LeakingPipeManager.addPos(level, blockPos);
            }
        }
    }

    @Override
    public void updateBlockEntityValues(World level, BlockPos pos, @NotNull BlockState state) {
        if (state.getBlock() instanceof CopperPipe) {
            Direction direction = state.get(Properties.FACING);
            Direction directionOpp = direction.getOpposite();
            Block dirBlock = level.getBlockState(pos.offset(direction)).getBlock();
            BlockState oppState = level.getBlockState(pos.offset(directionOpp));
            Block oppBlock = oppState.getBlock();
            this.canDispense = (dirBlock == Blocks.AIR || dirBlock == Blocks.WATER) &&
                (oppBlock != Blocks.AIR && oppBlock != Blocks.WATER);
            this.shootsControlled = oppBlock == Blocks.DROPPER;
            this.shootsSpecial = oppBlock == Blocks.DISPENSER;
            this.canAccept = !(
                oppBlock instanceof CopperPipe ||
                    oppBlock instanceof CopperFitting ||
                    oppState.isSolidBlock(level, pos));
            this.canWater = (oppBlock == Blocks.WATER || state.get(Properties.WATERLOGGED) ||
                (oppState.contains(Properties.WATERLOGGED) ? oppState.get(Properties.WATERLOGGED) : false)) &&
                SimpleCopperPipesConfig.get().carryWater;
            this.canLava =
                oppBlock == Blocks.LAVA &&
                    SimpleCopperPipesConfig.get().carryLava;
            boolean canWaterAndLava = this.canWater && this.canLava;
            boolean canWaterOrLava = this.canWater || this.canLava;
            this.canSmoke = (oppBlock instanceof CampfireBlock &&
                !canWaterOrLava ? oppState.get(Properties.LIT) : canWaterAndLava) &&
                SimpleCopperPipesConfig.get().carrySmoke;
            if (canWaterAndLava) {
                this.canWater = false;
                this.canLava = false;
            }
        }
    }

    public void pipeMove(World level, BlockPos blockPos, @NotNull BlockState blockState) {
        Direction facing = blockState.get(Properties.FACING);
        boolean bl1 = this.moveOut(level, blockPos, facing);
        int bl2 = this.moveIn(level, blockPos, blockState, facing);
        if (bl1 || bl2 >= 2) {
            setCooldown(blockState);
            markDirty(level, blockPos, blockState);
            if (bl2 == 3) {
                if (SimpleCopperPipesConfig.get().suctionSounds) {
                    level.playSound(
                        null, blockPos, ModSoundEvents.ITEM_IN,
                        SoundCategory.BLOCKS, 0.2f, (level.random.nextFloat() * 0.25f) + 0.8f);
                }
            }
        }
    }

    private int moveIn(World level, @NotNull BlockPos blockPos, BlockState blockState, @NotNull Direction facing) {
        int result = 0;
        Direction opposite = facing.getOpposite();
        BlockPos offsetOppPos = blockPos.offset(opposite);
        Storage<ItemVariant> inventory = getStorageAt(level, offsetOppPos, facing);
        Storage<ItemVariant> pipeInventory = getStorageAt(level, blockPos, opposite);
        if (inventory != null &&
            pipeInventory != null &&
            canTransfer(level, offsetOppPos, false, this, inventory, pipeInventory)) {
            for (StorageView<ItemVariant> storageView : inventory) {
                if (!storageView.isResourceBlank() && storageView.getAmount() > 0) {
                    Transaction transaction = Transaction.openOuter();
                    ItemVariant resource = storageView.getResource();
                    long extracted = inventory.extract(resource, MAX_TRANSFER_AMOUNT, transaction);
                    // If successfully extracted.
                    if (0 < extracted) {
                        long inserted = addItem(resource, pipeInventory, transaction);
                        // And if successfully inserted.
                        if (0 < inserted) {
                            // then apply changes.
                            transaction.commit();
                            if (blockState.isIn(ModBlockTags.SILENT_PIPES)) {
                                result = 2;
                            } else {
                                Block block = level.getBlockState(offsetOppPos).getBlock();
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
                }
            }
        }
        return result;
    }

    private boolean moveOut(World level, @NotNull BlockPos blockPos, Direction facing) {
        boolean result = false;
        BlockPos offsetPos = blockPos.offset(facing);
        Storage<ItemVariant> inventory = getStorageAt(level, offsetPos, facing.getOpposite());
        Storage<ItemVariant> pipeInventory = getStorageAt(level, blockPos, facing);
        if (inventory != null && pipeInventory != null && canTransfer(level, offsetPos, true, this, inventory, pipeInventory)) {
            boolean canMove = true;
            BlockState state = level.getBlockState(offsetPos);
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
        boolean powered = blockState.get(CopperPipe.POWERED);
        if (this.canDispense) {
            int slot = this.chooseNonEmptySlot(serverWorld.random);
            if (slot >= 0) {
                ItemStack itemStack = this.getStack(slot);
                if (!itemStack.isEmpty()) {
                    ItemStack itemStack2;
                    int shotLength = 4;
                    if (this.shootsControlled) { //If Dropper
                        shotLength = 10;
                        if (SimpleCopperPipesConfig.get().dispenseSounds) {
                            serverWorld.playSound(
                                null, blockPos, ModSoundEvents.LAUNCH,
                                SoundCategory.BLOCKS, 0.2f, (serverWorld.random.nextFloat() * 0.25f) + 0.8f);
                        }
                    } else if (this.shootsSpecial) { //If Dispenser, Use Pipe-Specific Launch Length
                        if (blockState.getBlock() instanceof CopperPipe pipe) {
                            shotLength = pipe.dispenseShotLength;
                            if (SimpleCopperPipesConfig.get().dispenseSounds) {
                                serverWorld.playSound(
                                    null, blockPos, ModSoundEvents.LAUNCH,
                                    SoundCategory.BLOCKS, 0.2f, (serverWorld.random.nextFloat() * 0.25f) + 0.8f);
                            }
                        } else {
                            shotLength = 12;
                        }
                    }
                    boolean silent = blockState.isIn(ModBlockTags.SILENT_PIPES);
                    if (serverWorld.getBlockState(blockPos.offset(directionOpp)).getBlock() instanceof CopperFitting) {
                        itemStack2 = canonShoot(serverWorld, blockPos, itemStack, blockState, shotLength, powered, true, silent);
                    } else {
                        itemStack2 = canonShoot(serverWorld, blockPos, itemStack, blockState, shotLength, powered, false, silent);
                        serverWorld.syncWorldEvent(WorldEvents.CRAFTER_SHOOTS, blockPos, direction.getId());
                    }
                    this.setStack(slot, itemStack2);
                    return true;
                }
            }
        }
        return false;
    }

    private ItemStack canonShoot(
        ServerWorld serverWorld, @NotNull BlockPos pos, ItemStack itemStack, @NotNull
        BlockState state, int shotLength, boolean powered, boolean fitting, boolean silent) {
        Direction direction = state.get(Properties.FACING);
        Vec3d vec3 = CopperPipe.getOutputLocation(pos, direction);
        ItemStack itemStack2 = itemStack;
        // If powered.
        if (powered) {
            CopperPipeDispenseBehaviors.PoweredDispense poweredDispense =
                CopperPipeDispenseBehaviors.getDispense(itemStack2.getItem());
            if (poweredDispense != null) {
                itemStack2 = itemStack.split(1);
                poweredDispense.dispense(serverWorld, itemStack2, shotLength, direction, vec3, state, pos, this);
                if (!fitting && !silent) {
                    if (SimpleCopperPipesConfig.get().dispenseSounds) {
                        serverWorld.playSound(
                            null, pos, ModSoundEvents.ITEM_OUT,
                            SoundCategory.BLOCKS, 0.2F, (serverWorld.random.nextFloat() * 0.25F) + 0.8F);
                    }
                    serverWorld.emitGameEvent(null, GameEvent.ENTITY_PLACE, pos);
                }
                return itemStack;
            }
        }
        if (SimpleCopperPipesConfig.get().dispensing) {
            itemStack2 = itemStack.split(1);
            serverWorld.syncWorldEvent(WorldEvents.DISPENSER_ACTIVATED, pos, direction.getId());
            spawnItem(serverWorld, itemStack2, shotLength, direction, vec3, direction);
            if (!silent) {
                serverWorld.emitGameEvent(null, GameEvent.ENTITY_PLACE, pos);
                if (SimpleCopperPipesConfig.get().dispenseSounds) {
                    serverWorld.playSound(
                        null, pos, ModSoundEvents.ITEM_OUT,
                        SoundCategory.BLOCKS, 0.2F, (serverWorld.random.nextFloat() * 0.25F) + 0.8F);
                }
            }
        }
        return itemStack;
    }

    public int chooseNonEmptySlot(Random random) {
        this.generateLoot(null);
        int i = -1;
        int j = 1;
        for (int k = 0; k < this.inventory.size(); ++k) {
            if (!this.inventory.get(k).isEmpty() && random.nextInt(j++) == 0) {
                i = k;
            }
        }
        return i;
    }

    public void setCooldown(@NotNull BlockState state) {
        int i = 2;
        if (state.getBlock() instanceof CopperPipe pipe) {
            i = pipe.cooldown;
        }
        this.transferCooldown = i;
    }

    @Override
    public void readNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookupProvider) {
        super.readNbt(nbtCompound, lookupProvider);
        this.transferCooldown = nbtCompound.getInt("transferCooldown");
        this.dispenseCooldown = nbtCompound.getInt("dispenseCooldown");
        this.canDispense = nbtCompound.getBoolean("canDispense");
        this.shootsControlled = nbtCompound.getBoolean("shootsControlled");
        this.shootsSpecial = nbtCompound.getBoolean("shootsSpecial");
        this.canAccept = nbtCompound.getBoolean("canAccept");
    }

    @Override
    protected void writeNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookupProvider) {
        super.writeNbt(nbtCompound, lookupProvider);
        nbtCompound.putInt("transferCooldown", this.transferCooldown);
        nbtCompound.putInt("dispenseCooldown", this.dispenseCooldown);
        nbtCompound.putBoolean("canDispense", this.canDispense);
        nbtCompound.putBoolean("shootsControlled", this.shootsControlled);
        nbtCompound.putBoolean("shootsSpecial", this.shootsSpecial);
        nbtCompound.putBoolean("canAccept", this.canAccept);
    }

    @Override
    public boolean canAcceptMoveableNbt(MoveType moveType, Direction moveDirection, BlockState fromState) {
        if (moveType == MoveType.FROM_FITTING) {
            return this.getCachedState().get(Properties.FACING) == moveDirection;
        }
        return this.getCachedState().get(Properties.FACING) == moveDirection ||
            moveDirection == fromState.get(Properties.FACING);
    }

    @Override
    public boolean canMoveNbtInDirection(Direction direction, @NotNull BlockState state) {
        return direction != state.get(Properties.FACING).getOpposite();
    }

    @Override
    public void dispenseMoveableNbt(ServerWorld serverWorld, BlockPos blockPos, BlockState blockState) {
        if (this.canDispense) {
            ArrayList<MoveablePipeDataHandler.SaveableMovablePipeNbt> nbtList = this.moveablePipeDataHandler.getSavedNbtList();
            if (!nbtList.isEmpty()) {
                for (MoveablePipeDataHandler.SaveableMovablePipeNbt nbt : nbtList) {
                    if (nbt.getShouldMove()) {
                        nbt.dispense(serverWorld, blockPos, blockState, this);
                    }
                }
                this.moveMoveableNbt(serverWorld, blockPos, blockState);
            }
        }
    }
}
