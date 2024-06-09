package com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.player;

import com.eeeab.eeeabsmobs.sever.entity.VenerableEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class OwnerResetToPlayerGoal<T extends Mob & VenerableEntity<Player>> extends Goal {
    private final RandomSource random = RandomSource.create();
    private final double findRadius;
    private final T target;

    public OwnerResetToPlayerGoal(T venerable, double findRadius) {
        this.target = venerable;
        this.findRadius = findRadius;
    }

    @Override
    public boolean canUse() {
        return target.isSummon() && target.getOwner() == null && target.getOwnerUUID() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() && target.tickCount % (20 + random.nextInt(30)) == 0;
    }

    @Override
    public void start() {
        List<Player> players = target.level().getNearbyPlayers(TargetingConditions.DEFAULT, this.target, this.target.getBoundingBox().inflate(findRadius));
        for (Player player : players) {
            if (target.getOwnerUUID() != null && target.getOwnerUUID().equals(player.getUUID())) {
                target.setOwner(player);
                return;
            }
        }
    }
}

