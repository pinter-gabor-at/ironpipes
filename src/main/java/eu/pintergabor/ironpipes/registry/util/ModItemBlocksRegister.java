package eu.pintergabor.ironpipes.registry.util;

import eu.pintergabor.ironpipes.block.CopperFitting;
import eu.pintergabor.ironpipes.block.CopperPipe;
import eu.pintergabor.ironpipes.block.IronPipe;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;


public class ModItemBlocksRegister {

	public static @NotNull IronPipe registerIronPipe(
		String path, MapColor mapColor, SoundType soundType,
		float hardness, float resistance,
		int tickRate, int inventorySize
	) {
		return ModBlocksRegister.registerBlockAndItem(path,
			props -> new IronPipe(props, tickRate, inventorySize),
			BlockBehaviour.Properties.of()
				.mapColor(mapColor)
				.requiresCorrectToolForDrops()
				.strength(hardness, resistance)
				.sound(soundType)
		);
	}

	public static @NotNull CopperPipe registerCopperPipe(
		String path, MapColor mapColor,
		WeatherState weatherState,
		int tickRate, int inventorySize
	) {
		return ModBlocksRegister.registerBlockAndItem(path,
			props -> new CopperPipe(weatherState, props, tickRate, inventorySize),
			BlockBehaviour.Properties.of()
				.mapColor(mapColor)
				.requiresCorrectToolForDrops()
				.strength(1.5F, 3F)
				.sound(SoundType.COPPER)
		);
	}

	public static @NotNull CopperFitting registerCopperFitting(
		String path, MapColor mapColor,
		WeatherState weatherState,
		int tickRate, int inventorySize
	) {
		return ModBlocksRegister.registerBlockAndItem(path,
			props -> new CopperFitting(weatherState, props, tickRate, inventorySize),
			BlockBehaviour.Properties.of()
				.mapColor(mapColor)
				.requiresCorrectToolForDrops()
				.strength(1.5F, 3F)
				.sound(SoundType.COPPER)
		);
	}
}
