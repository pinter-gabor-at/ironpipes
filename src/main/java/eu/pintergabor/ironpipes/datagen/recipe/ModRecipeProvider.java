package eu.pintergabor.ironpipes.datagen.recipe;

import java.util.concurrent.CompletableFuture;

import eu.pintergabor.ironpipes.Global;
import org.jetbrains.annotations.NotNull;

import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.registry.RegistryWrapper;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;


public final class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(
        FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registries) {
        super(output, registries);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new ModRecipeGenerator(registryLookup, exporter);
    }

    @Override
    public @NotNull String getName() {
        return Global.MOD_ID + " recipes";
    }
}
