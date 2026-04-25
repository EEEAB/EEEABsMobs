package com.eeeab.eeeabsmobs.sever.item.eye;

import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityEyeOfStructure;
import com.eeeab.eeeabsmobs.sever.util.TranslateUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.Nullable;

import java.util.List;

//参考自: net.minecraft.world.item.EnderEyeItem
public abstract class ItemFindStructureEye extends Item {
    private static final int FIND_MAX_HEIGHT = 100;
    private final TagKey<Structure> FIND_STRUCTURE;

    protected ItemFindStructureEye(Properties properties, TagKey<Structure> tagKey) {
        super(properties);
        this.FIND_STRUCTURE = tagKey;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack eyeItem = player.getItemInHand(hand);
        player.startUsingItem(hand);
        if (level instanceof ServerLevel serverlevel) {
            BlockPos blockpos = serverlevel.findNearestMapStructure(FIND_STRUCTURE, player.blockPosition(), FIND_MAX_HEIGHT, false);
            if (blockpos != null) {
                boolean canConsumeItem = ModConfigHandler.COMMON.items.eyeOfStructureConfig1.get();
                EntityEyeOfStructure eye = new EntityEyeOfStructure(level, player.getX(), player.getY(0.5D), player.getZ(), canConsumeItem);
                eye.setItem(eyeItem);
                eye.signalTo(blockpos);
                level.gameEvent(GameEvent.PROJECTILE_SHOOT, eye.position(), GameEvent.Context.of(player));
                level.addFreshEntity(eye);
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.USED_ENDER_EYE.trigger((ServerPlayer) player, blockpos);
                }

                level.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                level.levelEvent((Player) null, 1003, player.blockPosition(), 0);
                if (canConsumeItem) {
                    if (!player.getAbilities().instabuild) eyeItem.shrink(1);
                } else {
                    player.getCooldowns().addCooldown(this, (int) (ModConfigHandler.COMMON.items.eyeOfStructureConfig2.get() * 20));
                }

                player.awardStat(Stats.ITEM_USED.get(this));
                player.swing(hand, true);
                return InteractionResultHolder.success(eyeItem);
            }
        }
        return InteractionResultHolder.consume(eyeItem);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        if (TranslateUtils.SHOW_ITEM_CD && !ModConfigHandler.COMMON.items.eyeOfStructureConfig1.get()) {
            tooltip.add(TranslateUtils.itemCoolTime(ModConfigHandler.COMMON.items.eyeOfStructureConfig2.get()));
        }
        tooltip.add(TranslateUtils.simpleText(TranslateUtils.ITEM_STRUCTURE_PREFIX, FIND_STRUCTURE.location().getPath(), ChatFormatting.GRAY));
    }

    public ParticleOptions getTrailParticle() {
        return ParticleTypes.PORTAL;
    }
}
