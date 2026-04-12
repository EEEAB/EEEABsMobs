package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate;

import com.eeeab.animate.server.ai.AnimationGroupAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.mob.immortal.EntityImmortalBoss;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.world.entity.LivingEntity;

public class ImmortalComboGoal extends AnimationGroupAI<EntityImmortalBoss> {

    public ImmortalComboGoal(EntityImmortalBoss entity) {
        super(entity, EntityImmortalBoss.PUNCH_RIGHT_ANIMATION, EntityImmortalBoss.HARDPUNCH_RIGHT_ANIMATION, EntityImmortalBoss.PUNCH_LEFT_ANIMATION, EntityImmortalBoss.HARDPUNCH_LEFT_ANIMATION);
    }

    @Override
    public void tick() {
        LivingEntity target = entity.getTarget();
        Animation animation = entity.getAnimation();
        int tick = entity.getAnimationTick();
        if (animation == EntityImmortalBoss.PUNCH_RIGHT_ANIMATION || animation == EntityImmortalBoss.PUNCH_LEFT_ANIMATION) {
            if (tick < 11) entity.anchorToGround();
            if (target != null && (tick <= 12 || tick > 20)) {
                entity.lookAt(target, 30F, 30F);
                entity.getLookControl().setLookAt(target, 30F, 30F);
            } else entity.setYRot(entity.yRotO);
            boolean left = animation == EntityImmortalBoss.PUNCH_LEFT_ANIMATION;
            if (tick == 10) entity.playSound(SoundInit.IMMORTAL_ATTACK.get(), 1.5F, entity.getVoicePitch());
            else if (tick == 11) entity.pounce(5F, 0, 0.35);
            else if (tick >= 14 && tick <= 16) {
                doHurtTargetAndTryBreakBlock(5F, left ? 40F : 120F, left ? 120F : 40F, 0.5F, tick > 14, false, tick == 14, false, false, 0.89F, 1.0F);
            }
        } else if (animation == EntityImmortalBoss.HARDPUNCH_RIGHT_ANIMATION || animation == EntityImmortalBoss.HARDPUNCH_LEFT_ANIMATION) {
            if (tick < 20) entity.anchorToGround();
            if (target != null && (tick < 20 || tick > 34)) {
                entity.lookAt(target, 30F, 30F);
                entity.getLookControl().setLookAt(target, 30F, 30F);
            } else entity.setYRot(entity.yRotO);
            if (tick == 19) entity.playSound(SoundInit.IMMORTAL_ATTACK2.get(), 1.5F, 1.4F);
            else if (tick >= 21 && tick < 24) {
                boolean flag = tick < 23;
                boolean left = animation == EntityImmortalBoss.HARDPUNCH_LEFT_ANIMATION;
                if (tick == 21) entity.pounce(6F, 0, 0.5);
                doHurtTargetAndTryBreakBlock(5.5F, left ? 20F : 90F, left ? 90F : 20F, 1F, flag, true, flag, true, flag, 0.9F, 0.8F);
            }
        }
    }

    private void doHurtTargetAndTryBreakBlock(float attackDistance, float rightAttackArc, float leftAttackArc, float strength, boolean continuous, boolean disableShied, boolean canHit, boolean ignoreArmor, boolean canStun, float baseDamageMultiplier, float damageMultiplier) {
        boolean hitFlag = false;
        for (LivingEntity entityHit : entity.getNearByLivingEntities(attackDistance)) {
            float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, entityHit);
            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - entity.getZ()) * (entityHit.getZ() - entity.getZ()) + (entityHit.getX() - entity.getX()) * (entityHit.getX() - entity.getX())) - entityHit.getBbWidth() / 2f;
            if (canHit && entityHitDistance <= attackDistance && (entityRelativeAngle >= -leftAttackArc / 2 && entityRelativeAngle <= rightAttackArc / 2) || (entityRelativeAngle >= 360 - leftAttackArc / 2 || entityRelativeAngle <= -360 + rightAttackArc / 2)) {
                if (canStun) entity.stun(null, entityHit, 30, false);
                boolean hit = entity.doHurtTarget(entityHit, disableShied, entityHit.hasEffect(EffectInit.ERODE_EFFECT.get()), ignoreArmor, baseDamageMultiplier, damageMultiplier);
                if (!disableShied) entity.disableShield(entityHit, 40);
                if (!hitFlag) {
                    hitFlag = true;
                    if (hit) entity.playSound(disableShied ? SoundInit.IMMORTAL_PUNCH_HARD_HIT.get() : SoundInit.IMMORTAL_PUNCH_HIT.get(), 1F, disableShied ? 1F : 1.1F);
                    entity.shakeGround(0F, 20F, 0.3F, 1, 5);
                    entity.level().broadcastEntityEvent(entity, (byte) (disableShied ? 7 : 6));
                }
                entity.knockBack(entityHit, hit ? strength : strength - 0.2, hit && canHit ? 0.15 : 0.1, true, continuous);
            }
        }
        if (canHit) {
            int offset = (int) (attackDistance / 2);
            if (ModEntityUtils.breakBlocksInRect(entity.level(), entity, 50, offset, (int) attackDistance, offset, 0, offset, true)) {
                if (!hitFlag) entity.level().broadcastEntityEvent(entity, (byte) 7);
            }
        }
    }
}
