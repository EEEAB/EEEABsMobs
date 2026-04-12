package com.eeeab.eeeabsmobs.sever.ability;

import com.eeeab.eeeabsmobs.sever.capability.AbilityCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;

public abstract class Ability<T extends LivingEntity> {
    private final AbilityPeriod[] abilityPeriods;
    private final AbilityCapability.IAbilityCapability abilityCapability;
    private final AbilityType<T, ? extends Ability<?>> abilityType;
    private int maxCoolingTick;
    private int sectionTick;
    private int sectionIndex;
    private int coolingTimer;
    private boolean inUse;
    private final T user;
    private int tick;


    public Ability(AbilityType<T, ? extends Ability<?>> abilityType, T user, AbilityPeriod[] abilityPeriods, int maxCoolingTick) {
        this.abilityCapability = AbilityHandler.INSTANCE.getAbilityCapability(user);
        this.abilityType = abilityType;
        this.user = user;
        this.abilityPeriods = abilityPeriods;
        this.maxCoolingTick = maxCoolingTick;
    }

    public void start() {
        if (isPassive()) return;
        abilityCapability.setAbility(this);
        tick = 0;
        sectionTick = 0;
        sectionIndex = 0;
        inUse = true;
    }

    public void tick() {
        if (isUsing()) {
            tickUsing();
            tick++;
            sectionTick++;
            AbilityPeriod section = getSection();
            if (section instanceof AbilityPeriod.AbilityPeriodInstant) {
                nextPeriod();
            } else if (section instanceof AbilityPeriod.AbilityPeriodDuration period) {
                if (sectionTick >= period.duration) nextPeriod();
            }
        } else {
            if (coolingTimer > 0) coolingTimer--;
        }
    }

    protected void tickUsing() {

    }

    public void end() {
        if (isPassive()) return;
        coolingTimer = getMaxCoolingTick();
        inUse = false;
        tick = 0;
        sectionTick = 0;
        sectionIndex = 0;
        abilityCapability.setAbility(null);
    }

    public boolean isPassive() {
        return getAbilityType().isPassive();
    }

    public AbilityType<T, ? extends Ability<?>> getAbilityType() {
        return abilityType;
    }

    public int getTick() {
        return tick;
    }

    public T getUser() {
        return user;
    }

    public boolean isUsing() {
        return !isPassive() && inUse;
    }

    public boolean isCooling() {
        return !isPassive() && coolingTimer > 0;
    }

    public boolean canUse() {
        return !isUsing() && !isCooling() && abilityCapability.getAbility() == null;
    }

    public int getMaxCoolingTick() {
        return maxCoolingTick;
    }

    public void setMaxCoolingTick(int maxCoolingTick) {
        this.maxCoolingTick = maxCoolingTick;
    }

    public void nextPeriod() {
        jumpPeriod(sectionIndex + 1);
    }

    public void jumpPeriod(int index) {
        sectionIndex = index;
        sectionTick = 0;
        if (sectionIndex >= abilityPeriods.length) {
            end();
        }
    }

    public AbilityPeriod getSection() {
        if (sectionIndex >= abilityPeriods.length) return null;
        return abilityPeriods[sectionIndex];
    }

    public CompoundTag writeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        if (isUsing()) {
            compoundTag.putInt("tick", tick);
            compoundTag.putInt("sectionIndex", sectionIndex);
            compoundTag.putInt("sectionTick", sectionTick);
        } else if (coolingTimer > 0) {
            compoundTag.putInt("coolingTick", coolingTimer);
        }
        return compoundTag;
    }

    public void readNBT(Tag nbt) {
        CompoundTag compoundTag = (CompoundTag) nbt;
        boolean inUse = compoundTag.contains("tick");
        if (inUse) {
            tick = compoundTag.getInt("tick");
            sectionIndex = compoundTag.getInt("sectionIndex");
            sectionTick = compoundTag.getInt("sectionTick");
        } else {
            coolingTimer = compoundTag.getInt("coolingTick");
        }
    }
}
