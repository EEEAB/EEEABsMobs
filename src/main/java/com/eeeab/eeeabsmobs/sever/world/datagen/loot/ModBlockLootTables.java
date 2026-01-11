package com.eeeab.eeeabsmobs.sever.world.datagen.loot;

import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(BlockInit.BOUNDARY_LAMP.get());
        this.dropSelf(BlockInit.IRON_GRATE.get());
        this.dropSelf(BlockInit.ANCIENT_BOUNDARY_STONE.get());
        this.dropSelf(BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get());
        this.dropSelf(BlockInit.POLISHED_ROUGH_BOUNDARY_STONE_STAIRS.get());
        this.dropSelf(BlockInit.POLISHED_ROUGH_BOUNDARY_STONE_SLAB.get());
        this.dropSelf(BlockInit.ROUGH_BOUNDARY_BRICKS.get());
        this.dropSelf(BlockInit.ROUGH_BOUNDARY_BRICK_STAIRS.get());
        this.dropSelf(BlockInit.ROUGH_BOUNDARY_BRICK_SLAB.get());
        this.dropSelf(BlockInit.ROUGH_BOUNDARY_BRICK_WALL.get());
        this.dropSelf(BlockInit.CRACKED_ROUGH_BOUNDARY_BRICKS.get());
        this.dropSelf(BlockInit.CHISELED_ROUGH_BOUNDARY_BRICKS.get());
        this.dropSelf(BlockInit.ROUGH_BOUNDARY_STONE_PILLAR.get());
        this.dropSelf(BlockInit.POLISHED_BOUNDARY_STONE.get());
        this.dropSelf(BlockInit.POLISHED_BOUNDARY_STONE_STAIRS.get());
        this.dropSelf(BlockInit.POLISHED_BOUNDARY_STONE_SLAB.get());
        this.dropSelf(BlockInit.POLISHED_BOUNDARY_BRICKS.get());
        this.dropSelf(BlockInit.POLISHED_BOUNDARY_BRICK_STAIRS.get());
        this.dropSelf(BlockInit.POLISHED_BOUNDARY_BRICK_SLAB.get());
        this.dropSelf(BlockInit.POLISHED_BOUNDARY_BRICK_WALL.get());
        this.dropSelf(BlockInit.CRACKED_POLISHED_BOUNDARY_BRICKS.get());
        this.dropSelf(BlockInit.CHISELED_POLISHED_BOUNDARY_BRICKS.get());
        this.dropSelf(BlockInit.POLISHED_BOUNDARY_STONE_PILLAR.get());
        this.dropSelf(BlockInit.STEPPING_POISON_TRAP.get());
        this.dropSelf(BlockInit.STEPPING_SKELETON_TRAP.get());
        this.dropSelf(BlockInit.REDSTONE_POISON_DART_TRAP.get());
        this.dropSelf(BlockInit.STEPPING_FLAME_TRAP.get());
        this.dropSelf(BlockInit.STEPPING_SHOCK_TRAP.get());
        this.dropSelf(BlockInit.EROSION_ROCK_BRICKS.get());
        this.dropSelf(BlockInit.IMMORTAL_BLOCK.get());
        this.dropSelf(BlockInit.GHOST_STEEL_BLOCK.get());
        this.dropSelf(BlockInit.CUT_GHOST_STEEL_BLOCK.get());
        this.dropSelf(BlockInit.CUT_GHOST_STEEL_BLOCK_STAIRS.get());
        this.dropSelf(BlockInit.CUT_GHOST_STEEL_BLOCK_SLAB.get());
        this.dropSelf(BlockInit.BLIGHTED_OAK_LOG.get());
        this.dropSelf(BlockInit.BLIGHTED_OAK_WOOD.get());
        this.dropSelf(BlockInit.STRIPPED_BLIGHTED_OAK_LOG.get());
        this.dropSelf(BlockInit.STRIPPED_BLIGHTED_OAK_WOOD.get());
        this.dropSelf(BlockInit.BLIGHTED_OAK_SAPLING.get());
        this.dropSelf(BlockInit.BLIGHTED_OAK_PLANKS.get());
        this.dropSelf(BlockInit.BLIGHTED_OAK_STAIRS.get());
        this.dropSelf(BlockInit.BLIGHTED_OAK_SLAB.get());
        this.dropSelf(BlockInit.BLIGHTED_OAK_FENCE.get());
        this.dropSelf(BlockInit.BLIGHTED_OAK_FENCE_GATE.get());
        this.dropSelf(BlockInit.BLIGHTED_OAK_TRAPDOOR.get());
        this.dropSelf(BlockInit.BLIGHTED_OAK_PRESSURE_PLATE.get());
        this.dropSelf(BlockInit.BLIGHTED_OAK_BUTTON.get());
        this.dropSelf(BlockInit.ERODED_SOIL.get());
        this.dropSelf(BlockInit.BLIGHTED_STONE_STAIRS.get());
        this.dropSelf(BlockInit.BLIGHTED_STONE_SLAB.get());
        this.dropSelf(BlockInit.BLIGHTED_STONE_PRESSURE_PLATE.get());
        this.dropSelf(BlockInit.BLIGHTED_STONE_BUTTON.get());
        this.dropSelf(BlockInit.BLIGHTED_COBBLESTONE.get());
        this.dropSelf(BlockInit.BLIGHTED_COBBLESTONE_STAIRS.get());
        this.dropSelf(BlockInit.BLIGHTED_COBBLESTONE_SLAB.get());
        this.dropSelf(BlockInit.BLIGHTED_COBBLESTONE_WALL.get());
        this.dropSelf(BlockInit.POLISHED_DARK_EROSION_ROCK_STAIRS.get());
        this.dropSelf(BlockInit.POLISHED_DARK_EROSION_ROCK_SLAB.get());
        this.dropSelf(BlockInit.POLISHED_DARK_EROSION_ROCK_WALL.get());
        this.dropSelf(BlockInit.VOIDSHARD.get());
        this.dropSelf(BlockInit.VOIDSHARD_STAIRS.get());
        this.dropSelf(BlockInit.VOIDSHARD_SLAB.get());
        this.dropSelf(BlockInit.VOIDSHARD_WALL.get());
        this.dropSelf(BlockInit.POLISHED_VOIDSHARD.get());
        this.dropSelf(BlockInit.POLISHED_VOIDSHARD_STAIRS.get());
        this.dropSelf(BlockInit.POLISHED_VOIDSHARD_SLAB.get());
        this.dropSelf(BlockInit.DARK_EROSION_ROCK.get());
        this.dropSelf(BlockInit.CHISELED_DARK_EROSION_ROCK.get());
        this.dropSelf(BlockInit.POLISHED_DARK_EROSION_ROCK.get());
        this.dropSelf(BlockInit.DARK_EROSION_ROCK_BRICKS.get());
        this.dropSelf(BlockInit.DARK_EROSION_ROCK_PILLAR.get());
        this.dropSelf(BlockInit.CRACKED_DARK_EROSION_ROCK_BRICKS.get());
        this.dropSelf(BlockInit.DARK_EROSION_ROCK_BRICKS_STAIRS.get());
        this.dropSelf(BlockInit.DARK_EROSION_ROCK_BRICKS_SLAB.get());
        this.dropSelf(BlockInit.DARK_EROSION_ROCK_BRICKS_WALL.get());
        this.add(BlockInit.BLIGHTED_OAK_DOOR.get(), this::createDoorTable);
        this.add(BlockInit.BLIGHTED_STONE.get(), (builder) -> this.createSingleItemTableWithSilkTouch(builder, BlockInit.BLIGHTED_COBBLESTONE.get()));
        this.add(BlockInit.DARKENED_COAL_ORE.get(), (builder) -> this.createOreDrop(builder, Items.COAL));
        this.add(BlockInit.DARKENED_IRON_ORE.get(), (builder) -> this.createOreDrop(builder, Items.RAW_IRON));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockInit.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
