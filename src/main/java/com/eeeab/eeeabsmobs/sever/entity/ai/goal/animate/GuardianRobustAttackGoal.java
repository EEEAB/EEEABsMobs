package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.util.damage.EMDamageSource;
import com.eeeab.animate.server.ai.AnimationSimpleAI;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;

public class GuardianRobustAttackGoal extends AnimationSimpleAI<EntityNamelessGuardian> {

    public GuardianRobustAttackGoal(EntityNamelessGuardian entity, Supplier<Animation> animationSupplier) {
        super(entity, animationSupplier);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public void tick() {
        int tick = entity.getAnimationTick();
        LivingEntity target = entity.getTarget();
        entity.setDeltaMovement(0, entity.onGround() ? 0 : entity.getDeltaMovement().y(), 0);
        if (tick < 32 && target != null) {
            entity.getLookControl().setLookAt(target, 30F, 30F);
            entity.lookAt(target, 30F, 30F);
        } else {
            entity.setYRot(entity.yRotO);
        }
        if (tick == 1) entity.playSound(SoundInit.NAMELESS_GUARDIAN_CREAK.get(), 1.0F, 1.0F);
        else if (tick == 30) entity.playSound(SoundInit.NAMELESS_GUARDIAN_WHOOSH.get(), 2F, 1.2F);
        else if (tick == 35) {
            final float attackArc = 40F;
            final float range = 4.6F;
            List<LivingEntity> entities = entity.getNearByLivingEntities(range, range - 0.6F, range, range);
            for (LivingEntity hitEntity : entities) {
                float entityRelativeAngle = ModEntityUtils.getTargetRelativeAngle(entity, hitEntity);
                float entityHitDistance = (float) Math.sqrt((hitEntity.getZ() - entity.getZ()) * (hitEntity.getZ() - entity.getZ()) + (hitEntity.getX() - entity.getX()) * (hitEntity.getX() - entity.getX())) - hitEntity.getBbWidth() / 2F;
                if ((entityHitDistance <= range && (entityRelativeAngle <= attackArc / 2F && entityRelativeAngle >= -attackArc / 2F) || (entityRelativeAngle >= 360 - attackArc / 2F || entityRelativeAngle <= -360 + attackArc / 2F))) {
                    double duration = 3;
                    if (Difficulty.HARD.equals(this.entity.level().getDifficulty())) duration = 5;
                    entity.stun(null, hitEntity, (int) (duration * 20), entity.isChallengeMode());
                    entity.guardianHurtTarget(EMDamageSource.guardianRobustAttack(entity), entity, hitEntity, 0.03F, 1.85F, 1.4F, true, true, true);
                }
            }
        } else if (tick == 36) {
            entity.playSound(SoundInit.GIANT_AXE_HIT.get(), 2F, 0.2F);
            EntityCameraShake.cameraShake(entity.level(), entity.position(), 10, 0.3F, 0, 10);
        } else if (tick == 45) {
            for (int i = 2; i <= 12; i++) {
                entity.shockAttack(EMDamageSource.guardianRobustAttack(this.entity), i, 3F, 2F, 4F, 0.025F, 1.3F, 1.25F, true, false, true);
            }
        } else if (tick == 46) {
            entity.playSound(SoundEvents.GENERIC_EXPLODE, 1.5F, 1F + entity.getRandom().nextFloat() * 0.1F);
            EntityCameraShake.cameraShake(entity.level(), entity.position(), 30, 0.2F, 10, 10);
        } else if (tick == 68) {
            if (entity.getMadnessTick() == 0 && entity.isPowered()) {
                if (entity.isChallengeMode()) {
                    entity.setMadnessTick(EntityNamelessGuardian.NEVER_STOP);
                    entity.setRobustTick();
                    return;
                }
                entity.setExecuteWeak(true);
                entity.playAnimation(this.entity.weakAnimation1);
            }
        }
    }
}
