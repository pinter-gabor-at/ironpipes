package eu.pintergabor.ironpipes.datagen;

import eu.pintergabor.ironpipes.datagen.loot.ModBlockLootProvider;
import eu.pintergabor.ironpipes.datagen.model.ModModelProvider;
import eu.pintergabor.ironpipes.datagen.recipe.ModRecipeRunner;
import eu.pintergabor.ironpipes.datagen.tag.ModBlockTagProvider;
import eu.pintergabor.ironpipes.datagen.tag.ModItemTagProvider;
import org.jetbrains.annotations.NotNull;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;


public final class ModDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(@NotNull FabricDataGenerator dataGenerator) {
		final FabricDataGenerator.Pack pack = dataGenerator.createPack();
		// Assets.
		pack.addProvider(ModModelProvider::new);
		// Data.
		pack.addProvider(ModBlockLootProvider::new);
		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModRecipeRunner::new);
	}
}
