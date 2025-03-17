package eu.pintergabor.ironpipes.block.entity.base;

import net.minecraft.world.World;


public class TickUtil {

    /**
     * Return {@link TickPos#START} and {@link TickPos#MIDDLE} once in every {@code 1 / rate} time
     */
    public static TickPos getTickPos(World world, int rate) {
        // Offset the gametime a little to make it better distributed.
        int timeSlot = Math.floorMod(world.getTime() + 11, rate);
        if (timeSlot == 0) {
            return TickPos.START;
        } else if (timeSlot == rate / 2) {
            return TickPos.MIDDLE;
        }
        return TickPos.NONE;
    }

    /**
     * Output of {@link #getTickPos(World, int)}.
     */
    public enum TickPos {
        NONE, START, MIDDLE
    }
}
