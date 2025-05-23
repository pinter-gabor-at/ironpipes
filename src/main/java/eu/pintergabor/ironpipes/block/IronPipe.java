package eu.pintergabor.ironpipes.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.level.block.BaseEntityBlock;

import org.jetbrains.annotations.NotNull;


public class IronPipe extends ItemPipe {
    public static final MapCodec<IronPipe> CODEC = RecordCodecBuilder.mapCodec(
        (instance) -> instance.group(
            propertiesCodec(),
            Codec.INT.fieldOf("tick_rate")
                .forGetter((pipe) -> pipe.tickRate)
        ).apply(instance, IronPipe::new));

    /**
     * Create pipe as the CODEC requires it.
     */
    public IronPipe(Properties props, int tickRate) {
        super(props, tickRate);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
