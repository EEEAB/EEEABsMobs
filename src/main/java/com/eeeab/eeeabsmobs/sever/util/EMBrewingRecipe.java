package com.eeeab.eeeabsmobs.sever.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;

//自定义药水配方
public class EMBrewingRecipe extends BrewingRecipe {
    private final Potion input;
    private final Item ingredient;
    private final Potion output;

    public EMBrewingRecipe(Potion input, Item ingredient, Potion output) {
        super(Ingredient.of(PotionUtils.setPotion(Items.POTION.getDefaultInstance(), input)), Ingredient.of(ingredient.getDefaultInstance()), PotionUtils.setPotion(Items.POTION.getDefaultInstance(), output));
        this.input = input;
        this.ingredient = ingredient;
        this.output = output;
    }

    @Override
    public boolean isInput(ItemStack input) {
        return PotionUtils.getPotion(input) == this.input;
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return ingredient.getItem() == this.ingredient;
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (!(isInput(input) && isIngredient(ingredient))) {
            return ItemStack.EMPTY;
        }
        return PotionUtils.setPotion(input.getItem().getDefaultInstance(), this.output);
    }
}