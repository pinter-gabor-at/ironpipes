package eu.pintergabor.ironpipes.block;

import eu.pintergabor.ironpipes.block.settings.FluidBlockSettings;


public sealed interface FluidCarryBlock extends CanCarryFluid
	permits FluidFitting, FluidPipe {

	/**
	 * How fast is this block?
	 * Higher value means slower operation.
	 * Min 2.
	 */
	default int getTickRate() {
		return 10;
	}

	/**
	 * See {@link FluidBlockSettings}.
	 */
	default FluidBlockSettings getFluidBlockSettings() {
		return new FluidBlockSettings(
			getTickRate(), canCarryWater(), canCarryLava(),
			getCloggingProbability(), getFireBreakProbability(),
			getFireDripProbability(), getWateringProbability(),
			getWaterDrippingProbability(), getLavaDrippingProbability(),
			getWaterFillingProbability(), getLavaFillingProbability());
	}
}
