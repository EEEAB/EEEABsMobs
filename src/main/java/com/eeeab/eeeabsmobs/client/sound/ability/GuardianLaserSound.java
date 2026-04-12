package com.eeeab.eeeabsmobs.client.sound.ability;

import com.eeeab.eeeabsmobs.sever.ability.AbilityHandler;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuardianLaserSound extends AbsAbilityTickableSound {
    public GuardianLaserSound(Player user) {
        super(user, SoundInit.LASER.get(), AbilityHandler.GUARDIAN_LASER_ABILITY);
        this.looping = true;
        this.volume = 0.8F;
        this.pitch = 1F;
        this.delay = 0;
    }
}
