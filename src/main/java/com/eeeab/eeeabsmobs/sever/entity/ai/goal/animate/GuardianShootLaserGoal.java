package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityGuardianLaser;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.animate.server.ai.AnimationSimpleAI;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;
import java.util.function.Supplier;

public class GuardianShootLaserGoal extends AnimationSimpleAI<EntityNamelessGuardian> {
    public GuardianShootLaserGoal(EntityNamelessGuardian entity, Supplier<Animation> animationSupplier) {
        super(entity, animationSupplier);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public void start() {
        super.start();
        this.entity.playSound(SoundInit.NAMELESS_GUARDIAN_ACCUMULATING.get(), 2F, (this.entity.getRandom().nextFloat() - this.entity.getRandom().nextFloat()) * 0.2F + 3.5F);
    }

    @Override
    public void tick() {
        LivingEntity entityTarget = this.entity.getTarget();
        this.entity.setDeltaMovement(0, this.entity.onGround() ? 0 : this.entity.getDeltaMovement().y, 0);
        int tick = this.entity.getAnimationTick();
        if (tick == 4 && !this.entity.level().isClientSide) {
            double px = this.entity.getX();
            double py = this.entity.getY() + 1.4;
            double pz = this.entity.getZ();
            float yHeadRotAngle = (float) Math.toRadians(this.entity.yHeadRot + 90);
            float xHeadRotAngle = (float) (float) Math.toRadians(-this.entity.getXRot());
            EntityGuardianLaser laser = new EntityGuardianLaser(EntityInit.GUARDIAN_LASER.get(), this.entity.level(), this.entity, px, py, pz, yHeadRotAngle, xHeadRotAngle, 70);
            this.entity.level().addFreshEntity(laser);
        }
        if (tick < 11) {
            if (entityTarget != null) {
                this.entity.getLookControl().setLookAt(entityTarget, 30F, 30F);
                this.entity.lookAt(entityTarget, 30F, 30F);
            }
        } else if (tick >= 22 && tick < 100) {
            if (tick == 22) this.entity.playSound(SoundInit.LASER.get(), 2F, 1.0F);
            float speed = 0F;
            try {
                if (this.entity.getTarget() != null) {
                    float distance = this.entity.distanceTo(this.entity.getTarget());
                    float minSpeed = 0.5F;
                    float maxSpeed = 3.5F;
                    float distanceScaleFactor = this.entity.isChallengeMode() ? 5F : 7.5F;
                    speed = Math.min(maxSpeed, Math.max(minSpeed, distance / distanceScaleFactor));
                }
            } catch (ArithmeticException ignored) {
            }
            float yMaxRotSpeed = 1.5F + speed;
            float xMaxRotAngle = 90F;
            if (entityTarget != null) {
                this.entity.getLookControl().setLookAt(entityTarget.getX(), entityTarget.getY() + entityTarget.getBbHeight() / 2, entityTarget.getZ(), yMaxRotSpeed, xMaxRotAngle);
            }
            if (tick == 92) {
                this.entity.playSound(SoundInit.NAMELESS_GUARDIAN_ACCUMULATING_END.get(), 2F, (this.entity.getRandom().nextFloat() - this.entity.getRandom().nextFloat()) * 0.2F + 2.5F);
            }
        } else {
            this.entity.setYRot(this.entity.yRotO);
        }
    }
}
