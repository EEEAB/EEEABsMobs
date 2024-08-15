package com.eeeab.eeeabsmobs.sever.entity.ai.navigate;

import com.eeeab.eeeabsmobs.sever.entity.EEEABMobEntity;
import com.eeeab.eeeabsmobs.sever.entity.ai.pathfinder.EMPathFinder;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

//基于自: https://github.com/BobMowzie/MowziesMobs/blob/master/src/main/java/com/bobmowzie/mowziesmobs/server/ai/MMPathNavigateGround.java
public class EMPathNavigateGround extends GroundPathNavigation {
    private static final float EPSILON = 1.0E-8F;

    public EMPathNavigateGround(EEEABMobEntity entity, Level world) {
        super(entity, world);
    }

    @Override
    protected @NotNull PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new WalkNodeEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);
        return new EMPathFinder(this.nodeEvaluator, maxVisitedNodes);
    }

    @Override
    protected void followThePath() {
        Path path = Objects.requireNonNull(this.path);
        Vec3 entityPos = this.getTempMobPos();
        int pathLength = path.getNodeCount();
        for (int i = path.getNextNodeIndex(); i < path.getNodeCount(); ++i) {
            if ((double) path.getNode(i).y != Math.floor(entityPos.y)) {
                pathLength = i;
                break;
            }
        }
        Vec3 base = entityPos.add(-this.mob.getBbWidth() * 0.5F, 0.0F, -this.mob.getBbWidth() * 0.5F);
        Vec3 max = base.add(this.mob.getBbWidth(), this.mob.getBbHeight(), this.mob.getBbWidth());
        this.maxDistanceToWaypoint = this.mob.getBbWidth() > 0.75F ? this.mob.getBbWidth() / 2.0F : 0.75F - this.mob.getBbWidth() / 2.0F;
        if (this.tryShortcut(path, new Vec3(this.mob.getX(), this.mob.getY(), this.mob.getZ()), pathLength, base, max) && (this.isAt(path, 0.5F) || this.atElevationChange(path) && this.isAt(path, this.maxDistanceToWaypoint))) {
            path.setNextNodeIndex(path.getNextNodeIndex() + 1);
        }
        this.doStuckDetection(entityPos);
    }

    private boolean isAt(Path path, float threshold) {
        final Vec3i pathPos = path.getNextNodePos();
        return Mth.abs((float) (this.mob.getX() - (pathPos.getX() + (this.mob.getBbWidth() + 1) / 2D))) <= threshold &&
                Mth.abs((float) (this.mob.getZ() - (pathPos.getZ() + (this.mob.getBbWidth() + 1) / 2D))) <= threshold &&
                Math.abs(this.mob.getY() - pathPos.getY()) < 1.0D;
    }

    private boolean atElevationChange(Path path) {
        final int curr = path.getNextNodeIndex();
        final int end = Math.min(path.getNodeCount(), curr + Mth.ceil(this.mob.getBbWidth() * 0.5F) + 1);
        final int currY = path.getNode(curr).y;
        for (int i = curr + 1; i < end; i++) {
            if (path.getNode(i).y != currY) {
                return true;
            }
        }
        return false;
    }

    private boolean tryShortcut(Path path, Vec3 entityPos, int pathLength, Vec3 base, Vec3 max) {
        for (int i = pathLength; --i > path.getNextNodeIndex(); ) {
            final Vec3 vec = path.getEntityPosAtNode(this.mob, i).subtract(entityPos);
            if (this.sweep(vec, base, max)) {
                path.setNextNodeIndex(i);
                return false;
            }
        }
        return true;
    }

    private boolean sweep(Vec3 vec, Vec3 base, Vec3 max) {
        float max_t = (float) vec.length();
        if (!(max_t < EPSILON)) {
            float[] tr = new float[3];
            int[] ldi = new int[3];
            int[] tri = new int[3];
            int[] step = new int[3];
            float[] tDelta = new float[3];
            float[] tNext = new float[3];
            float[] normed = new float[3];

            for (int i = 0; i < 3; ++i) {
                float value = element(vec, i);
                boolean dir = value >= 0.0F;
                step[i] = dir ? 1 : -1;
                float lead = element(dir ? max : base, i);
                tr[i] = element(dir ? base : max, i);
                ldi[i] = leadEdgeToInt(lead, step[i]);
                tri[i] = trailEdgeToInt(tr[i], step[i]);
                normed[i] = value / max_t;
                tDelta[i] = Mth.abs(max_t / value);
                float dist = dir ? (float) (ldi[i] + 1) - lead : lead - (float) ldi[i];
                tNext[i] = tDelta[i] < Float.POSITIVE_INFINITY ? tDelta[i] * dist : Float.POSITIVE_INFINITY;
            }
        }
        return true;
    }

    static int leadEdgeToInt(float lead, int step) {
        return Mth.floor(lead - step * EPSILON);
    }

    static int trailEdgeToInt(float lead, int step) {
        return Mth.floor(lead + step * EPSILON);
    }

    static float element(Vec3 v, int i) {
        return switch (i) {
            case 0 -> (float) v.x;
            case 1 -> (float) v.y;
            case 2 -> (float) v.z;
            default -> 0.0F;
        };
    }
}

