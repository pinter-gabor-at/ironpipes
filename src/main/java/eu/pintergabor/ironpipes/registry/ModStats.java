package eu.pintergabor.ironpipes.registry;

import eu.pintergabor.ironpipes.Global;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

public class ModStats {
	public static final Identifier INSPECT_PIPE = Global.modId("inspect_copper_pipe");
	public static final Identifier INSPECT_FITTING = Global.modId("inspect_copper_fitting");

	public static void init() {
		Registry.register(Registries.CUSTOM_STAT, INSPECT_PIPE, INSPECT_PIPE);
		Registry.register(Registries.CUSTOM_STAT, INSPECT_FITTING, INSPECT_FITTING);
		Stats.CUSTOM.getOrCreateStat(INSPECT_PIPE, StatFormatter.DEFAULT);
		Stats.CUSTOM.getOrCreateStat(INSPECT_FITTING, StatFormatter.DEFAULT);
	}
}
