package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.google.common.collect.ImmutableList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> recipeConsumer) {
        upgradeSmithingTemplate(recipeConsumer, ItemInit.ANCIENT_DRIVE_CRYSTAL.get(), ItemInit.BOUNDARY_BRICK.get(), ItemInit.RELICRON_UPGRADE_SMITHING_TEMPLATE.get());
        upgradeSmithingTemplate(recipeConsumer, Items.DIAMOND, ItemInit.IMMORTAL_INGOT.get(), ItemInit.GHOST_WARRIOR_UPGRADE_SMITHING_TEMPLATE.get());
        ghostWarriorSmithing(recipeConsumer, Items.NETHERITE_CHESTPLATE, RecipeCategory.COMBAT, ItemInit.GHOST_WARRIOR_CHESTPLATE.get());
        ghostWarriorSmithing(recipeConsumer, Items.NETHERITE_LEGGINGS, RecipeCategory.COMBAT, ItemInit.GHOST_WARRIOR_LEGGINGS.get());
        ghostWarriorSmithing(recipeConsumer, Items.NETHERITE_HELMET, RecipeCategory.COMBAT, ItemInit.GHOST_WARRIOR_HELMET.get());
        ghostWarriorSmithing(recipeConsumer, Items.NETHERITE_BOOTS, RecipeCategory.COMBAT, ItemInit.GHOST_WARRIOR_BOOTS.get());
        commonSmithing(ItemInit.RELICRON_UPGRADE_SMITHING_TEMPLATE.get(), ItemInit.CHAIN_GEAR.get(), recipeConsumer, Items.NETHERITE_SWORD, RecipeCategory.COMBAT, ItemInit.CHAINSWORD.get());
        commonSmithing(ItemInit.RELICRON_UPGRADE_SMITHING_TEMPLATE.get(), ItemInit.GUARDIAN_CUBE.get(), recipeConsumer, Items.NETHERITE_AXE, RecipeCategory.COMBAT, ItemInit.DOOMBOLT_AXE.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockInit.IRON_GRATE.get(), 1)
                .define('#', Blocks.IRON_BARS).pattern("###").pattern("###")
                .unlockedBy(getHasName(BlockInit.IRON_GRATE.get()), has(BlockInit.IRON_GRATE.get())).save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockInit.BOUNDARY_LAMP.get(), 1)
                .define('I', Items.IRON_NUGGET).define('S', Blocks.SEA_LANTERN).define('B', ItemInit.BOUNDARY_BRICK.get())
                .pattern("IBI")
                .pattern("BSB")
                .pattern("IBI")
                .unlockedBy(getHasName(ItemInit.BOUNDARY_BRICK.get()), has(ItemInit.BOUNDARY_BRICK.get())).save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemInit.BUSTER_GAUNTLET.get(), 1)
                .define('A', ItemInit.ANCIENT_DRIVE_CRYSTAL.get()).define('B', ItemInit.BOUNDARY_BRICK.get())
                .define('D', Items.DISPENSER).define('I', Items.IRON_SWORD)
                .pattern(" BA")
                .pattern("BDB")
                .pattern("IB ")
                .unlockedBy(getHasName(ItemInit.ANCIENT_DRIVE_CRYSTAL.get()), has(ItemInit.ANCIENT_DRIVE_CRYSTAL.get())).save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemInit.CHAINSWORD.get(), 1)
                .define('A', ItemInit.ANCIENT_DRIVE_CRYSTAL.get()).define('B', ItemInit.BOUNDARY_BRICK.get()).define('C', Items.CHAIN)
                .define('G', ItemInit.CHAIN_GEAR.get()).define('N', Items.NETHERITE_SWORD)
                .pattern(" BA")
                .pattern("BGC")
                .pattern("NC ")
                .unlockedBy(getHasName(ItemInit.CHAIN_GEAR.get()), has(ItemInit.CHAIN_GEAR.get())).save(recipeConsumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ItemInit.DOOMBOLT_AXE.get(), 1)
                .define('A', ItemInit.ANCIENT_DRIVE_CRYSTAL.get()).define('B', ItemInit.BOUNDARY_BRICK.get())
                .define('G', ItemInit.GUARDIAN_CUBE.get()).define('N', Items.NETHERITE_AXE)
                .pattern(" BA")
                .pattern("BGB")
                .pattern("NB ")
                .unlockedBy(getHasName(ItemInit.GUARDIAN_CUBE.get()), has(ItemInit.GUARDIAN_CUBE.get())).save(recipeConsumer);

        //古界岩→磨制粗界岩衍生
        twoByTwoPacker(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get(), ItemInit.BOUNDARY_BRICK.get(), 2, "from_boundary_brick");
        twoByTwoPacker(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get(), BlockInit.ANCIENT_BOUNDARY_STONE.get(), 4, "from_ancient_boundary_stone");
        stonecutterResultFromBase(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get(), BlockInit.ANCIENT_BOUNDARY_STONE.get());
        stairBuilder(BlockInit.POLISHED_ROUGH_BOUNDARY_STONE_STAIRS.get(), Ingredient.of(BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get())).unlockedBy(getHasName(BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get()), has(BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get())).save(recipeConsumer);
        stonecutterResultFromBase(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_ROUGH_BOUNDARY_STONE_STAIRS.get(), BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get());
        oneByTwoPacker(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.ROUGH_BOUNDARY_STONE_PILLAR.get(), BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get(), 2);
        stonecutterResultFromBase(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.ROUGH_BOUNDARY_STONE_PILLAR.get(), BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get());
        slab(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_ROUGH_BOUNDARY_STONE_SLAB.get(), BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get());
        stonecutterResultFromBase(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_ROUGH_BOUNDARY_STONE_SLAB.get(), BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get(), 2);
        oneByTwoPacker(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.CHISELED_ROUGH_BOUNDARY_BRICKS.get(), BlockInit.POLISHED_ROUGH_BOUNDARY_STONE_SLAB.get(), 1);
        stonecutterResultFromBase(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.CHISELED_ROUGH_BOUNDARY_BRICKS.get(), BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get());

        //磨制粗界岩→磨制粗界岩砖衍生
        twoByTwoPacker(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.ROUGH_BOUNDARY_BRICKS.get(), BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get(), 4);
        stonecutterResultFromBase(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.ROUGH_BOUNDARY_BRICKS.get(), BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get());
        stairBuilder(BlockInit.ROUGH_BOUNDARY_BRICK_STAIRS.get(), Ingredient.of(BlockInit.ROUGH_BOUNDARY_BRICKS.get())).unlockedBy(getHasName(BlockInit.ROUGH_BOUNDARY_BRICKS.get()), has(BlockInit.ROUGH_BOUNDARY_BRICKS.get())).save(recipeConsumer);
        stonecutterResultFromBase(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.ROUGH_BOUNDARY_BRICK_STAIRS.get(), BlockInit.ROUGH_BOUNDARY_BRICKS.get());
        slab(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.ROUGH_BOUNDARY_BRICK_SLAB.get(), BlockInit.ROUGH_BOUNDARY_BRICKS.get());
        stonecutterResultFromBase(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.ROUGH_BOUNDARY_BRICK_SLAB.get(), BlockInit.ROUGH_BOUNDARY_BRICKS.get(),2);
        wall(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.ROUGH_BOUNDARY_BRICK_WALL.get(), BlockInit.ROUGH_BOUNDARY_BRICKS.get());
        stonecutterResultFromBase(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.ROUGH_BOUNDARY_BRICK_WALL.get(), BlockInit.ROUGH_BOUNDARY_BRICKS.get());
        oreSmelting(recipeConsumer, ImmutableList.of(BlockInit.ROUGH_BOUNDARY_BRICKS.get()), RecipeCategory.MISC, BlockInit.CRACKED_ROUGH_BOUNDARY_BRICKS.get(), 0.1F, 200, "rough_boundary_bricks");

        //古界岩→磨制界岩衍生
        oreSmelting(recipeConsumer, ImmutableList.of(BlockInit.ANCIENT_BOUNDARY_STONE.get()), RecipeCategory.MISC, ItemInit.BOUNDARY_BRICK.get(), 0.1F, 200, "ancient_boundary_stone");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_BOUNDARY_STONE.get(), 2).define('Q', Items.QUARTZ).define('B', ItemInit.BOUNDARY_BRICK.get()).pattern("BQ").pattern("QB").unlockedBy("has_boundary_brick", has(ItemInit.BOUNDARY_BRICK.get())).save(recipeConsumer);
        stairBuilder(BlockInit.POLISHED_BOUNDARY_STONE_STAIRS.get(), Ingredient.of(BlockInit.POLISHED_BOUNDARY_STONE.get())).unlockedBy(getHasName(BlockInit.POLISHED_BOUNDARY_STONE.get()), has(BlockInit.POLISHED_BOUNDARY_STONE.get())).save(recipeConsumer);
        stonecutterResultFromBase(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_BOUNDARY_STONE_STAIRS.get(), BlockInit.POLISHED_BOUNDARY_STONE.get());
        slab(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_BOUNDARY_STONE_SLAB.get(), BlockInit.POLISHED_BOUNDARY_STONE.get());
        stonecutterResultFromBase(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_BOUNDARY_STONE_SLAB.get(), BlockInit.POLISHED_BOUNDARY_STONE.get(),2);
        oneByTwoPacker(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_BOUNDARY_STONE_PILLAR.get(), BlockInit.POLISHED_BOUNDARY_STONE.get(), 2);
        stonecutterResultFromBase(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_BOUNDARY_STONE_PILLAR.get(), BlockInit.POLISHED_BOUNDARY_STONE.get());

        //磨制界岩→磨制界岩砖衍生
        twoByTwoPacker(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_BOUNDARY_BRICKS.get(), BlockInit.POLISHED_BOUNDARY_STONE.get(), 4);
        stonecutterResultFromBase(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_BOUNDARY_BRICKS.get(), BlockInit.POLISHED_BOUNDARY_STONE.get());
        stairBuilder(BlockInit.POLISHED_BOUNDARY_BRICK_STAIRS.get(), Ingredient.of(BlockInit.POLISHED_BOUNDARY_BRICKS.get())).unlockedBy(getHasName(BlockInit.POLISHED_BOUNDARY_BRICKS.get()), has(BlockInit.POLISHED_BOUNDARY_BRICKS.get())).save(recipeConsumer);
        stonecutterResultFromBase(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_BOUNDARY_BRICK_STAIRS.get(), BlockInit.POLISHED_BOUNDARY_BRICKS.get());
        slab(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_BOUNDARY_BRICK_SLAB.get(), BlockInit.POLISHED_BOUNDARY_BRICKS.get());
        stonecutterResultFromBase(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_BOUNDARY_BRICK_SLAB.get(), BlockInit.POLISHED_BOUNDARY_BRICKS.get(),2);
        wall(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_BOUNDARY_BRICK_WALL.get(), BlockInit.POLISHED_BOUNDARY_BRICKS.get());
        stonecutterResultFromBase(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.POLISHED_BOUNDARY_BRICK_WALL.get(), BlockInit.POLISHED_BOUNDARY_BRICKS.get());
        oneByTwoPacker(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.CHISELED_POLISHED_BOUNDARY_BRICKS.get(), BlockInit.POLISHED_BOUNDARY_BRICK_SLAB.get(), 1);
        stonecutterResultFromBase(recipeConsumer, RecipeCategory.BUILDING_BLOCKS, BlockInit.CHISELED_POLISHED_BOUNDARY_BRICKS.get(), BlockInit.POLISHED_BOUNDARY_BRICKS.get());
        oreSmelting(recipeConsumer, ImmutableList.of(BlockInit.POLISHED_BOUNDARY_BRICKS.get()), RecipeCategory.MISC, BlockInit.CRACKED_POLISHED_BOUNDARY_BRICKS.get(), 0.1F, 200, "polished_boundary_bricks");

        woodFromLogs(recipeConsumer, BlockInit.BLIGHTED_OAK_WOOD.get().asItem(), BlockInit.BLIGHTED_OAK_LOG.get().asItem());
        woodFromLogs(recipeConsumer, BlockInit.STRIPPED_BLIGHTED_OAK_WOOD.get().asItem(), BlockInit.STRIPPED_BLIGHTED_OAK_LOG.get().asItem());
    }

    protected static void oneByTwoPacker(Consumer<FinishedRecipe> finishedRecipeConsumer, RecipeCategory category, ItemLike result, ItemLike material, int count) {
        ShapedRecipeBuilder.shaped(category, result, count)
                .define('#', material)
                .pattern("#")
                .pattern("#")
                .unlockedBy(getHasName(material), has(material)).save(finishedRecipeConsumer);
    }

    protected static void twoByTwoPacker(Consumer<FinishedRecipe> finishedRecipeConsumer, RecipeCategory category,
                                         ItemLike result, ItemLike material, int count, String recipeIdSuffix) {
        ShapedRecipeBuilder.shaped(category, result, count)
                .define('#', material)
                .pattern("##")
                .pattern("##")
                .unlockedBy(getHasName(material), has(material))
                .save(finishedRecipeConsumer, getItemName(result) + "_" + recipeIdSuffix);
    }

    protected static void twoByTwoPacker(Consumer<FinishedRecipe> finishedRecipeConsumer, RecipeCategory category,
                                         ItemLike result, ItemLike material, int count) {
        String suffix = getItemName(material);
        twoByTwoPacker(finishedRecipeConsumer, category, result, material, count, suffix);
    }

    private static void upgradeSmithingTemplate(Consumer<FinishedRecipe> recipeConsumer, Item base1, Item base2, Item result) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, result, 1).define('N', Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).define('X', base1).define('#', base2)
                .pattern("X#X").pattern("#N#").pattern("X#X").unlockedBy(getHasName(result), has(result)).save(recipeConsumer);
    }

    private static void ghostWarriorSmithing(Consumer<FinishedRecipe> finishedRecipeConsumer, Item ingredientItem, RecipeCategory category, Item resultItem) {
        commonSmithing(ItemInit.GHOST_WARRIOR_UPGRADE_SMITHING_TEMPLATE.get(), ItemInit.GHOST_STEEL_INGOT.get(), finishedRecipeConsumer, ingredientItem, category, resultItem);
    }

    private static void commonSmithing(Item smithingTemplate, Item ingredients, Consumer<FinishedRecipe> finishedRecipeConsumer, Item ingredientItem, RecipeCategory category, Item resultItem) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(smithingTemplate), Ingredient.of(ingredientItem), Ingredient.of(ingredients), category, resultItem).unlocks("has_" + getItemName(ingredients), has(ingredients)).save(finishedRecipeConsumer, getItemName(resultItem) + "_smithing");
    }
}
