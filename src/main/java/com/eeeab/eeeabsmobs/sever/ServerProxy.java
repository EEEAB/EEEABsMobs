package com.eeeab.eeeabsmobs.sever;

import com.eeeab.animate.server.message.MessageAnimation;
import com.eeeab.animate.server.message.MessagePlayAnimation;
import com.eeeab.animate.server.message.MessageStopAnimation;
import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.commands.CombatHintHudCommand;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityImmortalLaser;
import com.eeeab.eeeabsmobs.sever.handler.CapabilityHandler;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.AttributeInit;
import com.eeeab.eeeabsmobs.sever.message.*;
import com.eeeab.eeeabsmobs.sever.world.portal.VoidCrackTeleporter;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkRegistry;

public class ServerProxy {
    public static final String VERSION = "1.0";
    private static int ID = 0;

    public void initMod(IEventBus bus) {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigHandler.COMMON_SPEC);
        bus.addListener(VoidCrackTeleporter::onRegisterPointOfInterest);
        bus.addListener(CapabilityHandler::registerCapabilities);
        bus.addListener(this::onEntityAttributeModification);
    }

    public void initForge(IEventBus bus) {
        bus.addListener(this::onCommandRegister);
    }

    public void loadComplete(IEventBus bus) {
    }

    public Object getISTERProperties() {
        return null;
    }

    public Object getASTEProperties() {
        return null;
    }

    public void playGuardianLaserSound(Player player) {
    }

    public void stopGuardianLaserSound(Player player) {
    }

    public void playImmortalLaserSound(EntityImmortalLaser laser) {
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        CommandBuildContext context = event.getBuildContext();
        //TODO 编译jar包时注释这段代码
        CombatHintHudCommand.register(dispatcher, context);
    }

    @SubscribeEvent
    public void onEntityAttributeModification(final EntityAttributeModificationEvent event) {
        for (EntityType<? extends LivingEntity> type : event.getTypes()) {
            event.add(type, AttributeInit.CRIT_CHANCE.get());
        }
    }

    public void initNetwork() {
        EEEABMobs.NETWORK = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(EEEABMobs.MOD_ID, "net"))
                .networkProtocolVersion(() -> VERSION)
                .clientAcceptedVersions(VERSION::equals)
                .serverAcceptedVersions(VERSION::equals)
                .simpleChannel();
        EEEABMobs.NETWORK.messageBuilder(ICapabilityMessage.class, ID++).encoder(ICapabilityMessage::serialize).decoder(ICapabilityMessage::deserialize).consumerNetworkThread(new ICapabilityMessage.Handler()).add();
        EEEABMobs.NETWORK.messageBuilder(UseAbilityMessage.class, ID++).encoder(UseAbilityMessage::serialize).decoder(UseAbilityMessage::deserialize).consumerNetworkThread(new UseAbilityMessage.Handler()).add();
        EEEABMobs.NETWORK.messageBuilder(PlayerUseAbilityMessage.class, ID++).encoder(PlayerUseAbilityMessage::serialize).decoder(PlayerUseAbilityMessage::deserialize).consumerNetworkThread(new PlayerUseAbilityMessage.Handler()).add();
        EEEABMobs.NETWORK.messageBuilder(SyncMuzzlePosMessage.class, ID++).encoder(SyncMuzzlePosMessage::serialize).decoder(SyncMuzzlePosMessage::deserialize).consumerNetworkThread(new SyncMuzzlePosMessage.Handler()).add();
        EEEABMobs.NETWORK.messageBuilder(UpdateBossBarMessage.class, ID++).encoder(UpdateBossBarMessage::serialize).decoder(UpdateBossBarMessage::deserialize).consumerNetworkThread(new UpdateBossBarMessage.Handler()).add();
        EEEABMobs.NETWORK.messageBuilder(PopupNotificationMessage.class, ID++).encoder(PopupNotificationMessage::serialize).decoder(PopupNotificationMessage::deserialize).consumerMainThread(PopupNotificationMessage::handle).add();
        EEEABMobs.NETWORK.messageBuilder(MessageAnimation.class, ID++).encoder(MessageAnimation::serialize).decoder(MessageAnimation::deserialize).consumerNetworkThread(new MessageAnimation.Handler()).add();
        EEEABMobs.NETWORK.messageBuilder(MessagePlayAnimation.class, ID++).encoder(MessagePlayAnimation::serialize).decoder(MessagePlayAnimation::deserialize).consumerNetworkThread(new MessagePlayAnimation.Handler<>()).add();
        EEEABMobs.NETWORK.messageBuilder(MessageStopAnimation.class, ID++).encoder(MessageStopAnimation::serialize).decoder(MessageStopAnimation::deserialize).consumerNetworkThread(new MessageStopAnimation.Handler()).add();
    }
}
