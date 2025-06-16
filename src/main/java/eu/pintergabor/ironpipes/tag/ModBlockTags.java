package eu.pintergabor.ironpipes.tag;

import eu.pintergabor.ironpipes.Global;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;


public final class ModBlockTags {
	public static final TagKey<Block> WOODEN_PIPES = register("wooden_pipes");
	public static final TagKey<Block> WOODEN_FITTINGS = register("wooden_fittings");
	public static final TagKey<Block> STONE_PIPES = register("stone_pipes");
	public static final TagKey<Block> STONE_FITTINGS = register("stone_fittings");
	public static final TagKey<Block> ITEM_PIPES = register("item_pipes");
	public static final TagKey<Block> ITEM_FITTINGS = register("item_fittings");
	public static final TagKey<Block> COPPER_PIPES = register("copper_pipes");
	public static final TagKey<Block> COPPER_FITTINGS = register("copper_fittings");

	private ModBlockTags() {
		// Static class.
	}

	private static @NotNull TagKey<Block> register(@NotNull String path) {
		return TagKey.create(Registries.BLOCK, Global.modId(path));
	}
}
