package eu.pintergabor.ironpipes.block.util;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.Level;


/**
 * Divide tick to smaller time slots.
 */
public final class TickUtil {

	private TickUtil() {
		// Static class.
	}

	/**
	 * Return {@link TickPos#START} and {@link TickPos#MIDDLE} once in every {@code 1 / rate} time
	 */
	public static TickPos getTickPos(@NotNull Level level, int rate) {
		// Offset the gametime a little to make it better distributed.
		final int timeSlot = Math.floorMod(level.getGameTime() + 11, rate);
		if (timeSlot == 0) {
			return TickPos.START;
		}
		if (timeSlot == rate / 2) {
			return TickPos.MIDDLE;
		}
		return TickPos.NONE;
	}

	/**
	 * Output of {@link #getTickPos(Level, int)}.
	 */
	public enum TickPos {
		NONE, START, MIDDLE
	}
}
