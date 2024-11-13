package com.eeeab.eeeabsmobs.client.sound;

import com.eeeab.eeeabsmobs.sever.entity.projectile.EntityImmortalShuriken;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class ImmortalShurikenSound extends AbstractTickableSoundInstance {
    private final EntityImmortalShuriken shuriken;

    public ImmortalShurikenSound(EntityImmortalShuriken shuriken) {
        super(SoundInit.IMMORTAL_SHURIKEN_SPIN.get(), SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
        this.shuriken = shuriken;
        this.looping = true;
        this.volume = 0.2F;
        this.pitch = (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1F;
    }

    @Override
    public void tick() {
        if (!shuriken.isAlive()) {
            stop();
        }

        if (shuriken.getDeltaMovement().horizontalDistanceSqr() > 2.5000003E-7D) {
            x = shuriken.getX();
            y = shuriken.getY();
            z = shuriken.getZ();
        }
    }
}
