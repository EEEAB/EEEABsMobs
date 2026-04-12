package com.eeeab.eeeabsmobs.sever.entity.effect;

import com.eeeab.eeeabsmobs.client.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.particle.lib.AnimData;
import com.eeeab.eeeabsmobs.client.particle.lib.component.ParticleComponent;
import com.eeeab.eeeabsmobs.client.particle.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.entity.mob.IMob;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityAbsRelicron;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicAnnihilator;
import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicObserver;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.entity.util.damage.ModDamageSource;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.function.IntFunction;

public class EntityGuardianLaser extends EntityAbsBeam {
    private static final EntityDataAccessor<Boolean> DATA_IS_PLAYER = SynchedEntityData.defineId(EntityGuardianLaser.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_USER = SynchedEntityData.defineId(EntityGuardianLaser.class, EntityDataSerializers.INT);
    @OnlyIn(Dist.CLIENT)
    private Vec3[] attractorPos;
    public final ControlledAnimation scaleControlled = new ControlledAnimation(3);

    public EntityGuardianLaser(EntityType<? extends EntityGuardianLaser> type, Level level) {
        super(type, level, 20);
        if (level.isClientSide) {
            this.attractorPos = new Vec3[]{new Vec3(0, 0, 0)};
        }
    }

    public EntityGuardianLaser(Level world, LivingEntity caster, double x, double y, double z, int duration) {
        this(EntityInit.GUARDIAN_LASER.get(), world);
        this.setOwner(caster);
        this.setYaw((float) Math.toRadians(caster.yHeadRot + 90));
        this.setPitch((float) Math.toRadians(-caster.getXRot()));
        this.setDuration(duration);
        this.setPos(x, y, z);
        int id = 0;
        if (caster instanceof EntityRelicObserver) id = 1;
        else if (caster instanceof EntityRelicAnnihilator) id = 2;
        else if (caster instanceof EntityNamelessGuardian) id = 3;
        this.getEntityData().set(DATA_USER, id);
        if (!level().isClientSide) {
            setCasterId(caster.getId());
        }
        this.calculateEndPos(getBeamLengthByUser());
    }

    @Override
    public void beamTick() {
        this.scaleControlled.updatePrevTimer();
        UserType type = getUserType();
        LivingEntity owner = this.getOwner();
        if (!this.level().isClientSide) {
            if (isPlayer() && owner instanceof Player player) {
                this.updateWithPlayer(player);
            } else if (owner != null) {
                this.updateWithEntity(owner, type.wOffset, type.hOffset);
            }
        }

        if (owner != null) {
            this.yaw = (float) Math.toRadians(owner.yHeadRot + 90);
            this.pitch = (float) -Math.toRadians(owner.getXRot());
        }

        if (level().isClientSide && tickCount <= this.getCountDown() / 2 && owner != null) {
            double radians = Math.toRadians(owner.yHeadRot + 90);
            float wOffset = Math.max(type.wOffset - 0.1F, 0F);
            double rootX = owner.getX() + Math.cos(radians) * wOffset;
            double rootY = owner.getY(type.hOffset);
            double rootZ = owner.getZ() + Math.sin(radians) * wOffset;
            this.attractorPos[0] = new Vec3(rootX, rootY, rootZ);
            ModParticleUtils.advAttractorParticle(ParticleInit.ADV_ORB.get(), owner, 12, 0.5f, 5.0f, 8, new ParticleComponent[]{
                    new ParticleComponent.Attractor(this.attractorPos, 1.6f, 0.0f, ParticleComponent.Attractor.EnumAttractorBehavior.EXPONENTIAL),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.ALPHA, AnimData.KeyTrack.startAndEnd(0f, 0.6f), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.SCALE, AnimData.KeyTrack.startAndEnd(5f, 1f), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.RED, AnimData.KeyTrack.constant(0.56f), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.GREEN, AnimData.KeyTrack.constant(0.78f), false),
                    new ParticleComponent.PropertyControl(ParticleComponent.PropertyControl.EnumParticleProperty.BLUE, AnimData.KeyTrack.constant(0.86f), false),
            }, false);
        }
        if (this.tickCount >= this.getCountDown()) {
            if (this.displayControlled.isEnd()) this.scaleControlled.increaseTimer();
            this.calculateEndPos(getBeamLengthByUser());
            List<LivingEntity> hit = raytraceEntities(level(), new Vec3(getX(), getY(), getZ()), new Vec3(endPosX, endPosY, endPosZ)).getEntities();
            if (!this.level().isClientSide) {
                if (this.tickCount % 5 == 0) {
                    this.getBeamPathBlocks().forEach(pos -> {
                        if ((ModEntityUtils.canDestroyBlock(this.level(), pos, this, 1F))) {
                            if (ModEntityUtils.canMobDestroy(this) && owner instanceof EntityNamelessGuardian guardian) {
                                this.level().destroyBlock(pos, guardian.checkCanDropItems());
                            } else if (isPlayer() && owner instanceof Player && ModConfigHandler.COMMON.items.guardianCoreConfig2.get()) {
                                this.level().destroyBlock(pos, true);
                            }
                        }
                    });
                }
                for (LivingEntity target : hit) {
                    boolean hitFlag = false;
                    if (this.tickCount % 2 != 0) return;
                    if (owner instanceof EntityNamelessGuardian guardian) {
                        hitFlag = guardian.guardianHurtTarget(ModDamageSource.laser(this, guardian, true, true), guardian, target, 0.22F, 1F, guardian.isChallengeMode(), false, false);
                    } else if (target != owner) {
                        float finalDamage = this.getDamage();
                        if (owner instanceof IMob mob) finalDamage += mob.getDamageAmountByTargetHealthPct(target);
                        hitFlag = target.hurt(ModDamageSource.laser(this, owner, getUserType().id > 1, false), finalDamage);
                    }
                    if (hitFlag) target.setSecondsOnFire(type.fireDuration);
                }
            } else this.spawnExplosionParticles();
        }
    }

    @Override
    protected void spawnExplosionParticles() {
        if (this.blockSide != null) {
            Vec3 pos = new Vec3(collidePosX, collidePosY, collidePosZ);
            for (int i = 0; i < 2; i++) {
                EntityAbsRelicron.doFractalEffect(this, pos, pos.offsetRandom(this.random, 3F), 0.1F, 0.2F);
                final float velocity = 0.2F;
                float yaw = (float) (random.nextFloat() * 2 * Math.PI);
                float motionY = random.nextFloat() * velocity;
                float motionX = velocity * Mth.cos(yaw);
                float motionZ = velocity * Mth.sin(yaw);
                level().addParticle(ParticleTypes.SMOKE, pos.x, pos.y + 0.1, pos.z, motionX, motionY, motionZ);
            }
            if (this.random.nextFloat() < 0.2F) level().addParticle(ParticleTypes.LAVA, pos.x, pos.y, pos.z, 0, 0, 0);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_PLAYER, false);
        this.entityData.define(DATA_USER, 0);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 4096;
    }

    @Override
    protected boolean canBeInterrupted() {
        return super.canBeInterrupted() || (this.getOwner() instanceof IMob mob && mob.isStunned());
    }

    @Override
    protected float getDamage() {
        return switch (getUserType()) {
            case PLAYER -> ModConfigHandler.COMMON.items.guardianLaser.damage.get().floatValue();
            case RELIC_OBSERVER -> ModConfigHandler.COMMON.mobs.relicrons.relicObserver.guardianLaser.damage.get().floatValue();
            case RELIC_ANNIHILATOR -> ModConfigHandler.COMMON.mobs.relicrons.relicAnnihilator.guardianLaser.damage.get().floatValue();
            case NAMELESS_GUARDIAN -> ModConfigHandler.COMMON.mobs.relicrons.namelessGuardian.guardianLaser.damage.get().floatValue();
        };
    }

    public UserType getUserType() {
        return UserType.byId(getEntityData().get(DATA_USER));
    }

    public boolean isPlayer() {
        return getUserType() == UserType.PLAYER;
    }

    public double getBeamLengthByUser() {
        UserType type = getUserType();
        if (type == UserType.PLAYER) return ModConfigHandler.COMMON.items.guardianCoreConfig1.get();
        return type.beamLength;
    }

    private void updateWithPlayer(Player player) {
        this.setYaw((float) Math.toRadians(player.yHeadRot + 90));
        this.setPitch((float) Math.toRadians(-player.getXRot()));
        Vec3 vecOffset = player.getLookAngle().normalize().scale(1);
        this.setPos(player.getX() + vecOffset.x(), player.getY() + player.getBbHeight() * 0.5F + vecOffset.y(), player.getZ() + vecOffset.z());
    }

    public enum UserType {
        PLAYER(0, 0F, 0.5F, 3, 0.8F, 0.6F),
        RELIC_OBSERVER(1, 0F, 0.75F, 3, 0.45F, 0.4F, 10),
        RELIC_ANNIHILATOR(2, 1.2F, 0.6F, 5, 0.6F, 0.525F),
        NAMELESS_GUARDIAN(3, 1.35F, 0.8F, 5, 1.09F, 0.9F);

        private static final IntFunction<EntityGuardianLaser.UserType> BY_ID = ByIdMap.sparse(c -> c.id, values(), PLAYER);
        public final int id;
        public final float wOffset;
        public final float hOffset;
        public final int fireDuration;
        public final float radius;
        public final float beamRadius;
        public final double beamLength;

        UserType(int id, float wOffset, float hOffset, int fireDuration, float radius, float beamRadius) {
            this(id, wOffset, hOffset, fireDuration, radius, beamRadius, 32);
        }

        UserType(int id, float wOffset, float hOffset, int fireDuration, float radius, float beamRadius, float beamLength) {
            this.id = id;
            this.wOffset = wOffset;
            this.hOffset = hOffset;
            this.fireDuration = fireDuration;
            this.radius = radius;
            this.beamRadius = beamRadius;
            this.beamLength = beamLength;
        }

        public static EntityGuardianLaser.UserType byId(int id) {
            return BY_ID.apply(id);
        }
    }
}
