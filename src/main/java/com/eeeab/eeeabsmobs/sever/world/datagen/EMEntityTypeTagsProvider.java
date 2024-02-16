package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class EMEntityTypeTagsProvider extends EntityTypeTagsProvider {

    public EMEntityTypeTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, EEEABMobs.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(Tags.EntityTypes.BOSSES).add(EntityInit.NAMELESS_GUARDIAN.get());
        tag(EntityTypeTags.ARROWS).add(EntityInit.POISON_ARROW.get());
        tag(EMTagKey.TRAP_WHITELIST).add(EntityInit.IMMORTAL_GOLEM.get(), EntityInit.CORPSE.get(), EntityInit.CORPSE_VILLAGER.get(), EntityInit.CORPSE_SLAVERY.get());
    }
}
