package com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.player;

import com.eeeab.eeeabsmobs.sever.entity.VenerableEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class ReFindPlayerGoal<T extends Mob & VenerableEntity<Player>> extends Goal {
    private final RandomSource random = RandomSource.create();
    private final T target;

    public ReFindPlayerGoal(T venerable) {
        this.target = venerable;
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
        UUID uuid;
        if (target.level instanceof ServerLevel serverLevel && (uuid = target.getOwnerUUID()) != null) {
            Entity entity = serverLevel.getEntity(uuid);
            if (entity instanceof Player player) target.setOwner(player);
        }
    }
}

