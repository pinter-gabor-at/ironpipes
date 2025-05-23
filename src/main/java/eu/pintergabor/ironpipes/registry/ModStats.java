package eu.pintergabor.ironpipes.registry;

import eu.pintergabor.ironpipes.Global;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;


public final class ModStats {
	public static final Stat<ResourceLocation> INTERACTIONS = register("interactions");

	private ModStats() {
		// Static class.
	}

	/**
	 * Register statistics.
	 */
	@SuppressWarnings("SameParameterValue")
	private static Stat<ResourceLocation> register(@NotNull String path) {
		ResourceLocation id = Global.modId(path);
		return Stats.CUSTOM.get(
			Registry.register(BuiltInRegistries.CUSTOM_STAT, id, id),
			StatFormatter.DEFAULT);
	}

	public static void init() {
		// Everything has been done by static initializers.
	}
}
