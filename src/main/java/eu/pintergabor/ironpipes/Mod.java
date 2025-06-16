package eu.pintergabor.ironpipes;

import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import eu.pintergabor.ironpipes.registry.ModCreativeInventorySorting;
import eu.pintergabor.ironpipes.registry.ModFluidBlocks;
import eu.pintergabor.ironpipes.registry.ModItemBlocks;
import eu.pintergabor.ironpipes.registry.ModSoundEvents;
import eu.pintergabor.ironpipes.registry.ModStats;
import eu.pintergabor.ironpipes.registry.util.ModBlocksRegister;
import eu.pintergabor.ironpipes.registry.util.ModProperties;

import net.fabricmc.api.ModInitializer;


public final class Mod implements ModInitializer {

	@Override
	public void onInitialize() {
		ModProperties.init();
		ModFluidBlocks.init();
		ModItemBlocks.init();
		ModBlocksRegister.init();
		ModBlockEntities.init();
		ModSoundEvents.init();
		ModStats.init();
		ModCreativeInventorySorting.init();
	}
}
