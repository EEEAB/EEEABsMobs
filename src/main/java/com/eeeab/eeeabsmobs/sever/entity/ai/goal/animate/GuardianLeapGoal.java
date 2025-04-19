package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate;

import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.entity.util.ShockWaveUtils;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.function.Supplier;

public class GuardianLeapGoal extends AnimationSimpleAI<EntityNamelessGuardian> {
    public GuardianLeapGoal(EntityNamelessGuardian entity, Supplier<Animation> animationSupplier) {
        super(entity, animationSupplier);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public void tick() {
        LivingEntity target = entity.getTarget();
        int tick = entity.getAnimationTick();
        if (target != null) {
            if (tick < 12) {
                entity.getLookControl().setLookAt(target, 30F, 30F);
                entity.lookAt(target, 30F, 30F);
            } else {
                entity.getLookControl().setLookAt(target, 15F, 30F);
                entity.setYRot(entity.yRotO);
                if (tick == 12) {
                    doLeapEffect();
                    Vec3 vec3 = findTargetPoint(entity, target);
                    entity.setDeltaMovement(vec3.x * 0.155D, 1 + Mth.clamp(vec3.y * 0.055D, 0D, 12D), vec3.z * 0.155D);
                }
            }
        } else if (tick == 12) {
            doLeapEffect();
            float radians = (float) Math.toRadians(entity.yBodyRot + 90);
            entity.setDeltaMovement(3.0 * Math.cos(radians), 1, 3.0 * Math.sin(radians));
        } else {
            entity.setYRot(entity.yRotO);
        }

        if (tick >= 12) {
            ModEntityUtils.breakBlocksInRect(this.entity.level(), this.entity, 2F, 3, 5, 3, 0, 2F, entity.checkCanDropItems(), tick % 4 == 0);
            if (tick > 12 && entity.onGround()) {
                this.entity.playAnimation(this.entity.smashDownAnimation);
            }
        }
    }

    private void doLeapEffect() {
        Vec3 position = entity.position();
        EntityCameraShake.cameraShake(entity.level(), position, 12F, 0.125F, 5, 0);
        ShockWaveUtils.doRingShockWave(entity, position, 2.4D, -0.04F, false, 10);
    }

    private static Vec3 findTargetPoint(LivingEntity attacker, LivingEntity target) {
        Vec3 vec3 = target.position();
        float width = Math.min(target.getBbWidth(), 1.5F);
        RandomSource random = attacker.getRandom();
        double radians = Math.toRadians(attacker.getYRot() + 90);
        double randomXOffset = -(1.5 + width) * Math.cos(radians) + (random.nextDouble() - 0.5) * width * 2;
        double randomZOffset = -(1.5 + width) * Math.sin(radians) + (random.nextDouble() - 0.5) * width * 2;
        return (new Vec3(vec3.x - attacker.getX() + randomXOffset, vec3.y - attacker.getY(), vec3.z - attacker.getZ() + randomZOffset));
    }
}
