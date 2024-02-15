package com.eeeab.eeeabsmobs.sever;

import com.eeeab.eeeabsmobs.EEEABMobs;

import com.eeeab.eeeabsmobs.client.sound.AbilitySoundInstance;
import com.eeeab.eeeabsmobs.sever.ability.Ability;
import com.eeeab.eeeabsmobs.sever.ability.AbilityType;
import com.eeeab.eeeabsmobs.sever.advancements.EMCriteriaTriggers;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.message.MessagePlayerUseAbility;
import com.eeeab.eeeabsmobs.sever.message.MessageUseAbility;
import com.eeeab.eeeabsmobs.sever.message.MessageVertigoEffect;
import com.eeeab.eeeabsmobs.sever.world.portal.EMTeleporter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.registries.RegisterEvent;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ServerProxy {
    public static final String VERSION = "1.0";
    private static int id = 0;

    public static int nextID() {
        return id++;
    }

    public void registerMessage() {
        EEEABMobs.NETWORK = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(EEEABMobs.MOD_ID, "net"))
                .networkProtocolVersion(() -> VERSION)
                .clientAcceptedVersions(VERSION::equals)
                .serverAcceptedVersions(VERSION::equals)
                .simpleChannel();
        EEEABMobs.NETWORK.messageBuilder(MessageVertigoEffect.class, nextID()).encoder(MessageVertigoEffect::serialize).decoder(MessageVertigoEffect::deserialize).consumerNetworkThread(new MessageVertigoEffect.Handler()).add();
        EEEABMobs.NETWORK.messageBuilder(MessageUseAbility.class, nextID()).encoder(MessageUseAbility::serialize).decoder(MessageUseAbility::deserialize).consumerNetworkThread(new MessageUseAbility.Handler()).add();
        EEEABMobs.NETWORK.messageBuilder(MessagePlayerUseAbility.class, nextID()).encoder(MessagePlayerUseAbility::serialize).decoder(MessagePlayerUseAbility::deserialize).consumerNetworkThread(new MessagePlayerUseAbility.Handler()).add();
    }

    public void init(IEventBus bus) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EMConfigHandler.SPEC);
    }

    public void register(RegisterEvent event) {
        EMTeleporter.onRegisterPointOfInterest(event);
    }

    public void onLateInit(IEventBus bus) {
        EMCriteriaTriggers.register();
    }

    public Object getISTERProperties() {
        return null;
    }

    public void playLaserSound(Player player){}

    public void endLaserSound(Player player){}

}
