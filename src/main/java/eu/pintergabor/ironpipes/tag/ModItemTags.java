package eu.pintergabor.ironpipes.tag;

import eu.pintergabor.ironpipes.Global;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;


public final class ModItemTags {
	public static final TagKey<Item> WOODEN_PIPES = register("wooden_pipes");
	public static final TagKey<Item> WOODEN_FITTINGS = register("wooden_fittings");
	public static final TagKey<Item> STONE_PIPES = register("stone_pipes");
	public static final TagKey<Item> STONE_FITTINGS = register("stone_fittings");
	public static final TagKey<Item> FLUID_PIPES_AND_FITTINGS = register("fluid_pipes_and_fittings");
	public static final TagKey<Item> COPPER_PIPES = register("copper_pipes");
	public static final TagKey<Item> COPPER_FITTINGS = register("copper_fittings");
	public static final TagKey<Item> ITEM_PIPES_AND_FITTINGS = register("item_pipes_and_fittings");

	private ModItemTags() {
		// Static class.
	}

	private static @NotNull TagKey<Item> register(@NotNull String path) {
		return TagKey.create(Registries.ITEM, Global.modId(path));
	}
}
