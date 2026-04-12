package com.eeeab.eeeabsmobs.client;

import com.eeeab.eeeabsmobs.client.model.util.ModItemModels;
import com.eeeab.eeeabsmobs.client.render.BossBarTexReloadListener;
import com.eeeab.eeeabsmobs.client.render.LightningManager;
import com.eeeab.eeeabsmobs.client.render.util.ModArmorStackRenderProperties;
import com.eeeab.eeeabsmobs.client.render.util.ModItemStackRenderProperties;
import com.eeeab.eeeabsmobs.client.sound.ImmortalLaserSound;
import com.eeeab.eeeabsmobs.client.sound.ability.GuardianLaserSound;
import com.eeeab.eeeabsmobs.sever.ServerProxy;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityImmortalLaser;
import com.eeeab.eeeabsmobs.sever.handler.ClientEventHandler;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
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
    public static final Int2ObjectMap<AbstractTickableSoundInstance> ENTITY_SOUND_INSTANCE_MAP = new Int2ObjectOpenHashMap<>();
    public static final Map<UUID, ResourceLocation> BOSS_BAR_REGISTRY_NAMES = new HashMap<>();
    public static final LightningManager LIGHTNING_RENDER = new LightningManager();

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
    public void playImmortalLaserSound(EntityImmortalLaser laser) {
        Minecraft.getInstance().getSoundManager().play(new ImmortalLaserSound(laser));
    }

    @Override
    public void playTickableSound(Entity entity, int id) {
        if (!entity.level().isClientSide) return;
        AbstractTickableSoundInstance cache = ENTITY_SOUND_INSTANCE_MAP.get(entity.getId());
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        if (id == 0) {
            if (entity instanceof Player player) {
                GuardianLaserSound sound;
                if (cache instanceof GuardianLaserSound guardianLaserSound) {
                    sound = guardianLaserSound;
                } else {
                    sound = new GuardianLaserSound(player);
                }
                if (!soundManager.isActive(sound) && sound.canPlaySound()) {
                    soundManager.queueTickingSound(sound);
                    ENTITY_SOUND_INSTANCE_MAP.put(entity.getId(), sound);
                }
            }
        }
    }

    @Override
    public void stopTickableSound(Entity entity) {
        ENTITY_SOUND_INSTANCE_MAP.remove(entity.getId());
    }

    @SubscribeEvent
    public void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new BossBarTexReloadListener());
    }
}
