package com.eeeab.eeeabsmobs.sever.entity.ai.goal;

import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationAbstractGoal;
import com.eeeab.eeeabsmobs.sever.entity.impl.namelessguardian.EntityNamelessGuardian;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.world.entity.LivingEntity;

public class GuardianCombo2Goal extends AnimationAbstractGoal<EntityNamelessGuardian> {
    private boolean isPowered;
    private float range;
    private float attackArc;

    public GuardianCombo2Goal(EntityNamelessGuardian entity, float range, float attackArc) {
        super(entity);
        this.range = range;
        this.attackArc = attackArc;
    }

    @Override
    public void start() {
        super.start();
        this.isPowered = this.entity.isPowered();
    }


    @Override
    protected boolean test(Animation animation) {
        return animation == EntityNamelessGuardian.ATTACK2_ANIMATION_1 || animation == EntityNamelessGuardian.ATTACK2_ANIMATION_2 || animation == EntityNamelessGuardian.ATTACK2_ANIMATION_3;
    }

    @Override
    public void tick() {
        Animation animation = this.entity.getAnimation();
        LivingEntity target = this.entity.getTarget();
        if (animation == EntityNamelessGuardian.ATTACK2_ANIMATION_1) {

        } else if (animation == EntityNamelessGuardian.ATTACK2_ANIMATION_2) {

        } else if (animation == EntityNamelessGuardian.ATTACK2_ANIMATION_3) {

        }
    }

}
