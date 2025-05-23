package eu.pintergabor.ironpipes.registry.util;

import java.util.function.Function;

import eu.pintergabor.ironpipes.Global;
import eu.pintergabor.ironpipes.block.FluidCarryBlock;
import eu.pintergabor.ironpipes.block.FluidFitting;
import eu.pintergabor.ironpipes.block.FluidPipe;
import eu.pintergabor.ironpipes.block.settings.FluidBlockSettings;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;


public final class ModBlocksRegister {

	private ModBlocksRegister() {
		// Static class.
	}

	/**
	 * Create and register a {@link Block} without {@link Item}
	 * <p>
	 * See block registration in {@link Blocks} for details.
	 *
	 * @param path    The name of the block, without modid.
	 * @param factory The constructor of the block.
	 * @param props   Initial settings of the block.
	 * @param <T>     The returned block type.
	 * @return The registered block.
	 */
	private static <T extends Block> T registerBlock(
		String path,
		Function<Properties, T> factory,
		Properties props
	) {
		ResourceLocation id = Global.modId(path);
		/// See {@link Blocks#vanillaBlockId}.
		ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, id);
		/// See {@link Blocks#register(String, Function, Properties)}.
		T block = factory.apply(props.setId(key));
		return Registry.register(BuiltInRegistries.BLOCK, id, block);
	}

	/**
	 * Create and register a {@link Block} and the corresponding {@link Item}
	 * <p>
	 * See {@link #registerBlock(String, Function, Properties)} for details.
	 */
	private static <T extends Block> T registerBlockAndItem(
		String path,
		Function<Properties, T> factory,
		Properties props
	) {
		// Register the block.
		T registered = registerBlock(path, factory, props);
		// Register the item.
		Items.registerBlock(registered);
		return registered;
	}

	/**
	 * Create and register a pipe and its corresponding {@link Item}
	 *
	 * @param path        The name of the block, without modid.
	 * @param modSettings Mod specific settings, like speed, capabilities and probabilities.
	 * @param props       Generic settings, like color, hardness and resistance.
	 * @return The registered block.
	 */
	private static FluidPipe registerPipe(
		String path,
		FluidBlockSettings modSettings,
		Properties props
	) {
		return registerBlockAndItem(path,
			(props1) -> new FluidPipe(
				props1, modSettings),
			props);
	}

	/**
	 * Create and register a fitting and its corresponding {@link Item},
	 * matching {@code pipeBlock}
	 *
	 * @param path      The name of the block, without modid.
	 * @param pipeBlock The matching pipe.
	 * @return The registered block.
	 */
	public static FluidFitting registerFitting(
		String path, FluidCarryBlock pipeBlock
	) {
		return registerBlockAndItem(path,
			(settings1) -> new FluidFitting(
				settings1, pipeBlock.getFluidBlockSettings()),
			Properties.ofFullCopy((BlockBehaviour) pipeBlock));
	}

	/**
	 * Create and register a wooden pipe and its corresponding {@link Item}
	 *
	 * @param path     The name of the block, without modid.
	 * @param mapColor How it will be rendered on generated maps.
	 * @return The registered block.
	 */
	public static FluidPipe registerWoodenPipe(
		String path, MapColor mapColor,
		float hardness, float resistance,
		FluidBlockSettings modProperties
	) {
		return registerPipe(
			path, modProperties,
			Properties.of()
				.mapColor(mapColor)
				.requiresCorrectToolForDrops()
				.strength(hardness, resistance)
				.sound(SoundType.WOOD)
				.ignitedByLava());
	}

	/**
	 * Create and register a stone pipe and its corresponding {@link Item}
	 *
	 * @param path     The name of the block, without modid.
	 * @param mapColor How it will be rendered on generated maps.
	 * @return The registered block.
	 */
	public static FluidPipe registerStonePipe(
		String path, MapColor mapColor,
		float hardness, float resistance,
		FluidBlockSettings modProperties
	) {
		return registerPipe(
			path, modProperties,
			Properties.of()
				.mapColor(mapColor)
				.requiresCorrectToolForDrops()
				.strength(hardness, resistance)
				.sound(SoundType.STONE));
	}

	/**
	 * Create and register everything that was not done by static initializers
	 */
	public static void init() {
		// Everything has been done by static initializers.
	}
}
