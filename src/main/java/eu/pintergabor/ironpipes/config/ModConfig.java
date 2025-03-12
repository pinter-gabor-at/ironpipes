package eu.pintergabor.ironpipes.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import org.jetbrains.annotations.ApiStatus;


@ApiStatus.Internal
@Config(name = "ironpipes")
public class ModConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public boolean dispensing = true;

    @ConfigEntry.Gui.Tooltip
    public boolean dispenseSounds = true;

    @ConfigEntry.Gui.Tooltip
    public boolean suctionSounds = true;

    @ConfigEntry.Gui.Tooltip
    public boolean carryWater = true;

    @ConfigEntry.Gui.Tooltip
    public boolean carryLava = true;

    public static void init() {
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
    }

    public static ModConfig get() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
