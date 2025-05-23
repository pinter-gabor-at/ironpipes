package eu.pintergabor.ironpipes.datagen.tag;

import static net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider.ItemTagProvider;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import eu.pintergabor.ironpipes.registry.ModBlocks;
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
		// Pipes.
		add(ModItemTags.WOODEN_PIPES, ModBlocks.WOODEN_PIPES);
		add(ModItemTags.STONE_PIPES, ModBlocks.STONE_PIPES);
		// Fittings.
		add(ModItemTags.WOODEN_FITTINGS, ModBlocks.WOODEN_FITTINGS);
		add(ModItemTags.STONE_FITTINGS, ModBlocks.STONE_FITTINGS);
		// All pipes and fittings.
		getOrCreateTagBuilder(ModItemTags.FLUID_PIPES_AND_FITTINGS)
			.addOptionalTag(ModItemTags.WOODEN_PIPES)
			.addOptionalTag(ModItemTags.WOODEN_FITTINGS)
			.addOptionalTag(ModItemTags.STONE_PIPES)
			.addOptionalTag(ModItemTags.STONE_FITTINGS);
	}
}
