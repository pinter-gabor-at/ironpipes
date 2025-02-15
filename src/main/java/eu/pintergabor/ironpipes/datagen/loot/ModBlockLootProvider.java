package eu.pintergabor.ironpipes.datagen.loot;

import java.util.concurrent.CompletableFuture;

import eu.pintergabor.ironpipes.registry.SimpleCopperPipesBlocks;

import net.minecraft.registry.RegistryWrapper;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;


public final class ModBlockLootProvider extends FabricBlockLootTableProvider {

    public ModBlockLootProvider(
        FabricDataOutput dataOutput,
        CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generate() {
        // Pipes.
        drops(SimpleCopperPipesBlocks.COPPER_PIPE);
        drops(SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE);
        drops(SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE);
        drops(SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE);
        // Waxed pipes.
        drops(SimpleCopperPipesBlocks.WAXED_COPPER_PIPE);
        drops(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE);
        drops(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE);
        drops(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE);
        // Fittings.
        drops(SimpleCopperPipesBlocks.COPPER_FITTING);
        drops(SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING);
        drops(SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING);
        drops(SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING);
        // Waxed fitting.
        drops(SimpleCopperPipesBlocks.WAXED_COPPER_FITTING);
        drops(SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING);
        drops(SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING);
        drops(SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING);
    }
}
