package com.eeeab.eeeabsmobs.sever.ability.abilities;

import com.eeeab.eeeabsmobs.sever.ability.Ability;
import com.eeeab.eeeabsmobs.sever.ability.AbilityPeriod;
import com.eeeab.eeeabsmobs.sever.ability.AbilityType;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityGrenade;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class HowitzerAbility extends Ability<Player> {

    public HowitzerAbility(AbilityType<Player, ? extends Ability<Player>> abilityType, Player user) {
        super(abilityType, user, new AbilityPeriod[]{
                new AbilityPeriod.AbilityPeriodInstant(AbilityPeriod.AbilityPeriodType.ACTIVE)
        }, 0);
    }

    @Override
    public void start() {
        super.start();
        Player user = this.getUser();
        if (!user.level().isClientSide) {
            ServerLevel level = (ServerLevel) user.level();
            double yBodyRadians = Math.toRadians(user.yHeadRot + (180 * (user.getUsedItemHand() == InteractionHand.MAIN_HAND ? 1 : 2)));
            float width = user.getBbWidth();
            EntityGrenade grenade = new EntityGrenade(level, user);
            grenade.setMaxDamage(EMConfigHandler.COMMON.ITEM.itemHowitzerGrenadeDamage.get().floatValue());
            Vec3 lookAngle = user.getLookAngle();
            Vec3 vec3 = user.position().add(lookAngle);
            grenade.shoot(lookAngle.x, lookAngle.y, lookAngle.z, 0.85F, 1F);
            grenade.setPos(vec3.x + width * 0.8F * Math.cos(yBodyRadians), user.getY(0.45D), vec3.z + width * 0.8F * Math.sin(yBodyRadians));
            level.addFreshEntity(grenade);
        }
    }
}
