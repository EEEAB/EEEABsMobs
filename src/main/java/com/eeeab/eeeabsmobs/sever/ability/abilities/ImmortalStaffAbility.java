package com.eeeab.eeeabsmobs.sever.ability.abilities;

import com.eeeab.eeeabsmobs.sever.ability.Ability;
import com.eeeab.eeeabsmobs.sever.ability.AbilityPeriod;
import com.eeeab.eeeabsmobs.sever.ability.AbilityType;
import com.eeeab.eeeabsmobs.sever.entity.effect.projectile.EntityShamanBomb;
import com.eeeab.eeeabsmobs.client.particle.util.ModParticleUtils;
import com.eeeab.eeeabsmobs.sever.item.ItemImmortalStaff;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class ImmortalStaffAbility extends Ability<Player> {

    public ImmortalStaffAbility(AbilityType<Player, ? extends Ability<Player>> abilityType, Player user) {
        super(abilityType, user, new AbilityPeriod[]{
                new AbilityPeriod.AbilityPeriodInstant(AbilityPeriod.AbilityPeriodType.ACTIVE)
        }, 0);
    }

    @Override
    public void start() {
        super.start();
        Player user = this.getUser();
        if (!user.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) user.level();
            double yBodyRadians = Math.toRadians(user.yHeadRot + (180 * (user.getUsedItemHand() == InteractionHand.MAIN_HAND ? 1 : 2)));
            float width = user.getBbWidth() * 0.6F;
            EntityShamanBomb shamanBomb = new EntityShamanBomb(user.level(), user, user.getLookAngle().x, user.getLookAngle().y, user.getLookAngle().z);
            shamanBomb.setOwner(user);
            shamanBomb.setIsPlayer(true);
            shamanBomb.setDangerous(ItemImmortalStaff.isDangerBomb(user));
            shamanBomb.absMoveTo(shamanBomb.getX() + width * Math.cos(yBodyRadians), user.getY(0.55), shamanBomb.getZ() + width * Math.sin(yBodyRadians));
            serverLevel.addFreshEntity(shamanBomb);
        }
        user.playSound(SoundEvents.BLAZE_SHOOT);
        ModParticleUtils.annularParticleOutburst(user.level(), 5, ParticleTypes.SOUL_FIRE_FLAME, user.getX(), user.getY(), user.getZ(), 0.18, 0.15, 360F, 0F, Mth.PI);
        ModParticleUtils.annularParticleOutburst(user.level(), 5, ParticleTypes.LARGE_SMOKE, user.getX(), user.getY(), user.getZ(), 0.16, 0.1, 360F, 0F, -Mth.PI);
    }
}
