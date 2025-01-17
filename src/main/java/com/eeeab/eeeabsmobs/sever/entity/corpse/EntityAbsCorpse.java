package com.eeeab.eeeabsmobs.sever.entity.corpse;

import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.GlowEntity;
import com.eeeab.eeeabsmobs.sever.entity.VenerableEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public abstract class EntityAbsCorpse extends EEEABMobLibrary implements Enemy, GlowEntity, VenerableEntity<EntityAbsCorpse> {
    private UUID ownerUUID;
    private boolean isSummon;
    boolean valuable;
    @Nullable
    private EntityAbsCorpse owner;
    private static final EntityDataAccessor<Boolean> DATA_ACTIVE = SynchedEntityData.defineId(EntityAbsCorpse.class, EntityDataSerializers.BOOLEAN);

    public EntityAbsCorpse(EntityType<? extends EntityAbsCorpse> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, 5, false, false, null));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Zombie.class, 5, false, false, (zombie) -> !(zombie instanceof ZombifiedPiglin)));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractIllager.class, 5, false, false, null));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.getTarget() != null && !this.getTarget().isAlive()) setTarget(null);
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    protected void makePoofParticles() {
        for (int i = 0; i < 10; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(ParticleTypes.SOUL, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
        }
    }

    @Override
    public boolean isAlliedTo(Entity entity) {
        if (super.isAlliedTo(entity)) {
            return true;
        } else if (entity instanceof LivingEntity && entity == getOwner()) {
            return this.getTeam() == null && entity.getTeam() == null;
        } else {
            return false;
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ACTIVE, true);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.owner != null && this.owner.getHealth() > 0F) {
            compound.putUUID("owner", owner.getUUID());
        }
        compound.putBoolean("isSummon", this.isSummon);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setOwnerUUID(compound.hasUUID("owner") ? compound.getUUID("owner") : null);
        this.isSummon = compound.getBoolean("isSummon");
    }

    @Override
    protected boolean shouldDropLoot() {
        return super.shouldDropLoot() && !this.isSummon();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance instance, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        return groupData;
    }

    //设置该生物初始生成
    public void setInitSpawn() {
        this.isSummon = true;
        this.valuable = true;
    }

    public void setActive(boolean isActive) {
        this.entityData.set(DATA_ACTIVE, isActive);
    }

    public boolean isActive() {
        return this.entityData.get(DATA_ACTIVE);
    }

    @Nullable
    @Override
    public EntityAbsCorpse getOwner() {
        return owner;
    }

    @Override
    public void setOwner(@Nullable EntityAbsCorpse owner) {
        this.owner = owner;
    }

    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Override
    public void setOwnerUUID(UUID uuid) {
        this.ownerUUID = uuid;
    }

    @Override
    public boolean isSummon() {
        return this.isSummon;
    }

    @Override
    public boolean isGlow() {
        return getHealth() > 0;
    }
}