package com.eeeab.eeeabsmobs.sever.message;

import com.eeeab.eeeabsmobs.sever.capability.GeneralCapability;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.ArrayUtils;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MessageICapability {
    private int entityID;
    private int capIndex;
    private boolean flag;

    public MessageICapability() {
    }

    public MessageICapability(LivingEntity entity, boolean flag, Capability<?> capability) {
        entityID = entity.getId();
        this.flag = flag;
        this.capIndex = ArrayUtils.indexOf(HandlerCapability.CAPABILITIES, capability);
    }

    public static void serialize(final MessageICapability message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeVarInt(message.capIndex);
        buf.writeBoolean(message.flag);
    }

    public static MessageICapability deserialize(final FriendlyByteBuf buf) {
        final MessageICapability message = new MessageICapability();
        message.entityID = buf.readVarInt();
        message.capIndex = buf.readVarInt();
        message.flag = buf.readBoolean();
        return message;
    }

    public static class Handler implements BiConsumer<MessageICapability, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageICapability message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (Minecraft.getInstance().level != null) {
                    Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
                    if (entity instanceof LivingEntity livingEntity) {
                        Capability<?> cap = HandlerCapability.CAPABILITIES[message.capIndex];
                        if (HandlerCapability.getCapability(livingEntity, cap) instanceof GeneralCapability iCapability) {
                            if (message.flag) iCapability.onStart(livingEntity);
                            else iCapability.onEnd(livingEntity);
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
