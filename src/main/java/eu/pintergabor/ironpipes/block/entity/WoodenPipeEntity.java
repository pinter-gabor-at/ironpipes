package eu.pintergabor.ironpipes.block.entity;

import eu.pintergabor.ironpipes.block.entity.base.BaseFluidPipeEntity;
import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeManager;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.blockold.base.BaseBlock;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import eu.pintergabor.ironpipes.registry.ModBlockStateProperties;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;


public class WoodenPipeEntity extends BaseFluidPipeEntity {

    public WoodenPipeEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.WOODEN_PIPE_ENTITY, blockPos, blockState);
    }

//    public static void serverTick(@NotNull World world, BlockPos blockPos, BlockState blockState, WoodenPipeEntity entity) {
//        if (!world.isClient()) {

    /// /            boolean validWater = isValidFluidNBT(waterNbt) && ModConfig.get().carryWater;
    /// /            boolean validLava = isValidFluidNBT(lavaNbt) && ModConfig.get().carryLava;
    /// /            if (this.hasWater && this.hasLava) {
    /// /                validWater = false;
    /// /                validLava = false;
    /// /            }
    /// /            if (state.contains(ModBlockStateProperties.FLUID)) {
    /// /                state = state.with(ModBlockStateProperties.FLUID,
    /// /                    validWater ? PipeFluid.WATER : validLava ? PipeFluid.LAVA : PipeFluid.NONE);
    /// /            }
//            // TODO: Dispensing.
//            // Dripping.
//            if (blockState.get(ModBlockStateProperties.FLUID) == PipeFluid.WATER &&
//                blockState.get(Properties.FACING) != Direction.UP) {
//                LeakingPipeManager.addPos(world, blockPos);
//            }
//        }
//    }

    /**
     * Check if the block can be used as a water source.
     *
     * @param state {@link BlockState} (which includes reference to the {@link Block})
     * @return true if it is a water source
     */
    private static boolean isWaterSource(BlockState state) {
        Block block = state.getBlock();
        // If it is a still or flowing water block.
        boolean ret = (block == Blocks.WATER);
        // If it is a full water cauldron.
        ret = ret ||
            (block == Blocks.WATER_CAULDRON &&
                (state.get(Properties.LEVEL_3) == 3));
        if (!ret) {
            if (block instanceof BaseBlock) {
                // If it is a pipe of fitting carrying water.
                ret = state.contains(ModBlockStateProperties.FLUID) &&
                    (state.get(ModBlockStateProperties.FLUID) == PipeFluid.WATER);
            } else {
                // If it is a waterlogged block.
                ret = state.contains(Properties.WATERLOGGED) &&
                    (state.get(Properties.WATERLOGGED));
            }
        }
        return ret;
    }

    /**
     * Check if the block can be used as a water source.
     *
     * @param state {@link BlockState} (which includes reference to the {@link Block})
     * @return true if it is a water source
     */
    private static boolean isLavaSource(BlockState state) {
        Block block = state.getBlock();
        // If it is a still or flowing lava block.
        boolean ret = (block == Blocks.LAVA);
        // If it is a full lava cauldron.
        ret = ret ||
            (block == Blocks.LAVA_CAULDRON &&
                (state.get(Properties.LEVEL_3) == 3));
        if (!ret && block instanceof BaseBlock) {
            // If it is a pipe of fitting carrying lava.
            ret = state.contains(ModBlockStateProperties.FLUID) &&
                (state.get(ModBlockStateProperties.FLUID) == PipeFluid.LAVA);
        }
        return ret;
    }

    public static void serverTick(
        World world, BlockPos pos, BlockState state, WoodenPipeEntity entity) {
        // Pull fluid.
        Direction facing = state.get(Properties.FACING);
        Direction opposite = facing.getOpposite();
        BlockState backBlockState = world.getBlockState(pos.offset(opposite));
        if (isWaterSource(backBlockState)) {
            state = state.with(ModBlockStateProperties.FLUID, PipeFluid.WATER);
        } else {
            state = state.with(ModBlockStateProperties.FLUID, PipeFluid.NONE);
        }
        world.setBlockState(pos, state);
        // Dispense fluid.
        BlockState frontBlockState = world.getBlockState(pos.offset(facing));
        if (frontBlockState.isAir()) {
            BlockState water = Blocks.WATER.getDefaultState();
            world.setBlockState(pos.offset(facing), water);
            //world.scheduleFluidTick(pos.offset(facing), Fluids.WATER, Fluids.WATER.getTickRate(world));
        } else if (frontBlockState.isOf(Blocks.WATER)) {
            // world.setBlockState(pos.offset(facing), frontBlockState.with(Properties.LEVEL_15, 5));
        }
        // Dripping.
        if (state.get(ModBlockStateProperties.FLUID) == PipeFluid.WATER) {
            LeakingPipeManager.addPos(world, pos);
        }
    }

//    public static <T extends BlockEntity> void serverTick(World world, BlockPos pos, BlockState state, T t) {
//    }

//    @Override
//    public void readNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookupProvider) {
//        super.readNbt(nbtCompound, lookupProvider);
//        waterCooldown = nbtCompound.getInt("waterCooldown");
//        hasWater = nbtCompound.getBoolean("hasWater");
//        hasLava = nbtCompound.getBoolean("hasLava");
//    }
//
//    @Override
//    protected void writeNbt(@NotNull NbtCompound nbtCompound, RegistryWrapper.WrapperLookup lookupProvider) {
//        super.writeNbt(nbtCompound, lookupProvider);
//        nbtCompound.putInt("waterCooldown", waterCooldown);
//        nbtCompound.putBoolean("hasWater", hasWater);
//        nbtCompound.putBoolean("hasLava", hasLava);
//    }
}
