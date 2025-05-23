package eu.pintergabor.ironpipes.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.block.BaseEntityBlock;


public class IronFitting extends ItemFitting {
	public static final MapCodec<IronFitting> CODEC = RecordCodecBuilder.mapCodec(
		(instance) -> instance.group(
			propertiesCodec(),
			Codec.INT.fieldOf("tick_rate")
				.forGetter((p) -> p.tickRate),
			Codec.INT.fieldOf("inventory_size")
				.forGetter((p) -> p.inventorySize)
		).apply(instance, IronFitting::new));

	/**
	 * Create fitting as the CODEC requires it.
	 */
	public IronFitting(
		Properties props,
		int tickRate, int inventorySize
	) {
		super(props, tickRate, inventorySize);
	}

	@Override
	protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}
}
