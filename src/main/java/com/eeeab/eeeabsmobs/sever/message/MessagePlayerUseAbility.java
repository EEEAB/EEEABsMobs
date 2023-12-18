package com.eeeab.eeeabsmobs.sever.message;

import com.eeeab.eeeabsmobs.sever.ability.AbilityHandler;
import com.eeeab.eeeabsmobs.sever.capability.AbilityCapability;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MessagePlayerUseAbility {
    private int index;

    public MessagePlayerUseAbility() {

    }

    public MessagePlayerUseAbility(int index) {
        this.index = index;
    }

    public static void serialize(final MessagePlayerUseAbility message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.index);
    }

    public static MessagePlayerUseAbility deserialize(final FriendlyByteBuf buf) {
        final MessagePlayerUseAbility message = new MessagePlayerUseAbility();
        message.index = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessagePlayerUseAbility, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessagePlayerUseAbility message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            final ServerPlayer player = context.getSender();
            context.enqueueWork(() -> {
                AbilityCapability.IAbilityCapability abilityCapability = HandlerCapability.getCapability(player, HandlerCapability.CUSTOM_ABILITY_CAPABILITY);
                if (abilityCapability != null) {
                    AbilityHandler.INSTANCE.sendAbilityMessage(player, abilityCapability.getAbilityTypeByEntity(player)[message.index]);
                }
            });
            context.setPacketHandled(true);
        }
    }
}
