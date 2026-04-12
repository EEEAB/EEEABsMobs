package com.eeeab.eeeabsmobs.sever.entity.effect;

import com.eeeab.eeeabsmobs.sever.entity.mob.IMob;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicAnnihilator;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicAnnihilatorPart;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityInfraredRay extends EntityAbsBeam {
    public static final double RELICRON_RADIUS = 32;

    public EntityInfraredRay(EntityType<? extends EntityInfraredRay> type, Level level) {
        super(type, level, 1);
    }

    public EntityInfraredRay(Level world, LivingEntity caster, double x, double y, double z, int duration) {
        this(EntityInit.INFRARED_RAY.get(), world);
        this.setOwner(caster);
        this.setYaw((float) Math.toRadians(caster.yHeadRot + 90));
        this.setPitch((float) Math.toRadians(-caster.getXRot()));
        this.setDuration(duration);
        this.setPos(x, y, z);
        this.calculateEndPos(RELICRON_RADIUS);
        if (!level().isClientSide) {
            setCasterId(caster.getId());
        }
    }

    @Override
    protected void beamTick() {
        LivingEntity owner = this.getOwner();
        if (owner instanceof EntityRelicAnnihilator annihilator) {
            if (!this.level().isClientSide) {
                double radians = Math.toRadians(annihilator.yBodyRot + 90);
                this.setYaw((float) radians);
                this.setPitch((float) ((double) (-owner.getXRot()) * Math.PI / 180.0));
                EntityRelicAnnihilatorPart part = annihilator.getPartEntity();
                this.setPos(part.getX(), part.getY(0.24), part.getZ());
            }
        }

        if (this.tickCount >= this.getCountDown()) {
            calculateEndPos(RELICRON_RADIUS);
            raytraceEntities(level(), new Vec3(getX(), getY(), getZ()), new Vec3(endPosX, endPosY, endPosZ));
            if (!this.level().isClientSide && this.isBlinking() && this.tickCount % 3 == 0) {
                this.playSound(SoundInit.INFRARED_RAY.get(), 1F, this.getVoicePitch());
            }
        }
    }

    @Override
    protected boolean canBeInterrupted() {
        return super.canBeInterrupted() || (this.getOwner() instanceof IMob mob && mob.isStunned());
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 4096;
    }

    private float getVoicePitch() {
        int timeSinceStart = this.tickCount - this.getCountDown();
        if (timeSinceStart < 0 || timeSinceStart > 6) {
            return 1F;
        }
        float minPitch = 1F;
        float maxPitch = 3F;
        float progress = (float) timeSinceStart / 6F;
        return minPitch + (maxPitch - minPitch) * progress;
    }

    public boolean isBlinking() {
        int timeSinceStart = this.tickCount - this.getCountDown();
        int blinkStart = this.getDuration() / 3;
        if (timeSinceStart < blinkStart) return false;
        int blinkElapsed = timeSinceStart - blinkStart;
        int remainingTime = this.getDuration() - blinkStart;
        int stageDuration = remainingTime / 3;
        if (stageDuration == 0) return false;
        int stage = Math.min(blinkElapsed / stageDuration, 2);
        int interval = switch (stage) {
            case 0 -> 10;
            case 1 -> 5;
            default -> 2;
        };
        return this.tickCount % interval < interval / 2;
    }
}
