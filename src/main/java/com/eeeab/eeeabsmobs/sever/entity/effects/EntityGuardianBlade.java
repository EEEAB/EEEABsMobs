package com.eeeab.eeeabsmobs.sever.entity.effects;

import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.entity.guling.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityGuardianBlade extends EntityMagicEffects {
    public final ControlledAnimation controlled = new ControlledAnimation(30);
    private boolean moveOffset;
    private float damage = 1F;
    private static final float[][] BLOCK_OFFSETS = {
            {-0.5F, -0.5F},
            {-0.5F, 0.5F},
            {0.5F, 0.5F},
            {0.5F, -0.5F},
    };


    public EntityGuardianBlade(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }


    public EntityGuardianBlade(Level level, LivingEntity caster, double x, double y, double z, float yRot, boolean moveOffset) {
        this(EntityInit.GUARDIAN_BLADE.get(), level);
        this.damage = (float) caster.getAttributeValue(Attributes.ATTACK_DAMAGE) + EnchantmentHelper.getDamageBonus(caster.getMainHandItem(), caster.getMobType());
        this.setYRot((yRot * (180F / (float) Math.PI)) - 90F);
        this.moveOffset = moveOffset;
        this.caster = caster;
        this.setPos(x, y, z);
    }

    @Override
    protected float getEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.5F;
    }

    @Override
    public void tick() {
        super.tick();
        this.controlled.updatePrevTimer();
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.controlled.isStop()) {
            Vec3 lookAngle = this.getLookAngle();
            float speed = 1.35F;
            if (moveOffset) speed += this.random.nextFloat() * 0.5F;
            this.shoot(lookAngle.x, lookAngle.y, lookAngle.z, speed);
            this.controlled.increaseTimer(1);
        } else if (this.controlled.increaseTimerChain().getTimer() % 5 == 0)
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5));

        if (this.controlled.isEnd() || this.tickCount > this.controlled.getDuration() * 2) {
            this.discard();
        } else {
            this.breakBlockEffect();
            this.doHurtTarget();
        }
    }

    private void shoot(double x, double y, double z, double speed) {
        this.setDeltaMovement(x * speed, y * speed, z * speed);
    }

    private void doHurtTarget() {
        if (!this.level.isClientSide) {
            float progress = 1F - this.controlled.getAnimationFraction();
            List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.2));
            for (LivingEntity target : entities) {
                if (target == caster) continue;
                if (caster instanceof EntityNamelessGuardian) damage += target.getMaxHealth() * 0.05F;
                damage = Math.max(1, damage * progress);
                damage = ModEntityUtils.actualDamageIsCalculatedBasedOnArmor(damage, target.getArmorValue(), (float) target.getAttributeValue(Attributes.ARMOR_TOUGHNESS), 1F);
                target.hurt(DamageSource.indirectMagic(this, caster), damage);
            }
        }
    }

    private void breakBlockEffect() {
        if (this.level.isClientSide) {
            double theta = Math.toRadians(this.getYRot());
            double x = getX() + Math.cos(theta + Math.PI / 2);
            double y = getBoundingBox().minY + 0.1;
            double z = getZ() + Math.sin(theta + Math.PI / 2);
            int count = (int) Math.floor(15 * (Math.max(1F - (this.controlled.getAnimationFraction() + 0.2F), 0F)));
            ModParticleUtils.generateParticleEffects(level, x, y, z, theta, count, BLOCK_OFFSETS, pos -> level.getBlockState(pos), 1F);
            if (count > 3 && this.controlled.getTimer() % 2 == 0) {
                Vec3 movement = this.getDeltaMovement();
                level.addParticle(ParticleInit.GUARDIAN_SPARK.get(), getRandomX(0.25F), getRandomY(), getRandomZ(0.25F), movement.x * 0.25, this.random.nextFloat() * 0.05F, movement.z * 0.25);
            }
        }
    }
}
