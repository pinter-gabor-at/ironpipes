package eu.pintergabor.ironpipes.block.properties;

import java.util.Optional;

import eu.pintergabor.ironpipes.registry.RegisterPipeNbtMethods;

import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;


public enum PipeFluid implements StringIdentifiable {
    NONE("none", Optional.empty()),
    WATER("water", Optional.of(RegisterPipeNbtMethods.WATER)),
    LAVA("lava", Optional.of(RegisterPipeNbtMethods.LAVA)),
    SMOKE("smoke", Optional.of(RegisterPipeNbtMethods.SMOKE));
    public final Optional<Identifier> nbtID;
    private final String name;

    PipeFluid(String name, Optional<Identifier> nbtID) {
        this.name = name;
        this.nbtID = nbtID;
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
