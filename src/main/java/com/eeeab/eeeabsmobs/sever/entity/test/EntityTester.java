package com.eeeab.eeeabsmobs.sever.entity.test;

import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.ai.animation.AnimationDeactivate;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.EMAnimationHandler;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.MobLevel;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

//伤害测试单位 仅用于测试
public class EntityTester extends EEEABMobLibrary implements IEntity {
    public final Animation yesAnimation = Animation.create(5);
    public final Animation noAnimation = Animation.create(5);
    public final Animation[] animations = new Animation[]{
            yesAnimation,
            noAnimation,
    };
    private static final EntityDataAccessor<Boolean> ACTIVE = SynchedEntityData.defineId(EntityTester.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<Component>> DAMAGE = SynchedEntityData.defineId(EntityTester.class, EntityDataSerializers.OPTIONAL_COMPONENT);

    public EntityTester(EntityType<? extends EEEABMobLibrary> type, Level level) {
        super(type, level);
        active = true;
        AttributeInstance healthAttribute = this.getAttribute(Attributes.MAX_HEALTH);
        if (healthAttribute != null) {
            healthAttribute.setBaseValue(EMConfigHandler.COMMON.ENTITY.testerMaxHealth.get());
            this.setHealth(this.getMaxHealth());
        }
    }

    @Override
    protected MobLevel getMobLevel() {
        return MobLevel.NONE;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(7, new EMLookAtGoal(this, Player.class, 6.0F, true));
        goalSelector.addGoal(8, new EMLookAtGoal(this, Mob.class, 8.0F));
    }

    @Override
    protected void registerCustomGoals() {
        goalSelector.addGoal(0, new AnimationActivate<>(this, () -> yesAnimation) {
            @Override
            public void start() {
                super.start();
                this.entity.playSound(SoundEvents.VILLAGER_YES);
            }
        });
        goalSelector.addGoal(0, new AnimationDeactivate<>(this, () -> noAnimation) {
            @Override
            public void start() {
                super.start();
                this.entity.playSound(SoundEvents.VILLAGER_NO);
            }
        });
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().
                add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FLYING_SPEED, 0.0D);
    }

    @Override
    public void tick() {
        if (!this.isActive()) this.setDeltaMovement(0, onGround() ? 0 : getDeltaMovement().y, 0);
        super.tick();
        EMAnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (this.level().isClientSide) {
            return false;
        } else if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && source.getEntity() == null && EMConfigHandler.COMMON.ENTITY.immuneToEnvironmentalOrStatusDamage.get()) {
            return false;
        } else {
            this.lastDamageSource = source;
            return super.hurt(source, damage);
        }
    }

    @Override
    public void setHealth(float health) {
        if (!this.level().isClientSide && health < this.getHealth()) {
            if (this.lastDamageSource != null && !this.lastDamageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                this.setDamage(this.getHealth() - health);
                health = this.getMaxHealth();
                this.lastDamageSource = null;
            }
        }
        super.setHealth(health);
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
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.VILLAGER_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }

    @Override
    public boolean shouldShowName() {
        return true;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            this.setDamage(0.0);
            this.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP);
            player.displayClientMessage(Component.literal("reset success").withStyle(ChatFormatting.GREEN), true);
            return InteractionResult.SUCCESS;
        }
        if (this.isNoAnimation()) {
            setActive(!isActive());
            this.playAnimation(isActive() ? this.noAnimation : this.yesAnimation);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public Animation[] getAnimations() {
        return this.animations;
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
        this.entityData.define(DAMAGE, Optional.of(Component.keybind("0.0")));
    }

    public void setActive(boolean isActive) {
        this.entityData.set(ACTIVE, isActive);
    }

    public boolean isActive() {
        return this.entityData.get(ACTIVE);
    }

    public Component getDamage() {
        return this.entityData.get(DAMAGE).orElse(Component.keybind("0.0"));
    }

    public void setDamage(double damage) {
        this.entityData.set(DAMAGE, Optional.of(Component.keybind(String.format("%.1f", damage))));
    }
}
