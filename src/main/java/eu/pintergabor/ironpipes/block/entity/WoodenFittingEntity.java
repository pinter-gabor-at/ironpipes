package eu.pintergabor.ironpipes.block.entity;

import static eu.pintergabor.ironpipes.block.entity.base.TickUtil.getTickPos;

import eu.pintergabor.ironpipes.block.base.BaseBlock;
import eu.pintergabor.ironpipes.block.base.BaseFluidPipe;
import eu.pintergabor.ironpipes.block.entity.base.BaseFluidPipeEntity;
import eu.pintergabor.ironpipes.block.entity.base.TickUtil;
import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeManager;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import eu.pintergabor.ironpipes.registry.ModProperties;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;


public class WoodenFittingEntity extends BaseFluidPipeEntity {

    public WoodenFittingEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.WOODEN_FITTING_ENTITY, blockPos, blockState);
    }

    /**
     * Pull fluid from any pipe pointing to this fitting.
     *
     * @return true if state is changed.
     */
    @SuppressWarnings({"UnusedReturnValue", "unused"})
    private static boolean pull(
        World world, BlockPos pos, BlockState state, WoodenFittingEntity entity) {
        boolean changed = false;
        PipeFluid pipeFluid = state.get(ModProperties.FLUID);
        // Find a pipe pointing to this pipe from any side.
        boolean sideSourcing = false;
        for (Direction d : BaseBlock.DIRECTIONS) {
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
                    }
                    break;
                }
            }
        }
        if (!sideSourcing && pipeFluid != PipeFluid.NONE) {
            // No source from any side.
            pipeFluid = PipeFluid.NONE;
            changed = true;
        }
        if (changed) {
            world.setBlockState(pos, state.with(ModProperties.FLUID, pipeFluid));
        }
        return changed;
    }

    /**
     * Called at every tick on the server.
     */
    public static void serverTick(
        World world, BlockPos pos, BlockState state, WoodenFittingEntity entity) {
        TickUtil.TickPos tickPos = getTickPos(world, 10);
        if (tickPos == TickUtil.TickPos.START) {
            // Pull fluid.
            pull(world, pos, state, entity);
        }
        if (state.get(ModProperties.FLUID) == PipeFluid.WATER) {
            // Water plants and other water sensitive blocks and entities.
            LeakingPipeManager.addPos(world, pos);
        }
    }
}
