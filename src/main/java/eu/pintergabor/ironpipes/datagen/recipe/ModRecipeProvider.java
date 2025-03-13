package eu.pintergabor.ironpipes.datagen.recipe;

import java.util.concurrent.CompletableFuture;

import eu.pintergabor.ironpipes.Global;
import eu.pintergabor.ironpipes.registry.ModBlocks;
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
                // Wooden pipes.
                createShaped(RecipeCategory.TRANSPORTATION, ModBlocks.OAK_PIPE, 3)
                    .input('#', Items.OAK_PLANKS)
                    .pattern("###")
                    .pattern("   ")
                    .pattern("###")
                    .criterion(hasItem(Items.OAK_PLANKS),
                        conditionsFromItem(Items.OAK_PLANKS))
                    .offerTo(exporter);
                // Copper pipes.
                createShaped(RecipeCategory.REDSTONE, ModBlocks.COPPER_PIPE, 3)
                    .input('#', Items.COPPER_INGOT)
                    .pattern("###")
                    .pattern("   ")
                    .pattern("###")
                    .criterion(hasItem(Items.COPPER_INGOT),
                        conditionsFromItem(Items.COPPER_INGOT))
                    .offerTo(exporter);
                // Waxed copper pipes.
                createShapeless(RecipeCategory.REDSTONE, ModBlocks.WAXED_COPPER_PIPE)
                    .input(ModBlocks.COPPER_PIPE)
                    .input(Items.HONEYCOMB)
                    .criterion(hasItem(ModBlocks.COPPER_PIPE),
                        conditionsFromItem(ModBlocks.COPPER_PIPE))
                    .offerTo(exporter);
                createShapeless(RecipeCategory.REDSTONE, ModBlocks.WAXED_EXPOSED_COPPER_PIPE)
                    .input(ModBlocks.EXPOSED_COPPER_PIPE)
                    .input(Items.HONEYCOMB)
                    .criterion(hasItem(ModBlocks.EXPOSED_COPPER_PIPE),
                        conditionsFromItem(ModBlocks.EXPOSED_COPPER_PIPE))
                    .offerTo(exporter);
                createShapeless(RecipeCategory.REDSTONE, ModBlocks.WAXED_WEATHERED_COPPER_PIPE)
                    .input(ModBlocks.WEATHERED_COPPER_PIPE)
                    .input(Items.HONEYCOMB)
                    .criterion(hasItem(ModBlocks.WEATHERED_COPPER_PIPE),
                        conditionsFromItem(ModBlocks.WEATHERED_COPPER_PIPE))
                    .offerTo(exporter);
                createShapeless(RecipeCategory.REDSTONE, ModBlocks.WAXED_OXIDIZED_COPPER_PIPE)
                    .input(ModBlocks.OXIDIZED_COPPER_PIPE)
                    .input(Items.HONEYCOMB)
                    .criterion(hasItem(ModBlocks.OXIDIZED_COPPER_PIPE),
                        conditionsFromItem(ModBlocks.OXIDIZED_COPPER_PIPE))
                    .offerTo(exporter);
                // Copper fittings.
                createShaped(RecipeCategory.REDSTONE, ModBlocks.COPPER_FITTING, 4)
                    .input('#', Items.COPPER_INGOT)
                    .pattern("###")
                    .pattern("# #")
                    .pattern("###")
                    .criterion(hasItem(Items.COPPER_INGOT),
                        conditionsFromItem(Items.COPPER_INGOT))
                    .offerTo(exporter);
                // Waxed copper fittings.
                createShapeless(RecipeCategory.REDSTONE, ModBlocks.WAXED_COPPER_FITTING)
                    .input(ModBlocks.COPPER_FITTING)
                    .input(Items.HONEYCOMB)
                    .criterion(hasItem(ModBlocks.COPPER_FITTING),
                        conditionsFromItem(ModBlocks.COPPER_FITTING))
                    .offerTo(exporter);
                createShapeless(RecipeCategory.REDSTONE, ModBlocks.WAXED_EXPOSED_COPPER_FITTING)
                    .input(ModBlocks.EXPOSED_COPPER_FITTING)
                    .input(Items.HONEYCOMB)
                    .criterion(hasItem(ModBlocks.EXPOSED_COPPER_FITTING),
                        conditionsFromItem(ModBlocks.EXPOSED_COPPER_FITTING))
                    .offerTo(exporter);
                createShapeless(RecipeCategory.REDSTONE, ModBlocks.WAXED_WEATHERED_COPPER_FITTING)
                    .input(ModBlocks.WEATHERED_COPPER_FITTING)
                    .input(Items.HONEYCOMB)
                    .criterion(hasItem(ModBlocks.WEATHERED_COPPER_FITTING),
                        conditionsFromItem(ModBlocks.WEATHERED_COPPER_FITTING))
                    .offerTo(exporter);
                createShapeless(RecipeCategory.REDSTONE, ModBlocks.WAXED_OXIDIZED_COPPER_FITTING)
                    .input(ModBlocks.OXIDIZED_COPPER_FITTING)
                    .input(Items.HONEYCOMB)
                    .criterion(hasItem(ModBlocks.OXIDIZED_COPPER_FITTING),
                        conditionsFromItem(ModBlocks.OXIDIZED_COPPER_FITTING))
                    .offerTo(exporter);
            }
        };
    }

    @Override
    public @NotNull String getName() {
        return Global.MOD_ID + " recipes";
    }
}
