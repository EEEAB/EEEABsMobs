package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, EEEABMobs.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //刷怪蛋
        spawnEgg(ItemInit.TESTER_EGG);
        spawnEgg(ItemInit.CORPSE_EGG);
        spawnEgg(ItemInit.CORPSE_VILLAGER_EGG);
        spawnEgg(ItemInit.CORPSE_WARLOCK_EGG);
        spawnEgg(ItemInit.RELIC_OBSERVER_EGG);
        spawnEgg(ItemInit.RELIC_RIPPER_EGG);
        spawnEgg(ItemInit.RELIC_EARTHSHAKER_EGG);
        spawnEgg(ItemInit.RELIC_ANNIHILATOR_EGG);
        spawnEgg(ItemInit.NAMELESS_GUARDIAN_EGG);
        spawnEgg(ItemInit.IMMORTAL_GOLEM_EGG);
        spawnEgg(ItemInit.IMMORTAL_SKELETON_EGG);
        spawnEgg(ItemInit.IMMORTAL_ARCHER_EGG);
        spawnEgg(ItemInit.IMMORTAL_MAGE_EGG);
        spawnEgg(ItemInit.IMMORTAL_WARRIOR_EGG);
        spawnEgg(ItemInit.IMMORTAL_KNIGHT_EGG);
        spawnEgg(ItemInit.IMMORTAL_SHAMAN_EGG);
        spawnEgg(ItemInit.IMMORTAL_EXECUTIONER_EGG);
        spawnEgg(ItemInit.IMMORTAL_BOSS_EGG);
        //杂项
        simpleItem(ItemInit.LOGO_ITEM);
        simpleItem(ItemInit.REMOVE_MOB);
        simpleItem(ItemInit.ANIMATION_CONTROLLER);
        simpleItem(ItemInit.CORE_BASTION_EYE);
        simpleItem(ItemInit.BLOODY_ALTAR_EYE);
        simpleItem(ItemInit.GUARDIANS_MUSIC_DISC);
        simpleItem(ItemInit.THE_ARMY_OF_MINOTAUR_MUSIC_DISC);
        //材料
        simpleItem(ItemInit.HEART_OF_PAGAN);
        simpleItem(ItemInit.ANCIENT_DRIVE_CRYSTAL);
        simpleItem(ItemInit.BOUNDARY_BRICK);
        simpleItem(ItemInit.IMMORTAL_INGOT);
        simpleItem(ItemInit.GHOST_STEEL_INGOT);
        handheldItem(ItemInit.IMMORTAL_BONE);
        simpleItem(ItemInit.GHOST_WARRIOR_UPGRADE_SMITHING_TEMPLATE);
        //战斗
        simpleItem(ItemInit.SOUL_SUMMONING_NECKLACE);
        simpleItem(ItemInit.IMMORTAL_DEBRIS);
        handheldItem(ItemInit.IMMORTAL_AXE);
        handheldItem(ItemInit.IMMORTAL_SWORD);
        simpleItem(ItemInit.GHOST_WARRIOR_HELMET);
        simpleItem(ItemInit.GHOST_WARRIOR_CHESTPLATE);
        simpleItem(ItemInit.GHOST_WARRIOR_LEGGINGS);
        simpleItem(ItemInit.GHOST_WARRIOR_BOOTS);
        //方块
        simpleBlockItemBlockTexture(BlockInit.BLIGHTED_OAK_SAPLING);
        fenceItem(BlockInit.BLIGHTED_OAK_FENCE, BlockInit.BLIGHTED_OAK_PLANKS);
        simpleBlockItem(BlockInit.BLIGHTED_OAK_DOOR);
        trapdoorItem(BlockInit.BLIGHTED_OAK_TRAPDOOR);
        buttonItem(BlockInit.BLIGHTED_OAK_BUTTON, BlockInit.BLIGHTED_OAK_PLANKS);
        buttonItem(BlockInit.BLIGHTED_STONE_BUTTON, BlockInit.BLIGHTED_STONE);
        wallItem(BlockInit.ROUGH_BOUNDARY_BRICK_WALL, BlockInit.ROUGH_BOUNDARY_BRICKS);
        wallItem(BlockInit.POLISHED_BOUNDARY_BRICK_WALL, BlockInit.POLISHED_BOUNDARY_BRICKS);
        wallItem(BlockInit.BLIGHTED_COBBLESTONE_WALL, BlockInit.BLIGHTED_COBBLESTONE);
        wallItem(BlockInit.VOIDSHARD_WALL, BlockInit.VOIDSHARD);
        wallItem(BlockInit.POLISHED_DARK_EROSION_ROCK_WALL, BlockInit.POLISHED_DARK_EROSION_ROCK);
        wallItem(BlockInit.DARK_EROSION_ROCK_BRICKS_WALL, BlockInit.DARK_EROSION_ROCK_BRICKS);
    }

    private static LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();

    static {
        trimMaterials.put(TrimMaterials.QUARTZ, 0.1F);
        trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F);
        trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F);
        trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F);
        trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F);
        trimMaterials.put(TrimMaterials.AMETHYST, 1.0F);
    }

    /**
     * 生成原版盔甲物品模型
     *
     * @author El_Redstoniano
     */
    private void trimmedArmorItem(RegistryObject<Item> itemRegistryObject) {
        final String MOD_ID = EEEABMobs.MOD_ID;

        if (itemRegistryObject.get() instanceof ArmorItem armorItem) {
            trimMaterials.forEach((trimMaterial, value) -> {

                float trimValue = value;

                String armorType = switch (armorItem.getEquipmentSlot()) {
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };

                String armorItemPath = "item/" + armorItem;
                String trimPath = "trims/items/" + armorType + "_trim_" + trimMaterial.location().getPath();
                String currentTrimName = armorItemPath + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation armorItemResLoc = new ResourceLocation(MOD_ID, armorItemPath);
                ResourceLocation trimResLoc = new ResourceLocation(trimPath); // minecraft namespace
                ResourceLocation trimNameResLoc = new ResourceLocation(MOD_ID, currentTrimName);

                // This is used for making the ExistingFileHelper acknowledge that this texture exist, so this will
                // avoid an IllegalArgumentException
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");

                // Trimmed armorItem files
                getBuilder(currentTrimName)
                        .parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", armorItemResLoc)
                        .texture("layer1", trimResLoc);

                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(itemRegistryObject.getId().getPath(),
                                mcLoc("item/generated"))
                        .override()
                        .model(new ModelFile.UncheckedModelFile(trimNameResLoc))
                        .predicate(mcLoc("trim_type"), trimValue).end()
                        .texture("layer0",
                                new ResourceLocation(MOD_ID,
                                        "item/" + itemRegistryObject.getId().getPath()));
            });
        }
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