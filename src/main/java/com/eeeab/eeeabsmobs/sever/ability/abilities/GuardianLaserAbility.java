package com.eeeab.eeeabsmobs.sever.ability.abilities;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.ability.Ability;
import com.eeeab.eeeabsmobs.sever.ability.AbilityPeriod;
import com.eeeab.eeeabsmobs.sever.ability.AbilityType;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityGuardianLaser;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class GuardianLaserAbility extends Ability<Player> {
    private EntityGuardianLaser guardianLaser;

    public GuardianLaserAbility(AbilityType<Player, ? extends Ability<?>> abilityType, Player user) {
        super(abilityType, user, new AbilityPeriod[]{
                new AbilityPeriod.AbilityPeriodDuration(AbilityPeriod.AbilityPeriodType.STARTUP, 20),
                new AbilityPeriod.AbilityPeriodDuration(AbilityPeriod.AbilityPeriodType.ACTIVE, 60),
                new AbilityPeriod.AbilityPeriodDuration(AbilityPeriod.AbilityPeriodType.RECOVERY, 20)
        }, 20);
    }

    @Override
    public void start() {
        super.start();
        Player player = getUser();
        if (player.level instanceof ServerLevel level) {
            double px = player.getX();
            double py = player.getY() + player.getBbHeight() * 0.6F;
            double pz = player.getZ();
            float yHeadRotAngle = (float) Math.toRadians(player.yHeadRot + 90);
            float xHeadRotAngle = (float) (float) -Math.toRadians(player.getXRot());
            EntityGuardianLaser guardianLaser = new EntityGuardianLaser(EntityInit.GUARDIAN_LASER.get(), player.level, player, px, py, pz, yHeadRotAngle, xHeadRotAngle, 100);
            guardianLaser.setPlayer(true);
            level.addFreshEntity(guardianLaser);
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2, false, false));
            this.guardianLaser = guardianLaser;
        }
        player.swing(InteractionHand.MAIN_HAND, true);
    }

    @Override
    protected void tickUsing() {
        Player player = this.getUser();
        Level level = player.level;
        if (level.isClientSide) EEEABMobs.PROXY.playGuardianLaserSound(player);
    }

    @Override
    public void end() {
        super.end();
        Player player = this.getUser();
        player.playSound(SoundInit.NAMELESS_GUARDIAN_ACCUMULATING_END.get(), 1.5F, (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.2F + 1.5F);
        Level level = player.level;
        if (guardianLaser != null) {
            guardianLaser.discard();
        }
        player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        if (level.isClientSide) EEEABMobs.PROXY.stopGuardianLaserSound(player);
    }
}
