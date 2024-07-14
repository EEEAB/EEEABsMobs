package com.eeeab.eeeabsmobs.sever.entity.immortal;

import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class EntityImmortalKnight extends EntityAbsImmortalSkeleton implements IEntity {
    private static final UniformInt ALERT_INTERVAL = TimeUtil.rangeOfSeconds(3, 5);
    private static final UniformInt ROAR_INTERVAL = TimeUtil.rangeOfSeconds(15, 30);
    private int ticksUntilNextAlert;
    private int nextBoostTick;

    public EntityImmortalKnight(EntityType<? extends EntityImmortalKnight> type, Level level) {
        super(type, level);
        active = true;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.88f;
    }


    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, () -> roarAnimation, EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP)) {
            @Override
            public void tick() {
                super.tick();
                if (getAnimationTick() == 8) {
                    playSound(SoundInit.IMMORTAL_SKELETON_ROAR.get(), 0.8F, (entity.random.nextFloat() - entity.random.nextFloat()) * 0.2F + 1.0F);
                }
                LivingEntity target = getTarget();
                if (target != null) entity.getLookControl().setLookAt(target, 30F, 30F);
            }

            @Override
            public void stop() {
                entity.invigorateOthers();
                super.stop();
            }
        });
        super.registerCustomGoals();
    }

    @Override
    protected EMConfigHandler.AttributeConfig getAttributeConfig() {
        return EMConfigHandler.COMMON.MOB.IMMORTAL.IMMORTAL_KNIGHT.combatConfig;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource) {
        return false;
    }


    @Override
    public void tick() {
        super.tick();
        boolean flag = this.random.nextFloat() < 0.1F + this.level.getCurrentDifficultyAt(this.blockPosition()).getSpecialMultiplier();
        if (!this.level.isClientSide && !this.isNoAi() && this.isActive() && this.getTarget() != null && this.isNoAnimation() && this.tickCount % 60 == 0 && this.nextBoostTick == 0 && flag) {
            this.playAnimation(this.roarAnimation);
            this.nextBoostTick = ROAR_INTERVAL.sample(this.random);
        }
    }

    @Override
    protected void customServerAiStep() {
        if (getTarget() != null) {
            maybeAlertOthers();
            if (this.nextBoostTick > 0) {
                --this.nextBoostTick;
            }
        }
        super.customServerAiStep();
    }

    private void maybeAlertOthers() {
        if (this.ticksUntilNextAlert > 0) {
            --this.ticksUntilNextAlert;
        } else {
            LivingEntity target = this.getTarget();
            if (target != null && this.getSensing().hasLineOfSight(target)) {
                this.alertOthers();
            }

            this.ticksUntilNextAlert = ALERT_INTERVAL.sample(this.random);
        }
    }

    private void invigorateOthers() {
        double range = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        AABB aabb = AABB.unitCubeFromLowerCorner(this.position()).inflate(range, 5.0D, range);
        if (getTarget() != null) {
            this.level.getEntitiesOfClass(EntityAbsImmortalSkeleton.class, aabb, EntitySelector.NO_SPECTATORS).
                    stream().
                    filter(skeleton -> !(skeleton instanceof EntityImmortalKnight))
                    .forEach(skeleton -> skeleton.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 0), this
                    ));
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 10, 0), this);
        }
    }

    private void alertOthers() {
        double range = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        AABB aabb = AABB.unitCubeFromLowerCorner(this.position()).inflate(range, 5.0D, range);
        if (getTarget() != null) {
            this.level.getEntitiesOfClass(EntityImmortalSkeleton.class, aabb, EntitySelector.NO_SPECTATORS).
                    stream().
                    filter(immortalSkeleton -> immortalSkeleton.getTarget() == null && !isAlliedTo(getTarget())).
                    forEach(immortalSkeleton -> immortalSkeleton.setTarget(getTarget())
                    );
        }
    }

    public void setTarget(@Nullable LivingEntity entity) {
        if (this.getTarget() == null && entity != null) {
            this.ticksUntilNextAlert = ALERT_INTERVAL.sample(this.random);
        }

        if (entity instanceof Player) {
            this.setLastHurtByPlayer((Player) entity);
        }

        super.setTarget(entity);
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (!this.level.isClientSide) {
            if (getTarget() == null && source.getEntity() instanceof LivingEntity livingEntity
                    && livingEntity.getMobType() != getMobType()) {
                this.setLastHurtByMob(livingEntity);
            }
            if (ModEntityUtils.isProjectileSource(source)) {
                damage *= 0.5F;
            }
            return super.hurt(source, damage);
        }
        return false;
    }

    @Override
    protected int getCareerId(RandomSource random) {
        return CareerType.KNIGHT.id;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyIn) {
        this.setItemSlot(EquipmentSlot.MAINHAND, randomSource.nextFloat() < 0.2F ? CareerType.KNIGHT.holdItems[0].getDefaultInstance() : CareerType.KNIGHT.holdItems[1].getDefaultInstance());
        this.setDropChance(EquipmentSlot.MAINHAND, 0);
        if (randomSource.nextFloat() < 0.2F) {
            this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
            this.setDropChance(EquipmentSlot.OFFHAND, 0);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity, float damageMultiplier, float knockBackMultiplier,
                                boolean canDisableShield) {
        if (!super.doHurtTarget(entity, damageMultiplier, knockBackMultiplier, canDisableShield)) {
            return false;
        } else {
            if (entity instanceof LivingEntity targetEntity) {
                targetEntity.addEffect(new MobEffectInstance(EffectInit.ERODE_EFFECT.get(), 200), this);
            }
        }
        return true;
    }

    @Override
    protected AbstractArrow getArrow(ItemStack arrowStack, float distanceFactor) {
        AbstractArrow arrow = super.getArrow(arrowStack, distanceFactor);
        if (arrow instanceof Arrow) {
            ((Arrow) arrow).addEffect(new MobEffectInstance(EffectInit.ERODE_EFFECT.get(), 200));
        }
        return arrow;
    }
}
