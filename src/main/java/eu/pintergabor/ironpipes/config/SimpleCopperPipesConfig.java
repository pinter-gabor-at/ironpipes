package eu.pintergabor.ironpipes.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@Config(name = "simple_copper_pipes")
public class SimpleCopperPipesConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public boolean openableFittings = false;

    @ConfigEntry.Gui.Tooltip
    public boolean dispensing = true;

    @ConfigEntry.Gui.Tooltip
    public boolean dispenseSounds = true;

    @ConfigEntry.Gui.Tooltip
    public boolean suctionSounds = true;

    @ConfigEntry.Gui.Tooltip
    public boolean senseGameEvents = true;

    @ConfigEntry.Gui.Tooltip
    public boolean carryWater = true;

    @ConfigEntry.Gui.Tooltip
    public boolean carryLava = true;

    @ConfigEntry.Gui.Tooltip
    public boolean carrySmoke = true;

    public static void init() {
        AutoConfig.register(SimpleCopperPipesConfig.class, Toml4jConfigSerializer::new);
    }

    public static SimpleCopperPipesConfig get() {
        return AutoConfig.getConfigHolder(SimpleCopperPipesConfig.class).getConfig();
    }
}
