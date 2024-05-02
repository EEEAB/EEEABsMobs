package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

//TODO 待优化 部分参数可配置 延迟爆炸(爆炸前要有蓄力过程)
public class EntityGrenade extends EntityMagicEffects {
    private static final float GRAVITY = 0.03F;
    private static final float RADIUS = 2.5F;

    public EntityGrenade(EntityType<? extends EntityMagicEffects> entityType, Level level) {
        super(entityType, level);
    }

    public EntityGrenade(Level level, LivingEntity caster) {
        super(EntityInit.GRENADE.get(), level);
        this.caster = caster;
    }

    @Override
    public void tick() {
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS) {
            if (!this.level().isClientSide) {
                Vec3 position = this.position();
                this.level().explode(this, this.damageSources().explosion(this, this.caster), null, this.position(), RADIUS, false, Level.ExplosionInteraction.NONE);
                EntityCameraShake.cameraShake(this.level(), position, 16F, 0.125F, 10, 10);
                this.discard();
            }
        }
        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        if (this.isInWater()) {
            for (int i = 0; i < 2; ++i) {
                this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25D, d1 - vec3.y * 0.25D, d2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
            }
        }
        for (int i = 0; i < 4; ++i) {
            this.level().addParticle(ParticleTypes.SMOKE, d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);
        }
        if (!this.isNoGravity()) {
            Vec3 vec31 = this.getDeltaMovement();
            this.setDeltaMovement(vec31.x, vec31.y - GRAVITY, vec31.z);
        }
        this.setPos(d0, d1, d2);
    }

    protected boolean canHitEntity(Entity entity) {
        if (!entity.canBeHitByProjectile()) {
            return false;
        } else {
            return caster == null || !caster.isPassengerOfSameVehicle(entity);
        }
    }

    @Override
    protected void defineSynchedData() {
    }
}
