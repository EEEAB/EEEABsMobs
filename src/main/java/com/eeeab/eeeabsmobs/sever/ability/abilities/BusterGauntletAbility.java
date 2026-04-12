package com.eeeab.eeeabsmobs.sever.ability.abilities;

import com.eeeab.eeeabsmobs.sever.ability.Ability;
import com.eeeab.eeeabsmobs.sever.ability.AbilityPeriod;
import com.eeeab.eeeabsmobs.sever.ability.AbilityType;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.effect.projectile.EntityPulsedGrenade;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class BusterGauntletAbility extends Ability<Player> {

    public BusterGauntletAbility(AbilityType<Player, ? extends Ability<Player>> abilityType, Player user) {
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
            EntityPulsedGrenade grenade = new EntityPulsedGrenade(level, user, true);
            grenade.setRadius(ModConfigHandler.COMMON.items.busterGauntletConfig2.get().floatValue());
            Vec3 lookPos = user.getLookAngle();
            Vec3 playerPos = user.position();
            grenade.shoot(lookPos.x, lookPos.y, lookPos.z, 0.7F, 1F);
            grenade.setPos(playerPos.x + width * 0.7F * Math.cos(yBodyRadians), user.getY(0.6D), playerPos.z + width * 0.7F * Math.sin(yBodyRadians));
            level.addFreshEntity(grenade);
        }
        user.playSound(SoundInit.PULSED_GRENADE_LAUNCH.get());
    }
}
