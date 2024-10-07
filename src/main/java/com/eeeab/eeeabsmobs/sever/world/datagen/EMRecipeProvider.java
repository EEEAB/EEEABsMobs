package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class EMRecipeProvider extends VanillaRecipeProvider {
    public EMRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> recipeConsumer) {
        upgradeSmithingTemplate(recipeConsumer, Items.DIAMOND, ItemInit.IMMORTAL_INGOT.get(), ItemInit.GHOST_WARRIOR_UPGRADE_SMITHING_TEMPLATE.get());
        ghostWarriorSmithing(recipeConsumer, Items.NETHERITE_CHESTPLATE, RecipeCategory.COMBAT, ItemInit.GHOST_WARRIOR_CHESTPLATE.get());
        ghostWarriorSmithing(recipeConsumer, Items.NETHERITE_LEGGINGS, RecipeCategory.COMBAT, ItemInit.GHOST_WARRIOR_LEGGINGS.get());
        ghostWarriorSmithing(recipeConsumer, Items.NETHERITE_HELMET, RecipeCategory.COMBAT, ItemInit.GHOST_WARRIOR_HELMET.get());
        ghostWarriorSmithing(recipeConsumer, Items.NETHERITE_BOOTS, RecipeCategory.COMBAT, ItemInit.GHOST_WARRIOR_BOOTS.get());
    }


    private static void upgradeSmithingTemplate(Consumer<FinishedRecipe> recipeConsumer, Item base1, Item base2, Item result) {
        coreItemWithBasesRecipe(recipeConsumer, Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, base1, base2, result);
    }

    private static void coreItemWithBasesRecipe(Consumer<FinishedRecipe> recipeConsumer, Item core, Item base1, Item base2, Item result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 1).define('$', core).define('X', base1).define('#', base2)
                .pattern("X#X").pattern("#$#").pattern("X#X").unlockedBy(getHasName(result), has(result)).save(recipeConsumer);
    }

    private static void ghostWarriorSmithing(Consumer<FinishedRecipe> finishedRecipeConsumer, Item ingredientItem, RecipeCategory category, Item resultItem) {
        commonSmithing(ItemInit.GHOST_WARRIOR_UPGRADE_SMITHING_TEMPLATE.get(), ItemInit.GHOST_STEEL_INGOT.get(), finishedRecipeConsumer, ingredientItem, category, resultItem);
    }

    private static void commonSmithing(Item smithingTemplate, Item ingredients, Consumer<FinishedRecipe> finishedRecipeConsumer, Item ingredientItem, RecipeCategory category, Item resultItem) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(smithingTemplate), Ingredient.of(ingredientItem), Ingredient.of(ingredients), category, resultItem).unlocks("has_" + getItemName(ingredients), has(ingredients)).save(finishedRecipeConsumer, getItemName(resultItem) + "_smithing");
    }
}
