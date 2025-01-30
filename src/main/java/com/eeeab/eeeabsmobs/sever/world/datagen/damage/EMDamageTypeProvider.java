package com.eeeab.eeeabsmobs.sever.world.datagen.damage;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.util.EMResourceKey;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

//自定义伤害
public class EMDamageTypeProvider extends DamageTypeTagsProvider {
    public EMDamageTypeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, future, EEEABMobs.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(DamageTypeTags.IS_PROJECTILE).add(EMResourceKey.SHAMAN_BOMBING);
        tag(DamageTypeTags.WITCH_RESISTANT_TO).add(EMResourceKey.IMMORTAL_MAGIC);
        tag(DamageTypeTags.BYPASSES_COOLDOWN).add(EMResourceKey.IMMORTAL_MAGIC, EMResourceKey.OVERLOAD_EXPLODE);
        tag(DamageTypeTags.BYPASSES_RESISTANCE).add(EMResourceKey.IMMORTAL_MAGIC);
        tag(DamageTypeTags.BYPASSES_ARMOR).add(EMResourceKey.GUARDIAN_LASER, EMResourceKey.IMMORTAL_MAGIC, EMResourceKey.IGNORE_ARMOR_ATTACK);
        tag(DamageTypeTags.BYPASSES_SHIELD).remove(EMResourceKey.IMMORTAL_MAGIC);
        tag(DamageTypeTags.IS_EXPLOSION).add(EMResourceKey.OVERLOAD_EXPLODE);
        tag(EMTagKey.GENERAL_UNRESISTANT_TO).add(DamageTypes.FELL_OUT_OF_WORLD, DamageTypes.GENERIC_KILL);
        tag(EMTagKey.MAGIC_UNRESISTANT_TO).addTag(DamageTypeTags.WITCH_RESISTANT_TO);
        tag(EMTagKey.CAN_CRIT_HEAL).add(EMResourceKey.CRIT_HEAL);
    }
}
