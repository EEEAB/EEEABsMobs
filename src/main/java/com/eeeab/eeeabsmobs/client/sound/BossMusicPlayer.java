package com.eeeab.eeeabsmobs.client.sound;

import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.EEEABMobEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class BossMusicPlayer {
    public static BossMusicSoundInstance bossMusic;

    public static void playBossMusic(EEEABMobEntity boss) {
        if (!EMConfigHandler.COMMON.OTHER.enablePlayBossMusic.get()) return;
        SoundEvent music = boss.getBossMusic();
        if (music != null && boss.isAlive()) {
            Player player = Minecraft.getInstance().player;
            if (bossMusic != null) {
                float volume = Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.MUSIC);
                if (volume <= 0) {
                    bossMusic = null;
                } else if (bossMusic.getBoss() == boss && !boss.canPlayerHearMusic(player)) {
                    bossMusic.setBoss(null);
                } else if (bossMusic.getBoss() == null && bossMusic.getSoundEvent() == music) {
                    bossMusic.setBoss(boss);
                }
            } else {
                if (boss.canPlayerHearMusic(player)) {
                    bossMusic = new BossMusicSoundInstance(boss.getBossMusic(), boss);
                }
            }
            if (bossMusic != null && !Minecraft.getInstance().getSoundManager().isActive(bossMusic)) {
                Minecraft.getInstance().getSoundManager().play(bossMusic);
            }
        }
    }

    public static void stopBossMusic(EEEABMobEntity boss) {
        if (!EMConfigHandler.COMMON.OTHER.enablePlayBossMusic.get()) return;

        if (bossMusic != null && bossMusic.getBoss() == boss) {
            bossMusic.setBoss(null);
        }
    }

    public static void resetBossMusic(EEEABMobEntity boss) {
        if (!EMConfigHandler.COMMON.OTHER.enablePlayBossMusic.get()) return;

        if (bossMusic != null) {
            bossMusic.setBoss(null);
        }
        playBossMusic(boss);
    }

}
