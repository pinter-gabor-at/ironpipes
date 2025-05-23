package eu.pintergabor.ironpipes.block;

import eu.pintergabor.ironpipes.block.util.TickUtil;
import eu.pintergabor.ironpipes.registry.ModStats;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;


/**
 * Base block of the mod.
 * <p>
 * Pipes and fittings are rendered normally, they do not block light,
 * and entities cannot walk through them.
 */
public sealed abstract class BaseBlock extends BaseEntityBlock implements SimpleWaterloggedBlock
	permits BaseFitting, BasePipe {

	// Common BlockState properties.
	public static final BooleanProperty waterlogged =
		BlockStateProperties.WATERLOGGED;
	public final int tickRate;
	// All directions in pull priority order.
	public static final Direction[] DIRECTIONS = {
		Direction.UP, Direction.NORTH, Direction.EAST,
		Direction.SOUTH, Direction.WEST, Direction.DOWN};

	protected BaseBlock(Properties props, int tickRate) {
		super(props);
		this.tickRate = tickRate;
		registerDefaultState(getStateDefinition().any()
			.setValue(waterlogged, false));
	}

	@Override
	public void setPlacedBy(
		@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state,
		@Nullable LivingEntity placer, @NotNull ItemStack itemStack
	) {
		super.setPlacedBy(level, pos, state, placer, itemStack);
		if (!level.isClientSide &&
			placer instanceof ServerPlayer serverPlayer) {
			// Increment statistics on the server.
			serverPlayer.awardStat(ModStats.INTERACTIONS);
		}
	}

	@Override
	protected void createBlockStateDefinition(
		@NotNull StateDefinition.Builder<Block, BlockState> builder
	) {
		super.createBlockStateDefinition(builder);
		builder.add(waterlogged);
	}

	/**
	 * Determine the initial state of the pipe based on its surroundings.
	 *
	 * @return the initial state of the block
	 */
	@Override
	public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
		final BlockState state = super.getStateForPlacement(context);
		if (state != null) {
			final BlockPos pos = context.getClickedPos();
			final Level level = context.getLevel();
			return state
				.setValue(waterlogged,
					level.getFluidState(pos).is(Fluids.WATER));
		}
		return null;
	}

	/**
	 * Handle state changes when the neighboring block's state changes.
	 *
	 * @return the state of the pipe after a neighboring block's state changes.
	 */
	@Override
	protected @NotNull BlockState updateShape(
		@NotNull BlockState blockState,
		@NotNull LevelReader level,
		@NotNull ScheduledTickAccess scheduledTickAccess,
		@NotNull BlockPos pos,
		@NotNull Direction direction,
		@NotNull BlockPos neighborPos,
		@NotNull BlockState neighborState,
		@NotNull RandomSource random
	) {
		if (blockState.getValue(waterlogged)) {
			scheduledTickAccess.scheduleTick(
				pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		return blockState;
	}

	/**
	 * Pipes and fittings do not block light.
	 *
	 * @return true
	 */
	@Override
	protected boolean propagatesSkylightDown(@NotNull BlockState blockState) {
		return true;
	}

	/**
	 * Entities cannot walk through a pipe or a fitting.
	 *
	 * @return false
	 */
	@Override
	protected boolean isPathfindable(
		@NotNull BlockState state, @NotNull PathComputationType pathComputationType
	) {
		return false;
	}

	/**
	 * Pipes and fittings are rendered normally.
	 */
	@Override
	@NotNull
	public RenderShape getRenderShape(@NotNull BlockState blockState) {
		return RenderShape.MODEL;
	}

	@Override
	@NotNull
	public FluidState getFluidState(@NotNull BlockState blockState) {
		if (blockState.getValue(waterlogged)) {
			return Fluids.WATER.getSource(false);
		}
		return super.getFluidState(blockState);
	}

	/**
	 * How fast is this block?
	 * Higher value means slower operation.
	 * Min 2.
	 */
	public int getTickRate() {
		return tickRate;
	}

	/**
	 * Return {@link TickUtil.TickPos#START} and {@link TickUtil.TickPos#MIDDLE} once in every {@code 1 / rate} time
	 */
	public static TickUtil.TickPos getTickPos(Level level, BlockState state) {
		final BaseBlock block = (BaseBlock) state.getBlock();
		final int rate = block.getTickRate();
		return TickUtil.getTickPos(level, rate);
	}
}
