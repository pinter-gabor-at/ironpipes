package eu.pintergabor.ironpipes.block.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import eu.pintergabor.ironpipes.block.entity.nbt.MoveablePipeDataHandler;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.config.SimpleCopperPipesConfig;
import eu.pintergabor.ironpipes.registry.RegisterPipeNbtMethods;
import eu.pintergabor.ironpipes.registry.ModBlockStateProperties;
import eu.pintergabor.ironpipes.tag.ModBlockTags;

import net.minecraft.world.WorldEvents;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AbstractModBlockEntity extends LootableContainerBlockEntity implements Inventory {
    public final MoveType moveType;
    public DefaultedList<ItemStack> inventory;
    public int waterCooldown;
    public int electricityCooldown;
    public boolean canWater;
    public boolean canLava;
    public MoveablePipeDataHandler moveablePipeDataHandler;

    public AbstractModBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, MoveType moveType) {
        super(blockEntityType, blockPos, blockState);
        this.inventory = DefaultedList.ofSize(5, ItemStack.EMPTY);
        this.waterCooldown = -1;
        this.electricityCooldown = -1;
        this.moveablePipeDataHandler = new MoveablePipeDataHandler();
        this.moveType = moveType;
    }

    public static void sendElectricity(World level, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            BlockPos pos = blockPos.offset(direction);
            if (level.isPosLoaded(pos)) {
                BlockState state = level.getBlockState(pos);
                if (state.contains(ModBlockStateProperties.HAS_ELECTRICITY)) {
                    BlockEntity entity = level.getBlockEntity(pos);
                    if (entity instanceof AbstractModBlockEntity copperBlockEntity) {
                        int axis = state.contains(Properties.FACING) ? state.get(Properties.FACING).getAxis().ordinal() : direction.getAxis().ordinal();
                        if (copperBlockEntity.electricityCooldown == -1) {
                            level.syncWorldEvent(WorldEvents.ELECTRICITY_SPARKS, pos, axis);
                            level.setBlockState(pos, state.with(ModBlockStateProperties.HAS_ELECTRICITY, true));
                        }
                    }
                }
            }
        }
    }

    public void serverTick(@NotNull World level, BlockPos blockPos, BlockState blockState) {
        BlockState state = blockState;
        if (!level.isClient()) {
            if (this.canWater && !this.canLava && SimpleCopperPipesConfig.get().carryWater) {
                this.moveablePipeDataHandler.setMoveablePipeNbt(RegisterPipeNbtMethods.WATER, new MoveablePipeDataHandler.SaveableMovablePipeNbt()
                    .withVec3dd(new Vec3d(11, 0, 0)).withShouldCopy(true).withNBTID(RegisterPipeNbtMethods.WATER));
            }
            if (this.canLava && !this.canWater && SimpleCopperPipesConfig.get().carryLava) {
                this.moveablePipeDataHandler.setMoveablePipeNbt(RegisterPipeNbtMethods.LAVA, new MoveablePipeDataHandler.SaveableMovablePipeNbt()
                    .withVec3dd(new Vec3d(11, 0, 0)).withShouldCopy(true).withNBTID(RegisterPipeNbtMethods.LAVA));
            }
            MoveablePipeDataHandler.SaveableMovablePipeNbt waterNbt = this.moveablePipeDataHandler.getMoveablePipeNbt(RegisterPipeNbtMethods.WATER);
            MoveablePipeDataHandler.SaveableMovablePipeNbt lavaNbt = this.moveablePipeDataHandler.getMoveablePipeNbt(RegisterPipeNbtMethods.LAVA);
            MoveablePipeDataHandler.SaveableMovablePipeNbt smokeNbt = this.moveablePipeDataHandler.getMoveablePipeNbt(RegisterPipeNbtMethods.SMOKE);
            boolean validWater = isValidFluidNBT(waterNbt) && SimpleCopperPipesConfig.get().carryWater;
            boolean validLava = isValidFluidNBT(lavaNbt) && SimpleCopperPipesConfig.get().carryLava;
            if (this.canWater && this.canLava) {
                validWater = false;
                validLava = false;
            }
            if (state.contains(ModBlockStateProperties.FLUID)) {
                state = state.with(ModBlockStateProperties.FLUID, validWater ? PipeFluid.WATER : validLava ? PipeFluid.LAVA : PipeFluid.NONE);
            }
            this.tickMoveableNbt((ServerWorld) level, blockPos, blockState);
            this.dispenseMoveableNbt((ServerWorld) level, blockPos, blockState);
            this.moveMoveableNbt((ServerWorld) level, blockPos, blockState);
            if (this.electricityCooldown >= 0) {
                --this.electricityCooldown;
            }
            if (this.electricityCooldown == -1 && state.get(ModBlockStateProperties.HAS_ELECTRICITY)) {
                this.electricityCooldown = 80;
                Block stateGetBlock = state.getBlock();
                Optional<Block> previous = Oxidizable.getDecreasedOxidationBlock(stateGetBlock);
                if (previous.isPresent() && !state.isIn(ModBlockTags.WAXED)) {
                    state = previous.get().getStateWithProperties(state);
                }
            }
            if (this.electricityCooldown == 79) {
                sendElectricity(level, blockPos);
            }
            if (this.electricityCooldown == 0) {
                if (state.contains(ModBlockStateProperties.HAS_ELECTRICITY)) {
                    state = state.with(ModBlockStateProperties.HAS_ELECTRICITY, false);
                }
            }
            if (state != blockState) {
                level.setBlockState(blockPos, state);
            }
        }
    }

    public boolean isValidFluidNBT(@Nullable MoveablePipeDataHandler.SaveableMovablePipeNbt fluidNBT) {
        if (fluidNBT != null) {
            return fluidNBT.getVec3dd().getX() > 0;
        }
        return false;
    }

    public void updateBlockEntityValues(World level, BlockPos pos, BlockState state) {

    }

    public boolean canAcceptMoveableNbt(MoveType moveType, Direction moveDirection, BlockState fromState) {
        return true;
    }

    public boolean canMoveNbtInDirection(Direction direction, BlockState blockState) {
        return true;
    }

    public void tickMoveableNbt(ServerWorld serverLevel, BlockPos blockPos, BlockState blockState) {
        for (MoveablePipeDataHandler.SaveableMovablePipeNbt nbt : (List<MoveablePipeDataHandler.SaveableMovablePipeNbt>) this.moveablePipeDataHandler.getSavedNbtList().clone()) {
            nbt.tick(serverLevel, blockPos, blockState, this);
        }
    }

    public void dispenseMoveableNbt(ServerWorld serverLevel, BlockPos blockPos, BlockState blockState) {

    }

    public void moveMoveableNbt(ServerWorld serverLevel, BlockPos blockPos, BlockState blockState) {
        ArrayList<MoveablePipeDataHandler.SaveableMovablePipeNbt> nbtList = moveablePipeDataHandler.getSavedNbtList();
        ArrayList<MoveablePipeDataHandler.SaveableMovablePipeNbt> usedNbts = new ArrayList<>();
        if (!nbtList.isEmpty()) {
            List<Direction> dirs = Util.copyShuffled(Direction.values(), serverLevel.getRandom());
            for (Direction direction : dirs) {
                if (this.canMoveNbtInDirection(direction, blockState)) {
                    BlockPos newPos = blockPos.offset(direction);
                    if (serverLevel.isPosLoaded(newPos)) {
                        BlockState state = serverLevel.getBlockState(newPos);
                        BlockEntity entity = serverLevel.getBlockEntity(newPos);
                        if (entity instanceof AbstractModBlockEntity copperEntity) {
                            if (copperEntity.canAcceptMoveableNbt(this.moveType, direction, blockState)) {
                                for (MoveablePipeDataHandler.SaveableMovablePipeNbt nbt : nbtList) {
                                    if (nbt.getShouldMove() && (!nbt.getCanOnlyGoThroughOnePipe() || !usedNbts.contains(nbt)) && nbt.canMove(serverLevel, newPos, state, copperEntity)) {
                                        MoveablePipeDataHandler.SaveableMovablePipeNbt onMove;
                                        if (nbt.getShouldCopy()) {
                                            onMove = nbt.copyOf();
                                        } else {
                                            onMove = nbt;
                                        }
                                        copperEntity.moveablePipeDataHandler.setMoveablePipeNbt(nbt.getNbtID(), onMove);
                                        onMove.onMove(serverLevel, newPos, state, copperEntity);
                                        if (!usedNbts.contains(nbt)) {
                                            usedNbts.add(nbt);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this.moveablePipeDataHandler.clearAllButNonMoveable();
            usedNbts.clear();
            this.markDirty();
        }
    }

    @Override
    public void readNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookupProvider) {
        super.readNbt(nbtCompound, lookupProvider);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.readLootTable(nbtCompound)) {
            Inventories.readNbt(nbtCompound, this.inventory, lookupProvider);
        }
        this.waterCooldown = nbtCompound.getInt("WaterCooldown");
        this.electricityCooldown = nbtCompound.getInt("electricityCooldown");
        this.canWater = nbtCompound.getBoolean("canWater");
        this.canLava = nbtCompound.getBoolean("canLava");
        this.moveablePipeDataHandler.readNbt(nbtCompound);
    }

    @Override
    protected void writeNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookupProvider) {
        super.writeNbt(nbtCompound, lookupProvider);
        if (!this.writeLootTable(nbtCompound)) {
            Inventories.writeNbt(nbtCompound, this.inventory, lookupProvider);
        }
        nbtCompound.putInt("WaterCooldown", this.waterCooldown);
        nbtCompound.putInt("electricityCooldown", this.electricityCooldown);
        nbtCompound.putBoolean("canWater", this.canWater);
        nbtCompound.putBoolean("canLava", this.canLava);
        this.moveablePipeDataHandler.writeNbt(nbtCompound);
    }

    @Override
    @NotNull
    protected DefaultedList<ItemStack> getHeldStacks() {
        return this.inventory;
    }

    @Override
    protected void setHeldStacks(DefaultedList<ItemStack> defaultedList) {
        this.inventory = defaultedList;
    }

    @Override
    @NotNull
    protected Text getContainerName() {
        return Text.translatable(this.getCachedState().getBlock().getTranslationKey());
    }

    @Override
    @NotNull
    protected ScreenHandler createScreenHandler(int i, PlayerInventory playerInventory) {
        return new HopperScreenHandler(i, playerInventory, this);
    }

    @Override
    public int size() {
        return this.inventory.size();
    }

    public enum MoveType {
        FROM_PIPE,
        FROM_FITTING
    }
}
