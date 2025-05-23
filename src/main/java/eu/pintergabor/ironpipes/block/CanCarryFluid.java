package eu.pintergabor.ironpipes.block;

import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.util.ModProperties;

import net.minecraft.world.level.block.state.BlockState;


/**
 * A pipe or fitting capable of storing or carrying water or lava.
 */
public interface CanCarryFluid {

	/**
	 * Get the fluid in the pipe.
	 *
	 * @param state {@link BlockState} of the pipe.
	 */
	static PipeFluid getFluid(BlockState state) {
		return state.getValueOrElse(ModProperties.FLUID, PipeFluid.NONE);
	}

	/**
	 * @return true if the pipe can carry water.
	 */
	default boolean canCarryWater() {
		return true;
	}

	/**
	 * @return true if the pipe can carry water.
	 */
	default boolean canCarryLava() {
		return true;
	}

	/**
	 * Get clogging probability.
	 *
	 * @return 0.0 = never clogs, 1.0 = always clogs.
	 */
	default float getCloggingProbability() {
		return 0F;
	}

	/**
	 * Get fire break probability.
	 * (The probability that the pipe breaks and gets replaced by fire.)
	 *
	 * @return 0.0 = never breaks.
	 */
	default float getFireBreakProbability() {
		return 0F;
	}

	/**
	 * Get fire drip probability.
	 * (The probability that lava dripping from the pipe causes fire.)
	 *
	 * @return 0.0 = dripping lava does not cause fire.
	 */
	default float getFireDripProbability() {
		return 0F;
	}

	/**
	 * Get watering efficiency probability.
	 *
	 * @return 1.0 = Reliable water source.
	 */
	default float getWateringProbability() {
		return 1F;
	}

	/**
	 * Get water dripping probability.
	 *
	 * @return 0.0 = no dripping.
	 */
	default float getWaterDrippingProbability() {
		return 0F;
	}

	/**
	 * Get lava dripping probability.
	 *
	 * @return 0.0 = no dripping.
	 */
	default float getLavaDrippingProbability() {
		return 0F;
	}

	/**
	 * Get water cauldron filling probability.
	 *
	 * @return 0.0 = no filling.
	 */
	default float getWaterFillingProbability() {
		return 1F;
	}

	/**
	 * Get lava cauldron filling probability.
	 *
	 * @return 0.0 = no filling.
	 */
	default float getLavaFillingProbability() {
		return 1F;
	}
}
