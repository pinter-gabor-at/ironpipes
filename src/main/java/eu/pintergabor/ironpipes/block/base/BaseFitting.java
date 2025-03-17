package eu.pintergabor.ironpipes.block.base;

import net.minecraft.item.ItemPlacementContext;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;


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

    protected BaseFitting(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
            .with(POWERED, false));
    }

    @Override
    protected void appendProperties(
        StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(POWERED);
    }

    @Override
    @NotNull
    public VoxelShape getOutlineShape(
        BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return FITTING_SHAPE;
    }

    @Override
    @NotNull
    public VoxelShape getRaycastShape(
        BlockState state, BlockView world, BlockPos pos) {
        return FITTING_SHAPE;
    }

    /**
     * Check if the pipe is receiving redstone power from any direction.
     *
     * @param world    The world
     * @param blockPos Position of this pipe
     * @return true if the pipe is receiving redstone power.
     */
    public static boolean isReceivingRedstonePower(
        World world, BlockPos blockPos) {
        for (Direction d : Direction.values()) {
            BlockPos neighbourPos = blockPos.offset(d);
            if (0 < world.getEmittedRedstonePower(neighbourPos, d)) {
                return true;
            }
        }
        return false;
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
            World world = context.getWorld();
            return state
                .with(POWERED, isReceivingRedstonePower(world, pos));
        }
        return null;
    }

    /**
     * Handle side effects when the neighboring block's state changes.
     */
    @Override
    protected void neighborUpdate(
        @NotNull BlockState state, @NotNull World world,
        BlockPos pos, Block block, @Nullable WireOrientation orientation, boolean notify) {
        boolean powered = isReceivingRedstonePower(world, pos);
        if (powered != state.get(POWERED)) {
            world.setBlockState(pos, state.with(POWERED, powered));
        }
    }
}
