package com.eeeab.eeeabsmobs.client.sound;

import com.eeeab.eeeabsmobs.sever.entity.EEEABMobEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BossMusicSoundInstance extends AbstractTickableSoundInstance {
    private EEEABMobEntity boss;
    private final SoundEvent sound;

    protected BossMusicSoundInstance(SoundEvent sound, EEEABMobEntity boss) {
        super(sound, SoundSource.MUSIC, SoundInstance.createUnseededRandom());
        this.boss = boss;
        this.sound = sound;
        this.attenuation = Attenuation.NONE;
        this.looping = true;
        this.delay = 0;
        this.x = boss.getX();
        this.y = boss.getY();
        this.z = boss.getZ();
    }

    @Override
    public boolean canPlaySound() {
        return BossMusicPlayer.bossMusic == this;
    }

    @Override
    public void tick() {
        //当音乐需要停止时
        if (boss == null || !boss.isAlive() || boss.isSilent()) {
            boss = null;
            stop();
            BossMusicPlayer.bossMusic = null;
            Minecraft.getInstance().getMusicManager().stopPlaying();
        }
    }

    public void setBoss(EEEABMobEntity boss) {
        this.boss = boss;
    }

    public EEEABMobEntity getBoss() {
        return boss;
    }

    public SoundEvent getSoundEvent() {
        return sound;
    }
}
