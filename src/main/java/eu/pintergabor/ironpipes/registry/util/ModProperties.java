package eu.pintergabor.ironpipes.registry.util;

import eu.pintergabor.ironpipes.block.properties.PipeFluid;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;


public final class ModProperties {
	public static final BooleanProperty FRONT_CONNECTED = BooleanProperty.create("front_connected");
	public static final BooleanProperty BACK_CONNECTED = BooleanProperty.create("back_connected");
	public static final BooleanProperty SMOOTH = BooleanProperty.create("smooth");
	public static final EnumProperty<PipeFluid> FLUID = EnumProperty.create("fluid", PipeFluid.class);
	public static final BooleanProperty OUTFLOW = BooleanProperty.create("outflow");

	private ModProperties() {
		// Static class.
	}

	public static void init() {
		// Everything has been done by static initializers.
	}
}
