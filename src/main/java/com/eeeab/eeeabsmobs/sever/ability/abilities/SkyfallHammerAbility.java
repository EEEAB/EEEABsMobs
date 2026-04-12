package com.eeeab.eeeabsmobs.sever.ability.abilities;

import com.eeeab.eeeabsmobs.sever.ability.Ability;
import com.eeeab.eeeabsmobs.sever.ability.AbilityPeriod;
import com.eeeab.eeeabsmobs.sever.ability.AbilityType;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.entity.util.ShockWaveUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

public class SkyfallHammerAbility extends Ability<Player> {
    private static final double MIN_HEIGHT = 2.0;
    private static final double MAX_HEIGHT = 100;
    private static final double HEIGHT_PHASE_THRESHOLD = 10;
    private static final double MAX_FALL_SPEED = -4;
    private static final double INITIAL_DOWNWARD_SPEED = -1.2;
    private static final float ACCELERATION_RATE = 0.1F;

    private double startY;
    private double maxFallSpeed;
    private boolean isFalling;
    private float accumulatedAcceleration;
    private double gravity;

    public SkyfallHammerAbility(AbilityType<Player, ? extends Ability<?>> abilityType, Player user) {
        super(abilityType, user, new AbilityPeriod[]{
                new AbilityPeriod.AbilityPeriodInfinite(AbilityPeriod.AbilityPeriodType.STARTUP),
                new AbilityPeriod.AbilityPeriodInfinite(AbilityPeriod.AbilityPeriodType.ACTIVE),
                new AbilityPeriod.AbilityPeriodInfinite(AbilityPeriod.AbilityPeriodType.RECOVERY)
        }, 0);
    }

    @Override
    public void start() {
        super.start();
        this.isFalling = true;
        Player player = getUser();
        this.startY = player.getY();
        this.accumulatedAcceleration = 0;
        this.gravity = player.getAttributeValue(ForgeMod.ENTITY_GRAVITY.get());
    }

    @Override
    protected void tickUsing() {
        AbilityPeriod.AbilityPeriodType type = getSection().periodType;
        if (type == AbilityPeriod.AbilityPeriodType.STARTUP) {
            applyInitialDownwardSpeed();
            nextPeriod();
        } else if (type == AbilityPeriod.AbilityPeriodType.ACTIVE) {
            applyContinuousAcceleration();
            if (getUser().onGround()) {
                calculateImpact();
                nextPeriod();
            }
        } else nextPeriod();
    }

    @Override
    public void end() {
        super.end();
        this.isFalling = false;
    }

    /**
     * 应用初始下坠速度
     */
    private void applyInitialDownwardSpeed() {
        Player player = getUser();
        player.setDeltaMovement(0, INITIAL_DOWNWARD_SPEED, 0);
        maxFallSpeed = INITIAL_DOWNWARD_SPEED;
    }

    /**
     * 应用持续加速效果
     */
    private void applyContinuousAcceleration() {
        Player player = getUser();
        Vec3 currentMotion = player.getDeltaMovement();
        accumulatedAcceleration += ACCELERATION_RATE;
        double extraAcceleration = ACCELERATION_RATE * 2;
        double fallTimeFactor = Math.min(getTick() / 20.0, 2.0);
        double totalAcceleration = gravity + extraAcceleration * (1.0 + fallTimeFactor * 0.5);
        double newY = currentMotion.y - totalAcceleration;
        newY = Math.max(newY, MAX_FALL_SPEED);
        player.setDeltaMovement(currentMotion.x, newY, currentMotion.z);
        maxFallSpeed = Math.min(maxFallSpeed, newY);
    }

    /**
     * 计算落地伤害和效果
     */
    private void calculateImpact() {
        Player player = getUser();
        double fallDistance = startY - player.getY();
        fallDistance = Math.min(fallDistance, MAX_HEIGHT);
        float damage = calculateDamage(player, fallDistance);
        double t = Math.min((fallDistance - MIN_HEIGHT) / (MAX_HEIGHT - MIN_HEIGHT), 1.0);
        float radius = calculateHeightMultiplier(fallDistance) * 3;
        applyImpactEffects(damage, radius, player);
        setMaxCoolingTick((int) ((1.0 + t * 9.0) * 20));
    }

    /**
     * 计算伤害
     */
    private float calculateDamage(Player player, double fallDistance) {
        float damage = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float heightMultiplier = calculateHeightMultiplier(fallDistance);
        return damage * heightMultiplier;
    }

    /**
     * 计算高度加成系数
     */
    private float calculateHeightMultiplier(double fallDistance) {
        if (fallDistance <= HEIGHT_PHASE_THRESHOLD) {
            return 1.0F + 0.5F * (float) (fallDistance / HEIGHT_PHASE_THRESHOLD);
        } else {
            double remainingHeight = Math.min(fallDistance - HEIGHT_PHASE_THRESHOLD, 90);
            return 1.5F + 1.5F * (float) (remainingHeight / 90);
        }
    }

    private void applyImpactEffects(float damage, float radius, Player player) {
        int layerCount = Math.max(1, (int) radius);
        for (int i = 1; i <= layerCount; i++) {
            float currentRadius = (float) i / layerCount * radius;
            for (Entity hit : ShockWaveUtils.doAdvShockWave(player, (int) currentRadius, 3F, 2F, 0, 2, false, false, 0)) {
                if (hit instanceof LivingEntity entityHit) {
                    float distance = player.distanceTo(entityHit);
                    float distanceFactor = 1.0F - Math.min(distance / radius, 1.0F);
                    float finalDamage = damage * distanceFactor;
                    entityHit.hurt(player.damageSources().playerAttack(player), finalDamage);
                    float radiusFactor = currentRadius / radius;
                    float knockbackStrength = distanceFactor * (1.0F + accumulatedAcceleration * 0.1F) * (1.0F + radiusFactor * 0.5F);
                    Vec3 knockbackDir = player.position().subtract(entityHit.position()).normalize();
                    ModEntityUtils.forceKnockBack(player, entityHit, knockbackStrength, knockbackDir.x, knockbackDir.z, true);
                }
            }
        }
    }

    public boolean isFalling() {
        return isFalling;
    }

    public static boolean canPlayerUseAbility(Player player) {
        if (player == null || player.onGround()) return false;
        if (player.getAbilities().flying || player.isSpectator()) {
            return false;
        }
        if (player.isInWater() || player.isInLava() || player.isInFluidType()) {
            return false;
        }
        if (player.isFallFlying() || player.isPassenger() || player.onClimbable()) {
            return false;
        }
        int groundY = player.level().getHeight(Heightmap.Types.MOTION_BLOCKING,
                (int) player.getX(),
                (int) player.getZ());
        return player.getY() - (groundY + 1.0) >= MIN_HEIGHT;
    }
}