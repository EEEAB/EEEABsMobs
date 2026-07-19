package com.eeeab.eeeabsmobs.client.sound;

import com.eeeab.eeeabsmobs.client.ControlledAnimation;
import com.eeeab.eeeabsmobs.server.entity.EEEABMobEntity;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

//复制自: https://github.com/BobMowzie/MowziesMobs-Public/blob/main/src/main/java/com/bobmowzie/mowziesmobs/client/sound/BossMusicSound.java
@OnlyIn(Dist.CLIENT)
public class BossMusicSound extends AbstractTickableSoundInstance {
    private EEEABMobEntity boss;
    private BossMusic music;

    private final SoundEvent soundEvent;
    ControlledAnimation volumeControl;

    private boolean shouldPlay;

    public BossMusicSound(SoundEvent sound, EEEABMobEntity boss, BossMusic music) {
        this(sound, boss, music, true);
    }

    public BossMusicSound(SoundEvent sound, EEEABMobEntity boss, BossMusic music, boolean looping) {
        super(sound, SoundSource.MUSIC, SoundInstance.createUnseededRandom());
        this.soundEvent = sound;
        this.boss = boss;
        this.music = music;
        this.attenuation = Attenuation.NONE;
        this.looping = looping;
        this.delay = 0;
        this.x = boss.getX();
        this.y = boss.getY();
        this.z = boss.getZ();

        volumeControl = new ControlledAnimation(5);
        volumeControl.setTimer(3);
        volume = volumeControl.getAnimationFraction() * music.volumeControl.getAnimationFraction();

        shouldPlay = true;
    }

    @Override
    public boolean canPlaySound() {
        return true;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    public void tick() {
        volumeControl.incrementOrDecreaseTimer(shouldPlay);

        if (volume < 0.025) {
            stop();
        }

        volume = volumeControl.getAnimationFraction() * music.volumeControl.getAnimationFraction();
    }

    public void setBoss(EEEABMobEntity boss) {
        this.boss = boss;
    }

    public EEEABMobEntity getBoss() {
        return boss;
    }

    public SoundEvent getSoundEvent() {
        return soundEvent;
    }

    public void doStop() {
        stop();
    }

    public void fadeOut() {
        shouldPlay = false;
    }

    public void fadeIn() {
        shouldPlay = true;
    }

    public void cutIn() {
        shouldPlay = true;
        volumeControl.setTimer(40);
    }

    public void cutOut() {
        shouldPlay = false;
        volumeControl.setTimer(0);
    }
}
