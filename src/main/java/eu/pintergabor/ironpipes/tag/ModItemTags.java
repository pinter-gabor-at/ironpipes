package eu.pintergabor.ironpipes.tag;

import eu.pintergabor.ironpipes.Global;
import org.jetbrains.annotations.NotNull;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

public class ModItemTags {
    public static final TagKey<Item> COPPER_PIPES = bind("copper_pipes");
    public static final TagKey<Item> COPPER_FITTINGS = bind("copper_fittings");
    public static final TagKey<Item> IGNORES_COPPER_PIPE_MENU = bind("ignores_copper_pipe_menu");

    @NotNull
    private static TagKey<Item> bind(@NotNull String path) {
        return TagKey.of(Registries.ITEM.getKey(), Global.modId(path));
    }
}
