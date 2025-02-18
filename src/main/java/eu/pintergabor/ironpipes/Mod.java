package eu.pintergabor.ironpipes;

import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeDripBehaviors;
import eu.pintergabor.ironpipes.block.entity.leaking.LeakingPipeManager;
import eu.pintergabor.ironpipes.config.SimpleCopperPipesConfig;
import eu.pintergabor.ironpipes.networking.ModNetworking;
import eu.pintergabor.ironpipes.registry.CopperPipeDispenseBehaviors;
import eu.pintergabor.ironpipes.registry.ModBlockEntities;
import eu.pintergabor.ironpipes.registry.ModBlockStateProperties;
import eu.pintergabor.ironpipes.registry.ModCreativeInventorySorting;
import eu.pintergabor.ironpipes.registry.ModSoundEvents;
import eu.pintergabor.ironpipes.registry.ModStats;
import eu.pintergabor.ironpipes.registry.PipeMovementRestrictions;
import eu.pintergabor.ironpipes.registry.RegisterPipeNbtMethods;
import eu.pintergabor.ironpipes.registry.SimpleCopperPipesBlocks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;


public class Mod implements ModInitializer {

    @Override
    public void onInitialize() {
        ModBlockStateProperties.init();
        SimpleCopperPipesConfig.init();
        SimpleCopperPipesBlocks.init();
        ModBlockEntities.init();
        ModSoundEvents.init();
        ModStats.init();
        RegisterPipeNbtMethods.init();
        CopperPipeDispenseBehaviors.init();
        PipeMovementRestrictions.init();
        LeakingPipeDripBehaviors.init();
        ModNetworking.init();
        ModCreativeInventorySorting.init();
        ServerLifecycleEvents.SERVER_STOPPED.register(
            server -> LeakingPipeManager.clearAll());
        ServerTickEvents.START_SERVER_TICK.register(
            listener -> LeakingPipeManager.clearAndSwitch());
    }
}
