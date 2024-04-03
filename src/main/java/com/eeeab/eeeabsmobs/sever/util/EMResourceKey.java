package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;

public class EMResourceKey {
    public static ResourceKey<Structure> ANCIENT_TOMB = registerResourceKey(Registry.STRUCTURE_REGISTRY, "ancient_tomb");
    public static ResourceKey<Structure> BLOODY_ALTAR = registerResourceKey(Registry.STRUCTURE_REGISTRY, "bloody_altar");
    public static final ResourceKey<PoiType> EROSION_PORTAL = registerResourceKey(Registry.POINT_OF_INTEREST_TYPE_REGISTRY, "erosion_portal");
    public static final ResourceKey<Level> EROSION = registerResourceKey(Registry.DIMENSION_REGISTRY, "erosion");

    private static <T> ResourceKey<T> registerResourceKey(ResourceKey<Registry<T>> registry, String key) {
        return ResourceKey.create(registry, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }
}
