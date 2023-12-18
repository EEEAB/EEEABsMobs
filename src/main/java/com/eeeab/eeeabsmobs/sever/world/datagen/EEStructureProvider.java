package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.util.ModKey;
import com.eeeab.eeeabsmobs.sever.util.ModTag;
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
        this.tag(ModTag.EYE_OF_ANCIENT_TOMB).add(ModKey.ANCIENT_TOMB);
    }
}
