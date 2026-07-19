package com.eeeab.eeeabsmobs.client.sound;

import com.eeeab.eeeabsmobs.server.entity.EEEABMobEntity;
import com.eeeab.eeeabsmobs.server.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.server.init.SoundInit;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

//复制自: https://github.com/BobMowzie/MowziesMobs-Public/blob/main/src/main/java/com/bobmowzie/mowziesmobs/client/sound/BossMusicPlayer.java
@OnlyIn(Dist.CLIENT)
public class BossMusicPlayer {
    public static BossMusic currentMusic;
    public static final BossMusic REALM_WARDEN_MUSIC = new BossMusic(SoundInit.REALM_WARDEN_THEME.get());
    public static final BossMusic THE_IMMORTAL_MUSIC = new BossMusic(SoundInit.THE_IMMORTAL_THEME.get());
    public static final BossMusic NAMELESS_GUARDIAN_MUSIC = new BossMusic(SoundInit.NAMELESS_GUARDIAN_THEME.get());
    private static final BossMusic[] BOSS_MUSICS = new BossMusic[]{
            REALM_WARDEN_MUSIC,
            THE_IMMORTAL_MUSIC,
            NAMELESS_GUARDIAN_MUSIC
    };

    public static void requestBossMusic(EEEABMobEntity entity) {
        // Don't play if config has music turned off
        if (!ModConfigHandler.CLIENT.enablePlayBossMusic.get()) return;

        // Get the music object for the boss theme from the entity
        BossMusic requestedMusic = entity.getBossMusic();

        if (requestedMusic != null && entity.isAlive()) {
            Player player = Minecraft.getInstance().player;
            // If there is boss music playing
            if (player != null && currentMusic != null) {
                // Don't play the music if the music settings volume is 0
                float f2 = Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.MUSIC);
                if (f2 <= 0) {
                    currentMusic = null;
                }
                // Stop the music if the player doesn't meet the criteria for hearing it
                else if (currentMusic.getBoss() == entity && !entity.canPlayerHearMusic(player)) {
                    currentMusic.setBoss(null);
                }
                // Otherwise, if the current music has no boss set and the requested music is the one currently playing, reset the current music's boss to this one
                // This is to handle cases where there are two bosses of the same type and the one handling the music dies
                else if (currentMusic.getBoss() == null && currentMusic == requestedMusic) {
                    currentMusic.setBoss(entity);
                }
            }
            // If there is no boss music playing
            else {
                // And players meet the criteria to hear this boss's music
                if (entity.canPlayerHearMusic(player)) {
                    // Then set the current music to the requested music
                    currentMusic = requestedMusic;
                    // And set its current entity to the one requesting music
                    currentMusic.setBoss(entity);
                }
            }

            // If the music exists and is not already playing, play it
            if (currentMusic != null && !currentMusic.isPlaying()) {
                currentMusic.play();
            }
        }
    }

    public static void stopBossMusic(EEEABMobEntity entity) {
        if (currentMusic != null && currentMusic.getBoss() == entity) currentMusic.setBoss(null);
    }

    @OnlyIn(Dist.CLIENT)
    public static void tick() {
        for (BossMusic music : BOSS_MUSICS) {
            if (music.isPlaying()) {
                music.tick();
            }
        }
    }
}
