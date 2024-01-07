package com.eeeab.eeeabsmobs.sever.handler;


import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.ability.AbilityHandler;
import com.eeeab.eeeabsmobs.sever.capability.AbilityCapability;
import com.eeeab.eeeabsmobs.sever.capability.VertigoCapability;
import com.eeeab.eeeabsmobs.sever.entity.impl.immortal.EntityImmortal;
import com.eeeab.eeeabsmobs.sever.entity.impl.namelessguardian.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.item.util.EEArmorMaterial;
import com.eeeab.eeeabsmobs.sever.item.util.EEArmorUtil;
import com.eeeab.eeeabsmobs.sever.message.MessageVertigoEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

//服务端事件处理器
public final class HandlerServerEvent {

    //实体加载到世界时
    @SubscribeEvent
    public void onJoinWorld(final EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            AbilityCapability.IAbilityCapability capability = AbilityHandler.INSTANCE.getAbilityCapability((LivingEntity) entity);
            if (capability != null) capability.onInit((LivingEntity) entity);
        }

        if (entity instanceof AbstractGolem abstractGolem && !(abstractGolem instanceof Shulker)) {
            abstractGolem.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(abstractGolem, EntityImmortal.class, 5, false, false, null));
        }
        if (entity instanceof AbstractSkeleton abstractSkeleton) {
            abstractSkeleton.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(abstractSkeleton, EntityImmortal.class, 0, true, false, null));
        }
        if (entity instanceof Zombie zombie && !(zombie instanceof ZombifiedPiglin)) {
            zombie.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(zombie, EntityImmortal.class, 0, true, false, null));
        }
        if (entity instanceof AbstractVillager villager) {
            villager.goalSelector.addGoal(3, new AvoidEntityGoal<>(villager, EntityImmortal.class, 6.0F, 1.0D, 1.2D));
        }
        if (entity instanceof Raider raider && !(raider instanceof SpellcasterIllager)) {
            raider.goalSelector.addGoal(5, new NearestAttackableTargetGoal<>(raider, EntityImmortal.class, 5, true, false, null));
        }
    }

    //实体游戏刻
    @SubscribeEvent
    public void onLivingEntityTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() != null) {
            LivingEntity entity = event.getEntity();

            VertigoCapability.IVertigoCapability vertigoCapability = HandlerCapability.getCapability(entity, HandlerCapability.MOVING_CONTROLLER_CAPABILITY);
            if (vertigoCapability != null) {
                vertigoCapability.tick(entity);
            }

            AbilityCapability.IAbilityCapability abilityCapability = HandlerCapability.getCapability(entity, HandlerCapability.CUSTOM_ABILITY_CAPABILITY);
            if (abilityCapability != null) {
                abilityCapability.tick(entity);
            }
        }

    }

    @SubscribeEvent
    public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START || event.player == null) {
        }
    }

    //新效果或者已存在但是等级更高或时间更长的效果 添加到实体触发
    @SubscribeEvent
    public void onAddPointEffect(MobEffectEvent.Added event) {
        if (event.getEffectInstance().getEffect() == EffectInit.VERTIGO_EFFECT.get()) {
            if (!event.getEntity().level().isClientSide) {
                EEEABMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageVertigoEffect(event.getEntity(), true));
                VertigoCapability.IVertigoCapability capability = HandlerCapability.getCapability(event.getEntity(), HandlerCapability.MOVING_CONTROLLER_CAPABILITY);
                if (capability != null) {
                    capability.onStart(event.getEntity());
                }
            }
        }
    }

    //效果被移除时
    @SubscribeEvent
    public void onRemovePotionEffect(MobEffectEvent.Remove event) {
        if (!event.getEntity().level().isClientSide() && event.getEffect() == EffectInit.VERTIGO_EFFECT.get()) {
            EEEABMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageVertigoEffect(event.getEntity(), false));
            VertigoCapability.IVertigoCapability capability = HandlerCapability.getCapability(event.getEntity(), HandlerCapability.MOVING_CONTROLLER_CAPABILITY);
            if (capability != null) {
                capability.onEnd(event.getEntity());
            }
        }
    }

    //药水失效时
    @SubscribeEvent
    public void onExpirePotionEffect(MobEffectEvent.Expired event) {
        MobEffectInstance effectInstance = event.getEffectInstance();
        if (!event.getEntity().level().isClientSide() && effectInstance != null) {
            if (effectInstance.getEffect() == EffectInit.VERTIGO_EFFECT.get()) {
                EEEABMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(event::getEntity), new MessageVertigoEffect(event.getEntity(), false));
                VertigoCapability.IVertigoCapability capability = HandlerCapability.getCapability(event.getEntity(), HandlerCapability.MOVING_CONTROLLER_CAPABILITY);
                if (capability != null) {
                    capability.onEnd(event.getEntity());
                }
            }
        }
    }

    //实体跳起来时
    @SubscribeEvent
    public void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() != null) {
            LivingEntity entity = event.getEntity();
            if (entity.hasEffect(EffectInit.VERTIGO_EFFECT.get()) && entity.onGround()) {
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(1, 0, 1));
            }
        }
    }

    //玩家攻击实体时
    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        if (event.isCancelable() && event.getEntity().hasEffect(EffectInit.VERTIGO_EFFECT.get())) {
            event.setCanceled(true);
        }
    }

    //玩家右键物品时
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickItem event) {
        if (event.isCancelable() && event.getEntity().hasEffect(EffectInit.VERTIGO_EFFECT.get())) {
            event.setCanceled(true);
        }
    }

    //玩家右键方块时
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCancelable() && event.getEntity().hasEffect(EffectInit.VERTIGO_EFFECT.get())) {
            event.setCanceled(true);
        }
    }

    //玩家右键实体时
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.isCancelable() && event.getEntity().hasEffect(EffectInit.VERTIGO_EFFECT.get())) {
            event.setCanceled(true);
        }
    }

    //玩家右键空白区域时
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickEmpty event) {
        Player entity = event.getEntity();
        if (event.isCancelable() && entity.hasEffect(EffectInit.VERTIGO_EFFECT.get())) {
            event.setCanceled(true);
            return;
        }

        //AbilityCapability.IAbilityCapability capability = AbilityHandler.INSTANCE.getAbilityCapability(entity);
        //if (capability != null && event.isCancelable() && capability.checkConflicting()) {
        //    event.setCanceled(true);
        //    return;
        //}
        //
        //if (event.getLevel().isClientSide && entity.getInventory().getSelected().isEmpty() && entity.hasEffect(EffectInit.ERODE_EFFECT.get())) {
        //    if (entity.isShiftKeyDown()) {
        //        AbilityHandler.INSTANCE.sendPlayerAbilityMessage(entity, AbilityHandler.GUARDIAN_LASER_ABILITY_TYPE);
        //    }
        //}
    }

    //玩家将要打破方块时
    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        if (event.isCancelable() && event.getPlayer().hasEffect(EffectInit.VERTIGO_EFFECT.get())) {
            event.setCanceled(true);
        }
    }

    //玩家放置方块前时
    @SubscribeEvent
    public void onPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity living) {
            if (event.isCancelable() && living.hasEffect(EffectInit.VERTIGO_EFFECT.get())) {
                event.setCanceled(true);
            }
        }
    }

    //实体使用物品时
    @SubscribeEvent
    public void onUseItem(LivingEntityUseItemEvent event) {
        LivingEntity living = event.getEntity();
        if (event.isCancelable() && living.hasEffect(EffectInit.VERTIGO_EFFECT.get())) {
            event.setCanceled(true);
        }
    }

    //实体被击退
    @SubscribeEvent
    public void onLivingEntityKnockBack(LivingKnockBackEvent event) {
        LivingEntity living = event.getEntity();
        if (event.isCancelable() && living instanceof EntityNamelessGuardian) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLivingEntityHurt(LivingHurtEvent event) {
        LivingEntity hurtEntity = event.getEntity();
        Entity attacker = event.getSource().getEntity();
        if (hurtEntity instanceof Player player && attacker instanceof EntityImmortal) {
            if (EEArmorUtil.checkFullSuitOfArmor(EEArmorMaterial.IMMORTAL_MATERIAL, player)) {
                float damage = event.getAmount();
                damage -= damage * 0.1F;//减少10%伤害
                event.setAmount(damage);
            }
        }
    }

    //实体设置目标时
    @SubscribeEvent
    public void onLivingEntityChangeTarget(LivingChangeTargetEvent event) {
        LivingEntity entity = event.getEntity();
        VertigoCapability.IVertigoCapability capability = HandlerCapability.getCapability(entity, HandlerCapability.MOVING_CONTROLLER_CAPABILITY);
        if (capability != null) {
            if (event.isCancelable() && entity instanceof Mob && capability.isVertigo()) {
                if (event.getOriginalTarget() != null || entity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
