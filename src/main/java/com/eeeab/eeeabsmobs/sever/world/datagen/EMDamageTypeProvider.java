package com.eeeab.eeeabsmobs.sever.world.datagen;

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
        tag(DamageTypeTags.BYPASSES_ARMOR).add(EMResourceKey.GUARDIAN_LASER);
        tag(EMTagKey.GENERAL_UNRESISTANT_TO).add(DamageTypes.FELL_OUT_OF_WORLD).add(DamageTypes.GENERIC_KILL);
        tag(EMTagKey.MAGIC_UNRESISTANT_TO).add(DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC, DamageTypes.SONIC_BOOM, DamageTypes.THORNS);
    }
}
