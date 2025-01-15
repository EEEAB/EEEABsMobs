package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortal;
import com.eeeab.eeeabsmobs.sever.entity.util.ShockWaveUtils;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.world.entity.LivingEntity;

public class ImmortalStunGoal extends AnimationAI<EntityImmortal> {
    public ImmortalStunGoal(EntityImmortal entity) {
        super(entity);
    }

    @Override
    public void stop() {
        super.stop();
        entity.blockEntity = null;
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == entity.hurt1Animation;
    }

    @Override
    public void tick() {
        int tick = entity.getAnimationTick();
        if (tick < 10) {
            if (entity.blockEntity != null) {
                entity.lookAt(entity.blockEntity, 90F, 90F);
                entity.getLookControl().setLookAt(entity.blockEntity, 200F, 30F);
            }
        } else {
            if (tick > 15) entity.anchorToGround();
            if (tick >= 44) {
                LivingEntity target = entity.blockEntity;
                if (target == null || !target.isAlive()) {
                    target = entity.getTarget();
                    entity.blockEntity = target;
                }
                if (target != null) {
                    entity.lookAt(target, 30F, 30F);
                    entity.getLookControl().setLookAt(target, 30F, 30F);
                }
            } else entity.setYRot(entity.yRotO);
        }
        if (tick == 2) {
            double radians = Math.toRadians(entity.getYRot() + 270);
            double speed = entity.onGround() ? 0.8 : 0.6;
            entity.setDeltaMovement(entity.getDeltaMovement().add(Math.cos(radians) * speed, 0D, Math.sin(radians) * speed));
        } else if (tick == 13) entity.playSound(SoundInit.IMMORTAL_SHAKE_GROUND.get(), 1F, 1F);
        else if (tick == 15) {
            for (LivingEntity entityHit : ShockWaveUtils.doRingShockWave(entity.level(), entity.getPosOffset(true, 1F, this.entity.getBbWidth() * 0.6F, -0.1F), 2D, 0F, false, 10)) {
                if (entityHit == entity) continue;
                entity.doHurtTarget(entityHit, false, false, false, false, 0F, 1F, 0.5F);
            }
        } else if (tick == 16) entity.shakeGround(0.2F, 20F, 0.125F, 3, 2);
    }
}
