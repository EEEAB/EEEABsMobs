package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.world.datagen.damage.EMDamageTypeProvider;
import com.eeeab.eeeabsmobs.sever.world.datagen.entity.EMEntityTypeTagsProvider;
import com.eeeab.eeeabsmobs.sever.world.datagen.entity.EMMobEffectProvider;
import com.eeeab.eeeabsmobs.sever.world.datagen.loot.EMBlockLootTables;
import com.eeeab.eeeabsmobs.sever.world.datagen.world.EMBiomeTagsProvider;
import com.eeeab.eeeabsmobs.sever.world.datagen.world.EMStructureTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Set;
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
        //手动执行
        generator.addProvider(true, new EMDatapackBuiltinProvider(packOutput, provider));
        generator.addProvider(includeServer, new EMItemModelProvider(packOutput, helper));
        generator.addProvider(includeServer, new EMRecipeProvider(packOutput));
        generator.addProvider(includeServer, new EMMobEffectProvider(packOutput, provider, helper));
        generator.addProvider(includeServer, new EMDamageTypeProvider(packOutput, provider, helper));
        generator.addProvider(includeServer, new EMStructureTagsProvider(packOutput, provider, helper));
        generator.addProvider(includeServer, new EMBiomeTagsProvider(packOutput, provider, helper));
        generator.addProvider(includeServer, new EMEntityTypeTagsProvider(packOutput, provider, helper));
        EMBlockTagsProvider blockTagsProvider = new EMBlockTagsProvider(packOutput, provider, helper);
        generator.addProvider(includeServer, createLootProvider(packOutput));
        generator.addProvider(includeServer, blockTagsProvider);
        generator.addProvider(includeServer, new EMItemTagsProvider(packOutput, provider, blockTagsProvider.contentsGetter(), helper));
        generator.addProvider(includeServer, new EMBlockStateProvider(packOutput, helper));
    }

    private static LootTableProvider createLootProvider(PackOutput output) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(EMBlockLootTables::new, LootContextParamSets.BLOCK)
        ));
    }
}
