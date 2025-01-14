package com.eeeab.eeeabsmobs.client.sound;

import com.eeeab.eeeabsmobs.sever.entity.effects.EntityImmortalLaser;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ImmortalLaserSound extends AbstractTickableSoundInstance {
    private final EntityImmortalLaser laser;

    public ImmortalLaserSound(EntityImmortalLaser laser) {
        super(SoundInit.LASER2.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.attenuation = Attenuation.NONE;
        this.laser = laser;
        this.volume = 2.0F;
    }

    @Override
    public void tick() {
        if (!laser.isAlive()) {
            this.volume -= 0.3F;
            if (this.volume <= 0) stop();
        } else {
            this.x = laser.getX();
            this.y = laser.getY();
            this.z = laser.getZ();
        }
    }
}
