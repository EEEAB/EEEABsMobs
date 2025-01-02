package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.block.BlockErosionPortal;
import com.eeeab.eeeabsmobs.sever.block.BlockTombstone;
import com.eeeab.eeeabsmobs.sever.block.trapImpl.BlockTomeArrowsTarp;
import com.eeeab.eeeabsmobs.sever.block.trapImpl.BlockTombGasTrap;
import com.eeeab.eeeabsmobs.sever.block.trapImpl.BlockTombSummonTrap;
import com.eeeab.eeeabsmobs.sever.item.util.EMBlockEntityItemRender;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, EEEABMobs.MOD_ID);

    public static final RegistryObject<Block> IMMORTAL_BLOCK = registryBlock("immortal_block", () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.IRON_BLOCK)
                    .sound(SoundType.METAL)),
            false);

    public static final RegistryObject<Block> GHOST_STEEL_BLOCK = registryBlock("ghost_steel_block", () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.NETHERITE_BLOCK)
                    .sound(SoundType.METAL)),
            false, new Item.Properties().rarity(Rarity.RARE).fireResistant());

    public static final RegistryObject<Block> SOUL_LIGHT = registryBlock("soul_light", () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.SHROOMLIGHT)
                    .lightLevel((blockState) -> 13)),
            false);

    public static final RegistryObject<Block> TOMB_GAS_TRAP = registryBlock("tomb_gas_trap", () -> new BlockTombGasTrap(BlockBehaviour.Properties
                    .copy(Blocks.DEEPSLATE_BRICKS)
                    .randomTicks()
                    .strength(100F, 1200F)
                    .lightLevel(state -> state.getValue(BlockStateProperties.OPEN) ? 2 : 0)),
            false);

    public static final RegistryObject<Block> TOMB_SUMMON_TRAP = registryBlock("tomb_summon_trap", () -> new BlockTombSummonTrap(BlockBehaviour.Properties
                    .copy(Blocks.DEEPSLATE_BRICKS)
                    .randomTicks()
                    .strength(100F, 1200F)
                    .lightLevel(state -> state.getValue(BlockStateProperties.OPEN) ? 2 : 0)),
            false);

    public static final RegistryObject<Block> TOMB_ARROWS_TRAP = registryBlock("tomb_arrows_trap", () -> new BlockTomeArrowsTarp(BlockBehaviour.Properties
                    .copy(Blocks.DEEPSLATE_BRICKS)
                    .strength(100F, 1200F)
                    .isValidSpawn((pState, pLevel, pPos, type) -> true))
            , false);

    public static final RegistryObject<Block> EROSION_DEEPSLATE_BRICKS = registryBlock("erosion_deepslate_bricks", () -> new Block(BlockBehaviour.Properties
                    .copy(Blocks.REINFORCED_DEEPSLATE)
                    .strength(-1.0F, 3600000.0F)
                    .noLootTable()),
            false);

    public static final RegistryObject<Block> EROSION_PORTAL = registryBlock("erosion_portal", () -> new BlockErosionPortal(BlockBehaviour.Properties.of()
                    .noCollission()
                    .randomTicks()
                    .pushReaction(PushReaction.BLOCK)
                    .strength(-1.0F)
                    .sound(SoundType.GLASS)
                    .lightLevel(s -> 11)
                    .noLootTable()),
            false);

    public static final RegistryObject<Block> TOMBSTONE = registryBlock("tombstone", BlockTombstone::new, true);

    public static final RegistryObject<Block> EROSION_ROCK_BRICKS = registryBlock("erosion_rock_bricks", () -> new Block(BlockBehaviour.Properties
            .copy(Blocks.DEEPSLATE_BRICKS)
            .strength(10.0F, 1200.0F)), false);

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
        ItemInit.ITEMS.register(name, () -> isEntityBlock ? new EMBlockEntityItemRender(block.get(), new Item.Properties()) : new BlockItem(block.get(), properties));
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
