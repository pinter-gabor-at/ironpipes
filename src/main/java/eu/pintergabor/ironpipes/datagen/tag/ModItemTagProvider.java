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
        // Wooden pipes.
        getOrCreateTagBuilder(ModItemTags.WOODEN_PIPES)
            .add(ModBlocks.OAK_PIPE.asItem())
            .add(ModBlocks.SPRUCE_PIPE.asItem())
            .add(ModBlocks.BIRCH_PIPE.asItem())
            .add(ModBlocks.JUNGLE_PIPE.asItem())
            .add(ModBlocks.ACACIA_PIPE.asItem())
            .add(ModBlocks.CHERRY_PIPE.asItem())
            .add(ModBlocks.DARK_OAK_PIPE.asItem())
            .add(ModBlocks.PALE_OAK_PIPE.asItem())
            .add(ModBlocks.MANGROVE_PIPE.asItem())
            .add(ModBlocks.BAMBOO_PIPE.asItem());
        // Copper pipes.
        getOrCreateTagBuilder(ModItemTags.COPPER_PIPES)
            .add(ModBlocks.COPPER_PIPE.asItem())
            .add(ModBlocks.EXPOSED_COPPER_PIPE.asItem())
            .add(ModBlocks.WEATHERED_COPPER_PIPE.asItem())
            .add(ModBlocks.OXIDIZED_COPPER_PIPE.asItem())
            .add(ModBlocks.WAXED_COPPER_PIPE.asItem())
            .add(ModBlocks.WAXED_EXPOSED_COPPER_PIPE.asItem())
            .add(ModBlocks.WAXED_WEATHERED_COPPER_PIPE.asItem())
            .add(ModBlocks.WAXED_OXIDIZED_COPPER_PIPE.asItem());
        // Copper fittings.
        getOrCreateTagBuilder(ModItemTags.COPPER_FITTINGS)
            .add(ModBlocks.COPPER_FITTING.asItem())
            .add(ModBlocks.EXPOSED_COPPER_FITTING.asItem())
            .add(ModBlocks.WEATHERED_COPPER_FITTING.asItem())
            .add(ModBlocks.OXIDIZED_COPPER_FITTING.asItem())
            .add(ModBlocks.WAXED_COPPER_FITTING.asItem())
            .add(ModBlocks.WAXED_EXPOSED_COPPER_FITTING.asItem())
            .add(ModBlocks.WAXED_WEATHERED_COPPER_FITTING.asItem())
            .add(ModBlocks.WAXED_OXIDIZED_COPPER_FITTING.asItem());
        // All pipes and fittings.
        getOrCreateTagBuilder(ModItemTags.PIPES_AND_FITTINGS)
            .addOptionalTag(ModItemTags.WOODEN_PIPES)
            .addOptionalTag(ModItemTags.COPPER_PIPES)
            .addOptionalTag(ModItemTags.COPPER_FITTINGS);
    }
}
