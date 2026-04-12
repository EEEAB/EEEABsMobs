package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.world.datagen.damage.ModDamageTypeProvider;
import com.eeeab.eeeabsmobs.sever.world.datagen.entity.ModEntityTypeTagsProvider;
import com.eeeab.eeeabsmobs.sever.world.datagen.entity.ModMobEffectProvider;
import com.eeeab.eeeabsmobs.sever.world.datagen.loot.ModBlockLootTables;
import com.eeeab.eeeabsmobs.sever.world.datagen.loot.ModEntityLootTables;
import com.eeeab.eeeabsmobs.sever.world.datagen.world.ModBiomeTagsProvider;
import com.eeeab.eeeabsmobs.sever.world.datagen.world.ModStructureTagsProvider;
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
        generator.addProvider(includeServer, new ModDatapackBuiltinProvider(packOutput, provider));
        generator.addProvider(includeServer, new ModBlockStateProvider(packOutput, helper));
        generator.addProvider(includeServer, new ModItemModelProvider(packOutput, helper));
        generator.addProvider(includeServer, new ModRecipeProvider(packOutput));
        generator.addProvider(includeServer, new ModMobEffectProvider(packOutput, provider, helper));
        generator.addProvider(includeServer, new ModDamageTypeProvider(packOutput, provider, helper));
        generator.addProvider(includeServer, new ModStructureTagsProvider(packOutput, provider, helper));
        generator.addProvider(includeServer, new ModBiomeTagsProvider(packOutput, provider, helper));
        generator.addProvider(includeServer, new ModEntityTypeTagsProvider(packOutput, provider, helper));
        ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(packOutput, provider, helper);
        generator.addProvider(includeServer, createLootProvider(packOutput));
        generator.addProvider(includeServer, blockTagsProvider);
        generator.addProvider(includeServer, new ModItemTagsProvider(packOutput, provider, blockTagsProvider.contentsGetter(), helper));
    }

    private static LootTableProvider createLootProvider(PackOutput output) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(ModBlockLootTables::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(ModEntityLootTables::new, LootContextParamSets.ENTITY)
        ));
    }
}
