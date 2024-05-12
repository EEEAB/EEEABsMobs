package com.eeeab.lib.server.ai.base;

import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.NeedStopAiEntity;
import com.eeeab.lib.server.animation.EMAnimatedEntity;
import com.eeeab.lib.server.animation.EMAnimation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import javax.annotation.Nullable;

public abstract class AnimationSpellAIGoal<T extends EEEABMobLibrary & EMAnimatedEntity & NeedStopAiEntity> extends Goal {
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
        this.spellCaster.playEMAnimation(getEMAnimation());
    }

    @Override
    public void tick() {
        --this.attackDelay;
        if (this.attackDelay == 0) {
            this.inSpellCasting();
        }
    }

    protected abstract void inSpellCasting();

    //距离触发时长
    protected int getKeyFrames() {
        return 20;
    }

    //冷却
    protected abstract int getSpellCastingCooling();


    @Nullable
    protected abstract SoundEvent getSpellCastingSound();

    //实体动画
    protected EMAnimation getEMAnimation() {
        return null;
    }
}
