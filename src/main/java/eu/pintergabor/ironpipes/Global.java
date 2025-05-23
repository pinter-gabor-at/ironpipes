package eu.pintergabor.ironpipes;

import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import net.minecraft.resources.ResourceLocation;


public final class Global {

	private Global() {
		// Static class.
	}

	/**
	 * Used for logging and registration.
	 */
	public static final String MODID = "ironpipes";

	/**
	 * This logger is used to write text to the console and the log file.
	 */
	@SuppressWarnings("unused")
	public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MODID);

	/**
	 * Create a mod specific name.
	 *
	 * @param path Name without {@link #MODID}.
	 */
	@SuppressWarnings("unused")
	public static String modName(@NotNull String path) {
		return MODID + ":" + path;
	}

	/**
	 * Create a mod specific identifier.
	 *
	 * @param path Name without {@link #MODID}.
	 */
	@SuppressWarnings("unused")
	public static ResourceLocation modId(@NotNull String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}
}
