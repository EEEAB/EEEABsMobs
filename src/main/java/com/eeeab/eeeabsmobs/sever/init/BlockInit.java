package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.block.BlockTombstone;
import com.eeeab.eeeabsmobs.sever.item.util.EEBlockEntityItemRender;
import com.eeeab.eeeabsmobs.sever.util.EETabGroup;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, EEEABMobs.MOD_ID);

    public static final RegistryObject<Block> IMMORTAL_BLOCK = registryBlock("immortal_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.METAL)), false);

    public static final RegistryObject<Block> SOUL_LIGHT = registryBlock("soul_light",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.SHROOMLIGHT).lightLevel((blockState) -> 11)), false);

    public static final RegistryObject<Block> TOMBSTONE = registryBlock("tombstone",BlockTombstone::new, true);

    private static <T extends Block> RegistryObject<T> registryBlock(String name, Supplier<T> block, boolean isEntity) {
        return BLOCKS.register(name, block);
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
