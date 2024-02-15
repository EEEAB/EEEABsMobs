package com.eeeab.eeeabsmobs.sever.entity.corpse;

import com.eeeab.eeeabsmobs.sever.entity.XpReward;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.NeedStopAiEntity;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.KeepDistanceGoal;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityCorpseSlavery extends EntityAbsCorpse implements IEntity, NeedStopAiEntity {

    public EntityCorpseSlavery(EntityType<? extends EntityAbsCorpse> type, Level level) {
        super(type, level);
        this.active = true;
    }

    @Override
    protected XpReward getEntityReward() {
        return XpReward.XP_REWARD_ELITE;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, false));
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(6, new KeepDistanceGoal<>(this, 1.0D, 12F, 2F));
        this.goalSelector.addGoal(7, new EMLookAtGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new EMLookAtGoal(this, Mob.class, 6.0F));
    }

    public static AttributeSupplier.Builder setAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 40.0D).
                add(Attributes.MOVEMENT_SPEED, 0.3D).
                add(Attributes.FOLLOW_RANGE, 18.0D).
                add(Attributes.ATTACK_DAMAGE, 1D);
    }

    @Override
    public Animation getDeathAnimation() {
        return null;
    }

    @Override
    public Animation getHurtAnimation() {
        return null;
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[0];
    }

    @Override
    public boolean noConflictingTasks() {
        return true;
    }
}
