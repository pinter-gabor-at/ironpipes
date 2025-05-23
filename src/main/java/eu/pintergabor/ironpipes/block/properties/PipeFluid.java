package eu.pintergabor.ironpipes.block.properties;

import org.jetbrains.annotations.NotNull;

import net.minecraft.util.StringRepresentable;


public enum PipeFluid implements StringRepresentable {
	NONE("none"),
	WATER("water"),
	LAVA("lava");
	private final String name;

	PipeFluid(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public @NotNull String getSerializedName() {
		return name;
	}
}
