package eu.pintergabor.ironpipes.registry;

import eu.pintergabor.ironpipes.block.properties.PipeFluid;

import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;

public class ModProperties {
	public static final BooleanProperty FRONT_CONNECTED = BooleanProperty.of("front_connected");
	public static final BooleanProperty BACK_CONNECTED = BooleanProperty.of("back_connected");
	public static final BooleanProperty SMOOTH = BooleanProperty.of("smooth");
	public static final EnumProperty<PipeFluid> FLUID = EnumProperty.of("fluid", PipeFluid.class);
    public static final BooleanProperty OUTFLOW = BooleanProperty.of("outflow");
	public static final BooleanProperty HAS_ELECTRICITY = BooleanProperty.of("has_electricity");

	public static void init() {
	}
}
