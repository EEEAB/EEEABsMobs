package com.eeeab.eeeabsmobs.sever.entity.immortal;

import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.entity.XpReward;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.base.AnimationMeleeAIGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.OwnerDieGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.OwnerResetGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.OwnerCopyTargetGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.AnimationActivateGoal;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.AnimationAttackGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.AnimationHurtGoal;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import com.github.alexthe666.citadel.animation.Animation;

import javax.annotation.Nullable;

public class EntityImmortalGolem extends EntityAbsImmortal implements IEntity {
    public static final Animation DIE_ANIMATION = Animation.create(30);
    public static final Animation ATTACK_ANIMATION = Animation.create(12);
    public static final Animation HURT_ANIMATION = Animation.create(10);
    public static final Animation SPAWN_ANIMATION = Animation.create(40);
    private static final Animation[] ANIMATIONS = {
            DIE_ANIMATION,
            ATTACK_ANIMATION,
            HURT_ANIMATION,
            SPAWN_ANIMATION,
    };
    private boolean boom;

    public EntityImmortalGolem(EntityType<? extends EntityImmortalGolem> type, Level level) {
        super(type, level);
        this.dropAfterDeathAnim = false;
        this.active = false;
    }

    @Override
    protected XpReward getEntityReward() {
        return XpReward.XP_REWARD_NONE;
    }

    @Override
    protected EMConfigHandler.AttributeConfig getAttributeConfig() {
        return EMConfigHandler.COMMON.MOB.IMMORTAL.IMMORTAL_GOLEM.combatConfig;
    }


    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null));
        this.goalSelector.addGoal(7, new EMLookAtGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new EMLookAtGoal(this, EntityImmortalShaman.class, 6.0F));
    }

    @Override
    protected void registerCustomGoals() {
        this.goalSelector.addGoal(1, new AnimationActivateGoal<>(this, SPAWN_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationAttackGoal<>(this, ATTACK_ANIMATION, 7, 1.5f, 1.0f, 1.0f));
        this.goalSelector.addGoal(1, new AnimationHurtGoal<>(this, false));
        this.goalSelector.addGoal(2, new AnimationMeleeAIGoal<>(this, 1.0D, ATTACK_ANIMATION));
        this.goalSelector.addGoal(1, new OwnerResetGoal<>(this, EntityImmortalShaman.class, 20D));
        this.targetSelector.addGoal(2, new OwnerCopyTargetGoal<>(this));
        this.goalSelector.addGoal(3, new OwnerDieGoal<>(this));
    }


    @Override
    public void tick() {
        super.tick();
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (this.isDangerous() && this.getAnimation() == ATTACK_ANIMATION) {
            if (this.getAnimationTick() == 6) this.boom();
        }
        if (this.isDangerous() && this.isOnFire()) this.boom();
    }

    private void boom() {
        if (!this.level().isClientSide) {
            if (!this.boom) {
                this.boom = true;
                this.level().broadcastEntityEvent(this, (byte) 5);
                this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.0F, false, Level.ExplosionInteraction.NONE);
                this.kill();
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 5) {
            ModParticleUtils.roundParticleOutburst(level(), 10, new ParticleOptions[]{ParticleTypes.LARGE_SMOKE, ParticleTypes.SMOKE}, getX(), this.getY(0.5), getZ(), 0.5F);
            this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    protected void makePoofParticles() {
        if (!isDangerous()) super.makePoofParticles();
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        Entity sourceSource = source.getEntity();
        if (sourceSource != null) {
            if (getTarget() == null && sourceSource instanceof LivingEntity && !(sourceSource instanceof Player && ((Player) sourceSource).isCreative()) && !(((LivingEntity) sourceSource).getMobType() == this.getMobType())) {
                this.setTarget((LivingEntity) sourceSource);
            }
        }
        return super.hurt(source, damage);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).is(Items.FLINT_AND_STEEL)) {
            this.boom();
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    //在初始生成时调用
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        RandomSource randomsource = worldIn.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, difficultyIn);
        return spawnDataIn;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource p_217055_, DifficultyInstance p_217056_) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0f);
    }


    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 12.0D).
                add(Attributes.MOVEMENT_SPEED, 0.4D).
                add(Attributes.FOLLOW_RANGE, 12.0D).
                add(Attributes.ATTACK_DAMAGE, 2.5D);
    }

    @Override
    public Animation getDeathAnimation() {
        return DIE_ANIMATION;
    }

    @Override
    public Animation getHurtAnimation() {
        return HURT_ANIMATION;
    }


    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }

    @Override
    public Animation getSpawnAnimation() {
        return SPAWN_ANIMATION;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundInit.IMMORTAL_GOLEM_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundInit.IMMORTAL_GOLEM_DEATH.get();
    }

    public boolean isDangerous() {
        return this.getItemInHand(InteractionHand.MAIN_HAND).is(Items.TNT) || this.getItemInHand(InteractionHand.OFF_HAND).is(Items.TNT);
    }

    public void setDangerous(boolean dangerous) {
        if (dangerous) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.TNT));
        }
    }

}
