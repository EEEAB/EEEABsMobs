package com.eeeab.eeeabsmobs.sever.message;

import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import com.eeeab.eeeabsmobs.sever.capability.VertigoCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MessageVertigoEffect {
    private int entityID;
    private boolean isVertigo;

    public MessageVertigoEffect() {
    }

    public MessageVertigoEffect(LivingEntity entity, boolean isVertigo) {
        entityID = entity.getId();
        this.isVertigo = isVertigo;
    }

    public static void serialize(final MessageVertigoEffect message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeBoolean(message.isVertigo);
    }

    public static MessageVertigoEffect deserialize(final FriendlyByteBuf buf) {
        final MessageVertigoEffect message = new MessageVertigoEffect();
        message.entityID = buf.readVarInt();
        message.isVertigo = buf.readBoolean();
        return message;
    }

    public static class Handler implements BiConsumer<MessageVertigoEffect, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageVertigoEffect message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (Minecraft.getInstance().level != null) {
                    Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
                    if (entity instanceof LivingEntity livingEntity) {
                        VertigoCapability.IVertigoCapability livingCapability = HandlerCapability.getCapability(livingEntity, HandlerCapability.MOVING_CONTROLLER_CAPABILITY);
                        if (livingCapability != null) {
                            if (message.isVertigo) livingCapability.onStart(livingEntity);
                            else livingCapability.onEnd(livingEntity);
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
