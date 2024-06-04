package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EEEABMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
//json数据生成
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        boolean includeServer = event.includeServer();
        generator.addProvider(includeServer, new EMStructureTagsProvider(generator, helper));
        generator.addProvider(includeServer, new EMBiomeTagsProvider(generator, helper));
        generator.addProvider(includeServer, new EMEntityTypeTagsProvider(generator, helper));
        EMBlockTagsProvider blockTagsProvider = new EMBlockTagsProvider(generator, helper);
        generator.addProvider(includeServer, blockTagsProvider);
        generator.addProvider(includeServer, new EMItemTagsProvider(generator, blockTagsProvider, helper));
        generator.addProvider(includeServer, new EMBlockStateProvider(generator, helper));
        //generator.addProvider(includeServer, new EMWorldGenProvider(packOutput, provider));
    }
}
