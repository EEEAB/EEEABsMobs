package com.eeeab.eeeabsmobs.sever.world.datagen.damage;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.util.ModResourceKey;
import com.eeeab.eeeabsmobs.sever.util.ModTagKey;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

//自定义伤害
public class ModDamageTypeProvider extends DamageTypeTagsProvider {
    public ModDamageTypeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, future, EEEABMobs.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(DamageTypeTags.IS_PROJECTILE).add(ModResourceKey.SHAMAN_BOMBING);
        tag(DamageTypeTags.WITCH_RESISTANT_TO).add(ModResourceKey.IMMORTAL_MAGIC);
        tag(DamageTypeTags.BYPASSES_COOLDOWN).add(ModResourceKey.IMMORTAL_MAGIC, ModResourceKey.OVERLOAD_EXPLODE);
        tag(DamageTypeTags.BYPASSES_RESISTANCE).add(ModResourceKey.IMMORTAL_MAGIC);
        tag(DamageTypeTags.BYPASSES_ARMOR).add(ModResourceKey.GUARDIAN_LASER, ModResourceKey.IMMORTAL_MAGIC, ModResourceKey.IGNORE_ARMOR_ATTACK);
        tag(DamageTypeTags.BYPASSES_SHIELD).remove(ModResourceKey.IMMORTAL_MAGIC);
        tag(DamageTypeTags.IS_EXPLOSION).add(ModResourceKey.OVERLOAD_EXPLODE);
        tag(ModTagKey.BYPASSES_DAMAGE_CAP).add(DamageTypes.FELL_OUT_OF_WORLD, DamageTypes.GENERIC_KILL);
        tag(ModTagKey.MAGIC_RESISTANT_TO).addTag(DamageTypeTags.WITCH_RESISTANT_TO);
        tag(ModTagKey.CAN_CRIT_HEAL).add(ModResourceKey.CRIT_HEAL);
    }
}
