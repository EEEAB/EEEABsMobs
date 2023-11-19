package com.eeeab.eeeabsmobs.sever.ability.abilities;

import com.eeeab.eeeabsmobs.sever.ability.Ability;
import com.eeeab.eeeabsmobs.sever.ability.AbilityPeriod;
import com.eeeab.eeeabsmobs.sever.ability.AbilityType;
import com.eeeab.eeeabsmobs.sever.entity.impl.effect.EntityGuardianLaser;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;

//TODO 镭射能力 暂未实装进游戏
public class GuardianLaserAbility extends Ability<Player> {
    private EntityGuardianLaser guardianLaser;

    public GuardianLaserAbility(AbilityType<Player, ? extends Ability<?>> abilityType, Player user) {
        super(abilityType, user, new AbilityPeriod[]{
                new AbilityPeriod.AbilityPeriodDuration(AbilityPeriod.AbilityPeriodType.STARTUP, 20),
                new AbilityPeriod.AbilityPeriodDuration(AbilityPeriod.AbilityPeriodType.ACTIVE, 50),
                new AbilityPeriod.AbilityPeriodDuration(AbilityPeriod.AbilityPeriodType.RECOVERY, 20)
        }, 10);
    }

    @Override
    public void start() {
        super.start();
        Player player = getUser();
        if (player.level() instanceof ServerLevel level) {
            double px = player.getX();
            double py = player.getY() + player.getBbHeight() * 0.5F;
            double pz = player.getZ();
            float yHeadRotAngle = (float) Math.toRadians(player.yHeadRot + 90);
            float xHeadRotAngle = (float) (float) -Math.toRadians(player.getXRot());
            EntityGuardianLaser guardianLaser = new EntityGuardianLaser(EntityInit.GUARDIAN_LASER.get(), player.level(), player, px, py, pz, yHeadRotAngle, xHeadRotAngle, 50);
            guardianLaser.setPlayer(true);
            level.addFreshEntity(guardianLaser);
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 70, 2, false, false));
            this.guardianLaser = guardianLaser;
        }
        player.swing(InteractionHand.MAIN_HAND, true);
    }


    @Override
    public void end() {
        super.end();
        if (guardianLaser != null) {
            guardianLaser.discard();
        }
    }
}
