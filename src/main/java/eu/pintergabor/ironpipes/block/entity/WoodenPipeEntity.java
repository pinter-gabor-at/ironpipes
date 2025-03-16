package eu.pintergabor.ironpipes.block.entity;

import eu.pintergabor.ironpipes.block.entity.base.BaseFluidPipeEntity;
import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeManager;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.block.base.BaseBlock;
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
                // If it is a pipe or fitting carrying water.
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
        // If it is a lava cauldron.
        ret = ret || block == Blocks.LAVA_CAULDRON;
        if (!ret && block instanceof BaseBlock) {
            // If it is a pipe or fitting carrying lava.
            ret = state.contains(ModBlockStateProperties.FLUID) &&
                (state.get(ModBlockStateProperties.FLUID) == PipeFluid.LAVA);
        }
        return ret;
    }

    /**
     * Pull fluid from the block at the back of the pipe.
     *
     * @return true if state is changed.
     */
    @SuppressWarnings({"UnusedReturnValue", "unused"})
    private static boolean pull(World world, BlockPos pos, BlockState state, WoodenPipeEntity entity) {
        boolean changed = false;
        Direction facing = state.get(Properties.FACING);
        Direction opposite = facing.getOpposite();
        BlockState backBlockState = world.getBlockState(pos.offset(opposite));
        PipeFluid pipeFluid = state.get(ModBlockStateProperties.FLUID);
        if (isWaterSource(backBlockState)) {
            if (pipeFluid != PipeFluid.WATER) {
                pipeFluid = PipeFluid.WATER;
                changed = true;
            }
        } else if (isLavaSource(backBlockState)) {
            if (pipeFluid != PipeFluid.LAVA) {
                pipeFluid = PipeFluid.LAVA;
                changed = true;
            }
        } else {
            if (pipeFluid != PipeFluid.NONE) {
                pipeFluid = PipeFluid.NONE;
                changed = true;
            }
        }
        if (changed) {
            world.setBlockState(pos, state.with(ModBlockStateProperties.FLUID, pipeFluid));
        }
        return changed;
    }

    @SuppressWarnings({"UnusedReturnValue", "unused"})
    private static boolean dispense(World world, BlockPos pos, BlockState state, WoodenPipeEntity entity) {
        boolean changed = false;
        boolean outflow = state.get(ModBlockStateProperties.OUTFLOW);
        PipeFluid pipeFluid = state.get(ModBlockStateProperties.FLUID);
        Direction facing = state.get(Properties.FACING);
        BlockState frontBlockState = world.getBlockState(pos.offset(facing));
        Block frontBlock = frontBlockState.getBlock();
        if (outflow) {
            // If the block in front of the pipe does not match the dispensed fluid
            // then stop dispensing.
            if (!((frontBlock == Blocks.WATER && pipeFluid == PipeFluid.WATER) ||
                (frontBlock == Blocks.LAVA && pipeFluid == PipeFluid.LAVA))) {
                world.setBlockState(pos.offset(facing), Blocks.AIR.getDefaultState());
                outflow = false;
                changed = true;
            }
        } else {
            // If there is an empty space in front of the pipe ...
            if (frontBlockState.isAir()) {
                // ... and there is water in the pipe then start dispensing water.
                if (pipeFluid == PipeFluid.WATER) {
                    world.setBlockState(pos.offset(facing), Blocks.WATER.getDefaultState());
                    outflow = true;
                    changed = true;
                } else {
                    // ... and there is lava in the pipe then start dispensing lava.
                    if (pipeFluid == PipeFluid.LAVA) {
                        world.setBlockState(pos.offset(facing), Blocks.LAVA.getDefaultState());
                        outflow = true;
                        changed = true;
                    }
                }
            }
        }
        if (changed) {
            world.setBlockState(pos, state.with(ModBlockStateProperties.OUTFLOW, outflow));
        }
        return changed;
    }

    public static void serverTick(
        World world, BlockPos pos, BlockState state, WoodenPipeEntity entity) {
        // Pull fluid.
        pull(world, pos, state, entity);
        // Dispense fluid.
        dispense(world, pos, state, entity);
        // Dripping.
        if (state.get(ModBlockStateProperties.FLUID) != PipeFluid.NONE) {
            LeakingPipeManager.addPos(world, pos);
        }
    }

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
