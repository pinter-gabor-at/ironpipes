package eu.pintergabor.ironpipes.datagen.tag;

import java.util.concurrent.CompletableFuture;

import eu.pintergabor.ironpipes.registry.ModBlocks;
import eu.pintergabor.ironpipes.tag.ModItemTags;

import net.minecraft.registry.RegistryWrapper;

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
            .add(ModBlocks.COPPER_PIPE.asItem())
            .add(ModBlocks.EXPOSED_COPPER_PIPE.asItem())
            .add(ModBlocks.WEATHERED_COPPER_PIPE.asItem())
            .add(ModBlocks.OXIDIZED_COPPER_PIPE.asItem())
            .add(ModBlocks.WAXED_COPPER_PIPE.asItem())
            .add(ModBlocks.WAXED_EXPOSED_COPPER_PIPE.asItem())
            .add(ModBlocks.WAXED_WEATHERED_COPPER_PIPE.asItem())
            .add(ModBlocks.WAXED_OXIDIZED_COPPER_PIPE.asItem());
        // Fittings.
        this.getOrCreateTagBuilder(ModItemTags.COPPER_FITTINGS)
            .add(ModBlocks.COPPER_FITTING.asItem())
            .add(ModBlocks.EXPOSED_COPPER_FITTING.asItem())
            .add(ModBlocks.WEATHERED_COPPER_FITTING.asItem())
            .add(ModBlocks.OXIDIZED_COPPER_FITTING.asItem())
            .add(ModBlocks.WAXED_COPPER_FITTING.asItem())
            .add(ModBlocks.WAXED_EXPOSED_COPPER_FITTING.asItem())
            .add(ModBlocks.WAXED_WEATHERED_COPPER_FITTING.asItem())
            .add(ModBlocks.WAXED_OXIDIZED_COPPER_FITTING.asItem());
        // All pipes and fittings.
        this.getOrCreateTagBuilder(ModItemTags.PIPES_AND_FITTINGS)
            .addOptionalTag(ModItemTags.COPPER_PIPES)
            .addOptionalTag(ModItemTags.COPPER_FITTINGS);
    }
}
