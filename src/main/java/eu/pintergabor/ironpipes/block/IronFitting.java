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
                .forGetter((fitting) -> fitting.tickRate),
            Codec.INT.fieldOf("cooldown")
                .forGetter((copperFitting) -> copperFitting.cooldown)
        ).apply(instance, IronFitting::new));
    public final int cooldown;

    /**
     * Create fitting as the CODEC requires it.
     */
    public IronFitting(Properties props, int tickRate, int cooldown) {
        super(props, tickRate);
        this.cooldown = cooldown;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
