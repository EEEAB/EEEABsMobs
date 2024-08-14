package com.eeeab.eeeabsmobs.sever.entity.ai.goal;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityTheImmortal;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ImmortalComboGoal extends AnimationAI<EntityTheImmortal> {
    private static final float PUNCH_ATTACK_RANGE = 4.5F;

    public ImmortalComboGoal(EntityTheImmortal entity) {
        super(entity);
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == entity.punchCombo1Animation;
    }

    @Override
    public void tick() {
        LivingEntity target = entity.getTarget();
        Animation animation = entity.getAnimation();
        int tick = entity.getAnimationTick();
        if (animation == entity.punchCombo1Animation) {
            if (target != null && !(tick >= 10 && tick <= 18 || tick >= 24 && tick <= 34)) {
                entity.lookAt(target, 30F, 30F);
                entity.getLookControl().setLookAt(target, 30F, 30F);
            } else {
                entity.setYRot(entity.yRotO);
            }
            if (tick == 11) {
                entity.pursuit(4.5F, 2F, 0, 0.5F);
            } else if (tick == 12) {
                doHurtTargetAndTryBreakBlock(PUNCH_ATTACK_RANGE, PUNCH_ATTACK_RANGE - 0.5F, 100F, 50F, 0.8F, false, ModEntityUtils.canMobDestroy(entity));
            } else if (tick == 22) {
                entity.pursuit(5.2F, 2.2F, 0, 0.8F);
            } else if (tick == 26) {
                doHurtTargetAndTryBreakBlock(PUNCH_ATTACK_RANGE - 0.1F, PUNCH_ATTACK_RANGE - 0.6F, 50F, 100F, 1.25F, false, ModEntityUtils.canMobDestroy(entity));
            }
        }
    }

    private void doHurtTargetAndTryBreakBlock(float attackDistance, float attackHeight, float rightAttackArc, float leftAttackArc, float strength, boolean disableShied, boolean breakBlock) {
        float attackArc = rightAttackArc + leftAttackArc;
        List<LivingEntity> entitiesHit = entity.getNearByLivingEntities(attackDistance, attackHeight, attackDistance, attackDistance);
        boolean hitFlag = false;
        for (LivingEntity entityHit : entitiesHit) {
            float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, entityHit);
            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - entity.getZ()) * (entityHit.getZ() - entity.getZ()) + (entityHit.getX() - entity.getX()) * (entityHit.getX() - entity.getX())) - entityHit.getBbWidth() / 2f;
            if (entityHitDistance <= attackDistance && ((entityRelativeAngle >= -leftAttackArc / 2 && entityRelativeAngle <= rightAttackArc / 2) || (entityRelativeAngle >= 360 - attackArc / 2F || entityRelativeAngle <= -360 + attackArc / 2F))) {
                if (!hitFlag) {
                    hitFlag = true;
                    EntityCameraShake.cameraShake(this.entity.level(), this.entity.position(), 8F, 0.3F, 0, 5);
                    entity.level().broadcastEntityEvent(entity, (byte) 7);
                }
                boolean hit = entity.immortalHurtTarget(entityHit, disableShied);
                entity.knockBack(entityHit, hit ? strength : strength - 0.2, hit ? 0.2 : 0, true);
            }
        }
        if (breakBlock) {
            Vec3 attackVector = new Vec3(Math.cos(Math.toRadians(entity.yBodyRot)), 0, Math.sin(Math.toRadians(entity.yBodyRot))).scale(attackDistance);
            AABB aabb = entity.getBoundingBox().expandTowards(attackVector);
            int minx = Mth.floor(aabb.minX);
            int miny = Mth.floor(aabb.minY);
            int minz = Mth.floor(aabb.minZ);
            BlockPos min = new BlockPos(minx, miny, minz);
            int maxx = Mth.floor(aabb.maxX);
            int maxy = Mth.floor(aabb.maxY);
            int maxz = Mth.floor(aabb.maxZ);
            BlockPos max = new BlockPos(maxx, maxy, maxz);
            if (entity.level().hasChunksAt(min, max)) {
                for (BlockPos blockPos : BlockPos.betweenClosed(min, max)) {
                    if (ModEntityUtils.canDestroyBlock(entity.level(), blockPos, entity)) {
                        entity.level().destroyBlock(blockPos, true);
                        if (!hitFlag) {
                            hitFlag = true;
                            entity.level().broadcastEntityEvent(entity, (byte) 7);
                        }
                    }
                }
            }
        }
    }
}
