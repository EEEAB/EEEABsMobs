package com.eeeab.eeeabsmobs.server.item;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.particle.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.server.entity.effect.EntityCameraShake;
import com.eeeab.eeeabsmobs.server.entity.effect.EntityGuardianBlade;
import com.eeeab.eeeabsmobs.server.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.server.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.server.init.SoundInit;
import com.eeeab.eeeabsmobs.server.util.TranslateUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
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
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.SweepingEdgeEnchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ToolAction;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemGuardianAxe extends AxeItem implements ConfigurableItem, IUnbreakableItem {
    private Multimap<Attribute, AttributeModifier> defaultModifiers;
    private static final UUID GUARDIAN_BASE_KNOCKBACK_RESISTANCE_UUID = UUID.fromString("BFF48EEA-FF5B-45B6-88FC-3C8FBBAF78FA");

    public ItemGuardianAxe(Tier tier, Properties properties) {
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
        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (Objects.equals(hand, InteractionHand.MAIN_HAND) && HitResult.Type.BLOCK == result.getType() && Direction.UP == result.getDirection()) {
            player.swing(hand, true);
            player.playSound(SoundEvents.GENERIC_EXPLODE, 1.5F, 1F + player.getRandom().nextFloat() * 0.1F);
            EntityCameraShake.cameraShake(level, player.position(), 8, 0.125F, 0, 20);
            double x, y, z;
            BlockPos blockPos = result.getBlockPos();
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
            ModParticleUtils.roundParticleOutburst(player.level(), 40, new ParticleOptions[]{ParticleTypes.SOUL_FIRE_FLAME}, x, y, z, 0.3F);
            if (!level.isClientSide) {
                Vec3 lookAngle = player.getLookAngle();
                Vec3[] vec3s = new Vec3[]{lookAngle.yRot(0.5F), lookAngle, lookAngle.yRot(-0.5F)};
                Vec3 point = ModEntityUtils.checkSummonEntityPoint(player, player.getX(), player.getZ(), y - 5, y);
                for (Vec3 vec3 : vec3s) {
                    float f0 = (float) Mth.atan2(vec3.z, vec3.x);
                    float f1 = 1F + player.getBbWidth();
                    x = point.x + Mth.cos(f0) * f1;
                    y = point.y;
                    z = point.z + Mth.sin(f0) * f1;
                    EntityGuardianBlade blade = new EntityGuardianBlade(player.level(), player, x, y, z, f0, false);
                    player.level().addFreshEntity(blade);
                }
            }
            player.getCooldowns().addCooldown(this, (int) (ModConfigHandler.COMMON.items.doomboltAxeConfig.get() * 20));
            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide);
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
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity hitEntity, LivingEntity livingEntity) {
        if (!hitEntity.level().isClientSide) {
            hitEntity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1F, 0.2F);
        }
        return super.hurtEnemy(itemStack, hitEntity, livingEntity);
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        if (TranslateUtils.SHOW_ITEM_CD) tooltip.add(TranslateUtils.itemCoolTime(ModConfigHandler.COMMON.items.doomboltAxeConfig.get()));
        //int i = (int) (SweepingEdgeEnchantment.getSweepingDamageRatio(ModConfigHandler.COMMON.items.judgmentAxeConfig1.get()) * 100);
        int i = (int) (SweepingEdgeEnchantment.getSweepingDamageRatio(1) * 100);
        tooltip.addAll(TranslateUtils.complexText(TranslateUtils.ITEM_PREFIX, 2, ChatFormatting.GRAY, this.getDescriptionId(), Component.literal(i > 0 ? i + "%" : "1.0").withStyle(ChatFormatting.YELLOW)));
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        if (Enchantments.SWEEPING_EDGE.equals(enchantment)) {
            //return ModConfigHandler.COMMON.items.judgmentAxeConfig1.get();
            return 1;
        }
        return super.getEnchantmentLevel(stack, enchantment);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return net.minecraftforge.common.ToolActions.SWORD_SWEEP == toolAction || super.canPerformAction(stack, toolAction);
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
        builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(GUARDIAN_BASE_KNOCKBACK_RESISTANCE_UUID, "Weapon modifier", 0.1D, AttributeModifier.Operation.ADDITION));
        return builder.build();
    }

    @Override
    public void refreshAttributesFromConfig() {
        this.defaultModifiers = this.creatAttributesFromConfig();
    }

    @Override
    public boolean canBreakItem() {
        return false;
    }
}
