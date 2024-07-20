package com.eeeab.eeeabsmobs.sever;

import com.eeeab.eeeabsmobs.EEEABMobs;

import com.eeeab.eeeabsmobs.sever.advancements.EMCriteriaTriggers;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.message.MessageFrenzyEffect;
import com.eeeab.eeeabsmobs.sever.message.MessagePlayerUseAbility;
import com.eeeab.eeeabsmobs.sever.message.MessageUseAbility;
import com.eeeab.eeeabsmobs.sever.message.MessageVertigoEffect;
import com.eeeab.eeeabsmobs.sever.world.portal.VoidCrackTeleporter;
import com.eeeab.animate.server.message.AnimationMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.registries.RegisterEvent;

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
        EEEABMobs.NETWORK.messageBuilder(MessageFrenzyEffect.class, nextID()).encoder(MessageFrenzyEffect::serialize).decoder(MessageFrenzyEffect::deserialize).consumerNetworkThread(new MessageFrenzyEffect.Handler()).add();
        EEEABMobs.NETWORK.messageBuilder(AnimationMessage.class, nextID()).encoder(AnimationMessage::serialize).decoder(AnimationMessage::deserialize).consumerNetworkThread(new AnimationMessage.Handler()).add();
    }

    public void init(IEventBus bus) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EMConfigHandler.SPEC);
        bus.addListener(HandlerCapability::registerCapabilities);
        bus.addListener(this::register);
        bus.addListener(this::onModConfigInit);
    }

    @SubscribeEvent
    public void onModConfigInit(final ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (config.getSpec() == EMConfigHandler.SPEC) {
            EMConfigHandler.COMMON.ITEM.GUARDIAN_AXE_TOOL.attackSpeedValue = EMConfigHandler.COMMON.ITEM.GUARDIAN_AXE_TOOL.attackSpeed.get().floatValue();
            EMConfigHandler.COMMON.ITEM.GUARDIAN_AXE_TOOL.attackDamageValue = EMConfigHandler.COMMON.ITEM.GUARDIAN_AXE_TOOL.attackDamage.get().floatValue();
            EMConfigHandler.COMMON.ITEM.NETHERWORLD_KATANA_TOOL.attackSpeedValue = EMConfigHandler.COMMON.ITEM.NETHERWORLD_KATANA_TOOL.attackSpeed.get().floatValue();
            EMConfigHandler.COMMON.ITEM.NETHERWORLD_KATANA_TOOL.attackDamageValue = EMConfigHandler.COMMON.ITEM.NETHERWORLD_KATANA_TOOL.attackDamage.get().floatValue();
        }
    }

    public void register(RegisterEvent event) {
        VoidCrackTeleporter.onRegisterPointOfInterest(event);
    }

    public void loadComplete(IEventBus bus) {
        EMCriteriaTriggers.register();
        ItemInit.initializeAttributes();
    }

    public Object getISTERProperties() {
        return null;
    }

    public Object getASTEProperties() {
        return null;
    }

    public void playLaserSound(Player player) {
    }

    public void endLaserSound(Player player) {
    }

}
