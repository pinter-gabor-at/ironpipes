package eu.pintergabor.ironpipes.block.base;


import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.ModBlockStateProperties;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;


public abstract class BaseFluidFitting extends BaseFitting {
    public static final EnumProperty<PipeFluid> FLUID =
        ModBlockStateProperties.FLUID;

    protected BaseFluidFitting(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
            .with(FLUID, PipeFluid.NONE));
    }

    @Override
    protected void appendProperties(
        StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FLUID);
    }
}
