package com.eeeab.eeeabsmobs.sever.entity.immortal.skeleton;

import com.eeeab.animate.server.ai.AnimationMeleeAI;
import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleOrb;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleRing;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
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
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class EntityImmortalKnight extends EntityAbsImmortalSkeleton implements RangedAttackMob {
    private static final UniformInt ALERT_INTERVAL = TimeUtil.rangeOfSeconds(3, 5);
    private static final UniformInt INVIGORATE_INTERVAL = TimeUtil.rangeOfSeconds(15, 30);
    private int ticksUntilNextAlert;
    private int nextBoostTick;

    public EntityImmortalKnight(EntityType<? extends EntityImmortalKnight> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerCustomGoals() {
        this.addRangeAI(this);
        this.goalSelector.addGoal(1, new AnimationSimpleAI<>(this, () -> putUpAnimation, EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP)) {
            @Override
            public void tick() {
                LivingEntity target = getTarget();
                if (target != null) entity.getLookControl().setLookAt(target, 30F, 30F);
                if (entity.getAnimationTick() == 10) {
                    entity.level().broadcastEntityEvent(entity, (byte) 12);
                }
            }

            @Override
            public void stop() {
                entity.invigorateOthers();
                super.stop();
            }
        });
        this.goalSelector.addGoal(4, new AnimationMeleeAI<>(this, 1D, 10 + this.random.nextInt(10), e -> e.active, () -> meleeAnimation1, () -> meleeAnimation2));
        super.registerCustomGoals();
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
        if (!this.level().isClientSide && this.nextBoostTick == 0 && !this.isNoAi() && this.isActive() && this.getTarget() != null && this.isNoAnimation() && this.tickCount % 2 == 0 && this.random.nextFloat() < 0.1F) {
            this.playAnimation(this.putUpAnimation);
            this.nextBoostTick = INVIGORATE_INTERVAL.sample(this.random);
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

    @Override
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
        if (!level().isClientSide) {
            if (ModEntityUtils.isProjectileSource(source)) {
                damage *= 0.5F;
            }
            return super.hurt(source, damage);
        }
        return false;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyIn) {
        this.setItemSlot(EquipmentSlot.MAINHAND, randomSource.nextFloat() < 0.2F ? ClassType.KNIGHT.holdItems[0].getDefaultInstance() : ClassType.KNIGHT.holdItems[1].getDefaultInstance());
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
    public void performRangedAttack(LivingEntity target, float velocity) {
        this.performRangedAttack(target);
    }

    @Override
    protected AbstractArrow getArrow(ItemStack arrowStack, float distanceFactor) {
        AbstractArrow arrow = super.getArrow(arrowStack, distanceFactor);
        if (arrow instanceof Arrow) {
            ((Arrow) arrow).addEffect(new MobEffectInstance(EffectInit.ERODE_EFFECT.get(), 200));
        }
        return arrow;
    }

    @Override
    protected int getClassId() {
        return ClassType.KNIGHT.id;
    }

    @Override
    protected void doEnhanceEffect() {
        if (this.level().isClientSide) {
            for (int i = 0; i < 12; i++) {
                this.level().addParticle(new ParticleOrb.OrbData(0.2F, 0.92F, 1F, 2F, 30), this.getRandomX(1.5), this.getY(this.random.nextDouble() * 0.2), this.getRandomZ(1.5), 0, 0.2 + this.random.nextDouble() * 0.05, 0);
            }
            this.level().addParticle(new ParticleRing.RingData(0F, (float) (Math.PI / 2F), 15, 0.2F, 0.92F, 1F, 1F, 30F, false, ParticleRing.EnumRingBehavior.GROW), this.getX(), this.getY() + 0.1, this.getZ(), 0, 0, 0);
        }
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
            for (EntityAbsImmortalSkeleton skeleton : this.level().getEntitiesOfClass(EntityAbsImmortalSkeleton.class, aabb,
                    skeleton -> ClassType.KNIGHT != skeleton.getVariant() && ClassType.MAGE != skeleton.getVariant() && isAlliedTo(skeleton))) {
                this.level().broadcastEntityEvent(skeleton, (byte) 12);
                skeleton.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 0), this);
            }
            this.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 0), this);
        }
    }

    private void alertOthers() {
        double range = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        AABB aabb = AABB.unitCubeFromLowerCorner(this.position()).inflate(range, 5.0D, range);
        if (getTarget() != null) {
            this.level().getEntitiesOfClass(EntityAbsImmortalSkeleton.class, aabb, LivingEntity::isAlive).
                    stream().
                    filter(immortalSkeleton -> immortalSkeleton.getTarget() == null && isAlliedTo(immortalSkeleton)).
                    forEach(immortalSkeleton -> immortalSkeleton.setTarget(getTarget()));
        }
    }
}
