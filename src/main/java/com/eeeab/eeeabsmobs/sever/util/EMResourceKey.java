package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

public class EMResourceKey {
    public static ResourceKey<Structure> ANCIENT_TOMB = registerStructureKey("ancient_tomb");
    public static final ResourceKey<PoiType> EROSION_PORTAL = registerPoiTypeKey("erosion_portal");
    public static final ResourceKey<Level> EROSION = registerWorldKey("erosion");


    public static ResourceKey<Structure> registerStructureKey(String key) {
        return ResourceKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }


    public static ResourceKey<PoiType> registerPoiTypeKey(String key) {
        return ResourceKey.create(Registry.POINT_OF_INTEREST_TYPE_REGISTRY, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }

    public static ResourceKey<Level> registerWorldKey(String key) {
        return ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }
}
