package eu.pintergabor.ironpipes.registry;

import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

public class ModCreativeInventorySorting {

    public static void init() {
        // Creative tabs, functional item group.
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(
            entries -> {
                // Add normal copper pipes and fittings after similar copper bulbs.
                entries.addAfter(Items.COPPER_BULB,
                    SimpleCopperPipesBlocks.COPPER_PIPE, SimpleCopperPipesBlocks.COPPER_FITTING);
                entries.addAfter(Items.EXPOSED_COPPER_BULB,
                    SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE, SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING);
                entries.addAfter(Items.WEATHERED_COPPER_BULB,
                    SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE, SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING);
                entries.addAfter(Items.OXIDIZED_COPPER_BULB,
                    SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE, SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING);
                // Add waxed copper pipes and fittings after similar waxed copper bulbs.
                entries.addAfter(Items.WAXED_COPPER_BULB,
                    SimpleCopperPipesBlocks.WAXED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_COPPER_FITTING);
                entries.addAfter(Items.WAXED_EXPOSED_COPPER_BULB,
                    SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING);
                entries.addAfter(Items.WAXED_WEATHERED_COPPER_BULB,
                    SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING);
                entries.addAfter(Items.WAXED_OXIDIZED_COPPER_BULB,
                    SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING);
            });
        // Creative tabs, redstone item group.
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(
            entries -> {
                // Add waxed copper pipes and fittings after similar waxed copper bulbs.
                entries.addAfter(Items.WAXED_COPPER_BULB,
                    SimpleCopperPipesBlocks.WAXED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_COPPER_FITTING);
                entries.addAfter(Items.WAXED_EXPOSED_COPPER_BULB,
                    SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING);
                entries.addAfter(Items.WAXED_WEATHERED_COPPER_BULB,
                    SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING);
                entries.addAfter(Items.WAXED_OXIDIZED_COPPER_BULB,
                    SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE, SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING);
            });
    }
}
