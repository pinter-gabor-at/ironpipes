package eu.pintergabor.ironpipes.block.util;

import static eu.pintergabor.ironpipes.block.BaseBlock.DIRECTIONS;
import static eu.pintergabor.ironpipes.block.util.FluidUtil.oneSideSourceFluid;
import static eu.pintergabor.ironpipes.registry.util.ModProperties.FLUID;

import eu.pintergabor.ironpipes.block.CanCarryFluid;
import eu.pintergabor.ironpipes.block.FluidFitting;
import eu.pintergabor.ironpipes.block.entity.FluidFittingEntity;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.util.ModProperties;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;


/**
 * Utilities for fluid fittings.
 */
public final class FluidFittingUtil {

	private FluidFittingUtil() {
		// Static class.
	}

	/**
	 * Get the fluid coming from pipes pointing towards this fitting.
	 *
	 * @param level         The world.
	 * @param pos           Pipe position.
	 * @param canCarryWater Enable carrying water.
	 * @param canCarryLava  Enable carrying lava.
	 * @return The fluid coming from a side.
	 */
	public static PipeFluid sideSourceFluid(
		@NotNull Level level, @NotNull BlockPos pos,
		boolean canCarryWater, boolean canCarryLava
	) {
		for (Direction d : DIRECTIONS) {
			// Check all directions.
			final PipeFluid nFluid = oneSideSourceFluid(
				level, pos, d, canCarryWater, canCarryLava);
			if (nFluid != PipeFluid.NONE) {
				return nFluid;
			}
		}
		return PipeFluid.NONE;
	}

	/**
	 * Break the fitting carrying lava with some probability.
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
				// Replace the fitting with fire.
				level.setBlockAndUpdate(pos,
					Blocks.FIRE.defaultBlockState());
				return true;
			}

		}
		return false;
	}

	/**
	 * Pull fluid from any pipe pointing to this fitting.
	 *
	 * @return true if the state is changed.
	 */
	@SuppressWarnings({"UnusedReturnValue", "unused"})
	public static boolean pull(
		@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state,
		@NotNull FluidFittingEntity entity
	) {
		// This block.
		final PipeFluid pipeFluid = state.getValue(FLUID);
		final FluidFitting block = (FluidFitting) state.getBlock();
		final boolean canCarryWater = block.canCarryWater();
		final boolean canCarryLava = block.canCarryLava();
		// Find a pipe pointing to this pipe from any side.
		PipeFluid sideFluid = sideSourceFluid(
			level, pos,
			canCarryWater, canCarryLava);
		if (sideFluid != PipeFluid.NONE) {
			// Water or lava is coming from the side.
			if (pipeFluid != sideFluid) {
				level.setBlockAndUpdate(pos, state.setValue(FLUID, sideFluid));
				return true;
			}
		} else if (pipeFluid != PipeFluid.NONE) {
			// No source from any side.
			level.setBlockAndUpdate(pos, state.setValue(FLUID, PipeFluid.NONE));
			return true;
		}
		return false;
	}
}
