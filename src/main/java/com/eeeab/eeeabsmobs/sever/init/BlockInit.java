package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.block.*;
import com.eeeab.eeeabsmobs.sever.block.grower.BlightedOakGrower;
import com.eeeab.eeeabsmobs.sever.block.properties.SGACharacter;
import com.eeeab.eeeabsmobs.sever.block.trapImpl.*;
import com.eeeab.eeeabsmobs.sever.item.util.EMBlockEntityItemRender;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumSet;
import java.util.function.Supplier;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, EEEABMobs.MOD_ID);

    public static final RegistryObject<Block> DUNGEON_BRICK = registryBlock("dungeon_brick", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.BEDROCK).noLootTable()), false);

    public static final RegistryObject<Block> IRON_GRATE = registryBlock("iron_grate", () -> new BlockIronGrate(BlockBehaviour.Properties
            .of().strength(5.0F, 6.0F).sound(SoundType.METAL).noOcclusion().isValidSpawn(BlockInit::never)
            .isRedstoneConductor(BlockInit::never).isSuffocating(BlockInit::never).isViewBlocking(BlockInit::never)
    ), false);
    public static final RegistryObject<Block> BOUNDARY_LAMP = registryBlock("boundary_lamp", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.REDSTONE_LAMP).lightLevel((blockState) -> 15)), false);
    public static final RegistryObject<Block> ANCIENT_BOUNDARY_STONE = registryBlock("ancient_boundary_stone", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.COBBLED_DEEPSLATE).strength(4F, 6.0F)), false);

    public static final RegistryObject<Block> POLISHED_ROUGH_BOUNDARY_STONE = registryBlock("polished_rough_boundary_stone", () -> new Block(BlockBehaviour.Properties
            .copy(BlockInit.ANCIENT_BOUNDARY_STONE.get()).strength(4F, 6.0F).sound(SoundType.DEEPSLATE_BRICKS)), false);
    public static final RegistryObject<Block> POLISHED_ROUGH_BOUNDARY_STONE_STAIRS = registryBlock("polished_rough_boundary_stone_stairs", () -> new StairBlock(() -> POLISHED_ROUGH_BOUNDARY_STONE.get().defaultBlockState(), BlockBehaviour.Properties
            .copy(POLISHED_ROUGH_BOUNDARY_STONE.get())), false);
    public static final RegistryObject<Block> POLISHED_ROUGH_BOUNDARY_STONE_SLAB = registryBlock("polished_rough_boundary_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties
            .copy(POLISHED_ROUGH_BOUNDARY_STONE.get())), false);

    public static final RegistryObject<Block> ROUGH_BOUNDARY_BRICKS = registryBlock("rough_boundary_bricks", () -> new Block(BlockBehaviour.Properties
            .copy(BlockInit.POLISHED_ROUGH_BOUNDARY_STONE.get())), false);
    public static final RegistryObject<Block> CRACKED_ROUGH_BOUNDARY_BRICKS = registryBlock("cracked_rough_boundary_bricks", () -> new Block(BlockBehaviour.Properties
            .copy(BlockInit.ROUGH_BOUNDARY_BRICKS.get())), false);
    public static final RegistryObject<Block> CHISELED_ROUGH_BOUNDARY_BRICKS = registryBlock("chiseled_rough_boundary_bricks", () -> new Block(BlockBehaviour.Properties
            .copy(BlockInit.ROUGH_BOUNDARY_BRICKS.get())), false);
    public static final RegistryObject<Block> ROUGH_BOUNDARY_STONE_PILLAR = registryBlock("rough_boundary_stone_pillar", () -> new BlockRotatedPillar(BlockBehaviour.Properties
            .copy(BlockInit.ROUGH_BOUNDARY_BRICKS.get())), false);
    public static final RegistryObject<Block> ROUGH_BOUNDARY_BRICK_STAIRS = registryBlock("rough_boundary_brick_stairs", () -> new StairBlock(() -> ROUGH_BOUNDARY_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties
            .copy(ROUGH_BOUNDARY_BRICKS.get())), false);
    public static final RegistryObject<Block> ROUGH_BOUNDARY_BRICK_SLAB = registryBlock("rough_boundary_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties
            .copy(ROUGH_BOUNDARY_BRICKS.get())), false);
    public static final RegistryObject<Block> ROUGH_BOUNDARY_BRICK_WALL = registryBlock("rough_boundary_brick_wall", () -> new WallBlock(BlockBehaviour.Properties
            .copy(ROUGH_BOUNDARY_BRICKS.get())), false);

    public static final RegistryObject<Block> POLISHED_BOUNDARY_STONE = registryBlock("polished_boundary_stone", () -> new Block(BlockBehaviour.Properties
            .copy(BlockInit.ROUGH_BOUNDARY_BRICKS.get()).strength(3.0F, 5.0F)), false);
    public static final RegistryObject<Block> POLISHED_BOUNDARY_STONE_STAIRS = registryBlock("polished_boundary_stone_stairs", () -> new StairBlock(() -> ROUGH_BOUNDARY_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties
            .copy(POLISHED_BOUNDARY_STONE.get())), false);
    public static final RegistryObject<Block> POLISHED_BOUNDARY_STONE_SLAB = registryBlock("polished_boundary_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties
            .copy(POLISHED_BOUNDARY_STONE.get())), false);

    public static final RegistryObject<Block> POLISHED_BOUNDARY_BRICKS = registryBlock("polished_boundary_bricks", () -> new Block(BlockBehaviour.Properties
            .copy(BlockInit.POLISHED_BOUNDARY_STONE.get())), false);
    public static final RegistryObject<Block> CRACKED_POLISHED_BOUNDARY_BRICKS = registryBlock("cracked_polished_boundary_bricks", () -> new Block(BlockBehaviour.Properties
            .copy(BlockInit.POLISHED_BOUNDARY_BRICKS.get())), false);
    public static final RegistryObject<Block> CHISELED_POLISHED_BOUNDARY_BRICKS = registryBlock("chiseled_polished_boundary_bricks", () -> new Block(BlockBehaviour.Properties
            .copy(BlockInit.POLISHED_BOUNDARY_BRICKS.get())), false);
    public static final RegistryObject<Block> POLISHED_BOUNDARY_STONE_PILLAR = registryBlock("polished_boundary_stone_pillar", () -> new BlockRotatedPillar(BlockBehaviour.Properties
            .copy(BlockInit.POLISHED_BOUNDARY_BRICKS.get())), false);
    public static final RegistryObject<Block> POLISHED_BOUNDARY_BRICK_STAIRS = registryBlock("polished_boundary_brick_stairs", () -> new StairBlock(() -> POLISHED_BOUNDARY_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties
            .copy(POLISHED_BOUNDARY_BRICKS.get())), false);
    public static final RegistryObject<Block> POLISHED_BOUNDARY_BRICK_SLAB = registryBlock("polished_boundary_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties
            .copy(POLISHED_BOUNDARY_BRICKS.get())), false);
    public static final RegistryObject<Block> POLISHED_BOUNDARY_BRICK_WALL = registryBlock("polished_boundary_brick_wall", () -> new WallBlock(BlockBehaviour.Properties
            .copy(POLISHED_BOUNDARY_BRICKS.get())), false);

    public static final RegistryObject<Block> REDSTONE_POISON_DART_TRAP = registryBlock("redstone_poison_dart_trap", () -> new BlockTomeArrowsTrap(BlockBehaviour.Properties
            .copy(Blocks.DEEPSLATE_BRICKS).strength(50.0F, 1200.0F)), false);
    public static final RegistryObject<Block> STEPPING_FLAME_TRAP = registryBlock("stepping_flame_trap", () -> new BlockSteppingFlameTrap(BlockBehaviour.Properties
            .copy(Blocks.DEEPSLATE_BRICKS).strength(50.0F, 1200.0F).randomTicks().lightLevel(state -> state.getValue(BlockStateProperties.OPEN) ? 7 : 0)), false);
    public static final RegistryObject<Block> STEPPING_SHOCK_TRAP = registryBlock("stepping_shock_trap", () -> new BlockSteppingShockTrap(BlockBehaviour.Properties
            .copy(Blocks.DEEPSLATE_BRICKS).strength(50.0F, 1200.0F).randomTicks().lightLevel(state -> state.getValue(BlockStateProperties.OPEN) ? 2 : 0)), false);
    public static final RegistryObject<Block> UNCARVED_BOUNDARY_STONE = registryBlock("uncarved_boundary_stone", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.REINFORCED_DEEPSLATE).strength(-1.0F, 3600000.0F).noLootTable().lightLevel(value -> 3).sound(SoundType.STONE)), false);
    public static final RegistryObject<Block> RUNIC_BOUNDARY_STONE = registryBlock("runic_boundary_stone", () -> new BlockGalacticAlphabet(BlockBehaviour.Properties
            .copy(BlockInit.UNCARVED_BOUNDARY_STONE.get()).noLootTable(), EnumSet.of(SGACharacter.V, SGACharacter.O, SGACharacter.I, SGACharacter.D)), false);
    public static final RegistryObject<Block> BOUNDARY_CORE = registryBlock("boundary_core", () -> new BlockVoidCrackPortalLock(BlockBehaviour.Properties.of()
            .randomTicks().pushReaction(PushReaction.BLOCK).strength(-1.0F).sound(SoundType.STONE).lightLevel(s -> 3).noLootTable()), false);
    public static final RegistryObject<Block> VOID_CRACK_PORTAL = registryBlock("void_crack_portal", () -> new BlockVoidCrackPortal(BlockBehaviour.Properties.of()
            .noCollission().randomTicks().pushReaction(PushReaction.BLOCK).strength(-1.0F).isValidSpawn(BlockInit::never).sound(SoundType.GLASS).lightLevel(s -> 11).noLootTable()), false);
    public static final RegistryObject<Block> EROSION_DEEPSLATE_BRICKS = registryBlock("erosion_deepslate_bricks", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.REINFORCED_DEEPSLATE).strength(-1.0F, 3600000.0F).noLootTable()), false);


    public static final RegistryObject<Block> TOMBSTONE = registryBlock("tombstone", BlockTombstone::new, true);
    public static final RegistryObject<Block> EROSION_ROCK_BRICKS = registryBlock("erosion_rock_bricks", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.DEEPSLATE_BRICKS).strength(10.0F, 1200.0F)), false);
    public static final RegistryObject<Block> IMMORTAL_BLOCK = registryBlock("immortal_block", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.IRON_BLOCK).sound(SoundType.METAL)), false);
    public static final RegistryObject<Block> GHOST_STEEL_BLOCK = registryBlock("ghost_steel_block", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.NETHERITE_BLOCK).sound(SoundType.METAL)), false, new Item.Properties().rarity(Rarity.RARE).fireResistant());
    public static final RegistryObject<Block> CUT_GHOST_STEEL_BLOCK = registryBlock("cut_ghost_steel", () -> new Block(BlockBehaviour.Properties
            .copy(BlockInit.GHOST_STEEL_BLOCK.get())), false, new Item.Properties().rarity(Rarity.RARE).fireResistant());
    public static final RegistryObject<Block> CUT_GHOST_STEEL_BLOCK_STAIRS = registryBlock("cut_ghost_steel_stairs", () -> new StairBlock(() -> CUT_GHOST_STEEL_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties
            .copy(CUT_GHOST_STEEL_BLOCK.get())), false, new Item.Properties().rarity(Rarity.RARE).fireResistant());
    public static final RegistryObject<Block> CUT_GHOST_STEEL_BLOCK_SLAB = registryBlock("cut_ghost_steel_slab", () -> new SlabBlock(BlockBehaviour.Properties
            .copy(CUT_GHOST_STEEL_BLOCK.get())), false, new Item.Properties().rarity(Rarity.RARE).fireResistant());
    public static final RegistryObject<Block> ERODED_SOIL = registryBlock("eroded_soil", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.MUD).sound(SoundType.MUD)), false);
    public static final RegistryObject<Block> DARKENED_COAL_ORE = registryBlock("darkened_coal_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties
            .copy(Blocks.COAL_ORE), UniformInt.of(0, 2)), false);
    public static final RegistryObject<Block> DARKENED_IRON_ORE = registryBlock("darkened_iron_ore", () -> new DropExperienceBlock(BlockBehaviour.Properties
            .copy(Blocks.IRON_ORE)), false);
    public static final RegistryObject<Block> BLIGHTED_STONE = registryBlock("blighted_stone", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.STONE).sound(SoundType.STONE)), false);
    public static final RegistryObject<Block> BLIGHTED_STONE_STAIRS = registryBlock("blighted_stone_stairs", () -> new StairBlock(() -> BLIGHTED_STONE.get().defaultBlockState(), BlockBehaviour.Properties
            .copy(BLIGHTED_STONE.get())), false);
    public static final RegistryObject<Block> BLIGHTED_STONE_SLAB = registryBlock("blighted_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties
            .copy(BLIGHTED_STONE.get())), false);
    public static final RegistryObject<Block> BLIGHTED_STONE_PRESSURE_PLATE = registryBlock("blighted_stone_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties
            .copy(BLIGHTED_STONE.get()), BlockSetType.STONE), false);
    public static final RegistryObject<Block> BLIGHTED_STONE_BUTTON = registryBlock("blighted_stone_button", () -> new ButtonBlock(BlockBehaviour.Properties
            .copy(BLIGHTED_STONE.get()), BlockSetType.STONE, 20, false), false);
    public static final RegistryObject<Block> BLIGHTED_COBBLESTONE = registryBlock("blighted_cobblestone", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.COBBLESTONE).sound(SoundType.STONE)), false);
    public static final RegistryObject<Block> BLIGHTED_COBBLESTONE_STAIRS = registryBlock("blighted_cobblestone_stairs", () -> new StairBlock(() -> BLIGHTED_COBBLESTONE.get().defaultBlockState(), BlockBehaviour.Properties
            .copy(BLIGHTED_COBBLESTONE.get())), false);
    public static final RegistryObject<Block> BLIGHTED_COBBLESTONE_SLAB = registryBlock("blighted_cobblestone_slab", () -> new SlabBlock(BlockBehaviour.Properties
            .copy(BLIGHTED_COBBLESTONE.get())), false);
    public static final RegistryObject<Block> BLIGHTED_COBBLESTONE_WALL = registryBlock("blighted_cobblestone_wall", () -> new WallBlock(BlockBehaviour.Properties
            .copy(BLIGHTED_COBBLESTONE.get())), false);
    public static final RegistryObject<Block> VOIDSHARD = registryBlock("voidshard", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.GRANITE).sound(SoundType.STONE)), false);
    public static final RegistryObject<Block> VOIDSHARD_STAIRS = registryBlock("voidshard_stairs", () -> new StairBlock(() -> VOIDSHARD.get().defaultBlockState(), BlockBehaviour.Properties
            .copy(VOIDSHARD.get())), false);
    public static final RegistryObject<Block> VOIDSHARD_SLAB = registryBlock("voidshard_slab", () -> new SlabBlock(BlockBehaviour.Properties
            .copy(VOIDSHARD.get())), false);
    public static final RegistryObject<Block> VOIDSHARD_WALL = registryBlock("voidshard_wall", () -> new WallBlock(BlockBehaviour.Properties
            .copy(VOIDSHARD.get())), false);
    public static final RegistryObject<Block> POLISHED_VOIDSHARD = registryBlock("polished_voidshard", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.POLISHED_GRANITE).sound(SoundType.STONE)), false);
    public static final RegistryObject<Block> POLISHED_VOIDSHARD_STAIRS = registryBlock("polished_voidshard_stairs", () -> new StairBlock(() -> POLISHED_VOIDSHARD.get().defaultBlockState(), BlockBehaviour.Properties
            .copy(POLISHED_VOIDSHARD.get())), false);
    public static final RegistryObject<Block> POLISHED_VOIDSHARD_SLAB = registryBlock("polished_voidshard_slab", () -> new SlabBlock(BlockBehaviour.Properties
            .copy(POLISHED_VOIDSHARD.get())), false);
    public static final RegistryObject<Block> DARK_EROSION_ROCK = registryBlock("dark_erosion_rock", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.DEEPSLATE).sound(SoundType.STONE)), false);
    public static final RegistryObject<Block> POLISHED_DARK_EROSION_ROCK = registryBlock("polished_dark_erosion_rock", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.STONE)), false);
    public static final RegistryObject<Block> POLISHED_DARK_EROSION_ROCK_STAIRS = registryBlock("polished_dark_erosion_rock_stairs", () -> new StairBlock(() -> POLISHED_DARK_EROSION_ROCK.get().defaultBlockState(), BlockBehaviour.Properties
            .copy(POLISHED_DARK_EROSION_ROCK.get())), false);
    public static final RegistryObject<Block> POLISHED_DARK_EROSION_ROCK_SLAB = registryBlock("polished_dark_erosion_rock_slab", () -> new SlabBlock(BlockBehaviour.Properties
            .copy(POLISHED_DARK_EROSION_ROCK.get())), false);
    public static final RegistryObject<Block> POLISHED_DARK_EROSION_ROCK_WALL = registryBlock("polished_dark_erosion_rock_wall", () -> new WallBlock(BlockBehaviour.Properties
            .copy(POLISHED_DARK_EROSION_ROCK.get())), false);
    public static final RegistryObject<Block> CHISELED_DARK_EROSION_ROCK = registryBlock("chiseled_dark_erosion_rock", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.STONE)), false);
    public static final RegistryObject<Block> DARK_EROSION_ROCK_BRICKS = registryBlock("dark_erosion_rock_bricks", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.STONE)), false);
    public static final RegistryObject<Block> DARK_EROSION_ROCK_PILLAR = registryBlock("dark_erosion_rock_pillar", () -> new BlockRotatedPillar(BlockBehaviour.Properties
            .copy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.STONE)), false);
    public static final RegistryObject<Block> CRACKED_DARK_EROSION_ROCK_BRICKS = registryBlock("cracked_dark_erosion_rock_bricks", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.STONE)), false);
    public static final RegistryObject<Block> DARK_EROSION_ROCK_BRICKS_STAIRS = registryBlock("dark_erosion_rock_bricks_stairs", () -> new StairBlock(() -> DARK_EROSION_ROCK_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties
            .copy(DARK_EROSION_ROCK_BRICKS.get())), false);
    public static final RegistryObject<Block> DARK_EROSION_ROCK_BRICKS_SLAB = registryBlock("dark_erosion_rock_bricks_slab", () -> new SlabBlock(BlockBehaviour.Properties
            .copy(DARK_EROSION_ROCK_BRICKS.get())), false);
    public static final RegistryObject<Block> DARK_EROSION_ROCK_BRICKS_WALL = registryBlock("dark_erosion_rock_bricks_wall", () -> new WallBlock(BlockBehaviour.Properties
            .copy(DARK_EROSION_ROCK_BRICKS.get())), false);

    public static final RegistryObject<Block> BLIGHTED_OAK_LOG = registryBlock("blighted_oak_log", () -> new BlockRotatedPillar(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)), false);
    public static final RegistryObject<Block> STRIPPED_BLIGHTED_OAK_LOG = registryBlock("stripped_blighted_oak", () -> new BlockRotatedPillar(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG), 5, 5), false);
    public static final RegistryObject<Block> BLIGHTED_OAK_WOOD = registryBlock("blighted_oak_wood", () -> new BlockRotatedPillar(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)), false);
    public static final RegistryObject<Block> STRIPPED_BLIGHTED_OAK_WOOD = registryBlock("stripped_blighted_oak_wood", () -> new BlockRotatedPillar(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD), 5, 5), false);
    public static final RegistryObject<Block> BLIGHTED_OAK_SAPLING = registryBlock("blighted_oak_sapling", () -> new SaplingBlock(new BlightedOakGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)), false);
    public static final RegistryObject<Block> BLIGHTED_OAK_PLANKS = registryBlock("blighted_oak_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)), false);
    public static final RegistryObject<Block> BLIGHTED_OAK_STAIRS = registryBlock("blighted_oak_stairs", () -> new StairBlock(() -> BLIGHTED_OAK_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties
            .copy(BLIGHTED_OAK_PLANKS.get())), false);
    public static final RegistryObject<Block> BLIGHTED_OAK_SLAB = registryBlock("blighted_oak_slab", () -> new SlabBlock(BlockBehaviour.Properties
            .copy(BLIGHTED_OAK_PLANKS.get())), false);
    public static final RegistryObject<Block> BLIGHTED_OAK_FENCE = registryBlock("blighted_oak_fence", () -> new FenceBlock(BlockBehaviour.Properties
            .copy(BLIGHTED_OAK_PLANKS.get())), false);
    public static final RegistryObject<Block> BLIGHTED_OAK_FENCE_GATE = registryBlock("blighted_oak_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties
            .copy(BLIGHTED_OAK_PLANKS.get()), WoodType.OAK), false);
    public static final RegistryObject<Block> BLIGHTED_OAK_DOOR = registryBlock("blighted_oak_door", () -> new DoorBlock(BlockBehaviour.Properties
            .copy(BLIGHTED_OAK_PLANKS.get()).noOcclusion(), BlockSetType.OAK), false);
    public static final RegistryObject<Block> BLIGHTED_OAK_TRAPDOOR = registryBlock("blighted_oak_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties
            .copy(BLIGHTED_OAK_PLANKS.get()).noOcclusion(), BlockSetType.OAK), false);
    public static final RegistryObject<Block> BLIGHTED_OAK_PRESSURE_PLATE = registryBlock("blighted_oak_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties
            .copy(BLIGHTED_OAK_PLANKS.get()), BlockSetType.OAK), false);
    public static final RegistryObject<Block> BLIGHTED_OAK_BUTTON = registryBlock("blighted_oak_button", () -> new ButtonBlock(BlockBehaviour.Properties
            .copy(BLIGHTED_OAK_PLANKS.get()), BlockSetType.OAK, 30, true), false);
    public static final RegistryObject<Block> STEPPING_POISON_TRAP = registryBlock("stepping_poison_trap", () -> new BlockSteppingPoisonTrap(BlockBehaviour.Properties
            .copy(Blocks.DEEPSLATE_BRICKS).randomTicks().strength(50.0F, 1200.0F)), false);
    public static final RegistryObject<Block> STEPPING_SKELETON_TRAP = registryBlock("stepping_skeleton_trap", () -> new BlockSteppingSkeletonTrap(BlockBehaviour.Properties
            .copy(Blocks.DEEPSLATE_BRICKS).randomTicks().strength(50.0F, 1200.0F).lightLevel(state -> state.getValue(BlockStateProperties.OPEN) ? 2 : 0)), false);

    private static boolean always(BlockState state, BlockGetter getter, BlockPos pos) {
        return true;
    }

    private static boolean always(BlockState state, BlockGetter getter, BlockPos pos, EntityType<?> entityType) {
        return true;
    }

    private static Boolean never(BlockState state, BlockGetter getter, BlockPos pos, EntityType<?> entityType) {
        return false;
    }

    private static boolean never(BlockState state, BlockGetter getter, BlockPos pos) {
        return false;
    }

    private static <T extends Block> RegistryObject<T> registryBlock(String name, Supplier<T> block, boolean isEntity) {
        RegistryObject<T> register = BLOCKS.register(name, block);
        registerBlockItem(name, register, new Item.Properties(), isEntity);
        return register;
    }

    private static <T extends Block> RegistryObject<T> registryBlock(String name, Supplier<T> block, boolean isEntity, Item.Properties properties) {
        RegistryObject<T> register = BLOCKS.register(name, block);
        registerBlockItem(name, register, properties, isEntity);
        return register;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block, Item.Properties properties, boolean isEntityBlock) {
        ItemInit.ITEMS.register(name, () -> isEntityBlock ? new EMBlockEntityItemRender(block.get(), properties) : new BlockItem(block.get(), properties));
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
