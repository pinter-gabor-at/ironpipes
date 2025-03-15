package eu.pintergabor.ironpipes.registry;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;


public class ModCreativeInventorySorting {

    public static void init() {
        // Creative tabs, functional item group.
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(
            entries -> {
                // Add wooden pipes and fittings after the cauldron.
                entries.addAfter(Blocks.CAULDRON,
                    ModBlocks.WOODEN_PIPES);
                // Add normal copper pipes and fittings after similar copper bulbs.
                entries.addAfter(Items.COPPER_BULB,
                    ModBlocks.COPPER_PIPE, ModBlocks.COPPER_FITTING);
                entries.addAfter(Items.EXPOSED_COPPER_BULB,
                    ModBlocks.EXPOSED_COPPER_PIPE, ModBlocks.EXPOSED_COPPER_FITTING);
                entries.addAfter(Items.WEATHERED_COPPER_BULB,
                    ModBlocks.WEATHERED_COPPER_PIPE, ModBlocks.WEATHERED_COPPER_FITTING);
                entries.addAfter(Items.OXIDIZED_COPPER_BULB,
                    ModBlocks.OXIDIZED_COPPER_PIPE, ModBlocks.OXIDIZED_COPPER_FITTING);
                // Add waxed copper pipes and fittings after similar waxed copper bulbs.
                entries.addAfter(Items.WAXED_COPPER_BULB,
                    ModBlocks.WAXED_COPPER_PIPE, ModBlocks.WAXED_COPPER_FITTING);
                entries.addAfter(Items.WAXED_EXPOSED_COPPER_BULB,
                    ModBlocks.WAXED_EXPOSED_COPPER_PIPE, ModBlocks.WAXED_EXPOSED_COPPER_FITTING);
                entries.addAfter(Items.WAXED_WEATHERED_COPPER_BULB,
                    ModBlocks.WAXED_WEATHERED_COPPER_PIPE, ModBlocks.WAXED_WEATHERED_COPPER_FITTING);
                entries.addAfter(Items.WAXED_OXIDIZED_COPPER_BULB,
                    ModBlocks.WAXED_OXIDIZED_COPPER_PIPE, ModBlocks.WAXED_OXIDIZED_COPPER_FITTING);
            });
        // Creative tabs, redstone item group.
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(
            entries -> {
                // Add waxed copper pipes and fittings after similar waxed copper bulbs.
                entries.addAfter(Items.WAXED_COPPER_BULB,
                    ModBlocks.WAXED_COPPER_PIPE, ModBlocks.WAXED_COPPER_FITTING);
                entries.addAfter(Items.WAXED_EXPOSED_COPPER_BULB,
                    ModBlocks.WAXED_EXPOSED_COPPER_PIPE, ModBlocks.WAXED_EXPOSED_COPPER_FITTING);
                entries.addAfter(Items.WAXED_WEATHERED_COPPER_BULB,
                    ModBlocks.WAXED_WEATHERED_COPPER_PIPE, ModBlocks.WAXED_WEATHERED_COPPER_FITTING);
                entries.addAfter(Items.WAXED_OXIDIZED_COPPER_BULB,
                    ModBlocks.WAXED_OXIDIZED_COPPER_PIPE, ModBlocks.WAXED_OXIDIZED_COPPER_FITTING);
            });
    }
}
