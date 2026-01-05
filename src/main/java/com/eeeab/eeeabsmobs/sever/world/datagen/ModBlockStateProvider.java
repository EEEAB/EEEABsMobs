package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.block.BlockGalacticAlphabet;
import com.eeeab.eeeabsmobs.sever.block.BlockMirroredFlower;
import com.eeeab.eeeabsmobs.sever.block.properties.SGACharacter;
import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, EEEABMobs.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        cubeAll(BlockInit.DUNGEON_BRICK);
        cubeAll(BlockInit.ANCIENT_BOUNDARY_STONE);
        cubeAll(BlockInit.ROUGH_BOUNDARY_BRICKS);
        cubeAll(BlockInit.CRACKED_ROUGH_BOUNDARY_BRICKS);
        cubeAll(BlockInit.CHISELED_ROUGH_BOUNDARY_BRICKS);
        cubeAll(BlockInit.POLISHED_BOUNDARY_BRICKS);
        cubeAll(BlockInit.CRACKED_POLISHED_BOUNDARY_BRICKS);
        cubeAll(BlockInit.CHISELED_POLISHED_BOUNDARY_BRICKS);
        cubeAll(BlockInit.UNCARVED_BOUNDARY_STONE);
        cubeAll(BlockInit.IMMORTAL_BLOCK);
        cubeAll(BlockInit.GHOST_STEEL_BLOCK);
        cubeAll(BlockInit.CUT_GHOST_STEEL_BLOCK);
        cubeAll(BlockInit.EROSION_DEEPSLATE_BRICKS);
        cubeAll(BlockInit.BOUNDARY_LAMP);
        cubeAll(BlockInit.EROSION_ROCK_BRICKS);
        cubeAll(BlockInit.EROSION_OAK_PLANKS);
        cubeAll(BlockInit.BLIGHTED_OAK_PLANKS);
        cubeAll(BlockInit.ERODED_SOIL);
        cubeAll(BlockInit.DARKENED_COAL_ORE);
        cubeAll(BlockInit.DARKENED_IRON_ORE);
        cubeAll(BlockInit.BLIGHTED_STONE);
        cubeAll(BlockInit.BLIGHTED_COBBLESTONE);
        cubeAll(BlockInit.VOIDSHARD);
        cubeAll(BlockInit.POLISHED_VOIDSHARD);
        cubeAll(BlockInit.DARK_EROSION_ROCK);
        cubeAll(BlockInit.CHISELED_DARK_EROSION_ROCK);
        cubeAll(BlockInit.POLISHED_DARK_EROSION_ROCK);
        cubeAll(BlockInit.DARK_EROSION_ROCK_BRICKS);
        cubeAll(BlockInit.CRACKED_DARK_EROSION_ROCK_BRICKS);
        //cubeAll(BlockInit.TOMBSTONE);
        cubeAllWithRenderType(BlockInit.IRON_GRATE, "cutout");

        alphabets((BlockGalacticAlphabet) BlockInit.RUNIC_BOUNDARY_STONE.get());

        variantBlock(BlockInit.STEPPING_POISON_TRAP, BlockStateProperties.OPEN, "_on", "_off");
        simpleBlockItem(BlockInit.STEPPING_POISON_TRAP.get(), new ModelFile.UncheckedModelFile(blockTexture(BlockInit.STEPPING_POISON_TRAP.get(), "_off")));
        variantBlock(BlockInit.STEPPING_SKELETON_TRAP, BlockStateProperties.OPEN, "_on", "_off");
        simpleBlockItem(BlockInit.STEPPING_SKELETON_TRAP.get(), new ModelFile.UncheckedModelFile(blockTexture(BlockInit.STEPPING_SKELETON_TRAP.get(), "_off")));

        axisBlock(BlockInit.DARK_EROSION_ROCK_PILLAR, blockTexture(BlockInit.DARK_EROSION_ROCK_PILLAR.get()), extend(blockTexture(BlockInit.DARK_EROSION_ROCK_PILLAR.get()), "_top"));
        logBlock(BlockInit.EROSION_OAK_LOG);
        axisBlock(BlockInit.EROSION_OAK_WOOD, blockTexture(BlockInit.EROSION_OAK_LOG.get()), blockTexture(BlockInit.EROSION_OAK_LOG.get()));
        axisBlock(BlockInit.STRIPPED_EROSION_OAK_LOG, blockTexture(BlockInit.STRIPPED_EROSION_OAK_LOG.get()), extend(blockTexture(BlockInit.STRIPPED_EROSION_OAK_LOG.get()), "_top"));
        axisBlock(BlockInit.STRIPPED_EROSION_OAK_WOOD, blockTexture(BlockInit.STRIPPED_EROSION_OAK_LOG.get()), blockTexture(BlockInit.STRIPPED_EROSION_OAK_LOG.get()));
        logBlock(BlockInit.BLIGHTED_OAK_LOG);
        axisBlock(BlockInit.BLIGHTED_OAK_WOOD, blockTexture(BlockInit.BLIGHTED_OAK_LOG.get()), blockTexture(BlockInit.BLIGHTED_OAK_LOG.get()));
        axisBlock(BlockInit.STRIPPED_BLIGHTED_OAK_LOG, blockTexture(BlockInit.STRIPPED_BLIGHTED_OAK_LOG.get()), extend(blockTexture(BlockInit.STRIPPED_BLIGHTED_OAK_LOG.get()), "_top"));
        axisBlock(BlockInit.STRIPPED_BLIGHTED_OAK_WOOD, blockTexture(BlockInit.STRIPPED_BLIGHTED_OAK_LOG.get()), blockTexture(BlockInit.STRIPPED_BLIGHTED_OAK_LOG.get()));

        stairsBlock((StairBlock) BlockInit.EROSION_OAK_STAIRS.get(), blockTexture(BlockInit.EROSION_OAK_PLANKS.get()));
        blockItem(BlockInit.EROSION_OAK_STAIRS);
        slabBlock((SlabBlock) BlockInit.EROSION_OAK_SLAB.get(), blockTexture(BlockInit.EROSION_OAK_PLANKS.get()), blockTexture(BlockInit.EROSION_OAK_PLANKS.get()));
        blockItem(BlockInit.EROSION_OAK_SLAB);
        pressurePlateBlock((PressurePlateBlock) BlockInit.EROSION_OAK_PRESSURE_PLATE.get(), blockTexture(BlockInit.EROSION_OAK_PLANKS.get()));
        blockItem(BlockInit.EROSION_OAK_PRESSURE_PLATE);
        buttonBlock((ButtonBlock) BlockInit.EROSION_OAK_BUTTON.get(), blockTexture(BlockInit.EROSION_OAK_PLANKS.get()));
        fenceBlock((FenceBlock) BlockInit.EROSION_OAK_FENCE.get(), blockTexture(BlockInit.EROSION_OAK_PLANKS.get()));
        fenceGateBlock((FenceGateBlock) BlockInit.EROSION_OAK_FENCE_GATE.get(), blockTexture(BlockInit.EROSION_OAK_PLANKS.get()));
        blockItem(BlockInit.EROSION_OAK_FENCE_GATE);

        stairsBlock((StairBlock) BlockInit.BLIGHTED_OAK_STAIRS.get(), blockTexture(BlockInit.BLIGHTED_OAK_PLANKS.get()));
        blockItem(BlockInit.BLIGHTED_OAK_STAIRS);
        slabBlock((SlabBlock) BlockInit.BLIGHTED_OAK_SLAB.get(), blockTexture(BlockInit.BLIGHTED_OAK_PLANKS.get()), blockTexture(BlockInit.BLIGHTED_OAK_PLANKS.get()));
        blockItem(BlockInit.BLIGHTED_OAK_SLAB);
        pressurePlateBlock((PressurePlateBlock) BlockInit.BLIGHTED_OAK_PRESSURE_PLATE.get(), blockTexture(BlockInit.BLIGHTED_OAK_PLANKS.get()));
        blockItem(BlockInit.BLIGHTED_OAK_PRESSURE_PLATE);
        buttonBlock((ButtonBlock) BlockInit.BLIGHTED_OAK_BUTTON.get(), blockTexture(BlockInit.BLIGHTED_OAK_PLANKS.get()));
        fenceBlock((FenceBlock) BlockInit.BLIGHTED_OAK_FENCE.get(), blockTexture(BlockInit.BLIGHTED_OAK_PLANKS.get()));
        fenceGateBlock((FenceGateBlock) BlockInit.BLIGHTED_OAK_FENCE_GATE.get(), blockTexture(BlockInit.BLIGHTED_OAK_PLANKS.get()));
        blockItem(BlockInit.BLIGHTED_OAK_FENCE_GATE);

        stairsBlock((StairBlock) BlockInit.BLIGHTED_STONE_STAIRS.get(), blockTexture(BlockInit.BLIGHTED_STONE.get()));
        blockItem(BlockInit.BLIGHTED_STONE_STAIRS);
        slabBlock((SlabBlock) BlockInit.BLIGHTED_STONE_SLAB.get(), blockTexture(BlockInit.BLIGHTED_STONE.get()), blockTexture(BlockInit.BLIGHTED_STONE.get()));
        blockItem(BlockInit.BLIGHTED_STONE_SLAB);
        pressurePlateBlock((PressurePlateBlock) BlockInit.BLIGHTED_STONE_PRESSURE_PLATE.get(), blockTexture(BlockInit.BLIGHTED_STONE.get()));
        blockItem(BlockInit.BLIGHTED_STONE_PRESSURE_PLATE);
        buttonBlock((ButtonBlock) BlockInit.BLIGHTED_STONE_BUTTON.get(), blockTexture(BlockInit.BLIGHTED_STONE.get()));
        stairsBlock((StairBlock) BlockInit.BLIGHTED_COBBLESTONE_STAIRS.get(), blockTexture(BlockInit.BLIGHTED_COBBLESTONE.get()));
        blockItem(BlockInit.BLIGHTED_COBBLESTONE_STAIRS);
        slabBlock((SlabBlock) BlockInit.BLIGHTED_COBBLESTONE_SLAB.get(), blockTexture(BlockInit.BLIGHTED_COBBLESTONE.get()), blockTexture(BlockInit.BLIGHTED_COBBLESTONE.get()));
        blockItem(BlockInit.BLIGHTED_COBBLESTONE_SLAB);
        wallBlock((WallBlock) BlockInit.BLIGHTED_COBBLESTONE_WALL.get(), blockTexture(BlockInit.BLIGHTED_COBBLESTONE.get()));
        stairsBlock((StairBlock) BlockInit.POLISHED_DARK_EROSION_ROCK_STAIRS.get(), blockTexture(BlockInit.POLISHED_DARK_EROSION_ROCK.get()));
        blockItem(BlockInit.POLISHED_DARK_EROSION_ROCK_STAIRS);
        slabBlock((SlabBlock) BlockInit.POLISHED_DARK_EROSION_ROCK_SLAB.get(), blockTexture(BlockInit.POLISHED_DARK_EROSION_ROCK.get()), blockTexture(BlockInit.POLISHED_DARK_EROSION_ROCK.get()));
        blockItem(BlockInit.POLISHED_DARK_EROSION_ROCK_SLAB);
        wallBlock((WallBlock) BlockInit.POLISHED_DARK_EROSION_ROCK_WALL.get(), blockTexture(BlockInit.POLISHED_DARK_EROSION_ROCK.get()));
        stairsBlock((StairBlock) BlockInit.DARK_EROSION_ROCK_BRICKS_STAIRS.get(), blockTexture(BlockInit.DARK_EROSION_ROCK_BRICKS.get()));
        blockItem(BlockInit.DARK_EROSION_ROCK_BRICKS_STAIRS);
        slabBlock((SlabBlock) BlockInit.DARK_EROSION_ROCK_BRICKS_SLAB.get(), blockTexture(BlockInit.DARK_EROSION_ROCK_BRICKS.get()), blockTexture(BlockInit.DARK_EROSION_ROCK_BRICKS.get()));
        blockItem(BlockInit.DARK_EROSION_ROCK_BRICKS_SLAB);
        wallBlock((WallBlock) BlockInit.DARK_EROSION_ROCK_BRICKS_WALL.get(), blockTexture(BlockInit.DARK_EROSION_ROCK_BRICKS.get()));
        slabBlock((SlabBlock) BlockInit.VOIDSHARD_SLAB.get(), blockTexture(BlockInit.VOIDSHARD.get()), blockTexture(BlockInit.VOIDSHARD.get()));
        blockItem(BlockInit.VOIDSHARD_SLAB);
        stairsBlock((StairBlock) BlockInit.VOIDSHARD_STAIRS.get(), blockTexture(BlockInit.VOIDSHARD.get()));
        blockItem(BlockInit.VOIDSHARD_STAIRS);
        wallBlock((WallBlock) BlockInit.VOIDSHARD_WALL.get(), blockTexture(BlockInit.VOIDSHARD.get()));
        stairsBlock((StairBlock) BlockInit.POLISHED_VOIDSHARD_STAIRS.get(), blockTexture(BlockInit.POLISHED_VOIDSHARD.get()));
        blockItem(BlockInit.POLISHED_VOIDSHARD_STAIRS);
        slabBlock((SlabBlock) BlockInit.POLISHED_VOIDSHARD_SLAB.get(), blockTexture(BlockInit.POLISHED_VOIDSHARD.get()), blockTexture(BlockInit.POLISHED_VOIDSHARD.get()));
        blockItem(BlockInit.POLISHED_VOIDSHARD_SLAB);
        stairsBlock((StairBlock) BlockInit.CUT_GHOST_STEEL_BLOCK_STAIRS.get(), blockTexture(BlockInit.CUT_GHOST_STEEL_BLOCK.get()));
        blockItem(BlockInit.CUT_GHOST_STEEL_BLOCK_STAIRS);
        slabBlock((SlabBlock) BlockInit.CUT_GHOST_STEEL_BLOCK_SLAB.get(), blockTexture(BlockInit.CUT_GHOST_STEEL_BLOCK.get()), blockTexture(BlockInit.CUT_GHOST_STEEL_BLOCK.get()));
        blockItem(BlockInit.CUT_GHOST_STEEL_BLOCK_SLAB);

        doorBlock((DoorBlock) BlockInit.EROSION_OAK_DOOR.get(), extend(blockTexture(BlockInit.EROSION_OAK_DOOR.get()), "_bottom"), extend(blockTexture(BlockInit.EROSION_OAK_DOOR.get()), "_top"));
        doorBlockWithRenderType((DoorBlock) BlockInit.BLIGHTED_OAK_DOOR.get(), extend(blockTexture(BlockInit.BLIGHTED_OAK_DOOR.get()), "_bottom"), extend(blockTexture(BlockInit.BLIGHTED_OAK_DOOR.get()), "_top"), "cutout");
        trapdoorBlock((TrapDoorBlock) BlockInit.EROSION_OAK_TRAPDOOR.get(), blockTexture(BlockInit.EROSION_OAK_TRAPDOOR.get()), true);
        trapdoorBlockWithRenderType((TrapDoorBlock) BlockInit.BLIGHTED_OAK_TRAPDOOR.get(), blockTexture(BlockInit.BLIGHTED_OAK_TRAPDOOR.get()), true, "cutout");
        leavesBlock(BlockInit.EROSION_OAK_LEAVES);
        saplingBlock(BlockInit.EROSION_OAK_SAPLING);
        saplingBlock(BlockInit.EROSION_OAK_BERRY);
        saplingBlock(BlockInit.BLIGHTED_OAK_SAPLING);

        mirrored(BlockInit.BONE_BUSH, "cutout");
        ResourceLocation boneBushRL = blockTexture(BlockInit.BONE_BUSH.get());
        simpleBlockItem(BlockInit.BONE_BUSH.get(), models().cross(boneBushRL.getPath(), boneBushRL));
        simpleBlockWithItem(BlockInit.POTTED_BONE_BUSH.get(), models().singleTexture("potted_bone_bush", new ResourceLocation("flower_pot_cross"), "plant", boneBushRL).renderType("cutout"));
    }

    public void logBlock(RegistryObject<Block> rob) {
        super.logBlock((RotatedPillarBlock) rob.get());
        blockItem(rob);
    }

    public void axisBlock(RegistryObject<Block> rob, ResourceLocation side, ResourceLocation end) {
        super.axisBlock((RotatedPillarBlock) rob.get(), side, end);
        blockItem(rob);
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {
        return new ResourceLocation(rl.getNamespace(), rl.getPath() + suffix);
    }

    private ResourceLocation blockTexture(Block block, String suffix) {
        ResourceLocation name = ForgeRegistries.BLOCKS.getKey(block);
        return new ResourceLocation(name.getNamespace(), ModelProvider.BLOCK_FOLDER + "/" + name.getPath() + suffix);
    }

    private void blockItem(RegistryObject<Block> rob) {
        simpleBlockItem(rob.get(), new ModelFile.UncheckedModelFile(blockTexture(rob.get(), "")));
    }

    private void saplingBlock(RegistryObject<Block> rob) {
        simpleBlock(rob.get(), models().cross(ForgeRegistries.BLOCKS.getKey(rob.get()).getPath(), blockTexture(rob.get())).renderType("cutout"));
    }

    private void leavesBlock(RegistryObject<Block> rob) {
        simpleBlockWithItem(rob.get(), models().singleTexture(ForgeRegistries.BLOCKS.getKey(rob.get()).getPath(),
                new ResourceLocation("minecraft:block/leaves"), "all", blockTexture(rob.get())).renderType("cutout"));
    }

    /**
     * 生成同一面方块
     */
    private void cubeAll(RegistryObject<Block> rob) {
        simpleBlockWithItem(rob.get(), cubeAll(rob.get()));
    }

    /**
     * 生成同一面方块(指定渲染类型)
     */
    private void cubeAllWithRenderType(RegistryObject<Block> rob, String renderType) {
        Block block = rob.get();
        ResourceLocation name = ForgeRegistries.BLOCKS.getKey(block);
        simpleBlockWithItem(block, models().cubeAll(name.getPath(), blockTexture(block)).renderType(renderType));
    }

    /**
     * 镜像纹理(需单独提供镜像贴图)
     */
    private void mirrored(RegistryObject<Block> rob, String renderType) {
        getVariantBuilder(rob.get())
                .partialState().with(BlockMirroredFlower.MIRRORED, false)
                .modelForState().modelFile(models().cross(blockTexture(rob.get()).getPath(), blockTexture(rob.get())).renderType(renderType)).addModel()
                .partialState().with(BlockMirroredFlower.MIRRORED, true)
                .modelForState().modelFile(models().cross(blockTexture(rob.get()).getPath() + "_mirrored", blockTexture(rob.get(), "_mirrored")).renderType(renderType)).addModel();
    }

    private void variantBlock(RegistryObject<Block> block, BooleanProperty property, String trueSuffix, String falseSuffix) {
        Block targetBlock = block.get();
        ResourceLocation baseTexture = blockTexture(targetBlock);
        String basePath = baseTexture.getPath();
        VariantBlockStateBuilder builder = getVariantBuilder(targetBlock);
        builder.partialState().with(property, false).modelForState()
                .modelFile(models().cubeAll(
                        basePath + falseSuffix,
                        blockTexture(targetBlock, falseSuffix)))
                .addModel();
        builder.partialState().with(property, true).modelForState()
                .modelFile(models().cubeAll(
                        basePath + trueSuffix,
                        blockTexture(targetBlock, trueSuffix)))
                .addModel();
    }

    /**
     * 标准银河字母(SGA)纹理
     */
    private void alphabets(BlockGalacticAlphabet block) {
        String name = ForgeRegistries.BLOCKS.getKey(block).getPath();
        VariantBlockStateBuilder builder = getVariantBuilder(block);
        EnumSet<SGACharacter> supportedChars = block.getCharacters();
        SGACharacter defaultChar = supportedChars.iterator().next();
        Map<SGACharacter, ModelFile> modelCache = new HashMap<>();
        for (SGACharacter character : supportedChars) {
            ModelFile model = models().cubeAll(
                    name + "_" + character,
                    modLoc("block/" + name + "_" + character)
            );
            modelCache.put(character, model);
        }
        ModelFile defaultModel = modelCache.get(defaultChar);
        for (SGACharacter character : SGACharacter.values()) {
            ModelFile modelToUse;
            if (supportedChars.contains(character)) {
                modelToUse = modelCache.get(character);
            } else {
                modelToUse = defaultModel;
            }
            builder.partialState()
                    .with(BlockGalacticAlphabet.CHARACTER, character)
                    .setModels(new ConfiguredModel(modelToUse));
        }
        simpleBlockItem(block, defaultModel);
    }
}