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
                .forGetter((fitting) -> fitting.tickRate),
            Codec.INT.fieldOf("cooldown")
                .forGetter((pipe) -> pipe.cooldown)
        ).apply(instance, IronPipe::new));
    public final int cooldown;

    /**
     * Create pipe as the CODEC requires it.
     */
    public IronPipe(Properties props, int tickRate, int cooldown) {
        super(props, tickRate);
        this.cooldown = cooldown;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
