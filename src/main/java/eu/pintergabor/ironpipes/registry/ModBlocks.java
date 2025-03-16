package eu.pintergabor.ironpipes.registry;

import java.util.function.Function;

import eu.pintergabor.ironpipes.Global;
import eu.pintergabor.ironpipes.block.WoodenPipe;
import eu.pintergabor.ironpipes.blockold.CopperFitting;
import eu.pintergabor.ironpipes.blockold.CopperPipe;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.Oxidizable;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;


public final class ModBlocks {
    // Wooden pipes
    public static final WoodenPipe OAK_PIPE =
        registerWoodenPipe("oak_pipe", MapColor.OAK_TAN);
    public static final WoodenPipe SPRUCE_PIPE =
        registerWoodenPipe("spruce_pipe", MapColor.SPRUCE_BROWN);
    public static final WoodenPipe BIRCH_PIPE =
        registerWoodenPipe("birch_pipe", MapColor.PALE_YELLOW);
    public static final WoodenPipe JUNGLE_PIPE =
        registerWoodenPipe("jungle_pipe", MapColor.DIRT_BROWN);
    public static final WoodenPipe ACACIA_PIPE =
        registerWoodenPipe("acacia_pipe", MapColor.ORANGE);
    public static final WoodenPipe CHERRY_PIPE =
        registerWoodenPipe("cherry_pipe", MapColor.TERRACOTTA_WHITE);
    public static final WoodenPipe DARK_OAK_PIPE =
        registerWoodenPipe("dark_oak_pipe", MapColor.BROWN);
    public static final WoodenPipe PALE_OAK_PIPE =
        registerWoodenPipe("pale_oak_pipe", MapColor.OFF_WHITE);
    public static final WoodenPipe MANGROVE_PIPE =
        registerWoodenPipe("mangrove_pipe", MapColor.RED);
    public static final WoodenPipe BAMBOO_PIPE =
        registerWoodenPipe("bamboo_pipe", MapColor.YELLOW);
    public static final WoodenPipe[] WOODEN_PIPES = {
        OAK_PIPE,
        SPRUCE_PIPE,
        BIRCH_PIPE,
        JUNGLE_PIPE,
        ACACIA_PIPE,
        CHERRY_PIPE,
        DARK_OAK_PIPE,
        PALE_OAK_PIPE,
        MANGROVE_PIPE,
        BAMBOO_PIPE,
    };
    // Copper pipes
    public static final Block COPPER_PIPE = registerBlockAndItem("copper_pipe",
        properties -> new CopperPipe(Oxidizable.OxidationLevel.UNAFFECTED, properties, 2, 20),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.ORANGE)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block EXPOSED_COPPER_PIPE = registerBlockAndItem("exposed_copper_pipe",
        properties -> new CopperPipe(Oxidizable.OxidationLevel.EXPOSED, properties, 2, 18),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block WEATHERED_COPPER_PIPE = registerBlockAndItem("weathered_copper_pipe",
        properties -> new CopperPipe(Oxidizable.OxidationLevel.WEATHERED, properties, 2, 15),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.DARK_AQUA)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block OXIDIZED_COPPER_PIPE = registerBlockAndItem("oxidized_copper_pipe",
        properties -> new CopperPipe(Oxidizable.OxidationLevel.WEATHERED, properties, 2, 12),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.TEAL)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    // Waxed copper pipes.
    public static final Block WAXED_COPPER_PIPE = registerBlockAndItem("waxed_copper_pipe",
        properties -> new CopperPipe(Oxidizable.OxidationLevel.UNAFFECTED, properties, 1, 20),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.ORANGE)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block WAXED_EXPOSED_COPPER_PIPE = registerBlockAndItem("waxed_exposed_copper_pipe",
        properties -> new CopperPipe(Oxidizable.OxidationLevel.EXPOSED, properties, 1, 18),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block WAXED_WEATHERED_COPPER_PIPE = registerBlockAndItem("waxed_weathered_copper_pipe",
        properties -> new CopperPipe(Oxidizable.OxidationLevel.WEATHERED, properties, 1, 15),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.DARK_AQUA)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block WAXED_OXIDIZED_COPPER_PIPE = registerBlockAndItem("waxed_oxidized_copper_pipe",
        properties -> new CopperPipe(Oxidizable.OxidationLevel.WEATHERED, properties, 1, 12),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.TEAL)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    // Copper fittings.
    public static final Block COPPER_FITTING = registerBlockAndItem("copper_fitting",
        properties -> new CopperFitting(Oxidizable.OxidationLevel.UNAFFECTED, properties, 1),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.ORANGE)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block EXPOSED_COPPER_FITTING = registerBlockAndItem("exposed_copper_fitting",
        properties -> new CopperFitting(Oxidizable.OxidationLevel.EXPOSED, properties, 1),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block WEATHERED_COPPER_FITTING = registerBlockAndItem("weathered_copper_fitting",
        properties -> new CopperFitting(Oxidizable.OxidationLevel.WEATHERED, properties, 1),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.DARK_AQUA)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block OXIDIZED_COPPER_FITTING = registerBlockAndItem("oxidized_copper_fitting",
        properties -> new CopperFitting(Oxidizable.OxidationLevel.OXIDIZED, properties, 1),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.TEAL)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    // Waxed copper fittings.
    public static final Block WAXED_COPPER_FITTING = registerBlockAndItem("waxed_copper_fitting",
        properties -> new CopperFitting(Oxidizable.OxidationLevel.UNAFFECTED, properties, 0),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.ORANGE)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block WAXED_EXPOSED_COPPER_FITTING = registerBlockAndItem("waxed_exposed_copper_fitting",
        properties -> new CopperFitting(Oxidizable.OxidationLevel.EXPOSED, properties, 0),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.TERRACOTTA_LIGHT_GRAY)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block WAXED_WEATHERED_COPPER_FITTING = registerBlockAndItem("waxed_weathered_copper_fitting",
        properties -> new CopperFitting(Oxidizable.OxidationLevel.WEATHERED, properties, 0),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.DARK_AQUA)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );
    public static final Block WAXED_OXIDIZED_COPPER_FITTING = registerBlockAndItem("waxed_oxidized_copper_fitting",
        properties -> new CopperFitting(Oxidizable.OxidationLevel.OXIDIZED, properties, 0),
        AbstractBlock.Settings.create()
            .mapColor(MapColor.TEAL)
            .requiresTool()
            .strength(1.5F, 3F)
            .sounds(BlockSoundGroup.COPPER)
    );

    /**
     * Create and register a {@link Block} without {@link Item}
     * <p>
     * See block registration in {@link Blocks} for details.
     *
     * @param path     The name of the block, without modid.
     * @param factory  The constructor of the block.
     * @param settings Initial settings of the block.
     * @param <R>      The returned block type.
     * @return The registered block.
     */
    private static <R extends Block> R registerBlock(
        String path,
        Function<AbstractBlock.Settings, R> factory,
        AbstractBlock.Settings settings) {
        Identifier id = Global.modId(path);
        /// @see Blocks#keyOf.
        RegistryKey<Block> key = RegistryKey.of(RegistryKeys.BLOCK, id);
        /// @see Blocks#register(RegistryKey, Function, AbstractBlock.Settings).
        R block = factory.apply(settings.registryKey(key));
        return Registry.register(Registries.BLOCK, id, block);
    }

    /**
     * Create and register a {@link Block} and the corresponding {@link Item}
     * <p>
     * See {@link #registerBlock(String, Function, AbstractBlock.Settings)} for details.
     */
    private static <T extends Block> T registerBlockAndItem(
        String path,
        Function<AbstractBlock.Settings, T> factory,
        AbstractBlock.Settings settings) {
        // Register the block.
        T registered = registerBlock(path, factory, settings);
        // Register the item.
        Items.register(registered);
        return registered;
    }

    /**
     * Create and register a {@link WoodenPipe} and its corresponding {@link Item}
     *
     * @param path     The name of the block, without modid.
     * @param mapColor How it will be rendered on generated maps.
     * @return The registered block.
     */
    private static WoodenPipe registerWoodenPipe(String path, MapColor mapColor) {
        return registerBlockAndItem(path,
            WoodenPipe::new,
            AbstractBlock.Settings.create()
                .mapColor(mapColor)
                .requiresTool()
                .strength(1.0F)
                .sounds(BlockSoundGroup.WOOD)
                .burnable()
        );
    }

    /**
     * Create and register everything that was not done by static initializers
     */
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
}
