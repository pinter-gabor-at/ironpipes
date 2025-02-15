package eu.pintergabor.ironpipes;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Global {

    // Used for logging and registration.
    public static final String MOD_ID = "ironpipes";

    // This logger is used to write text to the console and the log file.
    @SuppressWarnings("unused")
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /**
     * Create a mod specific identifier
     * @param path Name, as in lang/*.json files without "*.modid." prefix
     */
    @Contract("_ -> new")
    public static @NotNull Identifier modId(String path) {
        return Identifier.of(MOD_ID, path);
    }
}
