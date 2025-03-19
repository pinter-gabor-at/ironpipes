package eu.pintergabor.ironpipes.datagen.loot;

import java.util.concurrent.CompletableFuture;

import eu.pintergabor.ironpipes.registry.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;


public final class ModBlockLootProvider extends FabricBlockLootTableProvider {

    public ModBlockLootProvider(
        FabricDataOutput dataOutput,
        CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    /**
     * Generate addDrop for an array of simple blocks.
     */
    private void generateSimpleDrops(Block[] blocks){
        for (Block b: blocks){
            addDrop(b);
        }
    }

    /**
     * Generate all addDrop.
     */
    @Override
    public void generate() {
        // Wooden pipes.
        generateSimpleDrops(ModBlocks.WOODEN_PIPES);
        // Wooden fittings.
        generateSimpleDrops(ModBlocks.WOODEN_FITTINGS);
        // Copper pipes.
        addDrop(ModBlocks.COPPER_PIPE);
        addDrop(ModBlocks.EXPOSED_COPPER_PIPE);
        addDrop(ModBlocks.WEATHERED_COPPER_PIPE);
        addDrop(ModBlocks.OXIDIZED_COPPER_PIPE);
        // Waxed copper pipes.
        addDrop(ModBlocks.WAXED_COPPER_PIPE);
        addDrop(ModBlocks.WAXED_EXPOSED_COPPER_PIPE);
        addDrop(ModBlocks.WAXED_WEATHERED_COPPER_PIPE);
        addDrop(ModBlocks.WAXED_OXIDIZED_COPPER_PIPE);
        // Copper fittings.
        addDrop(ModBlocks.COPPER_FITTING);
        addDrop(ModBlocks.EXPOSED_COPPER_FITTING);
        addDrop(ModBlocks.WEATHERED_COPPER_FITTING);
        addDrop(ModBlocks.OXIDIZED_COPPER_FITTING);
        // Waxed copper fittings.
        addDrop(ModBlocks.WAXED_COPPER_FITTING);
        addDrop(ModBlocks.WAXED_EXPOSED_COPPER_FITTING);
        addDrop(ModBlocks.WAXED_WEATHERED_COPPER_FITTING);
        addDrop(ModBlocks.WAXED_OXIDIZED_COPPER_FITTING);
    }
}
