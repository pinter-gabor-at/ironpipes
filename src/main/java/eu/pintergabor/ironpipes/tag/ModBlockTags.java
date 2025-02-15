package eu.pintergabor.ironpipes.tag;

import eu.pintergabor.ironpipes.Global;
import org.jetbrains.annotations.NotNull;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

public class ModBlockTags {
    public static final TagKey<Block> COPPER_PIPES = bind("copper_pipes");
    public static final TagKey<Block> COPPER_FITTINGS = bind("copper_fittings");
    public static final TagKey<Block> WAXED = bind("waxed");
    public static final TagKey<Block> SILENT_PIPES = bind("silent_pipes");

    @NotNull
    private static TagKey<Block> bind(@NotNull String path) {
        return TagKey.of(Registries.BLOCK.getKey(), Global.modId(path));
    }
}
