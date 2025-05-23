package eu.pintergabor.ironpipes.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.WeatheringCopper;


public class CopperPipe extends ItemPipe implements WeatheringCopper {
    public static final MapCodec<CopperPipe> CODEC = RecordCodecBuilder.mapCodec(
        (instance) -> instance.group(
            WeatherState.CODEC.fieldOf("weather_state")
                .forGetter((pipe -> pipe.weatherState)),
            propertiesCodec(),
            Codec.INT.fieldOf("tick_rate")
                .forGetter((pipe) -> pipe.tickRate)
        ).apply(instance, CopperPipe::new));
    public final WeatherState weatherState;

    /**
     * Create pipe as the CODEC requires it.
     */
    public CopperPipe(WeatherState weatherState, Properties props, int tickRate) {
        super(props, tickRate);
        this.weatherState = weatherState;
    }

    @Override
    protected void randomTick(
        BlockState state, ServerLevel level, BlockPos pos, RandomSource random
    ) {
        // Oxidizing.
        changeOverTime(state, level, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState blockState) {
        // Continue oxidizing until the last stage is reached.
        return WeatheringCopper.getNext(blockState.getBlock()).isPresent();
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
