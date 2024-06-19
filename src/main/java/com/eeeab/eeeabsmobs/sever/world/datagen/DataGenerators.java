package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = EEEABMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
//json数据生成
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        boolean includeServer = event.includeServer();
        generator.addProvider(includeServer, new EMItemModelProvider(packOutput, helper));
        generator.addProvider(includeServer, new EMDamageTypeProvider(packOutput, provider, helper));
        generator.addProvider(includeServer, new EMStructureTagsProvider(packOutput, provider, helper));
        generator.addProvider(includeServer, new EMBiomeTagsProvider(packOutput, provider, helper));
        generator.addProvider(includeServer, new EMEntityTypeTagsProvider(packOutput, provider, helper));
        EMBlockTagsProvider blockTagsProvider = new EMBlockTagsProvider(packOutput, provider, helper);
        generator.addProvider(includeServer, blockTagsProvider);
        generator.addProvider(includeServer, new EMItemTagsProvider(packOutput, provider, blockTagsProvider.contentsGetter(), helper));
        generator.addProvider(includeServer, new EMBlockStateProvider(packOutput, helper));
        //generator.addProvider(includeServer, new EMWorldGenProvider(packOutput, provider));
    }
}
