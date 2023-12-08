package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ModKey {
    //结构键
    public static ResourceKey<Structure> ANCIENT_TOMB = registerStructureKey("ancient_tomb");

    public static ResourceKey<Structure> registerStructureKey(String key) {
        return ResourceKey.create(Registries.STRUCTURE, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }
}
