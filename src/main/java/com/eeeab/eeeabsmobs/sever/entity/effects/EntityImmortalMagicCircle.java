package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.client.particle.base.ParticleOrb;
import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.util.EMMathUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EntityImmortalMagicCircle extends EntityMagicEffects {
    private static final EntityDataAccessor<Float> DATA_SCALE = SynchedEntityData.defineId(EntityImmortalMagicCircle.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_SPEED = SynchedEntityData.defineId(EntityImmortalMagicCircle.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_YAW = SynchedEntityData.defineId(EntityImmortalMagicCircle.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_DURATION = SynchedEntityData.defineId(EntityImmortalMagicCircle.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<String> DATA_TYPE = SynchedEntityData.defineId(EntityImmortalMagicCircle.class, EntityDataSerializers.STRING);
    public final ControlledAnimation processController = new ControlledAnimation(5);
    public boolean NO = true;

    public enum MagicCircleType {
        NONE,
        SPEED(0.22F, 0.76F, 0.87F, MobEffects.MOVEMENT_SPEED, MobEffects.DIG_SPEED),
        POWER(0.92F, 0.7F, 0.02F, MobEffects.DAMAGE_BOOST),
        HARMFUL(0.52F, 0.16F, 0.88F, MobEffects.MOVEMENT_SLOWDOWN, MobEffects.WEAKNESS),
        BENEFICIAL(0F, 0.72F, 0.47F, MobEffects.REGENERATION, MobEffects.ABSORPTION);

        MagicCircleType() {
            this.r = this.g = this.b = 1F;
            this.effect = new MobEffect[0];
        }

        MagicCircleType(float r, float g, float b, MobEffect... effect) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.effect = effect;
        }

        public final float r;
        public final float g;
        public final float b;
        public final MobEffect[] effect;
    }

    public EntityImmortalMagicCircle(EntityType<EntityImmortalMagicCircle> type, Level level) {
        super(type, level);
    }

    public EntityImmortalMagicCircle(Level level) {
        super(EntityInit.MAGIC_CIRCLE.get(), level);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        processController.updatePrevTimer();

        if (NO && processController.increaseTimerChain().isEnd()) {
            NO = false;
        }

        if (!NO) {
            if (tickCount > getDuration()) {
                if (processController.decreaseTimerChain().isStop()) {
                    discard();
                }
            } else {
                if (!level().isClientSide && tickCount % 5 == 0) {
                    MobEffect[] effects = this.getMagicCircleType().effect;
                    if (effects.length > 0) {
                        for (LivingEntity inRange : level().getEntitiesOfClass(LivingEntity.class, ModEntityUtils.makeAABBWithSize(getX(), getY(), getZ(), 0, getScale(), 1, getScale()))) {
                            for (MobEffect effect : effects) {
                                if (inRange.hasEffect(effect)) {
                                    MobEffectInstance instance = inRange.getEffect(effect);
                                    if (instance != null && instance.getAmplifier() >= 1) continue;
                                }
                                ModEntityUtils.addEffectStackingAmplifier(inRange, effect, getDuration(), 2, true, true, true, false);
                            }
                        }
                    }
                }
                if (level().isClientSide && this.random.nextInt(4) == 0) {
                    float factor = EMMathUtils.getTickFactor(tickCount, getDuration(), true);
                    for (float i = 0; i < 10 * factor; i++) {
                        double x = this.getX() + Mth.randomBetween(this.random, -getScale(), getScale());
                        double z = this.getZ() + Mth.randomBetween(this.random, -getScale(), getScale());
                        MagicCircleType type = getMagicCircleType();
                        ParticleOrb.OrbData orbData = new ParticleOrb.OrbData(type.r, type.g, type.b, 2F, (int) (15 + 5F * this.random.nextFloat()));
                        level().addParticle(orbData, x, this.getY() + 0.1, z, 0, 0.2 + this.random.nextGaussian() * 0.02D, 0);
                    }
                }
            }
        }
    }

    @Override
    public void setDeltaMovement(@NotNull Vec3 deltaMovement) {
    }

    @Override
    public void setDeltaMovement(double x, double y, double z) {
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SCALE, 2F);
        this.entityData.define(DATA_SPEED, 0F);
        this.entityData.define(DATA_YAW, 0F);
        this.entityData.define(DATA_DURATION, 20);
        this.entityData.define(DATA_TYPE, MagicCircleType.NONE.toString());
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putFloat("scale", getScale());
        compoundTag.putFloat("speed", getSpeed());
        compoundTag.putFloat("yaw", getYaw());
        compoundTag.putInt("duration", getDuration());
        compoundTag.putString("magicType", getMagicCircleType().toString());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        setScale(compoundTag.getFloat("scale"));
        setSpeed(compoundTag.getFloat("speed"));
        setYaw(compoundTag.getFloat("yaw"));
        setDuration(compoundTag.getInt("duration"));
        try {
            setMagicCircleType(MagicCircleType.valueOf(compoundTag.getString("magicType")));
        } catch (Exception ignored) {
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 2048;
    }

    public float getScale() {
        return entityData.get(DATA_SCALE);
    }

    public void setScale(float scale) {
        entityData.set(DATA_SCALE, scale);
    }

    public float getSpeed() {
        return entityData.get(DATA_SPEED);
    }

    public void setSpeed(float speed) {
        entityData.set(DATA_SPEED, speed);
    }

    public float getYaw() {
        return entityData.get(DATA_YAW);
    }

    public void setYaw(float yaw) {
        entityData.set(DATA_YAW, yaw);
    }

    public int getDuration() {
        return entityData.get(DATA_DURATION);
    }

    public void setDuration(int duration) {
        entityData.set(DATA_DURATION, duration);
    }

    public MagicCircleType getMagicCircleType() {
        String type = entityData.get(DATA_TYPE);
        if (type.isEmpty()) return MagicCircleType.NONE;
        return MagicCircleType.valueOf(type);
    }

    public void setMagicCircleType(MagicCircleType type) {
        entityData.set(DATA_TYPE, type.toString());
    }

    public static void spawn(Level level, Vec3 pos, float scale, float speed, int duration, float yaw, MagicCircleType type) {
        if (!level.isClientSide) {
            EntityImmortalMagicCircle entity = new EntityImmortalMagicCircle(level);
            entity.setScale(scale);
            entity.setSpeed(speed);
            entity.setDuration(10 + duration);
            entity.setMagicCircleType(type);
            entity.setPos(pos);
            entity.setYaw(yaw);
            level.addFreshEntity(entity);
        }
    }
}
