package eu.pintergabor.ironpipes.block.base;

import org.jetbrains.annotations.NotNull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;


/**
 * Base fitting.
 * <p>
 * All fittings have the same shape, rotating them is meaningless,
 * and there are special rules for connecting them to pipes.
 * <p>
 * Fittings can receive redstone power.
 */
public abstract class BaseFitting extends BaseBlock {
    protected static final VoxelShape FITTING_SHAPE =
        Block.createCuboidShape(2.5D, 2.5D, 2.5D, 13.5D, 13.5D, 13.5D);
    public static final BooleanProperty POWERED =
        Properties.POWERED;

    /**
     * Check if the pipe is receiving redstone power from any direction.
     *
     * @param world    The world
     * @param blockPos Position of this pipe
     * @return true if the pipe is receiving redstone power.
     */
    public static boolean isReceivingRedstonePower(World world, BlockPos blockPos) {
        for (Direction direction : Direction.values()) {
            if (world.getEmittedRedstonePower(blockPos.offset(direction), direction) > 0) {
                return true;
            }
        }
        return false;
    }

    protected BaseFitting(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
            .with(POWERED, false));
    }

    @Override
    @NotNull
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
        return FITTING_SHAPE;
    }

    @Override
    @NotNull
    public VoxelShape getRaycastShape(BlockState blockState, BlockView blockView, BlockPos blockPos) {
        return FITTING_SHAPE;
    }
}
