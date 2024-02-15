package com.eeeab.eeeabsmobs.client.sound.ability;

import com.eeeab.eeeabsmobs.client.sound.AbilitySoundInstance;
import com.eeeab.eeeabsmobs.sever.ability.AbilityHandler;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class GuardianLaserSoundInstance extends AbilitySoundInstance {
    public final static Map<Integer, GuardianLaserSoundInstance> INSTANCE_MAP = new HashMap<>();

    public GuardianLaserSoundInstance(Player user) {
        super(user, SoundInit.LASER.get(), AbilityHandler.GUARDIAN_LASER_ABILITY_TYPE);
        this.looping = true;
        this.volume = 0.8F;
        this.pitch = 1F;
        this.delay = 0;
    }

    public static GuardianLaserSoundInstance getInstance(Player user) {
        GuardianLaserSoundInstance sound = INSTANCE_MAP.get(user.getId());
        if (sound == null) {
            sound = new GuardianLaserSoundInstance(user);
            INSTANCE_MAP.put(user.getId(), sound);
        }
        return sound;
    }

    public static void clearById(int entityId) {
        INSTANCE_MAP.remove(entityId);
    }
}
