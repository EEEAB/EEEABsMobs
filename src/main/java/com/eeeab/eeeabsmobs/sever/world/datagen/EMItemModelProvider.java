package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EMItemModelProvider extends ItemModelProvider {
    public EMItemModelProvider(DataGenerator output, ExistingFileHelper existingFileHelper) {
        super(output, EEEABMobs.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //刷怪蛋
        spawnEgg(ItemInit.TESTER_EGG);
        spawnEgg(ItemInit.CORPSE_EGG);
        spawnEgg(ItemInit.CORPSE_VILLAGER_EGG);
        spawnEgg(ItemInit.CORPSE_WARLOCK_EGG);
        spawnEgg(ItemInit.GULING_SENTINEL_EGG);
        spawnEgg(ItemInit.GULING_SENTINEL_HEAVY_EGG);
        spawnEgg(ItemInit.NAMELESS_GUARDIAN_EGG);
        spawnEgg(ItemInit.IMMORTAL_GOLEM_EGG);
        spawnEgg(ItemInit.IMMORTAL_SKELETON_EGG);
        spawnEgg(ItemInit.IMMORTAL_KNIGHT_EGG);
        spawnEgg(ItemInit.IMMORTAL_SHAMAN_EGG);
        spawnEgg(ItemInit.IMMORTAL_BOSS_EGG);
        //杂项
        simpleItem(ItemInit.LOGO_ITEM);
        simpleItem(ItemInit.REMOVE_MOB);
        simpleItem(ItemInit.GULING_EYE);
        simpleItem(ItemInit.BLOODY_ALTAR_EYE);
        simpleItem(ItemInit.GUARDIANS_MUSIC_DISC);
        //材料
        simpleItem(ItemInit.HEART_OF_PAGAN);
        simpleItem(ItemInit.ANCIENT_DRIVE_CRYSTAL);
        simpleItem(ItemInit.IMMORTAL_INGOT);
        simpleItem(ItemInit.GHOST_STEEL_INGOT);
        handheldItem(ItemInit.IMMORTAL_BONE);
        //战斗
        simpleItem(ItemInit.SOUL_SUMMONING_NECKLACE);
        simpleItem(ItemInit.IMMORTAL_DEBRIS);
        handheldItem(ItemInit.IMMORTAL_AXE);
        handheldItem(ItemInit.IMMORTAL_SWORD);
        simpleItem(ItemInit.GHOST_WARRIOR_HELMET);
        simpleItem(ItemInit.GHOST_WARRIOR_CHESTPLATE);
        simpleItem(ItemInit.GHOST_WARRIOR_LEGGINGS);
        simpleItem(ItemInit.GHOST_WARRIOR_BOOTS);

    }

    private void spawnEgg(RegistryObject<Item> item) {
        withExistingParent(item.getId().getPath(), new ResourceLocation("item/template_spawn_egg"));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(EEEABMobs.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(EEEABMobs.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(EEEABMobs.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder simpleBlockItemBlockTexture(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(EEEABMobs.MOD_ID, "block/" + item.getId().getPath()));
    }

    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(EEEABMobs.MOD_ID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    //树苗物品模型
    private ItemModelBuilder saplingItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(EEEABMobs.MOD_ID, "block/" + item.getId().getPath()));
    }

    //活版门物品模型
    public void trapdoorItem(RegistryObject<Block> block) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath() + "_bottom"));
    }

    //围栏物品模型
    public void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory"))
                .texture("texture", new ResourceLocation(EEEABMobs.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    //按钮物品模型
    public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory"))
                .texture("texture", new ResourceLocation(EEEABMobs.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    //墙物品模型
    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", new ResourceLocation(EEEABMobs.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }
}