package com.eeeab.eeeabsmobs.sever.entity.immortal;

import com.eeeab.animate.server.ai.AnimationMeleeAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.ai.animation.AnimationHurt;
import com.eeeab.animate.server.ai.animation.AnimationMelee;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.handler.EMAnimationHandler;
import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.XpReward;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.CopyOwnerTargetGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.WhenOwnerDeadGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.ReFindOwnerGoal;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
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

import javax.annotation.Nullable;

public class EntityImmortalGolem extends EntityAbsImmortal implements IEntity {
    public final Animation hurtAnimation = Animation.create(5);
    public final Animation attackAnimation = Animation.create(12);
    public final Animation spawnAnimation = Animation.create(20);
    private final Animation[] animations = new Animation[]{
            hurtAnimation,
            attackAnimation,
            spawnAnimation
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
        this.goalSelector.addGoal(1, new AnimationActivate<>(this, () -> spawnAnimation));
        this.goalSelector.addGoal(1, new AnimationMelee<>(this, () -> attackAnimation, 7, 1.5f, 1.0f, 1.0f));
        this.goalSelector.addGoal(1, new AnimationHurt<>(this, false));
        this.goalSelector.addGoal(2, new AnimationMeleeAI<>(this, 1.0D, () -> attackAnimation));
        this.goalSelector.addGoal(1, new ReFindOwnerGoal<>(this, EntityImmortalShaman.class, 20D));
        this.targetSelector.addGoal(2, new CopyOwnerTargetGoal<>(this));
        this.goalSelector.addGoal(3, new WhenOwnerDeadGoal<>(this));
    }


    @Override
    public void tick() {
        super.tick();
        EMAnimationHandler.INSTANCE.updateAnimations(this);
        if (this.isDangerous() && this.getAnimation() == this.attackAnimation) {
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
    public Animation getSpawnAnimation() {
        return this.spawnAnimation;
    }

    @Override
    public Animation[] getAnimations() {
        return this.animations;
    }

    @Override
    public Animation getHurtAnimation() {
        return this.hurtAnimation;
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
