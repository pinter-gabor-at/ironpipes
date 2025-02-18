package eu.pintergabor.ironpipes.datagen.tag;

import java.util.concurrent.CompletableFuture;

import eu.pintergabor.ironpipes.registry.SimpleCopperPipesBlocks;
import eu.pintergabor.ironpipes.tag.ModItemTags;

import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;


public final class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registries) {
        super(output, registries);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup provider) {
        // Pipes.
        this.getOrCreateTagBuilder(ModItemTags.COPPER_PIPES)
            .add(SimpleCopperPipesBlocks.COPPER_PIPE.asItem())
            .add(SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE.asItem())
            .add(SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE.asItem())
            .add(SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE.asItem())
            .add(SimpleCopperPipesBlocks.WAXED_COPPER_PIPE.asItem())
            .add(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE.asItem())
            .add(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE.asItem())
            .add(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE.asItem());
        // Fittings.
        this.getOrCreateTagBuilder(ModItemTags.COPPER_FITTINGS)
            .add(SimpleCopperPipesBlocks.COPPER_FITTING.asItem())
            .add(SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING.asItem())
            .add(SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING.asItem())
            .add(SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING.asItem())
            .add(SimpleCopperPipesBlocks.WAXED_COPPER_FITTING.asItem())
            .add(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING.asItem())
            .add(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING.asItem())
            .add(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING.asItem());
        // TODO: What is it?
        this.getOrCreateTagBuilder(ModItemTags.IGNORES_COPPER_PIPE_MENU)
            .addOptionalTag(ModItemTags.COPPER_PIPES)
            .addOptionalTag(ModItemTags.COPPER_FITTINGS)
            .addOptional(Identifier.of("create", "wrench"));
    }
}
