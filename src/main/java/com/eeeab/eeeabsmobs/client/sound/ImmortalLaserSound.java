package com.eeeab.eeeabsmobs.client.sound;

import com.eeeab.eeeabsmobs.sever.entity.effects.EntityImmortalLaser;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class ImmortalLaserSound extends AbstractTickableSoundInstance {
    private final EntityImmortalLaser laser;
    private final boolean end;

    public ImmortalLaserSound(EntityImmortalLaser laser, boolean end) {
        super(SoundInit.LASER2.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.laser = laser;
        this.end = end;
        this.volume = 1.2F;
    }

    @Override
    public void tick() {
        if (!laser.isAlive()) {
            this.volume -= 0.2F;
            if (this.volume <= 0) stop();
        }
        if (end && laser.tickCount >= laser.getCountDown()) {
            x = laser.collidePosX;
            y = laser.collidePosY;
            z = laser.collidePosZ;
        } else {
            x = laser.getX();
            y = laser.getY();
            z = laser.getZ();
        }
    }
}
