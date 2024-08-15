package com.eeeab.eeeabsmobs.sever.entity.ai.goal;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityTheImmortal;
import com.eeeab.eeeabsmobs.sever.entity.util.ShockWaveUtils;
import net.minecraft.world.entity.LivingEntity;

public class ImmortalShakeGroundGoal extends AnimationAI<EntityTheImmortal> {
    public ImmortalShakeGroundGoal(EntityTheImmortal entity) {
        super(entity);
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == entity.smashGround1Animation;
    }

    @Override
    public void tick() {
        LivingEntity target = entity.getTarget();
        Animation animation = entity.getAnimation();
        int tick = entity.getAnimationTick();
        if (animation == entity.smashGround1Animation) {
            if (target != null && tick < 13) {
                entity.lookAt(target, 30F, 30F);
                entity.getLookControl().setLookAt(target, 30F, 30F);
            } else {
                entity.setYRot(entity.yRotO);
            }
            if (tick == 12) EntityCameraShake.cameraShake(entity.level, entity.position(), 20, 0.125F, 5, 5);
            else if (tick == 13) {
                double radians = Math.toRadians(entity.getYRot() + 90);
                for (LivingEntity entityHit : ShockWaveUtils.doRingShockWave(entity.level, entity.position().add(2.8F * Math.cos(radians), -1, 2.8F * Math.sin(radians)), 4.5D, 0F)) {
                    if (entityHit == entity) {
                        continue;
                    }
                    boolean hit = entity.immortalHurtTarget(entityHit, false);
                    entity.knockBack(entityHit, hit ? 2 : 1.5, hit ? 0.6 : 0.3, true);
                }
            }
        }
    }
}
