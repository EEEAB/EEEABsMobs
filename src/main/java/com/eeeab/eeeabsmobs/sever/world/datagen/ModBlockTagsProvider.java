package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import com.eeeab.eeeabsmobs.sever.util.ModTagKey;
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
                BlockInit.ANCIENT_BOUNDARY_STONE.get(),
                BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get(),
                BlockInit.POLISHED_ROUGH_BOUNDARY_STONE_STAIRS.get(),
                BlockInit.POLISHED_ROUGH_BOUNDARY_STONE_SLAB.get(),
                BlockInit.ROUGH_BOUNDARY_BRICKS.get(),
                BlockInit.ROUGH_BOUNDARY_BRICK_STAIRS.get(),
                BlockInit.ROUGH_BOUNDARY_BRICK_SLAB.get(),
                BlockInit.ROUGH_BOUNDARY_BRICK_WALL.get(),
                BlockInit.CRACKED_ROUGH_BOUNDARY_BRICKS.get(),
                BlockInit.CHISELED_ROUGH_BOUNDARY_BRICKS.get(),
                BlockInit.ROUGH_BOUNDARY_STONE_PILLAR.get(),
                BlockInit.POLISHED_BOUNDARY_STONE.get(),
                BlockInit.POLISHED_BOUNDARY_STONE_STAIRS.get(),
                BlockInit.POLISHED_BOUNDARY_STONE_SLAB.get(),
                BlockInit.POLISHED_BOUNDARY_BRICKS.get(),
                BlockInit.POLISHED_BOUNDARY_BRICK_STAIRS.get(),
                BlockInit.POLISHED_BOUNDARY_BRICK_SLAB.get(),
                BlockInit.POLISHED_BOUNDARY_BRICK_WALL.get(),
                BlockInit.CRACKED_POLISHED_BOUNDARY_BRICKS.get(),
                BlockInit.CHISELED_POLISHED_BOUNDARY_BRICKS.get(),
                BlockInit.POLISHED_BOUNDARY_STONE_PILLAR.get(),
                BlockInit.REDSTONE_POISON_DART_TRAP.get(),
                BlockInit.STEPPING_FLAME_TRAP.get(),
                BlockInit.STEPPING_SHOCK_TRAP.get(),
                BlockInit.STEPPING_POISON_TRAP.get(),
                BlockInit.STEPPING_SKELETON_TRAP.get(),
                BlockInit.IMMORTAL_BLOCK.get(),
                BlockInit.GHOST_STEEL_BLOCK.get(),
                BlockInit.CUT_GHOST_STEEL_BLOCK.get(),
                BlockInit.CUT_GHOST_STEEL_BLOCK_STAIRS.get(),
                BlockInit.CUT_GHOST_STEEL_BLOCK_SLAB.get(),
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
        tag(BlockTags.NEEDS_IRON_TOOL).add(
                BlockInit.IMMORTAL_BLOCK.get());
        tag(BlockTags.NEEDS_DIAMOND_TOOL).add(
                BlockInit.GHOST_STEEL_BLOCK.get(),
                BlockInit.CUT_GHOST_STEEL_BLOCK.get(),
                BlockInit.CUT_GHOST_STEEL_BLOCK_STAIRS.get(),
                BlockInit.CUT_GHOST_STEEL_BLOCK_SLAB.get(),
                BlockInit.REDSTONE_POISON_DART_TRAP.get(),
                BlockInit.STEPPING_FLAME_TRAP.get(),
                BlockInit.STEPPING_SHOCK_TRAP.get(),
                BlockInit.STEPPING_POISON_TRAP.get(),
                BlockInit.STEPPING_SKELETON_TRAP.get());
        tag(BlockTags.MINEABLE_WITH_AXE).add(
                BlockInit.BLIGHTED_OAK_LOG.get(),
                BlockInit.BLIGHTED_OAK_WOOD.get(),
                BlockInit.STRIPPED_BLIGHTED_OAK_LOG.get(),
                BlockInit.STRIPPED_BLIGHTED_OAK_WOOD.get()
        );
        //方块标签相关
        tag(ModTagKey.MOB_IMMUNE).add(
                BlockInit.DUNGEON_BRICK.get(),
                BlockInit.UNCARVED_BOUNDARY_STONE.get(),
                BlockInit.RUNIC_BOUNDARY_STONE.get(),
                BlockInit.BOUNDARY_CORE.get(),
                BlockInit.VOID_CRACK_PORTAL.get()
        );
        tag(Tags.Blocks.ENDERMAN_PLACE_ON_BLACKLIST).addTag(ModTagKey.MOB_IMMUNE);
        tag(BlockTags.FEATURES_CANNOT_REPLACE).addTag(ModTagKey.MOB_IMMUNE);
        tag(BlockTags.DRAGON_IMMUNE).addTag(ModTagKey.MOB_IMMUNE);
        tag(BlockTags.WITHER_IMMUNE).addTag(ModTagKey.MOB_IMMUNE);
        tag(BlockTags.GEODE_INVALID_BLOCKS).addTag(ModTagKey.MOB_IMMUNE);
        tag(BlockTags.DIRT).add(
                BlockInit.ERODED_SOIL.get()
        );
        tag(Tags.Blocks.STONE).add(
                BlockInit.ANCIENT_BOUNDARY_STONE.get(),
                BlockInit.BLIGHTED_STONE.get(),
                BlockInit.BLIGHTED_COBBLESTONE.get(),
                BlockInit.VOIDSHARD.get(),
                BlockInit.DARK_EROSION_ROCK.get()
        );
        tag(BlockTags.STONE_ORE_REPLACEABLES).add(BlockInit.BLIGHTED_STONE.get());
        //tag(Tags.Blocks.ORES_IN_GROUND_STONE).add(
        //        BlockInit.DARKENED_COAL_ORE.get(),
        //        BlockInit.DARKENED_IRON_ORE.get()
        //);
        //tag(Tags.Blocks.ORES).add(
        //        BlockInit.DARKENED_COAL_ORE.get(),
        //        BlockInit.DARKENED_IRON_ORE.get()
        //);
        //tag(Tags.Blocks.ORES_COAL).add(BlockInit.DARKENED_COAL_ORE.get());
        //tag(Tags.Blocks.ORES_IRON).add(BlockInit.DARKENED_IRON_ORE.get());
        tag(BlockTags.LOGS_THAT_BURN).add(
                BlockInit.BLIGHTED_OAK_LOG.get(),
                BlockInit.STRIPPED_BLIGHTED_OAK_LOG.get(),
                BlockInit.STRIPPED_BLIGHTED_OAK_WOOD.get()
        );
        tag(BlockTags.STAIRS).add(
                BlockInit.POLISHED_ROUGH_BOUNDARY_STONE_STAIRS.get(),
                BlockInit.ROUGH_BOUNDARY_BRICK_STAIRS.get(),
                BlockInit.POLISHED_BOUNDARY_STONE_STAIRS.get(),
                BlockInit.POLISHED_BOUNDARY_BRICK_STAIRS.get(),
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
                BlockInit.POLISHED_ROUGH_BOUNDARY_STONE_SLAB.get(),
                BlockInit.ROUGH_BOUNDARY_BRICK_SLAB.get(),
                BlockInit.POLISHED_BOUNDARY_STONE_SLAB.get(),
                BlockInit.POLISHED_BOUNDARY_BRICK_SLAB.get(),
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
                BlockInit.ROUGH_BOUNDARY_BRICK_WALL.get(),
                BlockInit.POLISHED_BOUNDARY_BRICK_WALL.get(),
                BlockInit.BLIGHTED_COBBLESTONE_WALL.get(),
                BlockInit.POLISHED_DARK_EROSION_ROCK_WALL.get(),
                BlockInit.VOIDSHARD_WALL.get(),
                BlockInit.DARK_EROSION_ROCK_BRICKS_WALL.get()
        );
        tag(BlockTags.FENCES).add(
                BlockInit.BLIGHTED_OAK_FENCE.get()
        );
        tag(BlockTags.FENCE_GATES).add(
                BlockInit.BLIGHTED_OAK_FENCE_GATE.get()
        );
        tag(BlockTags.DOORS).add(
                BlockInit.BLIGHTED_OAK_DOOR.get()
        );
        tag(BlockTags.TRAPDOORS).add(
                BlockInit.BLIGHTED_OAK_TRAPDOOR.get()
        );
        tag(BlockTags.PRESSURE_PLATES).add(
                BlockInit.BLIGHTED_OAK_PRESSURE_PLATE.get(),
                BlockInit.BLIGHTED_STONE_PRESSURE_PLATE.get()
        );
        tag(BlockTags.BUTTONS).add(
                BlockInit.BLIGHTED_OAK_BUTTON.get(),
                BlockInit.BLIGHTED_STONE_BUTTON.get()
        );
    }
}