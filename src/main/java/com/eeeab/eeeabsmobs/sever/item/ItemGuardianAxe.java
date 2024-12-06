package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.ability.AbilityHandler;
import com.eeeab.eeeabsmobs.sever.capability.AbilityCapability;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.util.EMTUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemGuardianAxe extends AxeItem implements ConfigurableItem {
    private Multimap<Attribute, AttributeModifier> defaultModifiers;
    private static final UUID GUARDIAN_BASE_KNOCKBACK_RESISTANCE_UUID = UUID.fromString("BFF48EEA-FF5B-45B6-88FC-3C8FBBAF78FA");

    public ItemGuardianAxe(Tier tier, Properties properties) {
        super(tier, (float) (-3D + EMConfigHandler.COMMON.ITEM.GUARDIAN_AXE_TOOL.attackDamageValue), (float) (-4D + EMConfigHandler.COMMON.ITEM.GUARDIAN_AXE_TOOL.attackSpeedValue), properties);
        this.defaultModifiers = this.creatAttributesFromConfig();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) EEEABMobs.PROXY.getISTERProperties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (Objects.equals(hand, InteractionHand.MAIN_HAND) && HitResult.Type.BLOCK == result.getType() && Direction.UP == result.getDirection()) {
            BlockPos blockPos = result.getBlockPos();
            player.swing(hand, true);
            AbilityCapability.IAbilityCapability capability = AbilityHandler.INSTANCE.getAbilityCapability(player);
            if (capability != null) {
                player.playSound(SoundEvents.GENERIC_EXPLODE, 1.5F, 1F + player.getRandom().nextFloat() * 0.1F);
                EntityCameraShake.cameraShake(level, player.position(), 8, 0.125F, 0, 20);
                if (level.isClientSide) {
                    double x, y, z;
                    if (player.position().distanceTo(blockPos.getCenter()) > 5) {
                        float f0 = (float) Math.toRadians(player.getYRot() + 90);
                        x = player.getX() + Mth.cos(f0) * 3.0D;
                        y = player.getY() + 0.1D;
                        z = player.getZ() + Mth.sin(f0) * 3.0D;
                    } else {
                        x = blockPos.getX();
                        y = blockPos.getY();
                        z = blockPos.getZ();
                    }
                    ModParticleUtils.roundParticleOutburst(level, 40, new ParticleOptions[]{ParticleInit.GUARDIAN_SPARK.get(), ParticleTypes.SOUL_FIRE_FLAME}, x, y, z, 0.25F);
                }
                AbilityHandler.INSTANCE.sendPlayerAbilityMessage(player, AbilityHandler.GUARDIAN_AXE_ABILITY_TYPE);
                player.getCooldowns().addCooldown(this, 100);
                return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide);
            }
        }
        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
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
        if (!InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340)) {
            tooltip.add(EMTUtils.UNABLE_BREAKS);
            tooltip.add(EMTUtils.simpleShiftDownText(null, EMTUtils.STYLE_GREEN));
        } else {
            tooltip.add(EMTUtils.itemCoolTime(5));
            tooltip.add(EMTUtils.simpleItemText(this.getDescriptionId(), EMTUtils.STYLE_GRAY));
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> creatAttributesFromConfig() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", EMConfigHandler.COMMON.ITEM.GUARDIAN_AXE_TOOL.attackDamageValue - 1D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", EMConfigHandler.COMMON.ITEM.GUARDIAN_AXE_TOOL.attackSpeedValue - 4D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(GUARDIAN_BASE_KNOCKBACK_RESISTANCE_UUID, "Weapon modifier", 0.1D, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    @Override
    public void refreshAttributesFromConfig() {
        this.defaultModifiers = this.creatAttributesFromConfig();
    }
}
