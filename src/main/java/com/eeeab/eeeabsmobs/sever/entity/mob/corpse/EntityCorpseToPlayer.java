package com.eeeab.eeeabsmobs.sever.entity.mob.corpse;

import com.eeeab.animate.server.ai.AnimationMeleeAI;
import com.eeeab.animate.server.ai.animation.AnimationActivate;
import com.eeeab.animate.server.ai.animation.AnimationMelee;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.mob.GlowEntity;
import com.eeeab.eeeabsmobs.sever.entity.mob.ModMobType;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.ModLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.WhenOwnerDeadGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.player.PlayerHatredRedirectionGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.player.ReFindPlayerGoal;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityCorpseToPlayer extends EntityCorpse implements GlowEntity {
    private static final int MAX_ACTIVE = 400;
    private final ReFindPlayerGoal reFindPlayerGoal = new ReFindPlayerGoal(this);
    private final PlayerHatredRedirectionGoal playerHatredRedirectionGoal = new PlayerHatredRedirectionGoal(this, 16F);
    private final WhenOwnerDeadGoal whenOwnerDeadGoal = new WhenOwnerDeadGoal(this);
    private final NearestAttackableTargetGoal<Mob> attackEnemyGoal = new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, (entity) -> entity instanceof Enemy && entity.getMobType() != ModMobType.PLAYER_SUMMONS);
    private int countdown = -1;


    public EntityCorpseToPlayer(EntityType<? extends EntityCorpseToPlayer> type, Level level) {
        super(type, level);
    }

    @Override
    public MobLevel getMobLevel() {
        return MobLevel.NONE;
    }

    @Override//应该禁用和平模式生成
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public @NotNull MobType getMobType() {
        return ModMobType.PLAYER_SUMMONS;
    }

    @Override
    protected boolean shouldDropLoot() {
        return false;
    }

    @Override
    protected ModConfigHandler.AttributeConfig getAttributeConfig() {
        return ModConfigHandler.COMMON.mobs.minions.CORPSE_MINION.combatConfig;
    }

    @Override
    protected void registerCorpseGoals() {
        //this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, (entity) -> entity instanceof Enemy && entity.getMobType() != ModMobType.PLAYER_SUMMONS));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this, Player.class));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(7, new ModLookAtGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new AnimationActivate<>(this, SPAWN_ANIMATION));
        this.goalSelector.addGoal(1, new AnimationMelee<>(this, ATTACK_ANIMATION1, 9, 2F, 1F, 1F));
        this.goalSelector.addGoal(1, new AnimationMelee<>(this, ATTACK_ANIMATION2, 9, 2F, 1F, 1F));
        this.goalSelector.addGoal(1, new AnimationMelee<>(this, ATTACK_ANIMATION3, 9, 2F, 1.5F, 1.5F));
        this.goalSelector.addGoal(2, new AnimationMeleeAI<>(this, 1.2D, 5, ATTACK_ANIMATION1, ATTACK_ANIMATION2, ATTACK_ANIMATION3));
        //this.goalSelector.addGoal(2, new ReFindPlayerGoal(this));
        //this.goalSelector.addGoal(3, new PlayerHatredRedirectionGoal(this, 16F));
        //this.goalSelector.addGoal(4, new WhenOwnerDeadGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.isSummon() && --countdown <= 0) {
            this.hurt(damageSources().starve(), getMaxHealth() * 0.25F);
            countdown = 20;
        }
    }

    @Override
    public void die(DamageSource source) {
        if (!this.level().isClientSide && this.getOwner() != null) {
            float healAmount = ModConfigHandler.COMMON.mobs.minions.CORPSE_MINION.minionDeathHealAmount.get().floatValue();
            this.getOwner().heal(healAmount);
            if (healAmount > 0 && this.level() instanceof ServerLevel serverLevel) {
                this.level().playSound(null, this.getOwner().blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1F, 0.9F + this.random.nextFloat() * 0.2F);
                for (int i = 0; i < 5; i++) {
                    serverLevel.sendParticles(ParticleInit.WARLOCK_HEAL.get(), this.getOwner().getRandomX(1.5), this.getOwner().getY(0.5D), this.getOwner().getRandomZ(1.5), 1, this.random.nextGaussian() * 0.1D, this.random.nextGaussian() * 0.1D, this.random.nextGaussian() * 0.1D, 0.5D);
                }
            }
        }
        super.die(source);
    }

    @Override
    protected void makePoofParticles() {
        for (int i = 0; i < 10; ++i) {
            double d0 = this.random.nextGaussian() * 0.015D;
            double d1 = this.random.nextGaussian() * 0.015D;
            double d2 = this.random.nextGaussian() * 0.015D;
            this.level().addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    @Override
    public void afterSpawn() {
        super.afterSpawn();
        this.addEffect(new MobEffectInstance(MobEffects.GLOWING, MAX_ACTIVE, 0, false, false));
        this.targetSelector.addGoal(3, attackEnemyGoal);
        this.goalSelector.addGoal(2, reFindPlayerGoal);
        this.goalSelector.addGoal(3, playerHatredRedirectionGoal);
        this.goalSelector.addGoal(4, whenOwnerDeadGoal);
    }

    public void brainwashing() {
        this.goalSelector.removeGoal(attackEnemyGoal);
        this.goalSelector.removeGoal(reFindPlayerGoal);
        this.goalSelector.removeGoal(playerHatredRedirectionGoal);
        this.goalSelector.removeGoal(whenOwnerDeadGoal);
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        this.countdown = MAX_ACTIVE;
        return groupData;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.FOLLOW_RANGE, 32.0D).add(Attributes.ATTACK_DAMAGE, 6D);
    }

    @Override
    public boolean isSummon() {
        return true;
    }

    @Override
    public boolean belongsToMob() {
        return false;
    }
}
