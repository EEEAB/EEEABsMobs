package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class EMMobEffectProvider extends TagsProvider<MobEffect> {
    protected EMMobEffectProvider(DataGenerator output, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registry.MOB_EFFECT, EEEABMobs.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(EMTagKey.EFFECTIVE_FOR_BOSSES).add(
                EffectInit.FRENZY_EFFECT.getKey(),
                EffectInit.ARMOR_LOWER_EFFECT.getKey());
    }
}
