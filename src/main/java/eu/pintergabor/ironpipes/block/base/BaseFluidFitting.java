package eu.pintergabor.ironpipes.block.base;


import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.ModProperties;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;

import org.jetbrains.annotations.NotNull;


public abstract class BaseFluidFitting extends BaseFitting {
    public static final EnumProperty<PipeFluid> FLUID =
        ModProperties.FLUID;

    protected BaseFluidFitting(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
            .with(FLUID, PipeFluid.NONE));
    }

    /**
     * Dripping particle generation uses RandomTick if the pipe contains water or lava.
     */
    @Override
    public boolean hasRandomTicks(@NotNull BlockState blockState) {
        PipeFluid fluid = blockState.get(FLUID, PipeFluid.NONE);
        return fluid == PipeFluid.WATER || fluid == PipeFluid.LAVA;
    }

    @Override
    protected void appendProperties(
        StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FLUID);
    }
}
