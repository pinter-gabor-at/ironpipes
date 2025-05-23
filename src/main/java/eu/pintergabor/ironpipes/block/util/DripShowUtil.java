package eu.pintergabor.ironpipes.block.util;

import eu.pintergabor.ironpipes.block.CanCarryFluid;
import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;


public final class DripShowUtil {

	private DripShowUtil() {
		// Static class.
	}

	/**
	 * @return a random number in the range of [-0.25…+0.25]
	 */
	private static float getDripRnd(RandomSource random) {
		return random.nextFloat() / 2F - 0.25F;
	}

	/**
	 * Show dripping particles.
	 *
	 * @param level   The world.
	 * @param pos     Pipe of fitting position.
	 * @param state   Pipe of fitting state
	 * @param yOffset Y offset of the dripping particle
	 *                from the center bottom of the pipe or fitting.
	 */
	public static void showDrip(
		@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state,
		double yOffset
	) {
		RandomSource random = level.random;
		// This block.
		CanCarryFluid block = (CanCarryFluid) state.getBlock();
		PipeFluid fluid = CanCarryFluid.getFluid(state);
		boolean waterDripping =
			random.nextFloat() <
				block.getWaterDrippingProbability() + block.getWateringProbability() * 0.2F;
		boolean lavaDripping =
			random.nextFloat() <
				block.getLavaDrippingProbability() * 100F;
		if ((waterDripping && fluid == PipeFluid.WATER) ||
			lavaDripping && fluid == PipeFluid.LAVA) {
			// Particle position.
			float rx = getDripRnd(random);
			float rz = getDripRnd(random);
			Vec3 pPos = pos.getBottomCenter().add(rx, yOffset, rz);
			level.addParticle(
				fluid == PipeFluid.WATER ? ParticleTypes.DRIPPING_WATER : ParticleTypes.DRIPPING_LAVA,
				pPos.x(), pPos.y(), pPos.z(),
				0.0, 0.0, 0.0);
		}
	}
}
