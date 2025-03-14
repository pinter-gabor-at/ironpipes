package eu.pintergabor.ironpipes.block.entity.leaking;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;

import org.jetbrains.annotations.NotNull;


public class DripUtil {

    private DripUtil(){
        // Static class.
    }

    /**
     * @return a random number in the range of [-0.25…+0.25]
     */
    private static double getDripRnd(Random random) {
        return random.nextDouble() / 2.0 - 0.25;
    }

    /**
     * Utility for calculating drip point for dripping pipes.
     *
     * @return random drip point X.
     */
    protected static double getDripX(@NotNull Direction direction, Random random) {
        return switch (direction) {
            case DOWN, SOUTH, NORTH -> 0.5 + getDripRnd(random);
            case UP -> 0.5;
            case EAST -> 1.05;
            case WEST -> -0.05;
        };
    }

    /**
     * Utility for calculating drip point for dripping pipes.
     *
     * @return random drip point Y.
     */
    protected static double getDripY(@NotNull Direction direction, Random random) {
        return switch (direction) {
            case DOWN -> -0.05;
            case UP -> 1.05;
            case NORTH, WEST, EAST, SOUTH -> 0.4375 + getDripRnd(random);
        };
    }

    /**
     * Utility for calculating drip point for dripping pipes.
     *
     * @return random drip point Z.
     */
    protected static double getDripZ(@NotNull Direction direction, Random random) {
        return switch (direction) {
            case DOWN, EAST, WEST -> 0.5 + getDripRnd(random);
            case UP -> 0.5;
            case NORTH -> -0.05;
            case SOUTH -> 1.05;
        };
    }
}
