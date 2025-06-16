package eu.pintergabor.ironpipes.block.util;

import static eu.pintergabor.ironpipes.registry.util.ModProperties.OUTFLOW;

import eu.pintergabor.ironpipes.block.CanCarryFluid;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.util.ModProperties;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;


/**
 * More utilities for fluid pipes.
 * <p>
 * Dispense.
 */
public final class FluidDispenseUtil {

	private FluidDispenseUtil() {
		// Static class.
	}

	/**
	 * Start dispensing {@code pipeFluid}, if possible.
	 *
	 * @param level      The world.
	 * @param frontPos   Position of the block in front of the pipe.
	 * @param frontState BlockState of block in front of the pipe.
	 * @param pipeFluid  Fluid to dispense.
	 * @return true if state changed.
	 */
	public static boolean startDispense(
		@NotNull Level level, @NotNull BlockPos frontPos, @NotNull BlockState frontState,
		@NotNull PipeFluid pipeFluid
	) {
		if (frontState.isAir()) {
			// If there is an empty space in front of the pipe ...
			if (pipeFluid == PipeFluid.WATER) {
				// ... and there is water in the pipe then start dispensing water.
				level.setBlockAndUpdate(frontPos,
					Blocks.WATER.defaultBlockState());
				return true;
			} else {
				if (pipeFluid == PipeFluid.LAVA) {
					// ... and there is lava in the pipe then start dispensing lava.
					level.setBlockAndUpdate(frontPos,
						Blocks.LAVA.defaultBlockState());
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Stop dispensing {@code pipeFluid}, if possible.
	 *
	 * @param level      The world.
	 * @param frontPos   Position of the block in front of the pipe.
	 * @param frontState BlockState of block in front of the pipe.
	 * @param pipeFluid  Fluid to dispense.
	 * @return true if state changed.
	 */
	public static boolean stopDispense(
		@NotNull Level level, @NotNull BlockPos frontPos, @NotNull BlockState frontState,
		@NotNull PipeFluid pipeFluid
	) {
		if (frontState.is(Blocks.WATER)) {
			if (pipeFluid != PipeFluid.WATER) {
				// If the block in front of the pipe is water, but the pipe
				// is not carrying water then remove the block and stop dispensing.
				level.setBlockAndUpdate(frontPos,
					Blocks.AIR.defaultBlockState());
				return true;
			}
		} else if (frontState.is(Blocks.LAVA)) {
			if (pipeFluid != PipeFluid.LAVA) {
				// If the block in front of the pipe is lava, but the pipe
				// is not carrying lava then remove the block and stop dispensing.
				level.setBlockAndUpdate(frontPos,
					Blocks.AIR.defaultBlockState());
				return true;
			}
		} else {
			// If the block in front of the pipe is neither water nor lava,
			// then stop dispensing, but do not change the block.
			return true;
		}
		return false;
	}

	/**
	 * Remove the outflow prior to breaking or turning the block.
	 * <p>
	 * Do not update the state, because it may be called before the break,
	 * and updating is not allowed there.
	 *
	 * @param level The world.
	 * @param pos   Position of the block.
	 * @param state BlockState of the block.
	 */
	public static void removeOutflow(
		@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		// This block.
		final Direction facing = state.getValue(BlockStateProperties.FACING);
		final PipeFluid fluid = state.getValue(ModProperties.FLUID);
		final boolean outflow = state.getValue(OUTFLOW);
		// The block in front of this.
		final BlockPos frontPos = pos.relative(facing);
		final BlockState frontState = level.getBlockState(frontPos);
		if (outflow) {
			if (frontState.is(Blocks.WATER)) {
				if (fluid == PipeFluid.WATER) {
					// If the block in front of the pipe is water, and the pipe
					// is carrying water then remove the block.
					level.setBlockAndUpdate(frontPos, Blocks.AIR.defaultBlockState());
				}
			} else if (frontState.is(Blocks.LAVA)) {
				if (fluid == PipeFluid.LAVA) {
					// If the block in front of the pipe is lava, and the pipe
					// is carrying lava then remove the block.
					level.setBlockAndUpdate(frontPos, Blocks.AIR.defaultBlockState());
				}
			}
		}
		// outflow shall be set to false and fluid to NONE, but not here.
	}

	/**
	 * Break the pipe carrying lava with some probability.
	 *
	 * @param level The world.
	 * @param pos   Position of the block.
	 * @param state BlockState of the block.
	 * @return true if state changed.
	 */
	@SuppressWarnings("UnusedReturnValue")
	public static boolean breakFire(
		@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		final PipeFluid fluid = state.getValue(ModProperties.FLUID);
		final boolean waterlogged = state.getValueOrElse(BlockStateProperties.WATERLOGGED, false);
		if (!waterlogged && fluid == PipeFluid.LAVA) {
			final CanCarryFluid block = (CanCarryFluid) state.getBlock();
			final boolean fire =
				level.random.nextFloat() < block.getFireBreakProbability();
			if (fire) {
				// Replace the pipe with fire.
				level.setBlockAndUpdate(pos,
					Blocks.FIRE.defaultBlockState());
				return true;
			}
		}
		return false;
	}

	/**
	 * Dispense fluid into the world.
	 *
	 * @return true if state is changed.
	 */
	@SuppressWarnings({"UnusedReturnValue", "unused"})
	public static boolean dispense(
		@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		// This block.
		final Direction facing = state.getValue(BlockStateProperties.FACING);
		final boolean outflow = state.getValue(OUTFLOW);
		final PipeFluid pipeFluid = state.getValue(ModProperties.FLUID);
		// The block in front of this.
		final BlockPos frontPos = pos.relative(facing);
		final BlockState frontState = level.getBlockState(frontPos);
		final Block frontBlock = frontState.getBlock();
		// Logic.
		if (!outflow) {
			if (startDispense(level, frontPos, frontState, pipeFluid)) {
				// Start dispensing.
				level.setBlockAndUpdate(pos, state
					.setValue(OUTFLOW, true));
				return true;
			}
		} else {
			if (stopDispense(level, frontPos, frontState, pipeFluid)) {
				// Stop dispensing.
				level.setBlockAndUpdate(pos, state
					.setValue(OUTFLOW, false));
				return true;
			}
		}
		return false;
	}
}
