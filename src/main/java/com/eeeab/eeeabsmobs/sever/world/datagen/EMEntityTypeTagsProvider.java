package com.eeeab.eeeabsmobs.sever.world.datagen;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
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
        tag(Tags.EntityTypes.BOSSES)
                .add(EntityInit.NAMELESS_GUARDIAN.get())
                .add(EntityInit.IMMORTAL_BOSS.get());
        tag(EntityTypeTags.ARROWS)
                .add(EntityInit.POISON_ARROW.get());
        //不朽者不主动进攻实体
        tag(EMTagKey.IMMORTAL_IGNORE_HUNT_TARGETS)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.ARMOR_STAND)
                .add(EntityType.WOLF);
        //陷阱白名单
        tag(EMTagKey.TRAP_WHITELIST)
                .add(EntityInit.IMMORTAL_GOLEM.get())
                .add(EntityInit.GULING_SENTINEL.get())
                .add(EntityInit.GULING_SENTINEL_HEAVY.get())
                .add(EntityInit.NAMELESS_GUARDIAN.get())
                .add(EntityInit.IMMORTAL_BOSS.get());
        //免疫强制切换目标
        tag(EMTagKey.RESISTS_FORCED_CHANGE_TARGET)
                .add(EntityInit.CORPSE_WARLOCK.get())
                .add(EntityInit.NAMELESS_GUARDIAN.get())
                .add(EntityInit.IMMORTAL_BOSS.get());
        //不朽军团
        tag(EMTagKey.IMMORTAL_ARMY)
                .add(EntityInit.IMMORTAL_GOLEM.get())
                .add(EntityInit.IMMORTAL_SKELETON.get())
                .add(EntityInit.IMMORTAL_KNIGHT.get())
                .add(EntityInit.IMMORTAL_SHAMAN.get())
                .add(EntityInit.IMMORTAL_EXECUTIONER.get())
                .add(EntityInit.IMMORTAL_BOSS.get());
        tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
                .add(EntityInit.NAMELESS_GUARDIAN.get())
                .add(EntityInit.CORPSE_WARLOCK.get())
                .addTag(EMTagKey.IMMORTAL_ARMY)
                .remove(EntityInit.IMMORTAL_EXECUTIONER.get());
        //Ice And Fire 免疫蛇发女妖石化
        tag(EMTagKey.BLINDED)
                .add(EntityInit.NAMELESS_GUARDIAN.get())
                .add(EntityInit.IMMORTAL_BOSS.get());
        tag(EMTagKey.IMMUNE_TO_GORGON_STONE)
                .add(EntityInit.NAMELESS_GUARDIAN.get())
                .add(EntityInit.IMMORTAL_BOSS.get());
        //Alex's Caves 免疫撼地龙咆哮
        tag(EMTagKey.RESISTS_TREMORSAURUS_ROAR)
                .add(EntityInit.CORPSE.get())
                .add(EntityInit.CORPSE_VILLAGER.get())
                .add(EntityInit.CORPSE_TO_PLAYER.get())
                .add(EntityInit.CORPSE_WARLOCK.get())
                .add(EntityInit.GULING_SENTINEL.get())
                .add(EntityInit.GULING_SENTINEL_HEAVY.get())
                .add(EntityInit.NAMELESS_GUARDIAN.get())
                .addTag(EMTagKey.IMMORTAL_ARMY);
        //免疫占有图腾
        tag(EMTagKey.RESISTS_TOTEM_OF_POSSESSION)
                .add(EntityInit.CORPSE_WARLOCK.get())
                .add(EntityInit.NAMELESS_GUARDIAN.get())
                .add(EntityInit.IMMORTAL_BOSS.get());
        //免疫辐射
        tag(EMTagKey.RESISTS_RADIATION)
                .add(EntityInit.CORPSE_WARLOCK.get())
                .add(EntityInit.NAMELESS_GUARDIAN.get())
                .add(EntityInit.IMMORTAL_BOSS.get());
    }
}
