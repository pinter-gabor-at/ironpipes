package eu.pintergabor.ironpipes.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;

import net.minecraft.client.gui.screen.Screen;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(SimpleCopperPipesConfig.class, parent).get();
    }
}
