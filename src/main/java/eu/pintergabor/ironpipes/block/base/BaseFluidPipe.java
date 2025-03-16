package eu.pintergabor.ironpipes.block.base;

import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.ModBlockStateProperties;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;


/**
 * A fluid pipe can carry water or lava.
 */
public abstract class BaseFluidPipe extends BasePipe {
    public static final EnumProperty<PipeFluid> FLUID =
        ModBlockStateProperties.FLUID;
    public static final BooleanProperty OUTFLOW =
        ModBlockStateProperties.OUTFLOW;

    protected BaseFluidPipe(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
            .with(FLUID, PipeFluid.NONE)
            .with(OUTFLOW, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FLUID, OUTFLOW);
    }

    /**
     * Check if this fluid block is an outflow from a pipe.
     *
     * @return true if it is an outflow.
     */
    public static boolean isOutflow(
        WorldAccess world,
        BlockPos pos,
        FlowableFluid fluid
    ) {
        // Look around to find a fluid pipe that is supplying fluid to this block.
        for (Direction d : DIRECTIONS) {
            BlockPos neighbourPos = pos.offset(d);
            BlockState neighbourState = world.getBlockState(neighbourPos);
            Block neighbourBlock = neighbourState.getBlock();
            if (neighbourBlock instanceof BaseFluidPipe) {
                boolean outflow = neighbourState.get(ModBlockStateProperties.OUTFLOW);
                Direction facing = neighbourState.get(Properties.FACING);
                if (outflow && facing == d.getOpposite()) {
                    PipeFluid pipeFluid = neighbourState.get(ModBlockStateProperties.FLUID);
                    if (pipeFluid == PipeFluid.WATER && fluid == Fluids.WATER) {
                        return true;
                    }
                    if (pipeFluid == PipeFluid.LAVA && fluid == Fluids.LAVA) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
