package eu.pintergabor.ironpipes.datagen.recipe;

import java.util.concurrent.CompletableFuture;

import eu.pintergabor.ironpipes.Global;
import eu.pintergabor.ironpipes.registry.SimpleCopperPipesBlocks;
import org.jetbrains.annotations.NotNull;

import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;


public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(
        FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registries) {
        super(output, registries);
    }

    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        return new RecipeGenerator(registryLookup, exporter) {

            @Override
            public void generate() {
                // Pipes.
                createShaped(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.COPPER_PIPE, 3)
                    .input('#', Items.COPPER_INGOT)
                    .pattern("###")
                    .pattern("   ")
                    .pattern("###")
                    .criterion(hasItem(Items.COPPER_INGOT),
                        conditionsFromItem(Items.COPPER_INGOT))
                    .offerTo(exporter);
                // Waxed pipes.
                createShapeless(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.WAXED_COPPER_PIPE)
                    .input(SimpleCopperPipesBlocks.COPPER_PIPE)
                    .input(Items.HONEYCOMB)
                    .criterion(hasItem(SimpleCopperPipesBlocks.COPPER_PIPE),
                        conditionsFromItem(SimpleCopperPipesBlocks.COPPER_PIPE))
                    .offerTo(exporter);
                createShapeless(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_PIPE)
                    .input(SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE)
                    .input(Items.HONEYCOMB)
                    .criterion(hasItem(SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE),
                        conditionsFromItem(SimpleCopperPipesBlocks.EXPOSED_COPPER_PIPE))
                    .offerTo(exporter);
                createShapeless(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_PIPE)
                    .input(SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE)
                    .input(Items.HONEYCOMB)
                    .criterion(hasItem(SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE),
                        conditionsFromItem(SimpleCopperPipesBlocks.WEATHERED_COPPER_PIPE))
                    .offerTo(exporter);
                createShapeless(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_PIPE)
                    .input(SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE)
                    .input(Items.HONEYCOMB)
                    .criterion(hasItem(SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE),
                        conditionsFromItem(SimpleCopperPipesBlocks.OXIDIZED_COPPER_PIPE))
                    .offerTo(exporter);
                // Fittings.
                createShaped(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.COPPER_FITTING, 4)
                    .input('#', Items.COPPER_INGOT)
                    .pattern("###")
                    .pattern("# #")
                    .pattern("###")
                    .criterion(hasItem(Items.COPPER_INGOT),
                        conditionsFromItem(Items.COPPER_INGOT))
                    .offerTo(exporter);
                // Waxed fittings
                createShapeless(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.WAXED_COPPER_FITTING)
                    .input(SimpleCopperPipesBlocks.COPPER_FITTING)
                    .input(Items.HONEYCOMB)
                    .criterion(hasItem(SimpleCopperPipesBlocks.COPPER_FITTING),
                        conditionsFromItem(SimpleCopperPipesBlocks.COPPER_FITTING))
                    .offerTo(exporter);
                createShapeless(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.WAXED_EXPOSED_COPPER_FITTING)
                    .input(SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING)
                    .input(Items.HONEYCOMB)
                    .criterion(hasItem(SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING),
                        conditionsFromItem(SimpleCopperPipesBlocks.EXPOSED_COPPER_FITTING))
                    .offerTo(exporter);
                createShapeless(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.WAXED_WEATHERED_COPPER_FITTING)
                    .input(SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING)
                    .input(Items.HONEYCOMB)
                    .criterion(hasItem(SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING),
                        conditionsFromItem(SimpleCopperPipesBlocks.WEATHERED_COPPER_FITTING))
                    .offerTo(exporter);
                createShapeless(RecipeCategory.REDSTONE, SimpleCopperPipesBlocks.WAXED_OXIDIZED_COPPER_FITTING)
                    .input(SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING)
                    .input(Items.HONEYCOMB)
                    .criterion(hasItem(SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING),
                        conditionsFromItem(SimpleCopperPipesBlocks.OXIDIZED_COPPER_FITTING))
                    .offerTo(exporter);
            }
        };
    }

    @Override
    public @NotNull String getName() {
        return Global.MOD_ID + " recipes";
    }
}
