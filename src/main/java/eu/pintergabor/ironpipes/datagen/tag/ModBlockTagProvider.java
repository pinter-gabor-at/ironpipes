package eu.pintergabor.ironpipes.datagen.tag;

import java.util.concurrent.CompletableFuture;

import eu.pintergabor.ironpipes.registry.ModBlocks;
import eu.pintergabor.ironpipes.tag.ModBlockTags;

import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;


public final class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagProvider(
        FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registries) {
        super(output, registries);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup provider) {
        // Remove pipes and fittings only with a pickaxe,
        // and wooden pipes with axe too.
        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)
            .addOptionalTag(ModBlockTags.WOODEN_PIPES);
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
            .addOptionalTag(ModBlockTags.WOODEN_PIPES)
            .addOptionalTag(ModBlockTags.COPPER_PIPES)
            .addOptionalTag(ModBlockTags.COPPER_FITTINGS);
        // Wooden pipes.
        getOrCreateTagBuilder(ModBlockTags.WOODEN_PIPES)
            .add(ModBlocks.OAK_PIPE);
        // Copper pipes.
        getOrCreateTagBuilder(ModBlockTags.COPPER_PIPES)
            .add(ModBlocks.COPPER_PIPE)
            .add(ModBlocks.EXPOSED_COPPER_PIPE)
            .add(ModBlocks.WEATHERED_COPPER_PIPE)
            .add(ModBlocks.OXIDIZED_COPPER_PIPE)
            .add(ModBlocks.WAXED_COPPER_PIPE)
            .add(ModBlocks.WAXED_EXPOSED_COPPER_PIPE)
            .add(ModBlocks.WAXED_WEATHERED_COPPER_PIPE)
            .add(ModBlocks.WAXED_OXIDIZED_COPPER_PIPE);
        // Copper fittings.
        getOrCreateTagBuilder(ModBlockTags.COPPER_FITTINGS)
            .add(ModBlocks.COPPER_FITTING)
            .add(ModBlocks.EXPOSED_COPPER_FITTING)
            .add(ModBlocks.WEATHERED_COPPER_FITTING)
            .add(ModBlocks.OXIDIZED_COPPER_FITTING)
            .add(ModBlocks.WAXED_COPPER_FITTING)
            .add(ModBlocks.WAXED_EXPOSED_COPPER_FITTING)
            .add(ModBlocks.WAXED_WEATHERED_COPPER_FITTING)
            .add(ModBlocks.WAXED_OXIDIZED_COPPER_FITTING);
        // Waxed copper pipes and fittings.
        getOrCreateTagBuilder(ModBlockTags.WAXED)
            .add(ModBlocks.WAXED_COPPER_PIPE)
            .add(ModBlocks.WAXED_EXPOSED_COPPER_PIPE)
            .add(ModBlocks.WAXED_WEATHERED_COPPER_PIPE)
            .add(ModBlocks.WAXED_OXIDIZED_COPPER_PIPE)
            .add(ModBlocks.WAXED_COPPER_FITTING)
            .add(ModBlocks.WAXED_EXPOSED_COPPER_FITTING)
            .add(ModBlocks.WAXED_WEATHERED_COPPER_FITTING)
            .add(ModBlocks.WAXED_OXIDIZED_COPPER_FITTING);
        // Silent pipes for the future.
        getOrCreateTagBuilder(ModBlockTags.SILENT_PIPES);
    }
}
