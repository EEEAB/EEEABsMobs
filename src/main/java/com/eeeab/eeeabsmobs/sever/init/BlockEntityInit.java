package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.block.EntityBlockTombstone;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, EEEABMobs.MOD_ID);

    public static final RegistryObject<BlockEntityType<EntityBlockTombstone>> ENTITY_TOMBSTONE = BLOCK_ENTITIES.register("tombstone", () ->
            BlockEntityType.Builder.<EntityBlockTombstone>of(EntityBlockTombstone::new, new Block[]{BlockInit.TOMBSTONE.get()}).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}
