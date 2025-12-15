package com.eeeab.eeeabsmobs.sever.world.datagen.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.util.ModTagKey;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
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
        tag(ModTagKey.IMMORTAL_IGNORE_HUNT_TARGETS)
                .add(EntityType.ENDER_DRAGON)
                .add(EntityType.ARMOR_STAND)
                .add(EntityType.WOLF)
                .addTag(ModTagKey.IMMORTAL_ARMY);
        //陷阱白名单
        tag(ModTagKey.TRAP_WHITELIST)
                .add(EntityInit.MAGIC_GOLEM.get())
                .add(EntityInit.RELIC_OBSERVER.get())
                .add(EntityInit.RELIC_RIPPER.get())
                .add(EntityInit.RELIC_EARTHSHAKER.get())
                .add(EntityInit.RELIC_ANNIHILATOR.get())
                .add(EntityInit.NAMELESS_GUARDIAN.get())
                .add(EntityInit.IMMORTAL_BOSS.get());
        //免疫强制切换目标
        tag(ModTagKey.RESISTS_FORCED_CHANGE_TARGET)
                .add(EntityInit.CORPSE_WARLOCK.get())
                .add(EntityInit.RELIC_ANNIHILATOR.get())
                .add(EntityInit.NAMELESS_GUARDIAN.get())
                .add(EntityInit.IMMORTAL_BOSS.get());
        //不朽军团
        tag(ModTagKey.IMMORTAL_ARMY)
                .add(EntityInit.MAGIC_GOLEM.get())
                .add(EntityInit.IMMORTAL_SKELETON.get())
                .add(EntityInit.IMMORTAL_KNIGHT.get())
                .add(EntityInit.IMMORTAL_SHAMAN.get())
                .add(EntityInit.IMMORTAL_EXECUTIONER.get())
                .add(EntityInit.IMMORTAL_BOSS.get());
        tag(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)
                .add(EntityInit.NAMELESS_GUARDIAN.get())
                .add(EntityInit.CORPSE_WARLOCK.get())
                .addTag(ModTagKey.IMMORTAL_ARMY)
                .remove(EntityInit.IMMORTAL_EXECUTIONER.get());
        //Ice And Fire 免疫蛇发女妖石化
        tag(ModTagKey.BLINDED)
                .add(EntityInit.NAMELESS_GUARDIAN.get())
                .add(EntityInit.IMMORTAL_BOSS.get());
        tag(ModTagKey.IMMUNE_TO_GORGON_STONE)
                .add(EntityInit.NAMELESS_GUARDIAN.get())
                .add(EntityInit.IMMORTAL_BOSS.get());
        //Alex's Caves 免疫撼地龙咆哮
        tag(ModTagKey.RESISTS_TREMORSAURUS_ROAR)
                .add(EntityInit.CORPSE.get())
                .add(EntityInit.CORPSE_VILLAGER.get())
                .add(EntityInit.CORPSE_TO_PLAYER.get())
                .add(EntityInit.CORPSE_WARLOCK.get())
                .add(EntityInit.RELIC_OBSERVER.get())
                .add(EntityInit.RELIC_RIPPER.get())
                .add(EntityInit.RELIC_EARTHSHAKER.get())
                .add(EntityInit.RELIC_ANNIHILATOR.get())
                .add(EntityInit.NAMELESS_GUARDIAN.get())
                .addTag(ModTagKey.IMMORTAL_ARMY);
        //免疫占有图腾
        tag(ModTagKey.RESISTS_TOTEM_OF_POSSESSION)
                .add(EntityInit.CORPSE_WARLOCK.get())
                .add(EntityInit.NAMELESS_GUARDIAN.get())
                .add(EntityInit.IMMORTAL_BOSS.get());
        //免疫辐射
        tag(ModTagKey.RESISTS_RADIATION)
                .add(EntityInit.CORPSE_WARLOCK.get())
                .add(EntityInit.NAMELESS_GUARDIAN.get())
                .add(EntityInit.IMMORTAL_BOSS.get());
    }
}
