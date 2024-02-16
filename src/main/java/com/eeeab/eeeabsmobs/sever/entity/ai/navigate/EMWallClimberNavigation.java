package com.eeeab.eeeabsmobs.sever.entity.ai.navigate;

import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EMWallClimberNavigation extends WallClimberNavigation {

    public EMWallClimberNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    public void tick() {
        if (this.mob.getTarget() != null) {
            super.tick();
        } else {
            ++this.tick;
            if (this.hasDelayedRecomputation) {
                this.recomputePath();
            }

            if (!this.isDone()) {
                if (this.canUpdatePath()) {
                    this.followThePath();
                } else if (this.path != null && !this.path.isDone()) {
                    Vec3 vec3 = this.getTempMobPos();
                    Vec3 vec31 = this.path.getNextEntityPos(this.mob);
                    if (vec3.y > vec31.y && !this.mob.isOnGround() && Mth.floor(vec3.x) == Mth.floor(vec31.x) && Mth.floor(vec3.z) == Mth.floor(vec31.z)) {
                        this.path.advance();
                    }
                }

                DebugPackets.sendPathFindingPacket(this.level, this.mob, this.path, this.maxDistanceToWaypoint);
                if (!this.isDone()) {
                    Vec3 vec32 = this.path.getNextEntityPos(this.mob);
                    this.mob.getMoveControl().setWantedPosition(vec32.x, this.getGroundY(vec32), vec32.z, this.speedModifier);
                }
            }
        }
    }
}
