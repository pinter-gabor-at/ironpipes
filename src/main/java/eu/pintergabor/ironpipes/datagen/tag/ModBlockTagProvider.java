package eu.pintergabor.ironpipes.datagen.tag;

import java.util.concurrent.CompletableFuture;

import eu.pintergabor.ironpipes.registry.SimpleCopperPipesBlocks;
import eu.pintergabor.ironpipes.tag.ModBlockTags;

import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;


public final class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registries) {
        super(output, registries);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup provider) {
        // Remove pipes and fittings only with a pickaxe.
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
            .addOptionalTag(ModBlockTags.COPPER_PIPES)
            .addOptionalTag(ModBlockTags.COPPER_FITTINGS);
        // Pipes.
        getOrCreateTagBuilder(ModBlockTags.COPPER_PIPES)
            .add(SimpleCopperPipesBlocks.COPPER_PIPE)
            .add(SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE)
            .add(SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE)
            .add(SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE)
            .add(SimpleCopperPipesBlocks.WAXED_COPPER_PIPE)
            .add(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE)
            .add(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE)
            .add(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE);
        // Fittings.
        getOrCreateTagBuilder(ModBlockTags.COPPER_FITTINGS)
            .add(SimpleCopperPipesBlocks.COPPER_FITTING)
            .add(SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING)
            .add(SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING)
            .add(SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING)
            .add(SimpleCopperPipesBlocks.WAXED_COPPER_FITTING)
            .add(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING)
            .add(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING)
            .add(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING);
        // Waxed pipes and fittings.
        getOrCreateTagBuilder(ModBlockTags.WAXED)
            .add(SimpleCopperPipesBlocks.WAXED_COPPER_PIPE)
            .add(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE)
            .add(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE)
            .add(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE)
            .add(SimpleCopperPipesBlocks.WAXED_COPPER_FITTING)
            .add(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING)
            .add(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING)
            .add(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING);
        // Silent pipes for the future.
        getOrCreateTagBuilder(ModBlockTags.SILENT_PIPES);
    }
}
