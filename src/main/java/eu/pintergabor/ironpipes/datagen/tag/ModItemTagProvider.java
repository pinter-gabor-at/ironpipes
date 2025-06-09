package eu.pintergabor.ironpipes.datagen.tag;

import static net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import eu.pintergabor.ironpipes.Mod;
import eu.pintergabor.ironpipes.registry.ModFluidBlocks;
import eu.pintergabor.ironpipes.registry.ModItemBlocks;
import eu.pintergabor.ironpipes.tag.ModItemTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;


public final class ModItemTagProvider extends ItemTagProvider {

	public ModItemTagProvider(
		FabricDataOutput output,
		CompletableFuture<HolderLookup.Provider> completableFuture
	) {
		super(output, completableFuture);
	}

	/**
	 * Add an array of blocks as items to an item tag.
	 */
	private void add(TagKey<Item> key, Block[] blocks) {
		final FabricTagBuilder builder = getOrCreateTagBuilder(key);
		Arrays.stream(blocks).map(Block::asItem).forEach(builder::add);
	}

	/**
	 * Create all item tags.
	 */
	@Override
	protected void addTags(HolderLookup.Provider wrapperLookup) {
		// Fluid pipes.
		add(ModItemTags.WOODEN_PIPES, ModFluidBlocks.WOODEN_PIPES);
		add(ModItemTags.STONE_PIPES, ModFluidBlocks.STONE_PIPES);
		// Fluid fittings.
		add(ModItemTags.WOODEN_FITTINGS, ModFluidBlocks.WOODEN_FITTINGS);
		add(ModItemTags.STONE_FITTINGS, ModFluidBlocks.STONE_FITTINGS);
		// All fluid pipes and fittings.
		getOrCreateTagBuilder(ModItemTags.FLUID_PIPES_AND_FITTINGS)
			.addOptionalTag(ModItemTags.WOODEN_PIPES)
			.addOptionalTag(ModItemTags.WOODEN_FITTINGS)
			.addOptionalTag(ModItemTags.STONE_PIPES)
			.addOptionalTag(ModItemTags.STONE_FITTINGS);
		// Item pipes.
		add(ModItemTags.COPPER_PIPES, ModItemBlocks.COPPER_PIPES);
		// Item fittings.
		add(ModItemTags.COPPER_FITTINGS, ModItemBlocks.COPPER_FITTINGS);
		// All item pipes and fittings.
		getOrCreateTagBuilder(ModItemTags.ITEM_PIPES_AND_FITTINGS)
			.addOptionalTag(ModItemTags.COPPER_PIPES)
			.addOptionalTag(ModItemTags.COPPER_FITTINGS)
			.add(ModItemBlocks.IRON_PIPE.asItem(), ModItemBlocks.GOLD_PIPE.asItem())
			.add(ModItemBlocks.IRON_FITTING.asItem(), ModItemBlocks.GOLD_FITTING.asItem());
	}
}
