package com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner.player;

import com.eeeab.eeeabsmobs.sever.entity.VenerableEntity;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.List;

public class PlayerHatredRedirectionGoal<T extends Mob & VenerableEntity<Player>> extends Goal {
    private final T venerable;
    private final float searchRange;
    private final int randomCooling;
    private final List<Mob> mandatoryTarget = new ArrayList<>();

    public PlayerHatredRedirectionGoal(T venerable, float searchRange) {
        this.venerable = venerable;
        this.searchRange = searchRange;
        this.randomCooling = 10 + this.venerable.getRandom().nextInt(10) + 1;
    }

    @Override
    public boolean canUse() {
        if (this.venerable.isAlive() && this.venerable.getOwner() != null) {
            Player owner = this.venerable.getOwner();
            if (!owner.isAlive()) return false;
            if (this.venerable.tickCount % randomCooling == 0) return false;
            this.mandatoryTarget.addAll(this.venerable.getNearByEntitiesByClass(Mob.class, this.venerable.level(), owner, searchRange, searchRange, searchRange, searchRange)
                    .stream().filter(mob -> mob.getTarget() == owner
                            && !mob.getType().is(EMTagKey.RESISTS_FORCED_CHANGE_TARGET)
                            && !mob.getType().is(Tags.EntityTypes.BOSSES)
                    )
                    .limit(Mth.floor(searchRange * 2))//查找上限
                    .toList());
        }
        return !mandatoryTarget.isEmpty();
    }

    @Override
    public void start() {
        try {
            this.mandatoryTarget.stream().filter(Mob::isAlive).forEach(mob -> {
                        mob.setTarget(this.venerable);
                        if (mob.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
                            mob.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, this.venerable);
                        }
                    }
            );
        } finally {
            this.mandatoryTarget.clear();
        }
    }
}
