package com.eeeab.eeeabsmobs.sever.entity.immortal.skeleton;

import com.eeeab.eeeabsmobs.sever.entity.immortal.EntityAbsImmortal;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;

public class EntityImmortalArcher extends EntityAbsImmortalSkeleton implements RangedAttackMob {
    public EntityImmortalArcher(EntityType<? extends EntityAbsImmortal> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerCustomGoals() {
        this.addRangeAI(this);
        super.registerCustomGoals();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getTarget() != null && !isNoAi() && isActive()) {
            LivingEntity target = getTarget();
            if (attackTick == 0 && getSensing().hasLineOfSight(target)) {
                attacking = true;
            }
            if (attacking && getSensing().hasLineOfSight(target) && canAttack(target)) {
                if (targetDistance < 1.5 || targetDistance < 2.5 && ModEntityUtils.checkTargetComingCloser(this, target)) {
                    this.stopUsingItem();
                    this.playAnimation(this.meleeAnimation1);
                    attackTick = 20;
                    attacking = false;
                }
            }
        } else {
            attacking = false;
        }
    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        this.performRangedAttack(target);
    }

    @Override
    protected int getCareerId() {
        return CareerType.ARCHER.id;
    }
}
