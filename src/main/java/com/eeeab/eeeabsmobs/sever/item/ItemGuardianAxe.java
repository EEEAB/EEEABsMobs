package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.sever.entity.impl.effect.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.impl.effect.EntityGuardianBlade;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class ItemGuardianAxe extends AxeItem {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;
    private static final UUID GUARDIAN_BASE_KNOCKBACK_RESISTANCE_UUID = UUID.fromString("BFF48EEA-FF5B-45B6-88FC-3C8FBBAF78FA");

    public ItemGuardianAxe(Tier tier, float attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", attackDamageModifier + tier.getAttackDamageBonus(), AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", attackSpeedModifier, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(GUARDIAN_BASE_KNOCKBACK_RESISTANCE_UUID, "Weapon modifier", 0.1D, AttributeModifier.Operation.ADDITION));
        defaultModifiers = builder.build();
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (livingEntity instanceof Player player) {
            ItemStack itemStack = player.getProjectile(stack);
            int i = this.getUseDuration(itemStack) - remainingUseDuration;
            if (i >= 20) {
                if (level.isClientSide) {
                    doWeaponEffect(level, player, Mth.clamp((int) Math.round(i * 0.1), 3, 6));
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int remainingUseDuration) {
        if (entity instanceof Player player) {
            ItemStack itemStack = player.getProjectile(stack);
            int i = this.getUseDuration(itemStack) - remainingUseDuration;
            if (i >= 20) {
                InteractionHand hand = player.getUsedItemHand();
                player.swing(hand, true);
                int round = (int) Math.round(i * 0.1);
                doSpawnBlade(player, Mth.clamp(round, 3, 6));
                player.getCooldowns().addCooldown(this, 100);
                player.playSound(SoundEvents.GENERIC_EXPLODE, 1.5F, 1F + player.getRandom().nextFloat() * 0.1F);
                EntityCameraShake.cameraShake(level, player.position(), 8, 0.2F + round * 0.01F, 0, 20);
            }
        }
    }

    private void doSpawnBlade(Player player, int spawnQuantity) {
        for (int i = 0; i < spawnQuantity; ++i) {
            float f1 = (float) (player.getYRot() + (float) i * (float) Math.PI * (2.0 / spawnQuantity));
            double x = player.getX() + Mth.cos(f1) * 1.5D;
            double y = player.getY();
            double z = player.getZ() + Mth.sin(f1) * 1.5D;
            EntityGuardianBlade blade = new EntityGuardianBlade(player.level(), player, x, y, z, f1);
            player.level().addFreshEntity(blade);
        }
    }

    private void doWeaponEffect(Level level, Player player, int spawnQuantity) {
        RandomSource random = player.getRandom();
        for (int i = 0; i < spawnQuantity; i++) {
            double x = player.getX() + random.nextGaussian();
            double y = player.getY() + 0.1F;
            double z = player.getZ() + random.nextGaussian();
            level.addParticle(ParticleTypes.GLOW, x, y, z, 0, 0.01, 0);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public boolean canBeDepleted() {
        return false;
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity hitEntity, LivingEntity livingEntity) {
        if (!hitEntity.level().isClientSide) {
            hitEntity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1F, 0.2F);
        }
        return super.hurtEnemy(itemStack, hitEntity, livingEntity);
    }

    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(Component.translatable("item.unable_depleted_tip").setStyle(ItemInit.TIPS_GRAY));
        if (!InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340)) {
            tooltip.add(Component.translatable("key_down.tip").setStyle(ItemInit.TIPS_GREEN));
        } else {
            tooltip.add(Component.translatable(getDescriptionId() + ".tip").setStyle(ItemInit.TIPS_GRAY));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }
}
