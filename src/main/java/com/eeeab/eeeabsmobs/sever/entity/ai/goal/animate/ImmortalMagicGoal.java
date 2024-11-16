package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animate;

import com.eeeab.animate.server.ai.AnimationAI;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityImmortalMagicCircle;
import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityImmortal;
import com.eeeab.eeeabsmobs.sever.entity.immortal.ImmortalMagic;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.entity.util.ModMobType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.function.Predicate;

public class ImmortalMagicGoal extends AnimationAI<EntityImmortal> {
    private final Predicate<LivingEntity> LIVING_ENTITY_SELECTOR = e -> entity.getSensing().hasLineOfSight(e) && e.isAlive() && (entity.getTarget() == e || (e.getMobType() != ModMobType.IMMORTAL && e.isAttackable() && !this.entity.isAlliedTo(e)
            && (e instanceof Enemy || e instanceof NeutralMob || (e instanceof Player player && !player.isCreative() && !player.isSpectator()))));

    public ImmortalMagicGoal(EntityImmortal entity) {
        super(entity);
    }

    @Override
    protected boolean test(Animation animation) {
        return animation == entity.trackingShurikenAnimation || animation == entity.unleashEnergyAnimation;
    }

    @Override
    public void tick() {
        Animation animation = entity.getAnimation();
        int tick = entity.getAnimationTick();
        LivingEntity target = entity.getTarget();
        entity.setDeltaMovement(0, entity.onGround() ? -0.01 : entity.getDeltaMovement().y, 0);
        if (animation == entity.trackingShurikenAnimation) {
            lookAtTarget(target);
            if (tick == 10) {
                EntityImmortalMagicCircle.spawn(entity.level(), entity, entity.position().add(0, 0.25, 0), 3F, 2F, 100, entity.getYRot(), EntityImmortalMagicCircle.MagicCircleType.HARMFUL, true);
                List<LivingEntity> entities = findTargets();
                int size = Math.max(entities.size(), 1);
                if (size >= 6) ImmortalMagic.spawnShurikenWithTargets(entity, Math.min(size, 10), entities, 5, 360, true);
                else ImmortalMagic.spawnShurikenWithTargets(entity, Math.min(size + 2, 5), entities, 1.5, 160, false);
            }
        } else if (animation == entity.unleashEnergyAnimation) {
            if (tick == 29) ImmortalMagic.spawnImmortalLaser(entity, 7, 1.85F, 31, entity.getPosOffset(false, 2F, 0F, entity.getBbHeight() * 1.4F));
            else if (tick == 49) ImmortalMagic.spawnImmortalLaser(entity, 8, 2F, 29, entity.getPosOffset(false, 2F, 0F, entity.getBbHeight() * 1.4F));
            else if (tick < 29 || tick > 40 && tick < 49) lookAtTarget(target);
            else entity.setYRot(entity.yRotO);
        }
    }

    private void lookAtTarget(LivingEntity target) {
        if (target != null) {
            entity.lookAt(target, 30F, 30F);
            entity.getLookControl().setLookAt(target, 30F, 30F);
        }
    }

    private List<LivingEntity> findTargets() {
        double findRange = entity.getAttributeValue(Attributes.FOLLOW_RANGE);
        return entity.level().getEntitiesOfClass(LivingEntity.class, ModEntityUtils.makeAABBWithSize(entity.getX(), entity.getY(), entity.getZ(), 0, findRange, findRange, findRange), LIVING_ENTITY_SELECTOR);
    }
}
