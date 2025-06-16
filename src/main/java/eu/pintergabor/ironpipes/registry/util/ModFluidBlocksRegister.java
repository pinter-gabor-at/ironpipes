package eu.pintergabor.ironpipes.registry.util;

import eu.pintergabor.ironpipes.block.FluidCarryBlock;
import eu.pintergabor.ironpipes.block.FluidFitting;
import eu.pintergabor.ironpipes.block.FluidPipe;
import eu.pintergabor.ironpipes.block.settings.FluidBlockSettings;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;


public final class ModFluidBlocksRegister {

	private ModFluidBlocksRegister() {
		// Static class.
	}

	/**
	 * Create and register a pipe and its corresponding {@link Item}
	 *
	 * @param path        The name of the block, without modid.
	 * @param modSettings Mod specific settings, like speed, capabilities and probabilities.
	 * @param props       Generic settings, like color, hardness and resistance.
	 * @return The registered block.
	 */
	private static @NotNull FluidPipe registerPipe(
		String path,
		FluidBlockSettings modSettings,
		BlockBehaviour.Properties props
	) {
		return ModBlocksRegister.registerBlockAndItem(path,
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
	public static @NotNull FluidFitting registerFitting(
		String path, FluidCarryBlock pipeBlock
	) {
		return ModBlocksRegister.registerBlockAndItem(path,
			(props1) -> new FluidFitting(
				props1, pipeBlock.getFluidBlockSettings()),
			BlockBehaviour.Properties.ofFullCopy((BlockBehaviour) pipeBlock));
	}

	/**
	 * Create and register a wooden pipe and its corresponding {@link Item}
	 *
	 * @param path     The name of the block, without modid.
	 * @param mapColor How it will be rendered on generated maps.
	 * @return The registered block.
	 */
	public static @NotNull FluidPipe registerWoodenPipe(
		String path, MapColor mapColor,
		float hardness, float resistance,
		FluidBlockSettings modProperties
	) {
		return registerPipe(
			path, modProperties,
			BlockBehaviour.Properties.of()
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
	public static @NotNull FluidPipe registerStonePipe(
		String path, MapColor mapColor,
		float hardness, float resistance,
		FluidBlockSettings modProperties
	) {
		return registerPipe(
			path, modProperties,
			BlockBehaviour.Properties.of()
				.mapColor(mapColor)
				.requiresCorrectToolForDrops()
				.strength(hardness, resistance)
				.sound(SoundType.STONE));
	}
}
