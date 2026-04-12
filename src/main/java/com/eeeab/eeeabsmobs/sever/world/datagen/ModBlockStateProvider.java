package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.block.BlockGalacticAlphabet;
import com.eeeab.eeeabsmobs.sever.block.properties.SGACharacter;
import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.TexturedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
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
        cubeAll(BlockInit.POLISHED_ROUGH_BOUNDARY_STONE);
        cubeAll(BlockInit.ROUGH_BOUNDARY_BRICKS);
        cubeAll(BlockInit.CRACKED_ROUGH_BOUNDARY_BRICKS);
        cubeAll(BlockInit.CHISELED_ROUGH_BOUNDARY_BRICKS);
        cubeAll(BlockInit.STEPPING_SHOCK_TRAP);
        cubeAll(BlockInit.POLISHED_BOUNDARY_STONE);
        cubeAll(BlockInit.POLISHED_BOUNDARY_BRICKS);
        cubeAll(BlockInit.CRACKED_POLISHED_BOUNDARY_BRICKS);
        cubeAll(BlockInit.CHISELED_POLISHED_BOUNDARY_BRICKS);
        cubeAll(BlockInit.IMMORTAL_BLOCK);
        cubeAll(BlockInit.GHOST_STEEL_BLOCK);
        cubeAll(BlockInit.CUT_GHOST_STEEL_BLOCK);
        cubeAll(BlockInit.EROSION_DEEPSLATE_BRICKS);
        cubeAll(BlockInit.BOUNDARY_LAMP);
        cubeAll(BlockInit.EROSION_ROCK_BRICKS);
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

        createHorizontallyRotatedBlock((HorizontalDirectionalBlock) BlockInit.UNCARVED_BOUNDARY_STONE.get());
        createHorizontallyRotatedBlock((HorizontalDirectionalBlock) BlockInit.BOUNDARY_CORE.get());
        alphabets((BlockGalacticAlphabet) BlockInit.RUNIC_BOUNDARY_STONE.get());

        variantCubeBottomTop(BlockInit.STEPPING_FLAME_TRAP.get(), BlockInit.POLISHED_BOUNDARY_STONE.get(), BlockStateProperties.OPEN, "_on", "_off");
        simpleBlockItem(BlockInit.STEPPING_FLAME_TRAP.get(), new ModelFile.UncheckedModelFile(blockTexture(BlockInit.STEPPING_FLAME_TRAP.get(), "_off")));
        variantCubeBottomTop(BlockInit.STEPPING_POISON_TRAP.get(), BlockInit.DARK_EROSION_ROCK_BRICKS.get(), BlockStateProperties.OPEN, "_on", "_off");
        simpleBlockItem(BlockInit.STEPPING_POISON_TRAP.get(), new ModelFile.UncheckedModelFile(blockTexture(BlockInit.STEPPING_POISON_TRAP.get(), "_off")));
        variantCubeBottomTop(BlockInit.STEPPING_SKELETON_TRAP.get(), null, BlockStateProperties.OPEN, "_on", "_off");
        simpleBlockItem(BlockInit.STEPPING_SKELETON_TRAP.get(), new ModelFile.UncheckedModelFile(blockTexture(BlockInit.STEPPING_SKELETON_TRAP.get(), "_off")));

        axisBlock(BlockInit.ROUGH_BOUNDARY_STONE_PILLAR, blockTexture(BlockInit.ROUGH_BOUNDARY_STONE_PILLAR.get()), extend(blockTexture(BlockInit.ROUGH_BOUNDARY_STONE_PILLAR.get()), "_top"));
        axisBlock(BlockInit.POLISHED_BOUNDARY_STONE_PILLAR, blockTexture(BlockInit.POLISHED_BOUNDARY_STONE_PILLAR.get()), extend(blockTexture(BlockInit.POLISHED_BOUNDARY_STONE_PILLAR.get()), "_top"));
        axisBlock(BlockInit.DARK_EROSION_ROCK_PILLAR, blockTexture(BlockInit.DARK_EROSION_ROCK_PILLAR.get()), extend(blockTexture(BlockInit.DARK_EROSION_ROCK_PILLAR.get()), "_top"));

        logBlock(BlockInit.BLIGHTED_OAK_LOG);
        axisBlock(BlockInit.BLIGHTED_OAK_WOOD, blockTexture(BlockInit.BLIGHTED_OAK_LOG.get()), blockTexture(BlockInit.BLIGHTED_OAK_LOG.get()));
        axisBlock(BlockInit.STRIPPED_BLIGHTED_OAK_LOG, blockTexture(BlockInit.STRIPPED_BLIGHTED_OAK_LOG.get()), extend(blockTexture(BlockInit.STRIPPED_BLIGHTED_OAK_LOG.get()), "_top"));
        axisBlock(BlockInit.STRIPPED_BLIGHTED_OAK_WOOD, blockTexture(BlockInit.STRIPPED_BLIGHTED_OAK_LOG.get()), blockTexture(BlockInit.STRIPPED_BLIGHTED_OAK_LOG.get()));

        stairsBlock(BlockInit.POLISHED_ROUGH_BOUNDARY_STONE_STAIRS, blockTexture(BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get()));
        slabBlock(BlockInit.POLISHED_ROUGH_BOUNDARY_STONE_SLAB, blockTexture(BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get()), blockTexture(BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get()));
        stairsBlock(BlockInit.ROUGH_BOUNDARY_BRICK_STAIRS, blockTexture(BlockInit.ROUGH_BOUNDARY_BRICKS.get()));
        slabBlock(BlockInit.ROUGH_BOUNDARY_BRICK_SLAB, blockTexture(BlockInit.ROUGH_BOUNDARY_BRICKS.get()), blockTexture(BlockInit.ROUGH_BOUNDARY_BRICKS.get()));
        wallBlock(BlockInit.ROUGH_BOUNDARY_BRICK_WALL, blockTexture(BlockInit.ROUGH_BOUNDARY_BRICKS.get()));

        stairsBlock(BlockInit.POLISHED_BOUNDARY_STONE_STAIRS, blockTexture(BlockInit.POLISHED_BOUNDARY_STONE.get()));
        slabBlock(BlockInit.POLISHED_BOUNDARY_STONE_SLAB, blockTexture(BlockInit.POLISHED_BOUNDARY_STONE.get()), blockTexture(BlockInit.POLISHED_BOUNDARY_STONE.get()));
        stairsBlock(BlockInit.POLISHED_BOUNDARY_BRICK_STAIRS, blockTexture(BlockInit.POLISHED_BOUNDARY_BRICKS.get()));
        slabBlock(BlockInit.POLISHED_BOUNDARY_BRICK_SLAB, blockTexture(BlockInit.POLISHED_BOUNDARY_BRICKS.get()), blockTexture(BlockInit.POLISHED_BOUNDARY_BRICKS.get()));
        wallBlock(BlockInit.POLISHED_BOUNDARY_BRICK_WALL, blockTexture(BlockInit.POLISHED_BOUNDARY_BRICKS.get()));

        stairsBlock(BlockInit.BLIGHTED_OAK_STAIRS, blockTexture(BlockInit.BLIGHTED_OAK_PLANKS.get()));
        slabBlock(BlockInit.BLIGHTED_OAK_SLAB, blockTexture(BlockInit.BLIGHTED_OAK_PLANKS.get()), blockTexture(BlockInit.BLIGHTED_OAK_PLANKS.get()));
        pressurePlateBlock((PressurePlateBlock) BlockInit.BLIGHTED_OAK_PRESSURE_PLATE.get(), blockTexture(BlockInit.BLIGHTED_OAK_PLANKS.get()));
        blockItem(BlockInit.BLIGHTED_OAK_PRESSURE_PLATE);
        buttonBlock((ButtonBlock) BlockInit.BLIGHTED_OAK_BUTTON.get(), blockTexture(BlockInit.BLIGHTED_OAK_PLANKS.get()));
        fenceBlock((FenceBlock) BlockInit.BLIGHTED_OAK_FENCE.get(), blockTexture(BlockInit.BLIGHTED_OAK_PLANKS.get()));
        fenceGateBlock((FenceGateBlock) BlockInit.BLIGHTED_OAK_FENCE_GATE.get(), blockTexture(BlockInit.BLIGHTED_OAK_PLANKS.get()));
        blockItem(BlockInit.BLIGHTED_OAK_FENCE_GATE);

        stairsBlock(BlockInit.BLIGHTED_STONE_STAIRS, blockTexture(BlockInit.BLIGHTED_STONE.get()));
        slabBlock(BlockInit.BLIGHTED_STONE_SLAB, blockTexture(BlockInit.BLIGHTED_STONE.get()), blockTexture(BlockInit.BLIGHTED_STONE.get()));
        pressurePlateBlock((PressurePlateBlock) BlockInit.BLIGHTED_STONE_PRESSURE_PLATE.get(), blockTexture(BlockInit.BLIGHTED_STONE.get()));
        blockItem(BlockInit.BLIGHTED_STONE_PRESSURE_PLATE);
        buttonBlock((ButtonBlock) BlockInit.BLIGHTED_STONE_BUTTON.get(), blockTexture(BlockInit.BLIGHTED_STONE.get()));
        stairsBlock(BlockInit.BLIGHTED_COBBLESTONE_STAIRS, blockTexture(BlockInit.BLIGHTED_COBBLESTONE.get()));
        slabBlock(BlockInit.BLIGHTED_COBBLESTONE_SLAB, blockTexture(BlockInit.BLIGHTED_COBBLESTONE.get()), blockTexture(BlockInit.BLIGHTED_COBBLESTONE.get()));
        wallBlock(BlockInit.BLIGHTED_COBBLESTONE_WALL, blockTexture(BlockInit.BLIGHTED_COBBLESTONE.get()));
        stairsBlock(BlockInit.POLISHED_DARK_EROSION_ROCK_STAIRS, blockTexture(BlockInit.POLISHED_DARK_EROSION_ROCK.get()));
        slabBlock(BlockInit.POLISHED_DARK_EROSION_ROCK_SLAB, blockTexture(BlockInit.POLISHED_DARK_EROSION_ROCK.get()), blockTexture(BlockInit.POLISHED_DARK_EROSION_ROCK.get()));
        wallBlock(BlockInit.POLISHED_DARK_EROSION_ROCK_WALL, blockTexture(BlockInit.POLISHED_DARK_EROSION_ROCK.get()));
        stairsBlock(BlockInit.DARK_EROSION_ROCK_BRICKS_STAIRS, blockTexture(BlockInit.DARK_EROSION_ROCK_BRICKS.get()));
        slabBlock(BlockInit.DARK_EROSION_ROCK_BRICKS_SLAB, blockTexture(BlockInit.DARK_EROSION_ROCK_BRICKS.get()), blockTexture(BlockInit.DARK_EROSION_ROCK_BRICKS.get()));
        wallBlock(BlockInit.DARK_EROSION_ROCK_BRICKS_WALL, blockTexture(BlockInit.DARK_EROSION_ROCK_BRICKS.get()));
        slabBlock(BlockInit.VOIDSHARD_SLAB, blockTexture(BlockInit.VOIDSHARD.get()), blockTexture(BlockInit.VOIDSHARD.get()));
        stairsBlock(BlockInit.VOIDSHARD_STAIRS, blockTexture(BlockInit.VOIDSHARD.get()));
        wallBlock(BlockInit.VOIDSHARD_WALL, blockTexture(BlockInit.VOIDSHARD.get()));
        stairsBlock(BlockInit.POLISHED_VOIDSHARD_STAIRS, blockTexture(BlockInit.POLISHED_VOIDSHARD.get()));
        slabBlock(BlockInit.POLISHED_VOIDSHARD_SLAB, blockTexture(BlockInit.POLISHED_VOIDSHARD.get()), blockTexture(BlockInit.POLISHED_VOIDSHARD.get()));
        stairsBlock(BlockInit.CUT_GHOST_STEEL_BLOCK_STAIRS, blockTexture(BlockInit.CUT_GHOST_STEEL_BLOCK.get()));
        slabBlock(BlockInit.CUT_GHOST_STEEL_BLOCK_SLAB, blockTexture(BlockInit.CUT_GHOST_STEEL_BLOCK.get()), blockTexture(BlockInit.CUT_GHOST_STEEL_BLOCK.get()));

        doorBlockWithRenderType((DoorBlock) BlockInit.BLIGHTED_OAK_DOOR.get(), extend(blockTexture(BlockInit.BLIGHTED_OAK_DOOR.get()), "_bottom"), extend(blockTexture(BlockInit.BLIGHTED_OAK_DOOR.get()), "_top"), "cutout");
        trapdoorBlockWithRenderType((TrapDoorBlock) BlockInit.BLIGHTED_OAK_TRAPDOOR.get(), blockTexture(BlockInit.BLIGHTED_OAK_TRAPDOOR.get()), true, "cutout");
        saplingBlock(BlockInit.BLIGHTED_OAK_SAPLING);
    }

    private void logBlock(RegistryObject<Block> rob) {
        super.logBlock((RotatedPillarBlock) rob.get());
        blockItem(rob);
    }

    private void axisBlock(RegistryObject<Block> rob, ResourceLocation side, ResourceLocation end) {
        super.axisBlock((RotatedPillarBlock) rob.get(), side, end);
        blockItem(rob);
    }

    private void stairsBlock(RegistryObject<Block> rob, ResourceLocation texture) {
        super.stairsBlock((StairBlock) rob.get(), texture);
        blockItem(rob);
    }

    private void slabBlock(RegistryObject<Block> rob, ResourceLocation doubleslab, ResourceLocation texture) {
        super.slabBlock((SlabBlock) rob.get(), doubleslab, texture);
        blockItem(rob);
    }

    private void wallBlock(RegistryObject<Block> rob, ResourceLocation texture) {
        super.wallBlock((WallBlock) rob.get(), texture);
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

    private void variantCubeBottomTop(Block top, @Nullable Block other, BooleanProperty property, String trueSuffix, String falseSuffix) {
        ResourceLocation baseTexture = blockTexture(top);
        String basePath = baseTexture.getPath();
        VariantBlockStateBuilder builder = getVariantBuilder(top);
        builder.partialState().with(property, false).modelForState()
                .modelFile(getModel(falseSuffix, basePath, top, other))
                .addModel();
        builder.partialState().with(property, true).modelForState()
                .modelFile(getModel(trueSuffix, basePath, top, other))
                .addModel();
    }

    private BlockModelBuilder getModel(String suffix, String basePath, Block b1, Block b2) {
        boolean reference = b2 == null;
        Block b0 = reference ? b1 : b2;
        return models().cubeBottomTop(
                basePath + suffix,
                blockTexture(b0, reference ? "_side" : ""),
                blockTexture(b0, reference ? "_bottom" : ""),
                blockTexture(b1, reference ? "_top" + suffix : suffix));
    }

    private void createHorizontallyRotatedBlock(HorizontalDirectionalBlock block) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);
        Direction[] horizontalDirections = Direction.Plane.HORIZONTAL.stream().toArray(Direction[]::new);
        ModelFile model = cubeAll(block);
        for (Direction facing : horizontalDirections) {
            int rotationY = (int) facing.toYRot();
            builder.partialState().with(BlockGalacticAlphabet.FACING, facing)
                    .setModels(new ConfiguredModel(model, 0, rotationY, false));
        }
        simpleBlockItem(block, model);
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
        Direction[] horizontalDirections = Direction.Plane.HORIZONTAL.stream().toArray(Direction[]::new);
        for (SGACharacter character : SGACharacter.values()) {
            ModelFile modelToUse = supportedChars.contains(character) ? modelCache.get(character) : defaultModel;
            for (Direction facing : horizontalDirections) {
                int rotationY = (int) facing.toYRot();
                builder.partialState()
                        .with(BlockGalacticAlphabet.CHARACTER, character)
                        .with(BlockGalacticAlphabet.FACING, facing)
                        .setModels(new ConfiguredModel(modelToUse, 0, rotationY, false));
            }
        }
        simpleBlockItem(block, defaultModel);
    }
}