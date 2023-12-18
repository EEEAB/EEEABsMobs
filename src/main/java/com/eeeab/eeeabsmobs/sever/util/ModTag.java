package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ModTag {
    //结构标签
    public static TagKey<Structure> EYE_OF_ANCIENT_TOMB = registerStructureTag("eye_of_ancient_tomb");

    ////伤害标签
    //public static final TagKey<DamageType> GENERAL_UNRESISTANT_TO = registerDamageTypeTag("general_unresistant_to");
    //public static final TagKey<DamageType> MAGIC_UNRESISTANT_TO = registerDamageTypeTag("magic_unresistant_to");
    //
    //
    //private static TagKey<DamageType> registerDamageTypeTag(String key) {
    //    return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(EEEABMobs.MOD_ID, key));
    //}

    private static TagKey<Structure> registerStructureTag(String key) {
        return TagKey.create(Registry.STRUCTURE_REGISTRY, new ResourceLocation(EEEABMobs.MOD_ID, key));
    }
}
