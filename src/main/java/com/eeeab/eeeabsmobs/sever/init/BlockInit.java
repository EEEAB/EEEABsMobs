package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.block.*;
import com.eeeab.eeeabsmobs.sever.block.grower.BlightedOakGrower;
import com.eeeab.eeeabsmobs.sever.block.grower.ErosionOakGrower;
import com.eeeab.eeeabsmobs.sever.block.trapImpl.BlockTomeArrowsTarp;
import com.eeeab.eeeabsmobs.sever.block.trapImpl.BlockTombGasTrap;
import com.eeeab.eeeabsmobs.sever.block.trapImpl.BlockTombSummonTrap;
import com.eeeab.eeeabsmobs.sever.item.util.EMBlockEntityItemRender;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, EEEABMobs.MOD_ID);
    public static final RegistryObject<Block> SOUL_LIGHT = registryBlock("soul_light", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.SHROOMLIGHT).lightLevel((blockState) -> 13)), false);
    public static final RegistryObject<Block> TOMB_GAS_TRAP = registryBlock("tomb_gas_trap", () -> new BlockTombGasTrap(BlockBehaviour.Properties
            .copy(Blocks.DEEPSLATE_BRICKS).randomTicks().strength(100F, 1200F).lightLevel(state -> state.getValue(BlockStateProperties.OPEN) ? 2 : 0)), false);
    public static final RegistryObject<Block> TOMB_SUMMON_TRAP = registryBlock("tomb_summon_trap", () -> new BlockTombSummonTrap(BlockBehaviour.Properties
            .copy(Blocks.DEEPSLATE_BRICKS).randomTicks().strength(100F, 1200F).lightLevel(state -> state.getValue(BlockStateProperties.OPEN) ? 2 : 0)), false);
    public static final RegistryObject<Block> TOMB_ARROWS_TRAP = registryBlock("tomb_arrows_trap", () -> new BlockTomeArrowsTarp(BlockBehaviour.Properties
            .copy(Blocks.DEEPSLATE_BRICKS).strength(100F, 1200F).isValidSpawn((pState, pLevel, pPos, type) -> true)), false);
    public static final RegistryObject<Block> EROSION_DEEPSLATE_BRICKS = registryBlock("erosion_deepslate_bricks", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.REINFORCED_DEEPSLATE).strength(-1.0F, 3600000.0F).noLootTable()), false);
    public static final RegistryObject<Block> EROSION_PORTAL = registryBlock("erosion_portal", () -> new BlockErosionPortal(BlockBehaviour.Properties.of()
            .noCollission().randomTicks().pushReaction(PushReaction.BLOCK).strength(-1.0F).sound(SoundType.GLASS).lightLevel(s -> 11).noLootTable()), false);
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
    public static final RegistryObject<Block> BONE_BUSH = BLOCKS.register("bone_bush", () -> new BlockMirroredFlower(() -> MobEffects.WEAKNESS, 9, BlockBehaviour.Properties
            .copy(Blocks.DANDELION).lightLevel((state) -> 1)));
    public static final RegistryObject<Item> BONE_BUSH_ITEM = ItemInit.ITEMS.register("bone_bush", () -> new BlockItem(BONE_BUSH.get(), new Item.Properties()) {
        @Override
        public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
            return 300;
        }
    });
    public static final RegistryObject<Block> POTTED_BONE_BUSH = registryBlock("potted_bone_bush", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, BONE_BUSH, BlockBehaviour.Properties
            .copy(Blocks.POTTED_DANDELION)), false);

    public static final RegistryObject<Block> EROSION_OAK_LOG = registryBlock("erosion_oak_log", () -> new BlockRotatedPillar(BlockBehaviour.Properties.copy(Blocks.OAK_LOG).strength(3F)), false);
    public static final RegistryObject<Block> STRIPPED_EROSION_OAK_LOG = registryBlock("stripped_erosion_oak", () -> new BlockRotatedPillar(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG).strength(3F), 5, 5), false);
    public static final RegistryObject<Block> EROSION_OAK_WOOD = registryBlock("erosion_oak_wood", () -> new BlockRotatedPillar(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD).strength(3F)), false);
    public static final RegistryObject<Block> STRIPPED_EROSION_OAK_WOOD = registryBlock("stripped_erosion_oak_wood", () -> new BlockRotatedPillar(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD).strength(3F), 5, 5), false);
    public static final RegistryObject<Block> EROSION_OAK_LEAVES = registryBlock("erosion_oak_leaves", () -> new BlockErosionOakLeaves(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES).strength(1F)
            .lightLevel(e -> Mth.clamp(e.getValue(BlockErosionOakLeaves.LAYER), 1, 3))), false);
    public static final RegistryObject<Block> EROSION_OAK_SAPLING = registryBlock("erosion_oak_sapling", () -> new SaplingBlock(new ErosionOakGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)), false);
    public static final RegistryObject<Block> EROSION_OAK_BERRY = registryBlock("erosion_oak_berry", () -> new BlockErosionOakBerry(BlockBehaviour.Properties
            .copy(Blocks.MANGROVE_PROPAGULE).lightLevel((blockState) -> 4)), false, new Item.Properties().food(new FoodProperties.Builder().fast().nutrition(2).effect(() -> new MobEffectInstance(MobEffects.GLOWING, 100, 0), 1.0F).build()));
    public static final RegistryObject<Block> EROSION_OAK_PLANKS = registryBlock("erosion_oak_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(3F)), false);
    public static final RegistryObject<Block> EROSION_OAK_STAIRS = registryBlock("erosion_oak_stairs", () -> new StairBlock(() -> EROSION_OAK_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties
            .copy(EROSION_OAK_PLANKS.get())), false);
    public static final RegistryObject<Block> EROSION_OAK_SLAB = registryBlock("erosion_oak_slab", () -> new SlabBlock(BlockBehaviour.Properties
            .copy(EROSION_OAK_PLANKS.get())), false);
    public static final RegistryObject<Block> EROSION_OAK_FENCE = registryBlock("erosion_oak_fence", () -> new FenceBlock(BlockBehaviour.Properties
            .copy(EROSION_OAK_PLANKS.get())), false);
    public static final RegistryObject<Block> EROSION_OAK_FENCE_GATE = registryBlock("erosion_oak_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties
            .copy(EROSION_OAK_PLANKS.get()), WoodType.OAK), false);
    public static final RegistryObject<Block> EROSION_OAK_DOOR = registryBlock("erosion_oak_door", () -> new DoorBlock(BlockBehaviour.Properties
            .copy(EROSION_OAK_PLANKS.get()).noOcclusion(), BlockSetType.OAK), false);
    public static final RegistryObject<Block> EROSION_OAK_TRAPDOOR = registryBlock("erosion_oak_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties
            .copy(EROSION_OAK_PLANKS.get()).noOcclusion(), BlockSetType.OAK), false);
    public static final RegistryObject<Block> EROSION_OAK_PRESSURE_PLATE = registryBlock("erosion_oak_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties
            .copy(EROSION_OAK_PLANKS.get()), BlockSetType.OAK), false);
    public static final RegistryObject<Block> EROSION_OAK_BUTTON = registryBlock("erosion_oak_button", () -> new ButtonBlock(BlockBehaviour.Properties
            .copy(EROSION_OAK_PLANKS.get()), BlockSetType.OAK, 30, true), false);


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
