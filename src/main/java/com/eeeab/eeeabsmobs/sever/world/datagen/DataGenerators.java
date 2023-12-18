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
        //CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
        //generator.addProvider(true, new EEDamageTypeProvider(packOutput, provider, helper));
        generator.addProvider(true, new EEStructureProvider(generator, helper));

        //generator.addProvider(true, new EERecipeProvider(packOutput));
        //generator.addProvider(true, EELootTableProvider.create(packOutput));
        //generator.addProvider(true, new EEItemModelProvider(packOutput, helper));
        //generator.addProvider(true, new EEBlockStateProvider(packOutput, helper));

    }
}
