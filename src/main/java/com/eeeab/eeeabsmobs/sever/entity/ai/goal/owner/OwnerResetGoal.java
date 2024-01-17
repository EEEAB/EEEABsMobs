package com.eeeab.eeeabsmobs.sever.entity.ai.goal.owner;

import com.eeeab.eeeabsmobs.sever.entity.VenerableEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;

public class OwnerResetGoal<T extends Mob & VenerableEntity<T>> extends Goal {
    private final RandomSource random = RandomSource.create();
    private final Class<? extends Mob> ownerClass;
    private final double findRadius;
    private final T target;

    public OwnerResetGoal(T venerable, Class<? extends Mob> ownerClass, double findRadius) {
        this.target = venerable;
        this.ownerClass = ownerClass;
        this.findRadius = findRadius;
    }

    @Override
    public boolean canUse() {
        return target.isSummon() && target.getOwner() == null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse() && target.tickCount % (20 + random.nextInt(30)) == 0;
    }

    @Override
    public void start() {
        List<? extends Mob> entities = target.getNearByEntitiesByClass(ownerClass, target.level, target, findRadius, 10F, findRadius, findRadius);
        for (Mob entity : entities) {
            if (target.getOwnerUUID() != null && target.getOwnerUUID().equals(entity.getUUID())) {
                this.target.setOwner((T) entity);
                return;
            }
        }
        /*
            执行到这会存在问题:
                当重新进入世界读取NBT数据时,实体会以自身为中心20*10*20范围内搜索Owner实体
                但是如果这个实体不在这个范围内,实体将无法重新设置Owner
                这时再次退出游戏,因为Owner==null,导致NBT数据丢失
                再次重新进入世界时,会因为读取不到NBT数据,最终导致Owner丢失
            解决方案:
                方案1:扩大搜索范围(对性能影响较大,不采用)
                方案2:周期性搜索范围内目标,得到List集合,随机的取出List中的元素作为新Owner
        */
        if (!entities.isEmpty()) target.setOwnerUUID(entities.get(random.nextInt(entities.size())).getUUID());
    }
}

