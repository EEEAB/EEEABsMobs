package com.eeeab.animate.server.ai.animation;

import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.List;

public class AnimationRepel<T extends EEEABMobLibrary & AnimatedEntity> extends AnimationSimpleAI<T> {
    private final int attackFrame;
    private final boolean canDisableShield;
    private final float range;
    private final float height;
    private final float applyKnockBackMultiplier;
    private final float damageMultiplier;

    public AnimationRepel(T entity, Animation animation, float range, int attackFrame, float applyKnockBackMultiplier, float damageMultiplier, boolean canDisableShield) {
        this(entity, animation, range, range, attackFrame, applyKnockBackMultiplier, damageMultiplier, canDisableShield);
    }

    public AnimationRepel(T entity, Animation animation, float range, float height, int attackFrame, float applyKnockBackMultiplier, float damageMultiplier, boolean canDisableShield) {
        super(entity, animation);
        this.range = range;
        this.height = height;
        this.attackFrame = attackFrame;
        this.applyKnockBackMultiplier = applyKnockBackMultiplier;
        this.damageMultiplier = damageMultiplier;
        this.canDisableShield = canDisableShield;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public void tick() {
        super.tick();
        if (entity.getAnimationTick() == attackFrame) {
            List<LivingEntity> hitEntities = entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(range, height, range),
                    target -> target != entity && !entity.isAlliedTo(target));
            for (LivingEntity hit : hitEntities) {
                if (preHit(hit)) continue;
                if (entity.doHurtTarget(hit, damageMultiplier, applyKnockBackMultiplier, canDisableShield)) {
                    onHit(hit);
                }
                if (!hit.isInvulnerable()) {
                    if (hit instanceof Player player && player.getAbilities().invulnerable) continue;
                    double angle = entity.getAngleBetweenEntities(entity, hit);
                    double x = applyKnockBackMultiplier * Math.cos(Math.toRadians(angle - 90));
                    double z = applyKnockBackMultiplier * Math.sin(Math.toRadians(angle - 90));
                    hit.setDeltaMovement(x, 0.35, z);
                    if (hit instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(hit));
                    }
                }
            }
        }
    }

    protected void onHit(LivingEntity entity) {
    }

    protected boolean preHit(LivingEntity entity) {
        return false;
    }
}
