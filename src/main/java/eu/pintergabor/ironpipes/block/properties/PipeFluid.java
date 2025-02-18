package eu.pintergabor.ironpipes.block.properties;

import eu.pintergabor.ironpipes.registry.RegisterPipeNbtMethods;

import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;


public enum PipeFluid implements StringIdentifiable {
    NONE("none", null),
    WATER("water", RegisterPipeNbtMethods.WATER),
    LAVA("lava", RegisterPipeNbtMethods.LAVA);
    private final Identifier id;
    private final String name;

    PipeFluid(String name, Identifier id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
