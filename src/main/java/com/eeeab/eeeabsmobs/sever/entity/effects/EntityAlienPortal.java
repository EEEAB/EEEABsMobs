package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class EntityAlienPortal extends EntityMagicEffects {
    private static final EntityDataAccessor<Boolean> DATA_FLAG = SynchedEntityData.defineId(EntityAlienPortal.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(EntityAlienPortal.class, EntityDataSerializers.INT);
    public final ControlledAnimation phaseControlled = new ControlledAnimation(5);

    public EntityAlienPortal(EntityType<EntityAlienPortal> type, Level level) {
        super(type, level);
        this.noCulling = true;
    }

    public EntityAlienPortal(Level level, LivingEntity caster) {
        this(EntityInit.ALIEN_PORTAL.get(), level);
        this.caster = caster;
    }


    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.caster == null) {
            this.discard();
        }
        this.phaseControlled.updatePrevTimer();
        if (this.entityData.get(DATA_FLAG)) {
            if (this.phaseControlled.decreaseTimerChain().isStop()) {
                if (this.getPhase() > 0) {
                    this.phaseControlled.setTimer(this.phaseControlled.getDuration());
                    this.setPhase(this.getPhase() - 1);
                } else {
                    this.discard();
                }
            }
        } else {
            if (this.phaseControlled.increaseTimerChain().isEnd()) {
                if (this.getPhase() < 4) {
                    this.phaseControlled.resetTimer();
                    this.setPhase(this.getPhase() + 1);
                } else {
                    this.phaseControlled.setTimer(this.phaseControlled.getDuration());
                    this.updateFlag();
                }
            }
        }
    }

    @Override
    public void setDeltaMovement(double x, double y, double z) {
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_FLAG, false);
        this.entityData.define(DATA_PHASE, 0);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 1024;
    }

    public int getPhase() {
        return this.entityData.get(DATA_PHASE);
    }

    public void setPhase(int phase) {
        this.entityData.set(DATA_PHASE, phase);
    }

    public void updateFlag() {
        this.entityData.set(DATA_FLAG, !this.entityData.get(DATA_FLAG));
    }
}
