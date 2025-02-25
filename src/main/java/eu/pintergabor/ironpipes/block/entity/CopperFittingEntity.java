package eu.pintergabor.ironpipes.block.entity;

import eu.pintergabor.ironpipes.block.CopperFitting;
import eu.pintergabor.ironpipes.config.SimpleCopperPipesConfig;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import org.jetbrains.annotations.NotNull;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;

public class CopperFittingEntity extends AbstractModBlockEntity {

    private static final int MAX_TRANSFER_AMOUNT = 1;

    public int transferCooldown;

    public CopperFittingEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.COPPER_FITTING_ENTITY, blockPos, blockState, MoveType.FROM_FITTING);
    }

    public static boolean canTransfer(@NotNull World level, BlockPos pos, Direction direction, boolean to) {
        BlockState blockState = level.getBlockState(pos);
        return level.getBlockEntity(pos) instanceof CopperPipeEntity pipe &&
            (!to || pipe.transferCooldown <= 0) &&
            blockState.contains(Properties.FACING) &&
            blockState.get(Properties.FACING) == direction;
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
    public void serverTick(@NotNull World level, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        super.serverTick(level, blockPos, blockState);
        if (!level.isClient()) {
            if (this.transferCooldown > 0) {
                --this.transferCooldown;
            } else {
                this.fittingMove(level, blockPos, blockState);
            }
        }
    }

    public void fittingMove(@NotNull World level, BlockPos blockPos, @NotNull BlockState blockState) {
        boolean bl1 = blockState.contains(Properties.POWERED) && !blockState.get(Properties.POWERED) && this.moveOut(level, blockPos, level.random);
        boolean bl2 = this.moveIn(level, blockPos, level.random);
        if (bl1 || bl2) {
            setCooldown(blockState);
            markDirty(level, blockPos, blockState);
        }
    }

    private boolean moveIn(World level, @NotNull BlockPos blockPos, Random randomSource) {
        boolean result = false;
        for (Direction direction : Util.copyShuffled(Direction.values(), randomSource)) {
            Direction opposite = direction.getOpposite();
            BlockPos offsetOppPos = blockPos.offset(opposite);
            Storage<ItemVariant> inventory = CopperPipeEntity.getStorageAt(level, offsetOppPos, direction);
            Storage<ItemVariant> fittingInventory = CopperPipeEntity.getStorageAt(level, blockPos, opposite);
            if (inventory != null && fittingInventory != null && canTransfer(level, offsetOppPos, direction, false)) {
                for (StorageView<ItemVariant> storageView : inventory) {
                    if (!storageView.isResourceBlank() && storageView.getAmount() > 0) {
                        Transaction transaction = Transaction.openOuter();
                        var resource = storageView.getResource();
                        long extracted = inventory.extract(resource, MAX_TRANSFER_AMOUNT, transaction);
                        if (extracted > 0) {
                            long inserted = CopperPipeEntity.addItem(resource, fittingInventory, transaction);
                            if (inserted > 0) {
                                transaction.commit(); // applies the changes
                                result = true;
                            }
                        }
                        transaction.close(); // if it cant commit, close it.
                        // make sure to close instead of commit bc the item would be deleted
                    }
                }
            }
        }
        return result;
    }

    private boolean moveOut(World level, @NotNull BlockPos blockPos, Random random) {
        boolean result = false;
        for (Direction direction : Util.copyShuffled(Direction.values(), random)) {
            BlockPos offsetPos = blockPos.offset(direction);
            Direction opposite = direction.getOpposite();
            Storage<ItemVariant> inventory = ItemStorage.SIDED.find(level, offsetPos, level.getBlockState(offsetPos), level.getBlockEntity(offsetPos), opposite);
            Storage<ItemVariant> fittingInventory = ItemStorage.SIDED.find(level, blockPos, level.getBlockState(blockPos), level.getBlockEntity(blockPos), direction);
            if (inventory != null && fittingInventory != null && canTransfer(level, offsetPos, direction, true)) {
                for (StorageView<ItemVariant> storageView : fittingInventory) {
                    if (!storageView.isResourceBlank() && 0 < storageView.getAmount()) {
                        Transaction transaction = Transaction.openOuter();
                        var resource = storageView.getResource();
                        long inserted = inventory.insert(resource, MAX_TRANSFER_AMOUNT, transaction);
                        // If sucessfully inserted the item.
                        if (0 < inserted) {
                            long extracted = fittingInventory.extract(resource, MAX_TRANSFER_AMOUNT, transaction);
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

    public void setCooldown(@NotNull BlockState state) {
        int i = 2;
        if (state.getBlock() instanceof CopperFitting fitting) {
            i = fitting.cooldown;
        }
        this.transferCooldown = i;
    }

    @Override
    public boolean canAcceptMoveableNbt(MoveType moveType, Direction moveDirection, BlockState fromState) {
        if (moveType == MoveType.FROM_FITTING) {
            return false;
        } else if (moveType == MoveType.FROM_PIPE) {
            return moveDirection == fromState.get(Properties.FACING);
        }
        return false;
    }

    @Override
    public void updateBlockEntityValues(World level, BlockPos pos, @NotNull BlockState state) {
        if (state.getBlock() instanceof CopperFitting) {
            this.canWater = state.get(Properties.WATERLOGGED) && SimpleCopperPipesConfig.get().carryWater;
        }
    }

    @Override
    public void readNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookupProvider) {
        super.readNbt(nbtCompound, lookupProvider);
        this.transferCooldown = nbtCompound.getInt("transferCooldown");
    }

    @Override
    protected void writeNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookupProvider) {
        super.writeNbt(nbtCompound, lookupProvider);
        nbtCompound.putInt("transferCooldown", this.transferCooldown);
    }

}
