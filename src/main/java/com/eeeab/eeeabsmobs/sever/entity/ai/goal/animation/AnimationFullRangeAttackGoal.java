package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation;

import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationCommonGoal;
import com.eeeab.eeeabsmobs.sever.entity.impl.EEEABMobLibrary;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;
import java.util.List;

//全范围击飞敌人
public class AnimationFullRangeAttackGoal<T extends EEEABMobLibrary & IAnimatedEntity> extends AnimationCommonGoal<T> {
    private final int attackFrame;
    private final boolean pureShotEffect;
    private final float range;
    private final float applyKnockBackMultiplier;
    private final float damageMultiplier;

    public AnimationFullRangeAttackGoal(T entity, Animation animation, float range, int attackFrame, float applyKnockBackMultiplier, float damageMultiplier, boolean pureShotEffect) {
        super(entity, animation);
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
