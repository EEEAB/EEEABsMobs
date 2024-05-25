package com.eeeab.animate.server.ai;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.Item;

import java.util.EnumSet;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class AnimationRangeAI<T extends EEEABMobLibrary & EMAnimatedEntity> extends Goal {
    protected T attacker;
    protected int seeTime;
    protected double speed;
    protected Item holdWeapon;
    protected int attackInterval;
    protected Supplier<Animation> emAnimationSupplier;
    protected int attackTime = -1;
    protected int strafingTime = -1;
    protected float attackRadiusSqr;
    protected Predicate<T> customFlag;
    protected boolean strafingClockwise;
    protected boolean strafingBackwards;
    protected boolean ignoreWeaponCheck = false;

    public AnimationRangeAI(T mob, double speedModifier, int attackIntervalMin, float attackRadius, Item weapon, Predicate<T> customFlag, Supplier<Animation> animationSupplier) {
        this.attacker = mob;
        this.holdWeapon = weapon;
        this.speed = speedModifier;
        this.emAnimationSupplier = animationSupplier;
        this.customFlag = customFlag;
        this.attackInterval = attackIntervalMin;
        this.attackRadiusSqr = attackRadius * attackRadius;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        if (weapon == null) {
            this.ignoreWeaponCheck = true;
        }
    }

    /**
     * 判断当前实体是否持有指定物品
     */
    protected boolean isHoldItem() {
        return this.attacker.isHolding(i -> i.is(holdWeapon));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.attacker.getTarget();
        return customFlag.test(this.attacker) && target != null && target.isAlive() && this.attacker.canAttack(target) && (this.ignoreWeaponCheck || this.isHoldItem());
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.attacker.getNavigation().isDone());
    }

    @Override
    public void start() {
        this.attacker.setAggressive(true);
    }

    @Override
    public void stop() {
        this.attacker.setAggressive(false);
        this.attacker.getMoveControl().strafe(0F, 0F);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity livingentity = this.attacker.getTarget();
        if (livingentity != null) {
            double d0 = this.attacker.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
            boolean flag = this.attacker.getSensing().hasLineOfSight(livingentity);
            boolean flag1 = this.seeTime > 0;
            if (flag != flag1) {
                this.seeTime = 0;
            }
            if (flag) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }
            if (!(d0 > (double) this.attackRadiusSqr) && this.seeTime >= 20) {
                this.attacker.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.attacker.getNavigation().moveTo(livingentity, this.speed);
                this.strafingTime = -1;
            }
            if (this.strafingTime >= 20) {
                if ((double) this.attacker.getRandom().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }
                if ((double) this.attacker.getRandom().nextFloat() < 0.3D) {
                    this.strafingBackwards = !this.strafingBackwards;
                }
                this.strafingTime = 0;
            }
            if (this.strafingTime > -1) {
                if (d0 > (double) (this.attackRadiusSqr * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (d0 < (double) (this.attackRadiusSqr * 0.25F)) {
                    this.strafingBackwards = true;
                }
                this.attacker.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                Entity entity = this.attacker.getControlledVehicle();
                if (entity instanceof Mob mob) {
                    mob.lookAt(livingentity, 30.0F, 30.0F);
                }
                this.attacker.lookAt(livingentity, 30.0F, 30.0F);
            } else {
                this.attacker.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
            }

            if (--this.attackTime <= 0 && this.seeTime >= -60) {
                this.attackTime = this.attackInterval;
                this.attacker.playAnimation(this.emAnimationSupplier.get());
            }
        }
    }
}
