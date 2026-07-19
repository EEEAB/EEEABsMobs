package com.eeeab.eeeabsmobs.server.entity.ai.goal;

import com.eeeab.eeeabsmobs.server.capability.ThreatMemoryCapability;
import com.eeeab.eeeabsmobs.server.handler.CapabilityHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

import java.util.EnumSet;

public class HighestThreatTargetGoal extends TargetGoal {
    private LivingEntity target;
    private int cooldown = 0;

    public HighestThreatTargetGoal(Mob mob) {
        super(mob, false);
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (++cooldown % 10 != 0) return false;
        ThreatMemoryCapability.IThreatMemoryCapability capability = CapabilityHandler.getCapability(this.mob, CapabilityHandler.THREATMEMORY_CAPABILITY);
        if (capability == null) return false;
        return (target = capability.getHighestThreat(this.mob)) != null;
    }

    @Override
    public void start() {
        this.mob.setTarget(this.target);
        super.start();
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity current = this.mob.getTarget();
        if (current == null) return false;
        if (++cooldown % 10 == 0) {
            ThreatMemoryCapability.IThreatMemoryCapability capability = CapabilityHandler.getCapability(this.mob, CapabilityHandler.THREATMEMORY_CAPABILITY);
            if (capability == null) return false;
            LivingEntity highest = capability.getHighestThreat(this.mob);
            if (highest == null || highest != current) return false;
        }
        return super.canContinueToUse();
    }

    @Override
    public void stop() {
        super.stop();
        this.target = null;
        this.cooldown = 0;
    }
}