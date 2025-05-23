package eu.pintergabor.ironpipes.tag;

import eu.pintergabor.ironpipes.Global;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;


public final class ModItemTags {
	public static final TagKey<Item> WOODEN_PIPES = bind("wooden_pipes");
	public static final TagKey<Item> WOODEN_FITTINGS = bind("wooden_fittings");
	public static final TagKey<Item> STONE_PIPES = bind("stone_pipes");
	public static final TagKey<Item> STONE_FITTINGS = bind("stone_fittings");
	public static final TagKey<Item> PIPES_AND_FITTINGS = bind("pipes_and_fittings");

	private ModItemTags() {
		// Static class.
	}

	@NotNull
	private static TagKey<Item> bind(@NotNull String path) {
		return TagKey.create(Registries.ITEM, Global.modId(path));
	}
}
