package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityGrenade extends EntityMagicEffects {
    private static final float GRAVITY = 0.03F;
    private static final float RADIUS = 2.5F;
    private float maxDamage = 10F;

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
            if (this.caster != null && !(this.caster instanceof Player)) {
                //基于使用者(非玩家)决定攻击伤害
                maxDamage = (float) this.caster.getAttributeValue(Attributes.ATTACK_DAMAGE);
            }
            EntityExplode.explode(this.level(), this.position(), this.damageSources().explosion(this, this.caster), this.caster, RADIUS, maxDamage);
            EntityCameraShake.cameraShake(this.level(), this.position(), 16F, 0.125F, 5, 15);
            this.discard();
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
        if (this.tickCount > 3) {
            for (int i = 0; i < 4; ++i) {
                double dx = this.getX() + this.random.nextGaussian() * 0.15D;
                double dy = this.getY() + this.random.nextGaussian() * 0.15D;
                double dz = this.getZ() + this.random.nextGaussian() * 0.15D;
                this.level().addParticle(ParticleInit.GUARDIAN_SPARK.get(), dx, dy, dz, -this.getDeltaMovement().x() * 0.25F, -this.getDeltaMovement().y() * 0.25F, -this.getDeltaMovement().z() * 0.25F);
            }
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
