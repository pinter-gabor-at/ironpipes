package eu.pintergabor.ironpipes.registry;

import java.util.function.Function;

import eu.pintergabor.ironpipes.Global;
import eu.pintergabor.ironpipes.blockold.CopperFitting;
import eu.pintergabor.ironpipes.blockold.CopperPipe;
import eu.pintergabor.ironpipes.block.WoodenPipe;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Oxidizable;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;


public final class ModBlocks {
    // Wooden pipes
    public static final Block OAK_PIPE = register("oak_pipe",
        WoodenPipe::new,
        //properties -> new CopperPipe(Oxidizable.OxidationLevel.UNAFFECTED, properties, 2, 20),
        AbstractBlock.Settings.create()
            .requiresTool()
            .strength(0.5F, 1F)
            .sounds(BlockSoundGroup.WOOD)
    );
    // Copper pipes
    public static final Block COPPER_PIPE = register("copper_pipe",
        properties -> new CopperPipe(Oxidizable.OxidationLevel.UNAFFECTED, properties, 2, 20),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.ORANGE)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block EXPOSED_COPPER_PIPE = register("exposed_copper_pipe",
        properties -> new CopperPipe(Oxidizable.OxidationLevel.EXPOSED, properties, 2, 18),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block WEATHERED_COPPER_PIPE = register("weathered_copper_pipe",
        properties -> new CopperPipe(Oxidizable.OxidationLevel.WEATHERED, properties, 2, 15),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.DARK_AQUA)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block OXIDIZED_COPPER_PIPE = register("oxidized_copper_pipe",
        properties -> new CopperPipe(Oxidizable.OxidationLevel.WEATHERED, properties, 2, 12),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.TEAL)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    // Waxed copper pipes.
    public static final Block WAXED_COPPER_PIPE = register("waxed_copper_pipe",
        properties -> new CopperPipe(Oxidizable.OxidationLevel.UNAFFECTED, properties, 1, 20),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.ORANGE)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block WAXED_EXPOSED_COPPER_PIPE = register("waxed_exposed_copper_pipe",
        properties -> new CopperPipe(Oxidizable.OxidationLevel.EXPOSED, properties, 1, 18),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block WAXED_WEATHERED_COPPER_PIPE = register("waxed_weathered_copper_pipe",
        properties -> new CopperPipe(Oxidizable.OxidationLevel.WEATHERED, properties, 1, 15),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.DARK_AQUA)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block WAXED_OXIDIZED_COPPER_PIPE = register("waxed_oxidized_copper_pipe",
        properties -> new CopperPipe(Oxidizable.OxidationLevel.WEATHERED, properties, 1, 12),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.TEAL)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    // Copper fittings.
    public static final Block COPPER_FITTING = register("copper_fitting",
        properties -> new CopperFitting(Oxidizable.OxidationLevel.UNAFFECTED, properties, 1),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.ORANGE)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block EXPOSED_COPPER_FITTING = register("exposed_copper_fitting",
        properties -> new CopperFitting(Oxidizable.OxidationLevel.EXPOSED, properties, 1),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block WEATHERED_COPPER_FITTING = register("weathered_copper_fitting",
        properties -> new CopperFitting(Oxidizable.OxidationLevel.WEATHERED, properties, 1),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.DARK_AQUA)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block OXIDIZED_COPPER_FITTING = register("oxidized_copper_fitting",
        properties -> new CopperFitting(Oxidizable.OxidationLevel.OXIDIZED, properties, 1),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.TEAL)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    // Waxed copper fittings.
    public static final Block WAXED_COPPER_FITTING = register("waxed_copper_fitting",
        properties -> new CopperFitting(Oxidizable.OxidationLevel.UNAFFECTED, properties, 0),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.ORANGE)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block WAXED_EXPOSED_COPPER_FITTING = register("waxed_exposed_copper_fitting",
        properties -> new CopperFitting(Oxidizable.OxidationLevel.EXPOSED, properties, 0),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block WAXED_WEATHERED_COPPER_FITTING = register("waxed_weathered_copper_fitting",
        properties -> new CopperFitting(Oxidizable.OxidationLevel.WEATHERED, properties, 0),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.DARK_AQUA)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block WAXED_OXIDIZED_COPPER_FITTING = register("waxed_oxidized_copper_fitting",
        properties -> new CopperFitting(Oxidizable.OxidationLevel.OXIDIZED, properties, 0),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.TEAL)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );

    public static void init() {
        OxidizableBlocksRegistry.registerOxidizableBlockPair(COPPER_PIPE, EXPOSED_COPPER_PIPE);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(EXPOSED_COPPER_PIPE, WEATHERED_COPPER_PIPE);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(WEATHERED_COPPER_PIPE, OXIDIZED_COPPER_PIPE);

        OxidizableBlocksRegistry.registerOxidizableBlockPair(COPPER_FITTING, EXPOSED_COPPER_FITTING);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(EXPOSED_COPPER_FITTING, WEATHERED_COPPER_FITTING);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(WEATHERED_COPPER_FITTING, OXIDIZED_COPPER_FITTING);

        OxidizableBlocksRegistry.registerWaxableBlockPair(COPPER_PIPE, WAXED_COPPER_PIPE);
        OxidizableBlocksRegistry.registerWaxableBlockPair(EXPOSED_COPPER_PIPE, WAXED_EXPOSED_COPPER_PIPE);
        OxidizableBlocksRegistry.registerWaxableBlockPair(WEATHERED_COPPER_PIPE, WAXED_WEATHERED_COPPER_PIPE);
        OxidizableBlocksRegistry.registerWaxableBlockPair(OXIDIZED_COPPER_PIPE, WAXED_OXIDIZED_COPPER_PIPE);

        OxidizableBlocksRegistry.registerWaxableBlockPair(COPPER_FITTING, WAXED_COPPER_FITTING);
        OxidizableBlocksRegistry.registerWaxableBlockPair(EXPOSED_COPPER_FITTING, WAXED_EXPOSED_COPPER_FITTING);
        OxidizableBlocksRegistry.registerWaxableBlockPair(WEATHERED_COPPER_FITTING, WAXED_WEATHERED_COPPER_FITTING);
        OxidizableBlocksRegistry.registerWaxableBlockPair(OXIDIZED_COPPER_FITTING, WAXED_OXIDIZED_COPPER_FITTING);
    }

    private static <T extends Block> T registerWithoutItem(String path, Function<AbstractBlock.Settings, T> block, AbstractBlock.Settings properties) {
        Identifier id = Global.modId(path);
        return doRegister(id, makeBlock(block, properties, id));
    }

    private static <T extends Block> T register(String path, Function<AbstractBlock.Settings, T> block, AbstractBlock.Settings properties) {
        T registered = registerWithoutItem(path, block, properties);
        Items.register(registered);
        return registered;
    }

    private static <T extends Block> T doRegister(Identifier id, T block) {
        if (Registries.BLOCK.getEntry(id).isEmpty()) {
            return Registry.register(Registries.BLOCK, id, block);
        }
        throw new IllegalArgumentException("Block with id " + id + " is already in the block registry.");
    }

    private static <T extends Block> T makeBlock(Function<AbstractBlock.Settings, T> function, AbstractBlock.Settings properties, Identifier id) {
        return function.apply(properties.registryKey(RegistryKey.of(Registries.BLOCK.getKey(), id)));
    }
}
