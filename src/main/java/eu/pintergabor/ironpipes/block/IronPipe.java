package eu.pintergabor.ironpipes.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.block.BaseEntityBlock;


public class IronPipe extends ItemPipe {
	public static final MapCodec<IronPipe> CODEC = RecordCodecBuilder.mapCodec(
		(instance) -> instance.group(
			propertiesCodec(),
			Codec.INT.fieldOf("tick_rate")
				.forGetter((p) -> p.tickRate),
			Codec.INT.fieldOf("inventory_size")
				.forGetter((p) -> p.inventorySize)
		).apply(instance, IronPipe::new));

	/**
	 * Create pipe as the CODEC requires it.
	 */
	public IronPipe(
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
