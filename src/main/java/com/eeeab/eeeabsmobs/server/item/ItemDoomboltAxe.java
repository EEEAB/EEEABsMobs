package com.eeeab.eeeabsmobs.server.item;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.server.entity.effect.EntityCameraShake;
import com.eeeab.eeeabsmobs.server.entity.effect.EntitySurge;
import com.eeeab.eeeabsmobs.server.entity.effect.projectile.EntityThrownDoomboltAxe;
import com.eeeab.eeeabsmobs.server.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.server.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.server.init.SoundInit;
import com.eeeab.eeeabsmobs.server.util.TranslateUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemDoomboltAxe extends AxeItem implements ConfigurableItem, SlidingDoorLockKey {
    private static final UUID BASE_ENTITY_REACH_UUID = UUID.fromString("C89158E7-CFEB-4C8E-9C64-B503374C6214");
    private Multimap<Attribute, AttributeModifier> defaultModifiers;

    public ItemDoomboltAxe(Tier tier, Properties properties) {
        super(tier, (float) (-3D + ModConfigHandler.COMMON.items.doomboltAxe.attackDamageValue), (float) (-4D + ModConfigHandler.COMMON.items.doomboltAxe.attackSpeedValue), properties);
        this.defaultModifiers = this.creatAttributesFromConfig();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) EEEABMobs.PROXY.getISTERProperties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.getDamageValue() >= itemStack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemStack);
        }
        if (player.isShiftKeyDown()) {
            BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            if (HitResult.Type.BLOCK == result.getType() && Direction.UP == result.getDirection()) {
                player.swing(hand, true);
                player.getCooldowns().addCooldown(this, (int) (ModConfigHandler.COMMON.items.doomboltAxeConfig.get() * 20));
                if (!level.isClientSide) {
                    Vec3 lookAngle = player.getLookAngle();
                    Vec3[] vec3s = new Vec3[]{lookAngle.yRot(0.5F), lookAngle, lookAngle.yRot(-0.5F)};
                    double reach = 2;
                    int baseCount = 5;
                    double spacing = 1.2D;
                    for (Vec3 vec3 : vec3s) {
                        for (int i = 0; i < baseCount; i++) {
                            double distance = spacing * (i + 1.5);
                            float f0 = (float) Mth.atan2(vec3.z, vec3.x);
                            double x = player.getX() + Mth.cos(f0) * distance;
                            double y = player.getY();
                            double z = player.getZ() + Mth.sin(f0) * distance;
                            Vec3 point = ModEntityUtils.checkSummonEntityPointNullable(player.level(), x, z, y - reach, y + reach);
                            if (point == null) point = new Vec3(x, y, z);
                            EntitySurge surge = new EntitySurge(player.level(), point.x, point.y, point.z, f0, i * 4, itemStack.copy(), player);
                            player.level().addFreshEntity(surge);
                        }
                    }
                }
                itemStack.hurtAndBreak(1, player, (event) -> event.broadcastBreakEvent(hand));
                player.playSound(SoundEvents.GENERIC_EXPLODE, 1F, 1F + player.getRandom().nextFloat() * 0.1F);
                EntityCameraShake.cameraShake(level, player.position(), 6, 0.15F, 3, 12);
                return InteractionResultHolder.consume(player.getItemInHand(hand));
            }
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            int i = this.getUseDuration(stack) - timeLeft;
            if (i >= 10) {
                if (!level.isClientSide) {
                    if (stack.getDamageValue() >= stack.getMaxDamage() - 1) return;
                    stack.hurtAndBreak(1, player, (event) -> event.broadcastBreakEvent(entity.getUsedItemHand()));
                    EntityThrownDoomboltAxe doomboltAxe = new EntityThrownDoomboltAxe(level, player, stack);
                    doomboltAxe.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 4F, 1.0F);
                    if (player.getAbilities().instabuild) {
                        doomboltAxe.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }
                    level.addFreshEntity(doomboltAxe);
                    level.playSound(null, doomboltAxe, SoundInit.DOOMBOLTAXE_THROW.get(), SoundSource.PLAYERS, 1.0F, 0.7F);
                    if (!player.getAbilities().instabuild) {
                        player.getInventory().removeItem(stack);
                    }
                    EntityCameraShake.cameraShake(level, player.position(), 4, 0.15F, 2, 8);
                }
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity hitEntity, LivingEntity livingEntity) {
        if (!hitEntity.level().isClientSide) {
            hitEntity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1F, 0.2F);
        }
        return super.hurtEnemy(itemStack, hitEntity, livingEntity);
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        if (TranslateUtils.SHOW_ITEM_CD) tooltip.add(TranslateUtils.itemCoolTime(ModConfigHandler.COMMON.items.doomboltAxeConfig.get()));
        tooltip.addAll(TranslateUtils.complexText(TranslateUtils.ITEM_PREFIX, 2, ChatFormatting.GRAY, this.getDescriptionId()));
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> creatAttributesFromConfig() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", ModConfigHandler.COMMON.items.doomboltAxe.attackDamageValue - 1D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", ModConfigHandler.COMMON.items.doomboltAxe.attackSpeedValue - 4D, AttributeModifier.Operation.ADDITION));
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(BASE_ENTITY_REACH_UUID, "Weapon modifier", 1D, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    @Override
    public void refreshAttributesFromConfig() {
        this.defaultModifiers = this.creatAttributesFromConfig();
    }
}
