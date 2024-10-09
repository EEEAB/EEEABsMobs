package com.eeeab.eeeabsmobs.sever.ability.abilities;

import com.eeeab.eeeabsmobs.sever.ability.Ability;
import com.eeeab.eeeabsmobs.sever.ability.AbilityPeriod;
import com.eeeab.eeeabsmobs.sever.ability.AbilityType;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityGuardianBlade;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
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
        if (!user.level().isClientSide) {
            Vec3 lookAngle = user.getLookAngle();
            Vec3[] vec3s = new Vec3[]{lookAngle.yRot(0.5F), lookAngle, lookAngle.yRot(-0.5F)};
            Vec3 point = ModEntityUtils.checkSummonEntityPoint(user, user.getX(), user.getZ(), user.getY(-5), user.getY());
            for (Vec3 vec3 : vec3s) {
                float f0 = (float) Mth.atan2(vec3.z, vec3.x);
                double x = point.x + Mth.cos(f0) * 3.0D;
                double y = point.y;
                double z = point.z + Mth.sin(f0) * 3.0D;
                EntityGuardianBlade blade = new EntityGuardianBlade(user.level(), user, x, y, z, f0, false);
                user.level().addFreshEntity(blade);
            }
        }
    }
}
