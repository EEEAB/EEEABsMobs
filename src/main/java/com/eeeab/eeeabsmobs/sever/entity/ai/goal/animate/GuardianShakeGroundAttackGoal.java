package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityGuardianBlade;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.util.damage.EMDamageSource;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

public class GuardianShakeGroundAttackGoal extends AnimationAI<EntityNamelessGuardian> {
    public GuardianShakeGroundAttackGoal(EntityNamelessGuardian entity) {
        super(entity);
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == this.entity.shakeGroundAttackAnimation1 || animation == this.entity.shakeGroundAttackAnimation2 || animation == this.entity.shakeGroundAttackAnimation3;
    }

    @Override
    public void tick() {
        int tick = this.entity.getAnimationTick();
        LivingEntity target = this.entity.getTarget();
        Animation animation = this.entity.getAnimation();
        entity.setDeltaMovement(0, entity.onGround() ? 0 : entity.getDeltaMovement().y, 0);
        if (animation == this.entity.shakeGroundAttackAnimation1) {
            if (tick < 23 && target != null) {
                entity.getLookControl().setLookAt(target, 30F, 30F);
                this.entity.lookAt(target, 30F, 30F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
            }
            if (tick == 19) {
                this.entity.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 1.95f, this.entity.getVoicePitch());
                if (this.entity.targetDistance > 1.8 || entity.getTarget() == null)
                    this.entity.move(MoverType.SELF, new Vec3(Math.cos(Math.toRadians(entity.getYRot() + 90)) * 1.2F, 0, Math.sin(Math.toRadians(entity.getYRot() + 90)) * 1.2F));
            } else if (tick == 24) {
                doHurtTarget(1F, 1.2F, false);
                doSpawnBlade(5, 1.75F);
            } else if (tick == 25) {
                this.entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1.5F, 0.2F);
                EntityCameraShake.cameraShake(entity.level(), entity.position(), 20, 0.125F, 5, 10);
            } else if (tick == 35) {
                this.entity.playAnimation(this.entity.shakeGroundAttackAnimation2);
            }
        } else if (animation == this.entity.shakeGroundAttackAnimation2) {
            tick = this.entity.getAnimationTick();
            if (tick < 17 && target != null) {
                entity.getLookControl().setLookAt(target, 30F, 30F);
                this.entity.lookAt(target, 30F, 30F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
            }
            if (tick == 13) {
                this.entity.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 1.95f, this.entity.getVoicePitch());
                if (this.entity.targetDistance > 1.8 || entity.getTarget() == null)
                    this.entity.move(MoverType.SELF, new Vec3(Math.cos(Math.toRadians(entity.getYRot() + 90)) * 1.2F, 0, Math.sin(Math.toRadians(entity.getYRot() + 90)) * 1.2F));
            } else if (tick == 17) {
                doHurtTarget(1F, 1F, false);
                doSpawnBlade(5, 1.75F);
            } else if (tick == 18) {
                this.entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1.5F, 0.2F);
                EntityCameraShake.cameraShake(entity.level(), entity.position(), 20, 0.125F, 5, 10);
            } else if (tick == 30 && this.entity.isPowered()) {
                this.entity.playAnimation(this.entity.shakeGroundAttackAnimation3);
            }
        } else if (animation == this.entity.shakeGroundAttackAnimation3) {
            tick = this.entity.getAnimationTick();
            if (tick < 23 && target != null) {
                entity.getLookControl().setLookAt(target, 25F, 30F);
                this.entity.lookAt(target, 25F, 30F);
            } else {
                this.entity.setYRot(this.entity.yRotO);
            }
            if (tick == 22) {
                this.entity.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 1.95f, this.entity.getVoicePitch());
                if (this.entity.targetDistance > 1.8 || entity.getTarget() == null)
                    this.entity.move(MoverType.SELF, new Vec3(Math.cos(Math.toRadians(entity.getYRot() + 90)) * 1.2F, 0, Math.sin(Math.toRadians(entity.getYRot() + 90)) * 1.2F));
            } else if (tick == 26) {
                doHurtTarget(1.5F, 1.2F, true);
                doSpawnBlade(6, 2F);
                this.entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 1.5F, 0.2F);
                EntityCameraShake.cameraShake(entity.level(), entity.position(), 20, 0.2F, 5, 10);
            }
        }
    }

    private void doHurtTarget(float baseDamageMultiplier, float damageMultiplier, boolean stun) {
        entity.rangeAttack(4.6, 4.6, 4.6, 4.6, 40F, 40F, hitEntity -> {
            if (stun) {
                double duration = 2;
                if (Difficulty.HARD.equals(this.entity.level().getDifficulty())) duration = 4;
                entity.stun(null, hitEntity, (int) (duration * 20), entity.isChallengeMode());
            }
            entity.guardianHurtTarget(EMDamageSource.guardianRobustAttack(entity), entity, hitEntity, baseDamageMultiplier, damageMultiplier, true, true, true);
        });
    }

    private void doSpawnBlade(int count, float totalOffset) {
        Vec3 looking = entity.getLookAngle();
        float angleStep = totalOffset / (count + 1);
        for (int i = 0; i < count; i++) {
            float angle = -totalOffset / 2.0f + (i + 1) * angleStep;
            Vec3 vec3 = looking.yRot(angle);
            float f0 = (float) Mth.atan2(vec3.z, vec3.x);
            double x = entity.getX() + Mth.cos(f0) * 2.5D;
            double y = entity.getY();
            double z = entity.getZ() + Mth.sin(f0) * 2.5D;
            EntityGuardianBlade blade = new EntityGuardianBlade(entity.level(), entity, x, y, z, f0, false);
            entity.level().addFreshEntity(blade);
        }
    }
}
