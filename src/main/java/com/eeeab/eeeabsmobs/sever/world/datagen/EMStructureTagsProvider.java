package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.util.EMResourceKey;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.StructureTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class EMStructureTagsProvider extends StructureTagsProvider {
    public EMStructureTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, EEEABMobs.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(EMTagKey.EYE_OF_ANCIENT_TOMB).add(EMResourceKey.ANCIENT_TOMB);
        this.tag(EMTagKey.EYE_OF_BLOODY_ALTAR).add(EMResourceKey.BLOODY_ALTAR);
    }
}
