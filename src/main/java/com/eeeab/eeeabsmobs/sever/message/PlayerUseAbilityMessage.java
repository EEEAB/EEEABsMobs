package com.eeeab.eeeabsmobs.sever.message;

import com.eeeab.eeeabsmobs.sever.ability.AbilityHandler;
import com.eeeab.eeeabsmobs.sever.capability.AbilityCapability;
import com.eeeab.eeeabsmobs.sever.handler.CapabilityHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class PlayerUseAbilityMessage {
    private int index;

    public PlayerUseAbilityMessage() {

    }

    public PlayerUseAbilityMessage(int index) {
        this.index = index;
    }

    public static void serialize(final PlayerUseAbilityMessage message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.index);
    }

    public static PlayerUseAbilityMessage deserialize(final FriendlyByteBuf buf) {
        final PlayerUseAbilityMessage message = new PlayerUseAbilityMessage();
        message.index = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<PlayerUseAbilityMessage, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final PlayerUseAbilityMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayer player = context.getSender();
            context.enqueueWork(() -> {
                AbilityCapability.IAbilityCapability abilityCapability = CapabilityHandler.getCapability(player, CapabilityHandler.ABILITY_CAPABILITY);
                if (abilityCapability != null) {
                    AbilityHandler.INSTANCE.sendAbilityMessage(player, abilityCapability.getAbilityTypeByEntity(player)[message.index]);
                }
            });
            context.setPacketHandled(true);
        }
    }
}
