package eu.pintergabor.ironpipes.block.entity;

import static eu.pintergabor.ironpipes.block.entity.base.TickUtil.TickPos;
import static eu.pintergabor.ironpipes.block.entity.base.TickUtil.getTickPos;

import eu.pintergabor.ironpipes.block.base.BaseBlock;
import eu.pintergabor.ironpipes.block.base.BaseFluidPipe;
import eu.pintergabor.ironpipes.block.entity.base.BaseFluidPipeEntity;
import eu.pintergabor.ironpipes.block.entity.base.FluidUtil;
import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeManager;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import eu.pintergabor.ironpipes.registry.ModProperties;

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
        BlockState backState = world.getBlockState(pos.offset(opposite));
        PipeFluid pipeFluid = state.get(ModProperties.FLUID);
        if (FluidUtil.isWaterSource(backState)) {
            // If a water source from the back is supplying water.
            if (pipeFluid != PipeFluid.WATER) {
                pipeFluid = PipeFluid.WATER;
                changed = true;
            }
        } else if (FluidUtil.isLavaSource(backState)) {
            // If a lava source from the back is supplying lava.
            if (pipeFluid != PipeFluid.LAVA) {
                pipeFluid = PipeFluid.LAVA;
                changed = true;
            }
        } else {
            // Find a pipe pointing to this pipe from any side.
            boolean sideSourcing = false;
            for (Direction d : BaseBlock.DIRECTIONS) {
                if (d == facing || d == opposite) continue;
                BlockState nState = world.getBlockState(pos.offset(d));
                Block nBlock = nState.getBlock();
                if (nBlock instanceof BaseFluidPipe &&
                    nState.get(Properties.FACING) == d.getOpposite()) {
                    PipeFluid nFluid = nState.get(ModProperties.FLUID);
                    if (nFluid != PipeFluid.NONE) {
                        // Water or lava is coming from the side.
                        sideSourcing = true;
                        if (pipeFluid != nFluid) {
                            pipeFluid = nFluid;
                            changed = true;
                            break;
                        }
                    }
                }
            }
            if (!sideSourcing && pipeFluid != PipeFluid.NONE) {
                // No source from any side.
                pipeFluid = PipeFluid.NONE;
                changed = true;
            }
        }
        if (changed) {
            world.setBlockState(pos, state.with(ModProperties.FLUID, pipeFluid));
        }
        return changed;
    }

    /**
     * Push fluid into the block at the front of the pipe.
     *
     * @return true if state is changed.
     */
    @SuppressWarnings({"UnusedReturnValue", "unused"})
    private static boolean push(
        World world, BlockPos pos, BlockState state, WoodenPipeEntity entity) {
        boolean changed = false;
        // This block.
        Direction facing = state.get(Properties.FACING);
        Direction opposite = facing.getOpposite();
        PipeFluid pipeFluid = state.get(ModProperties.FLUID);
        // The block in front of this.
        BlockPos frontPos = pos.offset(facing);
        BlockState frontState = world.getBlockState(frontPos);
        Block frontBlock = frontState.getBlock();
        if (pipeFluid != PipeFluid.NONE) {
            int rnd = world.random.nextInt(0x100);
            if (frontBlock == Blocks.CAULDRON) {
                if (pipeFluid == PipeFluid.WATER && rnd < 0x10) {
                    // Start filling an empty cauldron with water.
                    world.setBlockState(frontPos,
                        Blocks.WATER_CAULDRON.getDefaultState()
                        .with(Properties.LEVEL_3, 1));
                    changed = true;
                }
                if (pipeFluid == PipeFluid.LAVA && rnd < 0x04) {
                    // Fill an empty cauldron with lava.
                    world.setBlockState(frontPos,
                        Blocks.LAVA_CAULDRON.getDefaultState());
                    changed = true;
                }
            } else if (frontBlock == Blocks.WATER_CAULDRON &&
                frontState.get(Properties.LEVEL_3) < 3) {
                if (pipeFluid == PipeFluid.WATER && rnd < 0x10) {
                    // Continue filling a water cauldron.
                    world.setBlockState(frontPos,
                        frontState.cycle(Properties.LEVEL_3));
                    changed = true;
                }
            }
        }
        return changed;
    }

    /**
     * Dispense fluid into the world.
     *
     * @return true if state is changed.
     */
    @SuppressWarnings({"UnusedReturnValue", "unused"})
    private static boolean dispense(
        World world, BlockPos pos, BlockState state, WoodenPipeEntity entity) {
        boolean changed = false;
        boolean outflow = state.get(ModProperties.OUTFLOW);
        PipeFluid pipeFluid = state.get(ModProperties.FLUID);
        Direction facing = state.get(Properties.FACING);
        BlockState frontBlockState = world.getBlockState(pos.offset(facing));
        Block frontBlock = frontBlockState.getBlock();
        if (outflow) {
            if (frontBlock == Blocks.WATER) {
                if (pipeFluid != PipeFluid.WATER) {
                    // If the block in front of the pipe is water, but the pipe
                    // is not carrying water then remove the block and stop dispensing.
                    world.setBlockState(pos.offset(facing), Blocks.AIR.getDefaultState());
                    outflow = false;
                    changed = true;
                }
            } else if (frontBlock == Blocks.LAVA) {
                if (pipeFluid != PipeFluid.LAVA) {
                    // If the block in front of the pipe is lava, but the pipe
                    // is not carrying lava then remove the block and stop dispensing.
                    world.setBlockState(pos.offset(facing), Blocks.AIR.getDefaultState());
                    outflow = false;
                    changed = true;
                }
            } else {
                // If the block in front of the pipe is neither water nor lava,
                // then stop dispensing, but do not change the block.
                outflow = false;
                changed = true;
            }
        } else {
            if (frontBlockState.isAir()) {
                // If there is an empty space in front of the pipe ...
                if (pipeFluid == PipeFluid.WATER) {
                    // ... and there is water in the pipe then start dispensing water.
                    world.setBlockState(pos.offset(facing),
                        Blocks.WATER.getDefaultState());
                    outflow = true;
                    changed = true;
                } else {
                    if (pipeFluid == PipeFluid.LAVA) {
                        // ... and there is lava in the pipe then start dispensing lava.
                        world.setBlockState(pos.offset(facing),
                            Blocks.LAVA.getDefaultState());
                        outflow = true;
                        changed = true;
                    }
                }
            }
        }
        if (changed) {
            world.setBlockState(pos, state.with(ModProperties.OUTFLOW, outflow));
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
            // Push fluid into blocks not capable of pulling it.
            push(world, pos, state, entity);
            // Dispense fluid.
            dispense(world, pos, state, entity);
        }
        PipeFluid pipeFluid = state.get(ModProperties.FLUID);
        if (pipeFluid == PipeFluid.WATER) {
            // Water plants and other water sensitive blocks and entities.
            LeakingPipeManager.addPos(world, pos);
        }
    }
}
