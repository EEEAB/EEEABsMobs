package com.eeeab.eeeabsmobs.server.item;

import com.eeeab.eeeabsmobs.client.particle.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.server.entity.effect.projectile.EntityShamanBomb;
import com.eeeab.eeeabsmobs.server.util.TranslateUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemImmortalStaff extends Item {
    public ItemImmortalStaff() {
        super(new Item.Properties().stacksTo(1).fireResistant());
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeCharged) {
        if (entity instanceof Player player) {
            if (player.getCooldowns().isOnCooldown(this)) {
                return;
            }
            player.getCooldowns().addCooldown(this, 60);
        }
        InteractionHand hand = entity.getUsedItemHand();
        if (!level.isClientSide) {
            double yBodyRadians = Math.toRadians(entity.yHeadRot + (180 * (hand == InteractionHand.MAIN_HAND ? 1 : 2)));
            float width = entity.getBbWidth() * 0.6F;
            EntityShamanBomb shamanBomb = new EntityShamanBomb(entity.level(), entity, entity.getLookAngle().x, entity.getLookAngle().y, entity.getLookAngle().z);
            shamanBomb.setOwner(entity);
            shamanBomb.setIsPlayer(entity instanceof Player);
            shamanBomb.setDangerous(ItemImmortalStaff.isDangerBomb(entity));
            shamanBomb.absMoveTo(shamanBomb.getX() + width * Math.cos(yBodyRadians), entity.getY(0.55), shamanBomb.getZ() + width * Math.sin(yBodyRadians));
            level.addFreshEntity(shamanBomb);
        } else {
            ModParticleUtils.annularParticleOutburst(level, 5, ParticleTypes.SOUL_FIRE_FLAME, entity.getX(), entity.getY(), entity.getZ(), 0.18, 0.15, 360F, 0F, Mth.PI);
            ModParticleUtils.annularParticleOutburst(level, 5, ParticleTypes.LARGE_SMOKE, entity.getX(), entity.getY(), entity.getZ(), 0.16, 0.1, 360F, 0F, -Mth.PI);
        }
        entity.playSound(SoundEvents.BLAZE_SHOOT);
        entity.swing(hand, true);
    }

    @Override
    @NotNull
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        //ModConfigHandler.Item item = ModConfigHandler.COMMON.items;
        //if (TranslateUtils.SHOW_ITEM_CD) tooltip.add(TranslateUtils.itemCoolTime(item.immortalStaffConfig1.get()));
        if (TranslateUtils.SHOW_ITEM_CD) tooltip.add(TranslateUtils.itemCoolTime(3));
        tooltip.add(TranslateUtils.simpleItemText(this.getDescriptionId()));
    }

    /**
     * 根据使用者的幸运值判断是否发射强力弹丸
     *
     * @param caster 使用者
     * @return 是否发射强力弹丸
     */
    public static boolean isDangerBomb(LivingEntity caster) {
        float l0 = 0F;
        AttributeInstance luck = caster.getAttribute(Attributes.LUCK);
        if (luck != null) {
            l0 = (float) luck.getValue();
        }
        return caster.getRandom().nextFloat() < 0.25 + l0 * 0.15;
    }
}
