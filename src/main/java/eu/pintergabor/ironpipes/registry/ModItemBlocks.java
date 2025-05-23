package eu.pintergabor.ironpipes.registry;

import eu.pintergabor.ironpipes.block.CopperFitting;
import eu.pintergabor.ironpipes.block.CopperPipe;
import eu.pintergabor.ironpipes.block.IronFitting;
import eu.pintergabor.ironpipes.block.IronPipe;
import eu.pintergabor.ironpipes.block.ItemFitting;
import eu.pintergabor.ironpipes.block.ItemPipe;
import eu.pintergabor.ironpipes.registry.util.ModBlocksRegister;
import eu.pintergabor.ironpipes.registry.util.ModItemBlocksRegister;

import net.fabricmc.fabric.api.registry.OxidizableBlocksRegistry;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;


public final class ModItemBlocks {

    // Iron and gold pipes.
    public static final IronPipe IRON_PIPE =
        ModItemBlocksRegister.registerIronPipe("iron_pipe", MapColor.RAW_IRON, SoundType.IRON,
            1.5F, 3F, 10);

    public static final IronPipe GOLD_PIPE =
        ModItemBlocksRegister.registerIronPipe("gold_pipe", MapColor.GOLD, SoundType.NETHER_GOLD_ORE,
            1F, 2F, 2);
    // Copper pipes.
    public static final CopperPipe COPPER_PIPE =
        ModItemBlocksRegister.registerCopperPipe("copper_pipe", MapColor.COLOR_ORANGE,
            WeatherState.UNAFFECTED, 10);
    public static final CopperPipe EXPOSED_COPPER_PIPE =
        ModItemBlocksRegister.registerCopperPipe("exposed_copper_pipe", MapColor.TERRACOTTA_LIGHT_GRAY,
            WeatherState.EXPOSED, 15);
    public static final CopperPipe WEATHERED_COPPER_PIPE =
        ModItemBlocksRegister.registerCopperPipe("weathered_copper_pipe", MapColor.WARPED_STEM,
            WeatherState.WEATHERED, 20);
    public static final CopperPipe OXIDIZED_COPPER_PIPE =
        ModItemBlocksRegister.registerCopperPipe("oxidized_copper_pipe", MapColor.WARPED_NYLIUM,
            WeatherState.OXIDIZED, 25);
    public static final CopperPipe WAXED_COPPER_PIPE =
        ModItemBlocksRegister.registerCopperPipe("waxed_copper_pipe", MapColor.COLOR_ORANGE,
            WeatherState.UNAFFECTED, 5);
    public static final CopperPipe WAXED_EXPOSED_COPPER_PIPE =
        ModItemBlocksRegister.registerCopperPipe("waxed_exposed_copper_pipe", MapColor.TERRACOTTA_LIGHT_GRAY,
            WeatherState.EXPOSED, 10);
    public static final CopperPipe WAXED_WEATHERED_COPPER_PIPE =
        ModItemBlocksRegister.registerCopperPipe("waxed_weathered_copper_pipe", MapColor.WARPED_STEM,
            WeatherState.WEATHERED, 15);
    public static final CopperPipe WAXED_OXIDIZED_COPPER_PIPE =
        ModItemBlocksRegister.registerCopperPipe("waxed_oxidized_copper_pipe", MapColor.WARPED_NYLIUM,
            WeatherState.OXIDIZED, 20);
    public static final ItemPipe[] COPPER_PIPES = {
        COPPER_PIPE, EXPOSED_COPPER_PIPE, WEATHERED_COPPER_PIPE, OXIDIZED_COPPER_PIPE,
        WAXED_COPPER_PIPE, WAXED_EXPOSED_COPPER_PIPE, WAXED_WEATHERED_COPPER_PIPE, WAXED_OXIDIZED_COPPER_PIPE,
    };
    // All item pipes.
    public static final ItemPipe[] ITEM_PIPES = {
        IRON_PIPE, GOLD_PIPE,
        COPPER_PIPE, EXPOSED_COPPER_PIPE, WEATHERED_COPPER_PIPE, OXIDIZED_COPPER_PIPE,
        WAXED_COPPER_PIPE, WAXED_EXPOSED_COPPER_PIPE, WAXED_WEATHERED_COPPER_PIPE, WAXED_OXIDIZED_COPPER_PIPE,
    };
    // Iron and gold fittings.
    public static final IronFitting IRON_FITTING =
        ModBlocksRegister.registerBlockAndItem("iron_fitting",
            props -> new IronFitting(props, 10),
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.RAW_IRON)
                .requiresCorrectToolForDrops()
                .strength(1.5F, 3F)
                .sound(SoundType.IRON)
        );
    public static final IronFitting GOLD_FITTING =
        ModBlocksRegister.registerBlockAndItem("gold_fitting",
            props -> new IronFitting(props, 2),
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.GOLD)
                .requiresCorrectToolForDrops()
                .strength(1F, 2F)
                .sound(SoundType.NETHER_GOLD_ORE)
        );
    // Copper fittings.
    public static final CopperFitting COPPER_FITTING =
        ModItemBlocksRegister.registerCopperFitting("copper_fitting", MapColor.COLOR_ORANGE,
            WeatherState.UNAFFECTED, 10);
    public static final CopperFitting EXPOSED_COPPER_FITTING =
        ModItemBlocksRegister.registerCopperFitting("exposed_copper_fitting", MapColor.TERRACOTTA_LIGHT_GRAY,
            WeatherState.EXPOSED, 15);
    public static final CopperFitting WEATHERED_COPPER_FITTING =
        ModItemBlocksRegister.registerCopperFitting("weathered_copper_fitting", MapColor.WARPED_STEM,
            WeatherState.WEATHERED, 20);
    public static final CopperFitting OXIDIZED_COPPER_FITTING =
        ModItemBlocksRegister.registerCopperFitting("oxidized_copper_fitting", MapColor.WARPED_NYLIUM,
            WeatherState.OXIDIZED, 25);
    public static final CopperFitting WAXED_COPPER_FITTING =
        ModItemBlocksRegister.registerCopperFitting("waxed_copper_fitting", MapColor.COLOR_ORANGE,
            WeatherState.UNAFFECTED, 5);
    public static final CopperFitting WAXED_EXPOSED_COPPER_FITTING =
        ModItemBlocksRegister.registerCopperFitting("waxed_exposed_copper_fitting", MapColor.TERRACOTTA_LIGHT_GRAY,
            WeatherState.EXPOSED, 10);
    public static final CopperFitting WAXED_WEATHERED_COPPER_FITTING =
        ModItemBlocksRegister.registerCopperFitting("waxed_weathered_copper_fitting", MapColor.WARPED_STEM,
            WeatherState.WEATHERED, 15);
    public static final CopperFitting WAXED_OXIDIZED_COPPER_FITTING =
        ModItemBlocksRegister.registerCopperFitting("waxed_oxidized_copper_fitting", MapColor.WARPED_NYLIUM,
            WeatherState.OXIDIZED, 20);
    public static final ItemFitting[] COPPER_FITTINGS = {
        COPPER_FITTING, EXPOSED_COPPER_FITTING, WEATHERED_COPPER_FITTING, OXIDIZED_COPPER_FITTING,
        WAXED_COPPER_FITTING, WAXED_EXPOSED_COPPER_FITTING, WAXED_WEATHERED_COPPER_FITTING, WAXED_OXIDIZED_COPPER_FITTING,
    };
    // All item fittings.
    public static final ItemFitting[] ITEM_FITTINGS = {
        IRON_FITTING, GOLD_FITTING,
        COPPER_FITTING, EXPOSED_COPPER_FITTING, WEATHERED_COPPER_FITTING, OXIDIZED_COPPER_FITTING,
        WAXED_COPPER_FITTING, WAXED_EXPOSED_COPPER_FITTING, WAXED_WEATHERED_COPPER_FITTING, WAXED_OXIDIZED_COPPER_FITTING,
    };

    private ModItemBlocks() {
        // Static class.
    }

    /**
     * Create and register everything that was not done by static initializers.
     */
    public static void init() {
        // Oxidization of copper pipes.
        OxidizableBlocksRegistry.registerOxidizableBlockPair(COPPER_PIPE, EXPOSED_COPPER_PIPE);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(EXPOSED_COPPER_PIPE, WEATHERED_COPPER_PIPE);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(WEATHERED_COPPER_PIPE, OXIDIZED_COPPER_PIPE);
        // Oxidization of copper fittings.
        OxidizableBlocksRegistry.registerOxidizableBlockPair(COPPER_FITTING, EXPOSED_COPPER_FITTING);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(EXPOSED_COPPER_FITTING, WEATHERED_COPPER_FITTING);
        OxidizableBlocksRegistry.registerOxidizableBlockPair(WEATHERED_COPPER_FITTING, OXIDIZED_COPPER_FITTING);
        // Waxing of copper pipes.
        OxidizableBlocksRegistry.registerWaxableBlockPair(COPPER_PIPE, WAXED_COPPER_PIPE);
        OxidizableBlocksRegistry.registerWaxableBlockPair(EXPOSED_COPPER_PIPE, WAXED_EXPOSED_COPPER_PIPE);
        OxidizableBlocksRegistry.registerWaxableBlockPair(WEATHERED_COPPER_PIPE, WAXED_WEATHERED_COPPER_PIPE);
        OxidizableBlocksRegistry.registerWaxableBlockPair(OXIDIZED_COPPER_PIPE, WAXED_OXIDIZED_COPPER_PIPE);
        // Waxing of copper fittings.
        OxidizableBlocksRegistry.registerWaxableBlockPair(COPPER_FITTING, WAXED_COPPER_FITTING);
        OxidizableBlocksRegistry.registerWaxableBlockPair(EXPOSED_COPPER_FITTING, WAXED_EXPOSED_COPPER_FITTING);
        OxidizableBlocksRegistry.registerWaxableBlockPair(WEATHERED_COPPER_FITTING, WAXED_WEATHERED_COPPER_FITTING);
        OxidizableBlocksRegistry.registerWaxableBlockPair(OXIDIZED_COPPER_FITTING, WAXED_OXIDIZED_COPPER_FITTING);
    }
}
