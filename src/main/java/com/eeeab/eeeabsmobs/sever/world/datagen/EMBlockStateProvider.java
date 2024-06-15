package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.BlockInit;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class EMBlockStateProvider extends BlockStateProvider {
    public EMBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, EEEABMobs.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        cubeAll(BlockInit.IMMORTAL_BLOCK);
        cubeAll(BlockInit.GHOST_STEEL_BLOCK);
        cubeAll(BlockInit.EROSION_DEEPSLATE_BRICKS);
        cubeAll(BlockInit.SOUL_LIGHT);
        //cubeAll(BlockInit.TOMBSTONE);
    }

    /**
     * 生成同一面方块
     */
    private void cubeAll(RegistryObject<Block> rob) {
        simpleBlockWithItem(rob.get(), cubeAll(rob.get()));
    }

}