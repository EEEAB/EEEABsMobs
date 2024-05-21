package com.eeeab.animate.server.ai.animation;

import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;

public class AnimationRepel<T extends EEEABMobLibrary & EMAnimatedEntity> extends AnimationSimpleAI<T> {
    private final int attackFrame;
    private final boolean pureShotEffect;
    private final float range;
    private final float applyKnockBackMultiplier;
    private final float damageMultiplier;

    public AnimationRepel(T entity, Supplier<Animation> animationSupplier, float range, int attackFrame, float applyKnockBackMultiplier, float damageMultiplier, boolean pureShotEffect) {
        super(entity, animationSupplier);
        this.range = range;
        this.attackFrame = attackFrame;
        this.applyKnockBackMultiplier = applyKnockBackMultiplier;
        this.damageMultiplier = damageMultiplier;
        this.pureShotEffect = pureShotEffect;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public void tick() {
        super.tick();
        if (entity.getAnimationTick() == attackFrame) {
            List<LivingEntity> hitEntities = entity.getNearByLivingEntities(range, range * 2, range, range);
            for (LivingEntity hit : hitEntities) {
                if (preHit(hit)) continue;
                entity.doHurtTarget(hit, damageMultiplier, applyKnockBackMultiplier);
                if (pureShotEffect && !hit.isInvulnerable()) {
                    if (hit instanceof Player player && player.getAbilities().invulnerable) continue;
                    double angle = entity.getAngleBetweenEntities(entity, hit);
                    double x = applyKnockBackMultiplier * Math.cos(Math.toRadians(angle - 90));
                    double z = applyKnockBackMultiplier * Math.sin(Math.toRadians(angle - 90));
                    hit.setDeltaMovement(x, 0.35, z);
                    if (hit instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(hit));
                    }
                    onHit(hit);
                }
            }
        }
    }

    protected void onHit(LivingEntity entity) {
    }

    protected boolean preHit(LivingEntity entity){
        return false;
    }
}
