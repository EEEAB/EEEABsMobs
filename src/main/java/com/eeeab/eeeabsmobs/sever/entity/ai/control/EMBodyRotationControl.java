package com.eeeab.eeeabsmobs.sever.entity.ai.control;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

public class EMBodyRotationControl extends BodyRotationControl {
    private static final float MAX_ROTATE = 75;
    private final Mob mob;
    private int headStableTime;
    private float lastStableYHeadRot;

    public EMBodyRotationControl(Mob mob) {
        super(mob);
        this.mob = mob;
    }

    @Override
    public void clientTick() {
        double dx = this.mob.getX() - this.mob.xo;
        double dz = this.mob.getZ() - this.mob.zo;
        //实体正在移动
        if (this.isMoving()) {
            //double moveAngle = (float) Mth.atan2(dz, dx) * (180 / (float) Math.PI) - 90;
            //mob.yBodyRot += Mth.wrapDegrees(moveAngle - mob.yBodyRot) * 0.6F;
            //this.rotateHeadIfNecessary();
            //this.lastStableYHeadRot = this.mob.yHeadRot;
            //this.headStableTime = 0;
            this.mob.yBodyRot = this.mob.getYRot();
            this.rotateHeadIfNecessary();
            this.lastStableYHeadRot = this.mob.yHeadRot;
            this.headStableTime = 0;
        } else {
            if (this.notCarryingMobPassengers()) {
                float limit = MAX_ROTATE;
                if (Math.abs(this.mob.yHeadRot - this.lastStableYHeadRot) > 15) {
                    this.headStableTime = 0;
                    this.lastStableYHeadRot = this.mob.yHeadRot;
                    this.rotateBodyIfNecessary();
                } else {
                    headStableTime++;
                    final int speed = 10;
                    if (headStableTime > speed) {
                        limit = Math.max(1 - (headStableTime - speed) / (float) speed, 0) * MAX_ROTATE;
                    }
                    mob.yBodyRot = approach(mob.yHeadRot, mob.yBodyRot, limit);
                }
            }
        }
    }

    public static float approach(float target, float current, float limit) {
        float delta = Mth.wrapDegrees(current - target);
        if (delta < -limit) {
            delta = -limit;
        } else if (delta >= limit) {
            delta = limit;
        }
        return target + delta * 0.55F;
    }

    private boolean notCarryingMobPassengers() {
        return mob.getPassengers().isEmpty() || !(mob.getPassengers().get(0) instanceof Mob);
    }

    private boolean isMoving() {
        double d0 = this.mob.getX() - this.mob.xo;
        double d1 = this.mob.getZ() - this.mob.zo;
        return d0 * d0 + d1 * d1 > 2.5E-7;
    }

    private void rotateBodyIfNecessary() {
        this.mob.yBodyRot = Mth.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, (float) this.mob.getMaxHeadYRot());
    }

    private void rotateHeadIfNecessary() {
        this.mob.yHeadRot = Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, (float) this.mob.getMaxHeadYRot());
    }
}
