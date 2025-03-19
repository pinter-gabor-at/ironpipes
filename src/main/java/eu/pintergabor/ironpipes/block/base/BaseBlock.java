package eu.pintergabor.ironpipes.block.base;

import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import org.jetbrains.annotations.NotNull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;


/**
 * Base block of the mod.
 * <p>
 * Pipes and fittings are rendered normally, they do not block light,
 * and entities cannot walk through them.
 */
public abstract class BaseBlock extends BlockWithEntity implements Waterloggable {
    public static final BooleanProperty WATERLOGGED =
        Properties.WATERLOGGED;
    // All Directions in pull priority order.
    public static final Direction[] DIRECTIONS = {
        Direction.UP, Direction.NORTH, Direction.EAST,
        Direction.SOUTH, Direction.WEST, Direction.DOWN};

    protected BaseBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
            .with(WATERLOGGED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }

    /**
     * Determine the initial state of the pipe based on its surroundings.
     *
     * @return the initial state of the block
     */
    @Override
    public BlockState getPlacementState(@NotNull ItemPlacementContext context) {
        BlockState state = super.getPlacementState(context);
        if (state != null) {
            BlockPos pos = context.getBlockPos();
            return state
                .with(WATERLOGGED, context.getWorld().getFluidState(pos).getFluid() == Fluids.WATER);
        }
        return null;
    }

    /**
     * Handle state changes when the neighboring block's state changes.
     *
     * @return the state of the pipe after a neighboring block's state changes.
     */
    @Override
    protected @NotNull BlockState getStateForNeighborUpdate(
        @NotNull BlockState blockState,
        WorldView world,
        ScheduledTickView scheduledTickAccess,
        BlockPos pos,
        Direction direction,
        BlockPos neighborPos,
        BlockState neighborState,
        Random random) {
        if (blockState.get(WATERLOGGED)) {
            scheduledTickAccess.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return blockState;
    }

    /**
     * Pipes and fittings do not block light.
     *
     * @return true
     */
    @Override
    protected boolean isTransparent(@NotNull BlockState blockState) {
        return true;
    }

    /**
     * Entities cannot walk through a pipe or a fitting.
     *
     * @return false
     */
    @Override
    protected boolean canPathfindThrough(BlockState state, NavigationType navType) {
        return false;
    }

    /**
     * Pipes and fittings are rendered normally.
     */
    @Override
    @NotNull
    public BlockRenderType getRenderType(BlockState blockState) {
        return BlockRenderType.MODEL;
    }

    @Override
    @NotNull
    public FluidState getFluidState(@NotNull BlockState blockState) {
        if (blockState.get(WATERLOGGED)) {
            return Fluids.WATER.getStill(false);
        }
        return super.getFluidState(blockState);
    }
}
