package com.eeeab.eeeabsmobs.sever.entity.ai.navigate;

import net.minecraft.core.BlockPos;
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
                BlockPos pos = result.getBlockPos();
                if (this.level.getBlockState(pos).canOcclude()) {
                    Vec3 vec3 = new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
                    this.mob.getMoveControl().setWantedPosition(vec3.x, this.getGroundY(vec3), vec3.z, this.speedModifier);
                }
            }
        }
    }
}
