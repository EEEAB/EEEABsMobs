package com.eeeab.eeeabsmobs.sever.entity.ai.navigate;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class EMWallClimberNavigation extends GroundPathNavigation {

    public EMWallClimberNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            if (target.getY() - 3 >= this.mob.getY()) {
                BlockHitResult result = this.level.clip(new ClipContext(this.mob.getEyePosition(), this.mob.getEyePosition().add(this.mob.getViewVector(1F).scale(1F)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this.mob));
                if (this.level.getBlockState(result.getBlockPos()).canOcclude()) {
                    Vec3 vec3 = result.getBlockPos().getCenter();
                    this.mob.getMoveControl().setWantedPosition(vec3.x, this.getGroundY(vec3), vec3.z, this.speedModifier);
                }
            }
        }
    }
}
