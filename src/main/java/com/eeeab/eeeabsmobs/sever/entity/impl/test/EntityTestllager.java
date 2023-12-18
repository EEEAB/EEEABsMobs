package com.eeeab.eeeabsmobs.sever.entity.impl.test;

import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EELookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.AnimationActivateGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.AnimationDeactivateGoal;
import com.eeeab.eeeabsmobs.sever.entity.impl.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//伤害测试单位 仅用于测试
@Deprecated
public class EntityTestllager extends EEEABMobLibrary implements IEntity {
    public static final Animation YES = Animation.create(5);
    public static final Animation NO = Animation.create(5);
    public static final Animation[] ANIMATIONS = {YES, NO};
    private static final EntityDataAccessor<Boolean> ACTIVE = SynchedEntityData.defineId(EntityTestllager.class, EntityDataSerializers.BOOLEAN);

    public EntityTestllager(EntityType<? extends EEEABMobLibrary> type, Level level) {
        super(type, level);
        active = false;
    }

    @Override
    public void sendSystemMessage(@NotNull Component component) {
        Minecraft mc = Minecraft.getInstance();
        mc.gui.getChat().addMessage(component);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(7, new EELookAtGoal(this, Player.class, 6.0F, true));
        goalSelector.addGoal(8, new EELookAtGoal(this, Mob.class, 8.0F));
    }

    @Override
    protected void registerCustomGoals() {
        goalSelector.addGoal(0, new AnimationActivateGoal<>(this, YES) {
            @Override
            public void start() {
                super.start();
                this.entity.playSound(SoundEvents.VILLAGER_YES);
            }
        });
        goalSelector.addGoal(0, new AnimationDeactivateGoal<>(this, NO) {
            @Override
            public void start() {
                super.start();
                this.entity.playSound(SoundEvents.VILLAGER_NO);
            }
        });
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().
                add(Attributes.MAX_HEALTH, 20.0D);
    }

    @Override
    public void tick() {
        if (!this.isActive()) this.setDeltaMovement(0, onGround ? 0 : getDeltaMovement().y, 0);
        super.tick();
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (invulnerableTime > 0) {
            invulnerableTime = 0;
        }
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
    public boolean hurt(DamageSource source, float damage) {
        Entity entity = source.getEntity();
        if (entity != null) {
            //服务端崩溃
            //sendSystemMessage(Component.keybind("Damage source: " + entity.getDisplayName().getString() + " damage: " + damage));
            damage = 0;
            super.hurt(source, damage);
        }/* else if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return super.hurt(source, damage);
        }*/
        return false;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VILLAGER_AMBIENT;
    }

    @Override
    public void playAmbientSound() {
        if (isActive()) super.playAmbientSound();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return SoundEvents.VILLAGER_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }


    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (getAnimation() == NO_ANIMATION) {
            setActive(!isActive());
            this.playAnimation(isActive() ? YES : NO);
            return InteractionResult.SUCCESS;
        }
        //playSound(isActive() ? SoundEvents.VILLAGER_YES : SoundEvents.VILLAGER_NO, 1.0F, 1.0F);
        //setNoAi(isActive());
        return InteractionResult.PASS;
    }

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("isActive", isActive());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setActive(compound.getBoolean("isActive"));
        active = isActive();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ACTIVE, true);
    }

    public void setActive(boolean isActive) {
        this.entityData.set(ACTIVE, isActive);
    }

    public boolean isActive() {
        return this.entityData.get(ACTIVE);
    }
}
