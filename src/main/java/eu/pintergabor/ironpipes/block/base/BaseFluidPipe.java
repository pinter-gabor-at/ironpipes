package eu.pintergabor.ironpipes.block.base;

import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.ModBlockStateProperties;
import net.minecraft.block.Waterloggable;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;


/**
 * A fluid pipe can carry water or lava.
 */
public abstract class BaseFluidPipe extends BasePipe implements Waterloggable {
    public static final BooleanProperty WATERLOGGED =
            Properties.WATERLOGGED;
    public static final EnumProperty<PipeFluid> FLUID =
            ModBlockStateProperties.FLUID;

    protected BaseFluidPipe(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(WATERLOGGED, false)
                .with(FLUID, PipeFluid.NONE));
    }
}
