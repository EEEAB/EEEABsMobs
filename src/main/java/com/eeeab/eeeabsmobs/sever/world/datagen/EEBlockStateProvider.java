package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

//自定义方块物品
public class EEBlockStateProvider extends BlockStateProvider {
    public EEBlockStateProvider(DataGenerator output, ExistingFileHelper exFileHelper) {
        super(output, EEEABMobs.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject){
        simpleBlockItem(blockRegistryObject.get(),cubeAll(blockRegistryObject.get()));
    }
}
