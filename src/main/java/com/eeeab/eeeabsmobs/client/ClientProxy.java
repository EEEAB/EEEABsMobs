package com.eeeab.eeeabsmobs.client;

import com.eeeab.eeeabsmobs.client.model.util.EMItemModels;
import com.eeeab.eeeabsmobs.client.render.util.EMArmorStackRenderProperties;
import com.eeeab.eeeabsmobs.client.render.util.EMItemStackRenderProperties;
import com.eeeab.eeeabsmobs.client.sound.ImmortalLaserSound;
import com.eeeab.eeeabsmobs.client.sound.ability.GuardianLaserSoundInstance;
import com.eeeab.eeeabsmobs.sever.ServerProxy;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityImmortalLaser;
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
        bus.register(EMItemModels.class);
        MinecraftForge.EVENT_BUS.register(new HandlerClientEvent());
    }

    @Override
    public void register(RegisterEvent event) {
        super.register(event);
    }

    @Override
    public void loadComplete(IEventBus bus) {
        super.loadComplete(bus);
    }

    @Override
    public Object getISTERProperties() {
        return new EMItemStackRenderProperties();
    }

    @Override
    public Object getASTEProperties() {
        return new EMArmorStackRenderProperties();
    }

    @Override
    public void playGuardianLaserSound(Player player) {
        GuardianLaserSoundInstance sound = GuardianLaserSoundInstance.getInstance(player);
        if (!Minecraft.getInstance().getSoundManager().isActive(sound) && sound.canPlaySound()) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(sound);
        }
    }

    @Override
    public void stopGuardianLaserSound(Player player) {
        GuardianLaserSoundInstance.clearById(player.getId());
    }

    @Override
    public void playImmortalLaserSound(EntityImmortalLaser laser) {
        Minecraft.getInstance().getSoundManager().play(new ImmortalLaserSound(laser, true));
        Minecraft.getInstance().getSoundManager().play(new ImmortalLaserSound(laser, false));
    }
}
