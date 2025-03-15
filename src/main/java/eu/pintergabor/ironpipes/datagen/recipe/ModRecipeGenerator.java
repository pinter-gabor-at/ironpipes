package eu.pintergabor.ironpipes.datagen.recipe;

import eu.pintergabor.ironpipes.registry.ModBlocks;

import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;


class ModRecipeGenerator extends RecipeGenerator {

    public ModRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
        super(registryLookup, exporter);
    }

    /**
     * Create a pipe recipe.
     */
    @SuppressWarnings("SameParameterValue")
    private void createPipeRecipe(
        RecipeCategory recipeCategory, ItemConvertible input, ItemConvertible output, int outputCount) {
        createShaped(recipeCategory, output, outputCount)
            .input('#', input)
            .pattern("###")
            .pattern("   ")
            .pattern("###")
            .criterion(hasItem(input), conditionsFromItem(input))
            .offerTo(exporter);
    }

    /**
     * Create any waxed recipe.
     */
    @SuppressWarnings("SameParameterValue")
    private void createWaxedRecipe(
        RecipeCategory recipeCategory, ItemConvertible input, ItemConvertible output) {
        createShapeless(recipeCategory, output)
            .input(input)
            .input(Items.HONEYCOMB)
            .criterion(hasItem(input), conditionsFromItem(input))
            .offerTo(exporter);
    }

    /**
     * Create a fitting recipe.
     */
    @SuppressWarnings("SameParameterValue")
    private void createFittingRecipe(
        RecipeCategory recipeCategory, ItemConvertible input, ItemConvertible output, int outputCount) {
        createShaped(recipeCategory, output, outputCount)
            .input('#', input)
            .pattern("###")
            .pattern("# #")
            .pattern("###")
            .criterion(hasItem(input), conditionsFromItem(input))
            .offerTo(exporter);
    }

    /**
     * A record for const init tables.
     */
    private record RecipeIO(ItemConvertible input, ItemConvertible output) {
    }

    /**
     * Create wooden pipe recipes.
     */
    private void createWoodenPipeRecipes() {
        final ItemConvertible[] WOODEN_PLANKS = {
            Items.OAK_PLANKS,
            Items.SPRUCE_PLANKS,
            Items.BIRCH_PLANKS,
            Items.JUNGLE_PLANKS,
            Items.ACACIA_PLANKS,
            Items.CHERRY_PLANKS,
            Items.DARK_OAK_PLANKS,
            Items.PALE_OAK_PLANKS,
            Items.MANGROVE_PLANKS,
            Items.BAMBOO_PLANKS,
        };
        for (int i = 0; i < ModBlocks.WOODEN_PIPES.length; i++) {
            createPipeRecipe(RecipeCategory.MISC, WOODEN_PLANKS[i],
                ModBlocks.WOODEN_PIPES[i], 6);
        }
    }

    /**
     * Create waxed pipe recipes.
     */
    private void createWaxedPipeRecipes() {
        final RecipeIO[] WAXED = {
            new RecipeIO(ModBlocks.COPPER_PIPE, ModBlocks.WAXED_COPPER_PIPE),
            new RecipeIO(ModBlocks.EXPOSED_COPPER_PIPE, ModBlocks.WAXED_EXPOSED_COPPER_PIPE),
            new RecipeIO(ModBlocks.WEATHERED_COPPER_PIPE, ModBlocks.WAXED_WEATHERED_COPPER_PIPE),
            new RecipeIO(ModBlocks.OXIDIZED_COPPER_PIPE, ModBlocks.WAXED_OXIDIZED_COPPER_PIPE),
        };
        for (RecipeIO rio : WAXED) {
            createWaxedRecipe(RecipeCategory.MISC, rio.input, rio.output);
        }
    }

    /**
     * Create waxed fitting recipes.
     */
    private void createWaxedFittingRecipes() {
        final RecipeIO[] WAXED = {
            new RecipeIO(ModBlocks.COPPER_FITTING, ModBlocks.WAXED_COPPER_FITTING),
            new RecipeIO(ModBlocks.EXPOSED_COPPER_FITTING, ModBlocks.WAXED_EXPOSED_COPPER_FITTING),
            new RecipeIO(ModBlocks.WEATHERED_COPPER_FITTING, ModBlocks.WAXED_WEATHERED_COPPER_FITTING),
            new RecipeIO(ModBlocks.OXIDIZED_COPPER_FITTING, ModBlocks.WAXED_OXIDIZED_COPPER_FITTING),
        };
        for (RecipeIO rio : WAXED) {
            createWaxedRecipe(RecipeCategory.REDSTONE, rio.input, rio.output);
        }
    }

    /**
     * Generate all recipes.
     */
    @Override
    public void generate() {
        // Wooden pipes.
        createWoodenPipeRecipes();
        // Copper pipes.
        createPipeRecipe(RecipeCategory.MISC, Items.COPPER_INGOT,
            ModBlocks.COPPER_PIPE, 6);
        // Waxed copper pipes.
        createWaxedPipeRecipes();
        // Copper fittings.
        createFittingRecipe(RecipeCategory.REDSTONE, ModBlocks.COPPER_PIPE,
            ModBlocks.COPPER_FITTING, 8);
        // Waxed copper fittings.
        createWaxedFittingRecipes();
    }
}
