package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class EMEntityTypeTagsProvider extends EntityTypeTagsProvider {

    public EMEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, EEEABMobs.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Tags.EntityTypes.BOSSES).add(EntityInit.NAMELESS_GUARDIAN.get());
        tag(EntityTypeTags.ARROWS).add(EntityInit.POISON_ARROW.get());
        tag(EMTagKey.TRAP_WHITELIST).add(EntityInit.IMMORTAL_GOLEM.get(), EntityInit.CORPSE.get(), EntityInit.CORPSE_VILLAGER.get(), EntityInit.CORPSE_SLAVERY.get());
    }
}
