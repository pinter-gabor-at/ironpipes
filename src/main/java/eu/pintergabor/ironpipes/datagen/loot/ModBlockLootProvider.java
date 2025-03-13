package eu.pintergabor.ironpipes.datagen.loot;

import java.util.concurrent.CompletableFuture;

import eu.pintergabor.ironpipes.registry.ModBlocks;

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
        drops(ModBlocks.COPPER_PIPE);
        drops(ModBlocks.EXPOSED_COPPER_PIPE);
        drops(ModBlocks.WEATHERED_COPPER_PIPE);
        drops(ModBlocks.OXIDIZED_COPPER_PIPE);
        // Waxed pipes.
        drops(ModBlocks.WAXED_COPPER_PIPE);
        drops(ModBlocks.WAXED_EXPOSED_COPPER_PIPE);
        drops(ModBlocks.WAXED_WEATHERED_COPPER_PIPE);
        drops(ModBlocks.WAXED_OXIDIZED_COPPER_PIPE);
        // Fittings.
        drops(ModBlocks.COPPER_FITTING);
        drops(ModBlocks.EXPOSED_COPPER_FITTING);
        drops(ModBlocks.WEATHERED_COPPER_FITTING);
        drops(ModBlocks.OXIDIZED_COPPER_FITTING);
        // Waxed fitting.
        drops(ModBlocks.WAXED_COPPER_FITTING);
        drops(ModBlocks.WAXED_EXPOSED_COPPER_FITTING);
        drops(ModBlocks.WAXED_WEATHERED_COPPER_FITTING);
        drops(ModBlocks.WAXED_OXIDIZED_COPPER_FITTING);
    }
}
