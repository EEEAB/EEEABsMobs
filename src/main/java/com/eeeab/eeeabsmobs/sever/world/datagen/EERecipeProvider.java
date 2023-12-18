package com.eeeab.eeeabsmobs.sever.world.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;


public class EERecipeProvider extends RecipeProvider implements IConditionBuilder {
    public EERecipeProvider(DataGenerator output) {
        super(output);
    }


    //@Override
    //protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
    //}

    //protected static void oreSmelting(Consumer<FinishedRecipe> consumer, List<ItemLike> itemLikes, RecipeCategory recipeCategory, ItemLike itemLike, float experience, int duration, String name) {
    //    oreCooking(consumer, RecipeSerializer.SMELTING_RECIPE, itemLikes, recipeCategory, itemLike, experience, duration, name, "_from_smelting");
    //}

    //protected static void oreCooking(Consumer<FinishedRecipe> consumer, RecipeSerializer<? extends AbstractCookingRecipe> recipeSerializer, List<ItemLike> itemLikes, RecipeCategory category, ItemLike itemLike, float experience, int p_251316_, String name, String suffix) {
    //    for (ItemLike itemlike : itemLikes) {
    //        SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), category, itemLike, experience, p_251316_, recipeSerializer).group(name).unlockedBy(getHasName(itemlike), has(itemlike)).save(consumer, new ResourceLocation(EEEABMobs.MOD_ID, getItemName(itemLike)) + suffix + "_" + getItemName(itemlike));
    //    }
    //}
}
