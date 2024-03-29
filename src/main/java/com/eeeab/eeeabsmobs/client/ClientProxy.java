package com.eeeab.eeeabsmobs.client;

import com.eeeab.eeeabsmobs.client.render.util.EEItemStackRenderProperties;
import com.eeeab.eeeabsmobs.client.sound.ability.GuardianLaserSoundInstance;
import com.eeeab.eeeabsmobs.sever.ServerProxy;
import com.eeeab.eeeabsmobs.sever.handler.HandlerClientEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegisterEvent;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends ServerProxy {

    @Override
    public void init(IEventBus bus) {
        super.init(bus);
        MinecraftForge.EVENT_BUS.register(new HandlerClientEvent());
    }

    @Override
    public void register(RegisterEvent event) {
        super.register(event);
    }

    @Override
    public void onLateInit(IEventBus bus) {
        super.onLateInit(bus);
    }

    @Override
    public Object getISTERProperties() {
        return new EEItemStackRenderProperties();
    }

    @Override
    public void playLaserSound(Player player) {
        GuardianLaserSoundInstance sound = GuardianLaserSoundInstance.getInstance(player);
        if (!Minecraft.getInstance().getSoundManager().isActive(sound) && sound.canPlaySound()) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(sound);
        }
    }

    @Override
    public void endLaserSound(Player player) {
        GuardianLaserSoundInstance.clearById(player.getId());
    }
}
