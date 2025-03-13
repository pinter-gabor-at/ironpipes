package eu.pintergabor.ironpipes.block.base;

import eu.pintergabor.ironpipes.block.properties.PipeFluid;
import eu.pintergabor.ironpipes.registry.ModBlockStateProperties;

import net.minecraft.block.Waterloggable;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;


/**
 * A fluid pipe can carry water or lava.
 */
public abstract class FluidPipe extends BasePipe implements Waterloggable {
    public static final BooleanProperty WATERLOGGED =
        Properties.WATERLOGGED;
    public static final EnumProperty<PipeFluid> FLUID =
        ModBlockStateProperties.FLUID;

    protected FluidPipe(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
            .with(FACING, Direction.DOWN)
            .with(SMOOTH, false)
            .with(WATERLOGGED, false)
            .with(FLUID, PipeFluid.NONE));
    }
}
