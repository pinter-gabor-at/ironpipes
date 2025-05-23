package eu.pintergabor.ironpipes.block.util;

import static eu.pintergabor.ironpipes.block.BasePipe.FACING;

import eu.pintergabor.ironpipes.block.CanCarryFluid;
import eu.pintergabor.ironpipes.block.FluidPipe;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.util.ModProperties;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;


/**
 * Utilities common to fluid pipes and fittings.
 */
public final class FluidUtil {

	private FluidUtil() {
		// Static class.
	}

	/**
	 * Clog the pipe or fitting with some probability.
	 *
	 * @return true if the state is changed.
	 */
	@SuppressWarnings({"UnusedReturnValue", "unused"})
	public static boolean clog(
		@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull BlockState state
	) {
		final CanCarryFluid block = (CanCarryFluid) state.getBlock();
		final PipeFluid fluid = state.getValueOrElse(ModProperties.FLUID, PipeFluid.NONE);
		if (fluid != PipeFluid.NONE) {
			final boolean clogging =
				level.random.nextFloat() < block.getCloggingProbability();
			if (clogging) {
				level.setBlockAndUpdate(pos, state
					.setValue(ModProperties.FLUID, PipeFluid.NONE));
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the fluid coming from a pipe in direction {@code d}.
	 *
	 * @param level         The world.
	 * @param pos           Pipe position.
	 * @param dir           Direction to check.
	 * @param canCarryWater Enable carrying water.
	 * @param canCarryLava  Enable carrying lava.
	 * @return The fluid coming from side {@code d}.
	 */
	public static PipeFluid oneSideSourceFluid(
		@NotNull Level level, @NotNull BlockPos pos, @NotNull Direction dir,
		boolean canCarryWater, boolean canCarryLava
	) {
		final BlockState nState = level.getBlockState(pos.relative(dir));
		final Block nBlock = nState.getBlock();
		if (nBlock instanceof FluidPipe &&
			nState.getValue(FACING) == dir.getOpposite()) {
			final PipeFluid nFluid = nState.getValue(ModProperties.FLUID);
			if ((canCarryWater && nFluid == PipeFluid.WATER) ||
				(canCarryLava && nFluid == PipeFluid.LAVA)) {
				// Water or lava is coming from the side.
				return nFluid;
			}
		}
		return PipeFluid.NONE;
	}
}
