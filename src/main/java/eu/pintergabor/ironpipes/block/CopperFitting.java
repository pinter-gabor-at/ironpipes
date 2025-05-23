package eu.pintergabor.ironpipes.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.WeatheringCopper;


public class CopperFitting extends ItemFitting implements WeatheringCopper {
	public static final MapCodec<CopperFitting> CODEC = RecordCodecBuilder.mapCodec(
		(instance) -> instance.group(
			WeatherState.CODEC.fieldOf("weather_state")
				.forGetter((p -> p.weatherState)),
			propertiesCodec(),
			Codec.INT.fieldOf("tick_rate")
				.forGetter((p) -> p.tickRate),
			Codec.INT.fieldOf("inventory_size")
				.forGetter((p) -> p.inventorySize)
		).apply(instance, CopperFitting::new));
	public final WeatherState weatherState;

	/**
	 * Create fitting as the CODEC requires it.
	 */
	public CopperFitting(
		WeatherState weatherState, Properties props,
		int tickRate, int inventorySize
	) {
		super(props, tickRate, inventorySize);
		this.weatherState = weatherState;
	}

	@Override
	public @NotNull WeatherState getAge() {
		return weatherState;
	}

	@Override
	protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}
}
