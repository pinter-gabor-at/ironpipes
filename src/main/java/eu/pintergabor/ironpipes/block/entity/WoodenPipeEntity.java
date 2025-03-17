package eu.pintergabor.ironpipes.block.entity;

import static eu.pintergabor.ironpipes.block.entity.base.TickUtil.TickPos;
import static eu.pintergabor.ironpipes.block.entity.base.TickUtil.getTickPos;

import eu.pintergabor.ironpipes.block.entity.base.BaseFluidPipeEntity;
import eu.pintergabor.ironpipes.block.entity.base.FluidUtil;
import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeManager;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
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
        if (FluidUtil.isWaterSource(backBlockState)) {
            if (pipeFluid != PipeFluid.WATER) {
                pipeFluid = PipeFluid.WATER;
                changed = true;
            }
        } else if (FluidUtil.isLavaSource(backBlockState)) {
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
        TickPos tickPos = getTickPos(world, 10);
        if (tickPos == TickPos.START) {
            // Pull fluid.
            pull(world, pos, state, entity);
        }
        if (tickPos == TickPos.MIDDLE) {
            // Dispense fluid.
            dispense(world, pos, state, entity);
        }
        // Watering plants and other water sensitive blocks and entities.
        if (state.get(ModBlockStateProperties.FLUID) == PipeFluid.WATER) {
            LeakingPipeManager.addPos(world, pos);
        }
    }
}
