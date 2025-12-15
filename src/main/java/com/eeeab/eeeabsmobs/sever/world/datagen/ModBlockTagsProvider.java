package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, EEEABMobs.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        //挖掘相关
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                BlockInit.IMMORTAL_BLOCK.get(),
                BlockInit.GHOST_STEEL_BLOCK.get(),
                BlockInit.CUT_GHOST_STEEL_BLOCK.get(),
                BlockInit.CUT_GHOST_STEEL_BLOCK_STAIRS.get(),
                BlockInit.CUT_GHOST_STEEL_BLOCK_SLAB.get(),
                BlockInit.TOMB_ARROWS_TRAP.get(),
                BlockInit.TOMB_GAS_TRAP.get(),
                BlockInit.TOMB_SUMMON_TRAP.get(),
                BlockInit.EROSION_ROCK_BRICKS.get(),
                BlockInit.BLIGHTED_STONE.get(),
                BlockInit.DARKENED_COAL_ORE.get(),
                BlockInit.DARKENED_IRON_ORE.get(),
                BlockInit.BLIGHTED_STONE_STAIRS.get(),
                BlockInit.BLIGHTED_STONE_SLAB.get(),
                BlockInit.BLIGHTED_COBBLESTONE.get(),
                BlockInit.BLIGHTED_COBBLESTONE_STAIRS.get(),
                BlockInit.BLIGHTED_COBBLESTONE_SLAB.get(),
                BlockInit.BLIGHTED_COBBLESTONE_WALL.get(),
                BlockInit.POLISHED_DARK_EROSION_ROCK_STAIRS.get(),
                BlockInit.POLISHED_DARK_EROSION_ROCK_SLAB.get(),
                BlockInit.POLISHED_DARK_EROSION_ROCK_WALL.get(),
                BlockInit.VOIDSHARD.get(),
                BlockInit.VOIDSHARD_STAIRS.get(),
                BlockInit.VOIDSHARD_SLAB.get(),
                BlockInit.VOIDSHARD_WALL.get(),
                BlockInit.POLISHED_VOIDSHARD.get(),
                BlockInit.POLISHED_VOIDSHARD_STAIRS.get(),
                BlockInit.POLISHED_VOIDSHARD_SLAB.get(),
                BlockInit.DARK_EROSION_ROCK.get(),
                BlockInit.CHISELED_DARK_EROSION_ROCK.get(),
                BlockInit.POLISHED_DARK_EROSION_ROCK.get(),
                BlockInit.DARK_EROSION_ROCK_BRICKS.get(),
                BlockInit.DARK_EROSION_ROCK_PILLAR.get(),
                BlockInit.CRACKED_DARK_EROSION_ROCK_BRICKS.get(),
                BlockInit.DARK_EROSION_ROCK_BRICKS_STAIRS.get(),
                BlockInit.DARK_EROSION_ROCK_BRICKS_SLAB.get(),
                BlockInit.DARK_EROSION_ROCK_BRICKS_WALL.get()
        );
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
                BlockInit.ERODED_SOIL.get()
        );
        tag(BlockTags.MINEABLE_WITH_HOE).add(
                BlockInit.SOUL_LIGHT.get(),
                BlockInit.EROSION_OAK_LEAVES.get()
        );
        tag(BlockTags.NEEDS_IRON_TOOL).add(
                BlockInit.IMMORTAL_BLOCK.get());
        tag(BlockTags.NEEDS_DIAMOND_TOOL).add(
                BlockInit.GHOST_STEEL_BLOCK.get(),
                BlockInit.CUT_GHOST_STEEL_BLOCK.get(),
                BlockInit.CUT_GHOST_STEEL_BLOCK_STAIRS.get(),
                BlockInit.CUT_GHOST_STEEL_BLOCK_SLAB.get(),
                BlockInit.TOMB_ARROWS_TRAP.get(),
                BlockInit.TOMB_GAS_TRAP.get(),
                BlockInit.TOMB_SUMMON_TRAP.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(
                BlockInit.EROSION_OAK_LOG.get(),
                BlockInit.EROSION_OAK_WOOD.get(),
                BlockInit.STRIPPED_EROSION_OAK_LOG.get(),
                BlockInit.STRIPPED_EROSION_OAK_WOOD.get(),
                BlockInit.BLIGHTED_OAK_LOG.get(),
                BlockInit.BLIGHTED_OAK_WOOD.get(),
                BlockInit.STRIPPED_BLIGHTED_OAK_LOG.get(),
                BlockInit.STRIPPED_BLIGHTED_OAK_WOOD.get()
        );
        //方块标签相关
        tag(BlockTags.LEAVES).add(
                BlockInit.EROSION_OAK_LEAVES.get()
        );
        tag(BlockTags.PLANKS).add(
                BlockInit.EROSION_OAK_PLANKS.get()
        );
        tag(BlockTags.DIRT).add(
                BlockInit.ERODED_SOIL.get()
        );
        tag(Tags.Blocks.STONE).add(
                BlockInit.BLIGHTED_STONE.get(),
                BlockInit.BLIGHTED_COBBLESTONE.get(),
                BlockInit.VOIDSHARD.get(),
                BlockInit.DARK_EROSION_ROCK.get()
        );
        tag(BlockTags.STONE_ORE_REPLACEABLES).add(BlockInit.BLIGHTED_STONE.get());
        tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(
                BlockInit.DARKENED_COAL_ORE.get(),
                BlockInit.DARKENED_IRON_ORE.get()
        );
        tag(Tags.Blocks.ORES).add(
                BlockInit.DARKENED_COAL_ORE.get(),
                BlockInit.DARKENED_IRON_ORE.get()
        );
        tag(Tags.Blocks.ORES_COAL).add(BlockInit.DARKENED_COAL_ORE.get());
        tag(Tags.Blocks.ORES_IRON).add(BlockInit.DARKENED_IRON_ORE.get());
        tag(BlockTags.LOGS_THAT_BURN).add(
                BlockInit.EROSION_OAK_LOG.get(),
                BlockInit.STRIPPED_EROSION_OAK_LOG.get(),
                BlockInit.STRIPPED_EROSION_OAK_WOOD.get(),
                BlockInit.BLIGHTED_OAK_LOG.get(),
                BlockInit.STRIPPED_BLIGHTED_OAK_LOG.get(),
                BlockInit.STRIPPED_BLIGHTED_OAK_WOOD.get()
        );
        tag(BlockTags.STAIRS).add(
                BlockInit.EROSION_OAK_STAIRS.get(),
                BlockInit.BLIGHTED_OAK_STAIRS.get(),
                BlockInit.BLIGHTED_STONE_STAIRS.get(),
                BlockInit.BLIGHTED_COBBLESTONE_STAIRS.get(),
                BlockInit.POLISHED_DARK_EROSION_ROCK_STAIRS.get(),
                BlockInit.VOIDSHARD_STAIRS.get(),
                BlockInit.POLISHED_VOIDSHARD_STAIRS.get(),
                BlockInit.DARK_EROSION_ROCK_BRICKS_STAIRS.get(),
                BlockInit.CUT_GHOST_STEEL_BLOCK_STAIRS.get()
        );
        tag(BlockTags.SLABS).add(
                BlockInit.EROSION_OAK_SLAB.get(),
                BlockInit.BLIGHTED_OAK_SLAB.get(),
                BlockInit.BLIGHTED_STONE_SLAB.get(),
                BlockInit.BLIGHTED_COBBLESTONE_SLAB.get(),
                BlockInit.POLISHED_DARK_EROSION_ROCK_SLAB.get(),
                BlockInit.VOIDSHARD_SLAB.get(),
                BlockInit.POLISHED_VOIDSHARD_SLAB.get(),
                BlockInit.DARK_EROSION_ROCK_BRICKS_SLAB.get(),
                BlockInit.CUT_GHOST_STEEL_BLOCK_SLAB.get()
        );
        tag(BlockTags.WALLS).add(
                BlockInit.BLIGHTED_COBBLESTONE_WALL.get(),
                BlockInit.POLISHED_DARK_EROSION_ROCK_WALL.get(),
                BlockInit.VOIDSHARD_WALL.get(),
                BlockInit.DARK_EROSION_ROCK_BRICKS_WALL.get()
        );
        tag(BlockTags.FENCES).add(
                BlockInit.EROSION_OAK_FENCE.get(),
                BlockInit.BLIGHTED_OAK_FENCE.get()
        );
        tag(BlockTags.FENCE_GATES).add(
                BlockInit.EROSION_OAK_FENCE_GATE.get(),
                BlockInit.BLIGHTED_OAK_FENCE_GATE.get()
        );
        tag(BlockTags.DOORS).add(
                BlockInit.EROSION_OAK_DOOR.get(),
                BlockInit.BLIGHTED_OAK_DOOR.get()
        );
        tag(BlockTags.TRAPDOORS).add(
                BlockInit.EROSION_OAK_TRAPDOOR.get(),
                BlockInit.BLIGHTED_OAK_TRAPDOOR.get()
        );
        tag(BlockTags.PRESSURE_PLATES).add(
                BlockInit.EROSION_OAK_PRESSURE_PLATE.get(),
                BlockInit.BLIGHTED_OAK_PRESSURE_PLATE.get(),
                BlockInit.BLIGHTED_STONE_PRESSURE_PLATE.get()
        );
        tag(BlockTags.BUTTONS).add(
                BlockInit.EROSION_OAK_BUTTON.get(),
                BlockInit.BLIGHTED_OAK_BUTTON.get(),
                BlockInit.BLIGHTED_STONE_BUTTON.get()
        );
    }
}