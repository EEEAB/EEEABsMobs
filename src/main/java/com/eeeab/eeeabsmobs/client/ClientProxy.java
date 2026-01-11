package com.eeeab.eeeabsmobs.client;

import com.eeeab.eeeabsmobs.client.model.util.ModItemModels;
import com.eeeab.eeeabsmobs.client.render.BossBarTexReloadListener;
import com.eeeab.eeeabsmobs.client.render.util.ModArmorStackRenderProperties;
import com.eeeab.eeeabsmobs.client.render.util.ModItemStackRenderProperties;
import com.eeeab.eeeabsmobs.client.sound.ImmortalLaserSound;
import com.eeeab.eeeabsmobs.client.sound.ability.GuardianLaserSoundInstance;
import com.eeeab.eeeabsmobs.sever.ServerProxy;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityImmortalLaser;
import com.eeeab.eeeabsmobs.sever.handler.ClientEventHandler;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends ServerProxy {
    public static final Map<UUID, ResourceLocation> bossBarRegistryNames = new HashMap<>();

    @Override
    public void initMod(IEventBus bus) {
        super.initMod(bus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ModConfigHandler.CLIENT_SPEC);
        bus.register(ModItemModels.class);
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        bus.addListener(this::onRegisterReloadListeners);
    }

    @Override
    public void initForge(IEventBus bus) {
        super.initForge(bus);
    }

    @Override
    public void loadComplete(IEventBus bus) {
        super.loadComplete(bus);
    }

    @Override
    public Object getISTERProperties() {
        return new ModItemStackRenderProperties();
    }

    @Override
    public Object getASTEProperties() {
        return new ModArmorStackRenderProperties();
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
        Minecraft.getInstance().getSoundManager().play(new ImmortalLaserSound(laser));
    }

    @SubscribeEvent
    public void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new BossBarTexReloadListener());
    }
}
