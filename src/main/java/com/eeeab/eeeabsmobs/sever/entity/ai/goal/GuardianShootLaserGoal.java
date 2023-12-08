package com.eeeab.eeeabsmobs.sever.entity.ai.goal;

import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationCommonGoal;
import com.eeeab.eeeabsmobs.sever.entity.impl.effect.EntityGuardianLaser;
import com.eeeab.eeeabsmobs.sever.entity.impl.namelessguardian.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;

public class GuardianShootLaserGoal extends AnimationCommonGoal<EntityNamelessGuardian> {
    public GuardianShootLaserGoal(EntityNamelessGuardian entity, Animation animation) {
        super(entity, animation);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
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
            this.entity.setYRot(this.entity.yBodyRot);
            float yMaxRotSpeed = 1F + Mth.clamp((2F - Math.abs(this.entity.targetDistance) * 0.1F) + 0.5F, 0F, 2F);
            float xMaxRotAngle = 90F;
            if (entityTarget != null) {
                this.entity.getLookControl().setLookAt(entityTarget.getX(), entityTarget.getY() + entityTarget.getBbHeight() / 2, entityTarget.getZ(), yMaxRotSpeed, xMaxRotAngle);
            }
        } else {
            this.entity.setYRot(this.entity.yRotO);
        }
    }
}
