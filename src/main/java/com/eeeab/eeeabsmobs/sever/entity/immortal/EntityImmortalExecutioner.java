package com.eeeab.eeeabsmobs.sever.entity.immortal;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.XpReward;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityImmortalExecutioner extends EntityAbsImmortal implements IEntity {

    public EntityImmortalExecutioner(EntityType<EntityImmortalExecutioner> type, Level level) {
        super(type, level);
    }

    @Override
    protected XpReward getEntityReward() {
        return XpReward.XP_REWARD_ELITE;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(6, new EMLookAtGoal(this, EntityAbsImmortal.class, 6.0F));
        this.goalSelector.addGoal(7, new EMLookAtGoal(this, Player.class, 8.0F));
    }

    @Override
    protected void registerCustomGoals() {
        super.registerCustomGoals();
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 120.0D).
                add(Attributes.ATTACK_DAMAGE, 12.0D).
                add(Attributes.ARMOR, 12.0D).
                add(Attributes.MOVEMENT_SPEED, 0.3D).
                add(Attributes.FOLLOW_RANGE, 32.0D).
                add(Attributes.KNOCKBACK_RESISTANCE, 1D);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[0];
    }
}
