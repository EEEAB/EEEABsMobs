package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.util.EEResourceKey;
import com.eeeab.eeeabsmobs.sever.util.EETagKey;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.StructureTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class EEStructureProvider extends StructureTagsProvider {
    public EEStructureProvider(DataGenerator output, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, EEEABMobs.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(EETagKey.EYE_OF_ANCIENT_TOMB).add(EEResourceKey.ANCIENT_TOMB);
    }
}
