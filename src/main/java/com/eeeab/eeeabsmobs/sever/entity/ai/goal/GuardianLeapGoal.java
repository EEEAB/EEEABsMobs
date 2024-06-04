package com.eeeab.eeeabsmobs.sever.entity.ai.goal;

import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.ai.AnimationSimpleAI;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.function.Supplier;

public class GuardianLeapGoal extends AnimationSimpleAI<EntityNamelessGuardian> {
    public GuardianLeapGoal(EntityNamelessGuardian entity, Supplier<Animation> animationSupplier) {
        super(entity, animationSupplier);
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public void tick() {
        LivingEntity target = entity.getTarget();
        int tick = entity.getAnimationTick();
        if (target != null) {
            if (tick < 12) {
                entity.getLookControl().setLookAt(target, 30F, 30F);
                entity.lookAt(target, 30F, 30F);
            } else {
                entity.setYRot(entity.yRotO);
                if (tick == 12) {
                    Vec3 vec3 = findTargetPoint(entity, target);
                    double speedX = entity.isInWater() ? 0.3D : 0.155D;
                    double speedY = entity.isInWater() ? 0.2D : 0.055D;
                    entity.setDeltaMovement(vec3.x * speedX, 1.2 + Mth.clamp(vec3.y * speedY, 0D, 12D), vec3.z * speedX);
                }
            }
        } else if (tick == 12) {
            float radians = (float) Math.toRadians(entity.yBodyRot + 90);
            entity.setDeltaMovement(3.0 * Math.cos(radians), 1.2, 3.0 * Math.sin(radians));
        }

        if (tick > 12) {
            boolean onGround = entity.isOnGround();
            if (!this.entity.level.isClientSide &&
                    /* 判断是否开启生物破坏规则,这关乎那些不想被怪物破坏方块的玩家考虑~ */
                    ModEntityUtils.canMobDestroy(this.entity)
                    && !onGround) {
                AABB bb = this.entity.getBoundingBox();
                int minx = Mth.floor(bb.minX - 0.75D);
                int miny = Mth.floor(bb.minY + 0.0D);
                int minz = Mth.floor(bb.minZ - 0.75D);
                BlockPos min = new BlockPos(minx, miny, minz);
                int maxx = Mth.floor(bb.maxX + 0.75D);
                int maxy = Mth.floor(bb.maxY + 0.15D);
                int maxz = Mth.floor(bb.maxZ + 0.75D);
                BlockPos max = new BlockPos(maxx, maxy, maxz);
                if (this.entity.level.hasChunksAt(min, max)) {
                    BlockPos.betweenClosedStream(min, max).
                            filter((pos) -> {
                                BlockState blockState = this.entity.level.getBlockState(pos);
                                return this.entity.level.getBlockEntity(pos) == null && (blockState.is(Blocks.BLUE_ICE) || blockState.is(Blocks.ICE));
                            }).
                            forEach((pos) -> this.entity.level.destroyBlock(pos, true));
                }
            }
            if (onGround) {
                this.entity.playAnimation(this.entity.smashDownAnimation);
            }
        }
    }

    public static Vec3 findTargetPoint(Entity attacker, Entity target) {
        Vec3 vec3 = target.position();
        return (new Vec3(vec3.x - attacker.getX(), vec3.y - attacker.getY(), vec3.z - attacker.getZ()));
    }
}
