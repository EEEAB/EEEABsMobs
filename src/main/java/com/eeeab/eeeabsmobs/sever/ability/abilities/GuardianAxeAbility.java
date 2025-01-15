package com.eeeab.eeeabsmobs.sever.ability.abilities;

import com.eeeab.eeeabsmobs.client.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.ability.Ability;
import com.eeeab.eeeabsmobs.sever.ability.AbilityPeriod;
import com.eeeab.eeeabsmobs.sever.ability.AbilityType;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityGuardianBlade;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class GuardianAxeAbility extends Ability<Player> {

    public GuardianAxeAbility(AbilityType<Player, ? extends Ability<?>> abilityType, Player user) {
        super(abilityType, user, new AbilityPeriod[]{
                new AbilityPeriod.AbilityPeriodInstant(AbilityPeriod.AbilityPeriodType.ACTIVE)
        }, 10);
    }

    @Override
    public void start() {
        super.start();
        Player user = this.getUser();
        BlockHitResult result = getPlayerPOVHitResult(user.level(), user, ClipContext.Fluid.NONE);
        double x, y, z;
        BlockPos blockPos = result.getBlockPos();
        if (user.position().distanceTo(blockPos.getCenter()) > 5) {
            float f0 = (float) Math.toRadians(user.getYRot() + 90);
            x = user.getX() + Mth.cos(f0) * 3.0D;
            y = user.getY() + 0.1D;
            z = user.getZ() + Mth.sin(f0) * 3.0D;
        } else {
            x = blockPos.getX();
            y = blockPos.getY();
            z = blockPos.getZ();
        }
        ModParticleUtils.roundParticleOutburst(user.level(), 40, new ParticleOptions[]{ParticleInit.GUARDIAN_SPARK.get(), ParticleTypes.SOUL_FIRE_FLAME}, x, y, z, 0.3F);
        if (!user.level().isClientSide) {
            Vec3 lookAngle = user.getLookAngle();
            Vec3[] vec3s = new Vec3[]{lookAngle.yRot(0.5F), lookAngle, lookAngle.yRot(-0.5F)};
            Vec3 point = ModEntityUtils.checkSummonEntityPoint(user, user.getX(), user.getZ(), y - 5, y);
            for (Vec3 vec3 : vec3s) {
                float f0 = (float) Mth.atan2(vec3.z, vec3.x);
                float f1 = 1F + user.getBbWidth();
                x = point.x + Mth.cos(f0) * f1;
                y = point.y;
                z = point.z + Mth.sin(f0) * f1;
                EntityGuardianBlade blade = new EntityGuardianBlade(user.level(), user, x, y, z, f0, false);
                user.level().addFreshEntity(blade);
            }
        }
    }

    //复制自: net.minecraft.world.item.Item
    private static BlockHitResult getPlayerPOVHitResult(Level level, Player player, ClipContext.Fluid fluidMode) {
        float f = player.getXRot();
        float f1 = player.getYRot();
        Vec3 vec3 = player.getEyePosition();
        float f2 = Mth.cos(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f3 = Mth.sin(-f1 * ((float) Math.PI / 180F) - (float) Math.PI);
        float f4 = -Mth.cos(-f * ((float) Math.PI / 180F));
        float f5 = Mth.sin(-f * ((float) Math.PI / 180F));
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = player.getBlockReach();
        Vec3 vec31 = vec3.add((double) f6 * d0, (double) f5 * d0, (double) f7 * d0);
        return level.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE, fluidMode, player));
    }
}
