package eu.pintergabor.ironpipes.tag;

import eu.pintergabor.ironpipes.Global;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;


public final class ModBlockTags {
	public static final TagKey<Block> WOODEN_PIPES = bind("wooden_pipes");
	public static final TagKey<Block> WOODEN_FITTINGS = bind("wooden_fittings");
    public static final TagKey<Block> STONE_PIPES = bind("stone_pipes");
    public static final TagKey<Block> STONE_FITTINGS = bind("stone_fittings");
    public static final TagKey<Block> ITEM_PIPES = bind("item_pipes");
    public static final TagKey<Block> ITEM_FITTINGS = bind("item_fittings");
    public static final TagKey<Block> COPPER_PIPES = bind("copper_pipes");
    public static final TagKey<Block> COPPER_FITTINGS = bind("copper_fittings");

	private ModBlockTags() {
		// Static class.
	}

	@NotNull
	private static TagKey<Block> bind(@NotNull String path) {
		return TagKey.create(Registries.BLOCK, Global.modId(path));
	}
}
