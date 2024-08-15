package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class EMBlockStateProvider extends BlockStateProvider {
    public EMBlockStateProvider(DataGenerator generator, ExistingFileHelper exFileHelper) {
        super(generator, EEEABMobs.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        cubeAll(BlockInit.IMMORTAL_BLOCK);
        cubeAll(BlockInit.GHOST_STEEL_BLOCK);
        cubeAll(BlockInit.EROSION_DEEPSLATE_BRICKS);
        cubeAll(BlockInit.SOUL_LIGHT);
        cubeAll(BlockInit.EROSION_ROCK_BRICKS);
        //cubeAll(BlockInit.TOMBSTONE);
    }

    /**
     * 生成同一面方块
     */
    private void cubeAll(RegistryObject<Block> rob) {
        simpleBlock(rob.get(), cubeAll(rob.get()));
        simpleBlockItem(rob.get(), cubeAll(rob.get()));
    }

}