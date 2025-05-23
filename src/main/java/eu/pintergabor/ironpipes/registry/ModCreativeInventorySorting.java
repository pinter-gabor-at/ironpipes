package eu.pintergabor.ironpipes.registry;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;


public final class ModCreativeInventorySorting {

	private ModCreativeInventorySorting() {
		// Static class.
	}

	public static void init() {
		// Creative tabs, functional item group.
		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(
			entries -> {
				// Add pipes and fittings after the cauldron.
                entries.addAfter(Blocks.CAULDRON,
                    ModBlocks.ITEM_FITTINGS);
                entries.addAfter(Blocks.CAULDRON,
                    ModBlocks.ITEM_PIPES);
                entries.addAfter(Blocks.CAULDRON,
                    ModBlocks.STONE_FITTINGS);
                entries.addAfter(Blocks.CAULDRON,
                    ModBlocks.STONE_PIPES);
				entries.addAfter(Blocks.CAULDRON,
					ModBlocks.WOODEN_FITTINGS);
				entries.addAfter(Blocks.CAULDRON,
					ModBlocks.WOODEN_PIPES);
			});
	}
}
