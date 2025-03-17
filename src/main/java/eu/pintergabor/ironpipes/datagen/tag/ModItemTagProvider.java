package eu.pintergabor.ironpipes.datagen.tag;

import java.util.concurrent.CompletableFuture;

import eu.pintergabor.ironpipes.registry.ModBlocks;
import eu.pintergabor.ironpipes.tag.ModItemTags;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;


public final class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registries) {
        super(output, registries);
    }

    /**
     * Add an array of blocks as items to an item tag.
     */
    private void add(TagKey<Item> key, Block[] blocks) {
        FabricTagProvider<Item>.FabricTagBuilder builder = getOrCreateTagBuilder(key);
        for (Block b : blocks) {
            builder.add(b.asItem());
        }
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup provider) {
        // Wooden pipes.
        add(ModItemTags.WOODEN_PIPES, ModBlocks.WOODEN_PIPES);
        // Wooden fittings.
        add(ModItemTags.WOODEN_FITTINGS, ModBlocks.WOODEN_FITTINGS);
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
            .addOptionalTag(ModItemTags.WOODEN_FITTINGS)
            .addOptionalTag(ModItemTags.COPPER_PIPES)
            .addOptionalTag(ModItemTags.COPPER_FITTINGS);
    }
}
