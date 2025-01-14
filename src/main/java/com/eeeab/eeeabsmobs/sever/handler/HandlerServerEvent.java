package com.eeeab.eeeabsmobs.sever.handler;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.ability.Ability;
import com.eeeab.eeeabsmobs.sever.ability.AbilityHandler;
import com.eeeab.eeeabsmobs.sever.capability.AbilityCapability;
import com.eeeab.eeeabsmobs.sever.capability.FrenzyCapability;
import com.eeeab.eeeabsmobs.sever.capability.PlayerCapability;
import com.eeeab.eeeabsmobs.sever.capability.VertigoCapability;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobEntity;
import com.eeeab.eeeabsmobs.sever.entity.IBoss;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityAbsCorpse;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpseWarlock;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityAbsImmortal;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortalExecutioner;
import com.eeeab.eeeabsmobs.sever.entity.projectile.EntityBloodBall;
import com.eeeab.eeeabsmobs.sever.entity.projectile.EntityShamanBomb;
import com.eeeab.eeeabsmobs.sever.init.AttributeInit;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.item.ItemDemolisher;
import com.eeeab.eeeabsmobs.sever.message.MessageFrenzyEffect;
import com.eeeab.eeeabsmobs.sever.message.MessageVertigoEffect;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

//服务端事件处理器
public final class HandlerServerEvent {

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(new ResourceLocation(EEEABMobs.MOD_ID, "vertigo_processor"), new VertigoCapability.VertigoCapabilityProvider());
            event.addCapability(new ResourceLocation(EEEABMobs.MOD_ID, "frenzy_processor"), new FrenzyCapability.FrenzyCapabilityProvider());
        }
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(EEEABMobs.MOD_ID, "ability_processor"), new AbilityCapability.AbilityCapabilityProvider());
            event.addCapability(new ResourceLocation(EEEABMobs.MOD_ID, "player_processor"), new PlayerCapability.PlayerCapabilityProvider());
        }
    }

    //实体加载到世界时
    @SubscribeEvent
    public void onJoinWorld(final EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            AbilityCapability.IAbilityCapability capability = AbilityHandler.INSTANCE.getAbilityCapability((LivingEntity) entity);
            if (capability != null) capability.onInit((LivingEntity) entity);
        }

        if (event.getLevel().isClientSide) {
            return;
        }

        if (entity instanceof AbstractGolem abstractGolem && !(abstractGolem instanceof Shulker)) {
            abstractGolem.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(abstractGolem, EntityAbsImmortal.class, 5, false, false, null));
            abstractGolem.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(abstractGolem, EntityAbsCorpse.class, 5, false, false, null));
        }
        if (entity instanceof AbstractSkeleton abstractSkeleton) {
            abstractSkeleton.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(abstractSkeleton, EntityAbsImmortal.class, 0, true, false, null));
        }
        if (entity instanceof Zombie zombie && !(zombie instanceof ZombifiedPiglin)) {
            zombie.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(zombie, EntityAbsImmortal.class, 0, true, false, null));
            zombie.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(zombie, EntityAbsCorpse.class, 0, true, false, null));
        }
        if (entity instanceof AbstractVillager villager) {
            villager.goalSelector.addGoal(3, new AvoidEntityGoal<>(villager, EntityAbsImmortal.class, 6.0F, 1.0D, 1.2D));
            villager.goalSelector.addGoal(3, new AvoidEntityGoal<>(villager, EntityAbsCorpse.class, 6.0F, 1.0D, 1.2D));
        }
        if (entity instanceof Raider raider && !(raider instanceof SpellcasterIllager)) {
            raider.goalSelector.addGoal(5, new NearestAttackableTargetGoal<>(raider, EntityAbsImmortal.class, 5, true, false, null));
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

            FrenzyCapability.IFrenzyCapability frenzyCapability = HandlerCapability.getCapability(entity, HandlerCapability.FRENZY_EFFECT_CAPABILITY);
            if (frenzyCapability != null) {
                frenzyCapability.tick(entity);
            }
        }
    }

    //玩家游戏刻
    @SubscribeEvent
    public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START || event.player == null) {
            return;
        }
        useGuardianCoreStack(event);

        PlayerCapability.IPlayerCapability playerCapability = HandlerCapability.getCapability(event.player, HandlerCapability.PLAYER_CAPABILITY);
        if (playerCapability != null) {
            playerCapability.tick(event.player);
        }
    }

    /**
     * 守卫者核心的耐久减少或恢复
     */
    private void useGuardianCoreStack(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Ability<?> ability = AbilityHandler.INSTANCE.getAbility(player, AbilityHandler.GUARDIAN_LASER_ABILITY_TYPE);
        ItemStack itemStack = player.getUseItem();
        if (ability != null) {
            if (!ability.isUsing()) {
                for (ItemStack stack : player.getInventory().items) {
                    if (stack.is(ItemInit.GUARDIAN_CORE.get()))
                        stack.setDamageValue(Math.max(stack.getDamageValue() - 1, 0));
                }
                for (ItemStack stack : player.getInventory().offhand) {
                    if (stack.is(ItemInit.GUARDIAN_CORE.get()))
                        stack.setDamageValue(Math.max(stack.getDamageValue() - 1, 0));
                }
            } else if (itemStack.is(ItemInit.GUARDIAN_CORE.get())) {
                if (itemStack.getDamageValue() + 1 < itemStack.getMaxDamage()) {
                    itemStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(player.getUsedItemHand()));
                } else {
                    ability.end();
                }
            } else {
                ability.end();
            }
        }
    }

    //当实体生命值≤0时触发
    @SubscribeEvent
    public void onLivingDeathEvent(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (event.isCancelable() && entity instanceof EEEABMobEntity && entity.getHealth() > 0) {
            event.setCanceled(true);
        }
    }

    //新效果或者已存在但是等级更高或时间更长的效果 添加到实体触发
    @SubscribeEvent
    public void onAddPointEffect(MobEffectEvent.Added event) {
        MobEffectInstance effectInstance = event.getEffectInstance();
        if (!event.getEntity().level().isClientSide()) {
            doCRCapabilityWithEffect(effectInstance, event.getEntity(), true);
        }
    }

    //效果被移除时
    @SubscribeEvent
    public void onRemovePotionEffect(MobEffectEvent.Remove event) {
        MobEffectInstance effectInstance = event.getEffectInstance();
        if (!event.getEntity().level().isClientSide() && effectInstance != null) {
            doCRCapabilityWithEffect(effectInstance, event.getEntity(), false);
        }
    }

    //药水失效时
    @SubscribeEvent
    public void onExpirePotionEffect(MobEffectEvent.Expired event) {
        MobEffectInstance effectInstance = event.getEffectInstance();
        if (!event.getEntity().level().isClientSide() && effectInstance != null) {
            doCRCapabilityWithEffect(effectInstance, event.getEntity(), false);
        }
    }

    private void doCRCapabilityWithEffect(MobEffectInstance effectInstance, LivingEntity entity, boolean flag) {
        if (effectInstance.getEffect() == EffectInit.VERTIGO_EFFECT.get()) {
            EEEABMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MessageVertigoEffect(entity, flag));
            VertigoCapability.IVertigoCapability capability = HandlerCapability.getCapability(entity, HandlerCapability.MOVING_CONTROLLER_CAPABILITY);
            if (capability != null) {
                if (flag)
                    capability.onStart(entity);
                else
                    capability.onEnd(entity);
            }
        }
        if (effectInstance.getEffect() == EffectInit.FRENZY_EFFECT.get()) {
            EEEABMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MessageFrenzyEffect(entity, flag));
            FrenzyCapability.IFrenzyCapability capability = HandlerCapability.getCapability(entity, HandlerCapability.FRENZY_EFFECT_CAPABILITY);
            if (capability != null) {
                capability.setLevel(effectInstance.getAmplifier());
                if (flag)
                    capability.onStart(entity);
                else
                    capability.onEnd(entity);
            }
        }
    }

    //实体跳起来时
    @SubscribeEvent
    public void onLivingJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() != null) {
            LivingEntity entity = event.getEntity();
            if (entity.hasEffect(EffectInit.VERTIGO_EFFECT.get())) {
                entity.setDeltaMovement(entity.getDeltaMovement().multiply(1, 0, 1));
            }
        }
    }

    //玩家攻击实体时
    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        boolean cancelable = event.isCancelable();
        if (cancelable && event.getEntity().hasEffect(EffectInit.VERTIGO_EFFECT.get())) {
            event.setCanceled(true);
            return;
        }

        ItemStack itemStack = event.getEntity().getMainHandItem();
        if (cancelable && itemStack.getItem() instanceof ItemDemolisher && ItemDemolisher.getWeaponState(itemStack) == 1) {
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
        }
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
        LivingEntity entity = event.getEntity();
        if (event.isCancelable() && entity instanceof IBoss) {
            event.setStrength(0F);
            event.setCanceled(true);
        }
    }

    //挂载实体时
    @SubscribeEvent
    public void onEntityMountEntity(EntityMountEvent event) {
        Entity entity = event.getEntityMounting();
        if (event.isCancelable() && entity instanceof IBoss) {
            event.setCanceled(true);
        }
    }

    //实体受伤时
    @SubscribeEvent
    public void onLivingEntityHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        Entity directEntity = source.getDirectEntity();
        LivingEntity hurtEntity = event.getEntity();

        if (directEntity instanceof EntityShamanBomb shamanBomb) {
            if (shamanBomb.reboundFlag && !shamanBomb.isPlayer()) {
                hurtEntity.addEffect(new MobEffectInstance(EffectInit.VERTIGO_EFFECT.get(), 100, 0, false, false));
            }
        }

        FrenzyCapability.IFrenzyCapability frenzyCapability = HandlerCapability.getCapability(hurtEntity, HandlerCapability.FRENZY_EFFECT_CAPABILITY);
        if (frenzyCapability != null && frenzyCapability.isFrenzy() && !source.is(EMTagKey.GENERAL_UNRESISTANT_TO) && !source.is(DamageTypeTags.BYPASSES_ARMOR)) {
            float damage = event.getAmount();
            if (hurtEntity.getHealth() > 1F) {
                //至多减少50%伤害
                damage -= damage * (Math.min((frenzyCapability.getLevel() + 1F), 5)) * 0.1F;
            }
            event.setAmount(damage);
        }

        AttributeInstance attribute = hurtEntity.getAttribute(AttributeInit.CRIT_CHANCE.get());
        Entity attacker = source.getEntity();
        if (attribute != null && attacker != null) {
            double chance = attribute.getValue() - 1D;
            if (chance > 0 && hurtEntity.getRandom().nextFloat() <= chance) {
                float damage = event.getAmount();
                if (source.is(EMTagKey.CAN_CRIT_HEAL) && attacker instanceof LivingEntity mob) {
                    float maxHealth = mob.getMaxHealth();
                    mob.heal(Math.min(damage * 0.3F + maxHealth * 0.01F, maxHealth * 0.1F));
                }
                damage *= 1.5F;
                event.setAmount(damage);
                hurtEntity.level().playSound(null, hurtEntity.getX(), hurtEntity.getY(), hurtEntity.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, hurtEntity.getSoundSource(), 1.0F, 1.0F);
                if (hurtEntity.level() instanceof ServerLevel serverLevel) serverLevel.getChunkSource().broadcastAndSend(attacker, new ClientboundAnimatePacket(hurtEntity, 4));
            }
        }

        if (hurtEntity instanceof Player player) {
            PlayerCapability.IPlayerCapability playerCapability = HandlerCapability.getCapability(player, HandlerCapability.PLAYER_CAPABILITY);
            if (playerCapability != null) {
                playerCapability.hurt(player, source, event.getAmount());
            }
        }
    }

    //实体治疗时
    @SubscribeEvent
    public void onLivingEntityHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        MobEffectInstance instance = entity.getEffect(EffectInit.ERODE_EFFECT.get());
        if (instance != null) {
            float newAmount = Mth.clamp(1F - (instance.getAmplifier() + 1) * 0.1F, 0F, 1F);
            event.setAmount(event.getAmount() * newAmount);
        }
    }

    //实体呼吸事件
    @SubscribeEvent
    public void onLivingEntityBreathe(LivingBreatheEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.hasEffect(EffectInit.FRENZY_EFFECT.get())) {
            event.setCanBreathe(true);
        }
    }

    //实体设置目标时
    @SubscribeEvent
    public void onLivingEntityChangeTarget(LivingChangeTargetEvent event) {
        //LivingEntity entity = event.getEntity();
        //VertigoCapability.IVertigoCapability capability = HandlerCapability.getCapability(entity, HandlerCapability.MOVING_CONTROLLER_CAPABILITY);
        //if (capability != null) {
        //    if (event.isCancelable() && entity instanceof Mob && capability.isVertigo()) {
        //        if (event.getOriginalTarget() != null || entity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
        //            event.setCanceled(true);
        //        }
        //    }
        //}
    }

    //玩家攻击产生暴击时
    @SubscribeEvent
    public void onCriticalHit(CriticalHitEvent event) {

    }

    //当弹射物触及到物体时
    @SubscribeEvent
    public void onProjectileImpact(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        if (event.getRayTraceResult() instanceof EntityHitResult hitResult) {
            if (projectile instanceof EntityBloodBall bloodBall && !bloodBall.isHeal() && hitResult.getEntity() instanceof EntityCorpseWarlock) {
                event.setImpactResult(ProjectileImpactEvent.ImpactResult.SKIP_ENTITY);
                return;
            }
            if (projectile instanceof AbstractArrow arrow) {
                if (arrow.getPierceLevel() == 0 && !arrow.fireImmune() && !arrow.isOnFire() && hitResult.getEntity() instanceof EntityImmortalExecutioner) {
                    arrow.setSecondsOnFire(100);
                }
                if (arrow.getOwner() instanceof EntityAbsImmortal && hitResult.getEntity() instanceof EntityAbsImmortal && EMConfigHandler.COMMON.OTHER.enableSameMobsTypeInjury.get()) {
                    event.setImpactResult(ProjectileImpactEvent.ImpactResult.SKIP_ENTITY);
                }
            }
        }
    }
}
