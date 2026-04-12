package com.eeeab.eeeabsmobs.sever.ability.abilities;

import com.eeeab.eeeabsmobs.sever.ability.Ability;
import com.eeeab.eeeabsmobs.sever.ability.AbilityPeriod;
import com.eeeab.eeeabsmobs.sever.ability.AbilityType;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntitySurge;
import com.eeeab.eeeabsmobs.sever.entity.util.ModEntityUtils;
import com.eeeab.eeeabsmobs.sever.item.ItemChainsword;
import com.eeeab.eeeabsmobs.sever.item.ItemDoomboltAxe;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class DoomboltAxeAbility extends Ability<Player> {

    public DoomboltAxeAbility(AbilityType<Player, ? extends Ability<?>> abilityType, Player user) {
        super(abilityType, user, new AbilityPeriod[]{
                new AbilityPeriod.AbilityPeriodInstant(AbilityPeriod.AbilityPeriodType.ACTIVE)
        }, 10);
    }

    @Override
    public void start() {
        super.start();
        Player user = this.getUser();
        if (user == null) return;
        ItemStack itemStack = user.getMainHandItem();
        if (!(itemStack.getItem() instanceof ItemDoomboltAxe)) return;
        if (!user.level().isClientSide) {
            Vec3 lookAngle = user.getLookAngle();
            Vec3[] vec3s = new Vec3[]{lookAngle.yRot(0.5F), lookAngle, lookAngle.yRot(-0.5F)};
            double reach = 2;
            int baseCount = 5;
            double spacing = 1.2D;
            for (Vec3 vec3 : vec3s) {
                for (int i = 0; i < baseCount; i++) {
                    double distance = spacing * (i + 1.5);
                    float f0 = (float) Mth.atan2(vec3.z, vec3.x);
                    double x = user.getX() + Mth.cos(f0) * distance;
                    double y = user.getY();
                    double z = user.getZ() + Mth.sin(f0) * distance;
                    Vec3 point = ModEntityUtils.checkSummonEntityPointNullable(user.level(), x, z, y - reach, y + reach);
                    if (point == null) point = new Vec3(x, y, z);
                    EntitySurge surge = new EntitySurge(user.level(), point.x, point.y, point.z, f0, i * 4, itemStack.copy(), user);
                    user.level().addFreshEntity(surge);
                }
            }
        }
    }
}
