package com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base;

import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.NeedStopAiEntity;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;

public abstract class AnimationSpellAIGoal<T extends EEEABMobLibrary & IAnimatedEntity & NeedStopAiEntity> extends Goal {
    protected T spellCaster;
    protected int attackDelay;
    protected int nextAttackTickCount;

    public AnimationSpellAIGoal(T spellCaster) {
        this.spellCaster = spellCaster;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.spellCaster.getTarget();
        if (target != null && target.isAlive()) {
            if (!this.spellCaster.noConflictingTasks()) {
                return false;
            } else {
                return this.spellCaster.tickCount >= this.nextAttackTickCount;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.spellCaster.getTarget();
        return target != null && target.isAlive() && this.attackDelay > 0;
    }

    @Override
    public void start() {
        this.attackDelay = this.adjustedTickDelay(this.getKeyFrames());
        this.nextAttackTickCount = this.spellCaster.tickCount + this.getSpellCastingCooling();
        SoundEvent soundevent = this.getSpellCastingSound();
        if (soundevent != null) {
            this.spellCaster.playSound(soundevent, 1.0F, 1.0F);
        }
        this.spellCaster.playAnimation(getAnimation());
    }

    @Override
    public void tick() {
        --this.attackDelay;
        if (this.attackDelay == 0) {
            this.inSpellCasting();
        }
    }

    /**
     * 施法
     */
    protected abstract void inSpellCasting();

    /**
     * @return 距离触发时长
     */
    protected int getKeyFrames() {
        return 20;
    }

    /**
     * @return 施法冷却
     */
    protected abstract int getSpellCastingCooling();

    /**
     * @return 施法音效
     */
    @Nullable
    protected abstract SoundEvent getSpellCastingSound();

    /**
     * @return 实体动画
     */
    protected Animation getAnimation() {
        return null;
    }
}
