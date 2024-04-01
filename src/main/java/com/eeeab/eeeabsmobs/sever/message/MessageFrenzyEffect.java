package com.eeeab.eeeabsmobs.sever.message;

import com.eeeab.eeeabsmobs.sever.capability.FrenzyCapability;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MessageFrenzyEffect {
    private int entityID;
    private boolean isFrenzy;

    public MessageFrenzyEffect() {
    }

    public MessageFrenzyEffect(LivingEntity entity, boolean isFrenzy) {
        entityID = entity.getId();
        this.isFrenzy = isFrenzy;
    }

    public static void serialize(final MessageFrenzyEffect message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeBoolean(message.isFrenzy);
    }

    public static MessageFrenzyEffect deserialize(final FriendlyByteBuf buf) {
        final MessageFrenzyEffect message = new MessageFrenzyEffect();
        message.entityID = buf.readVarInt();
        message.isFrenzy = buf.readBoolean();
        return message;
    }

    public static class Handler implements BiConsumer<MessageFrenzyEffect, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageFrenzyEffect message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (Minecraft.getInstance().level != null) {
                    Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
                    if (entity instanceof LivingEntity livingEntity) {
                        FrenzyCapability.IFrenzyCapability livingCapability = HandlerCapability.getCapability(livingEntity, HandlerCapability.FRENZY_CAPABILITY_CAPABILITY);
                        if (livingCapability != null) {
                            if (message.isFrenzy) livingCapability.onStart(livingEntity);
                            else livingCapability.onEnd(livingEntity);
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
