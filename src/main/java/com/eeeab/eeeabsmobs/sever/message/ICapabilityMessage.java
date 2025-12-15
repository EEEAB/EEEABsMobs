package com.eeeab.eeeabsmobs.sever.message;

import com.eeeab.eeeabsmobs.sever.capability.GeneralCapability;
import com.eeeab.eeeabsmobs.sever.handler.CapabilityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.network.NetworkEvent;
import org.apache.commons.lang3.ArrayUtils;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ICapabilityMessage {
    private int entityID;
    private int capIndex;
    private boolean flag;

    public ICapabilityMessage() {

    }

    public ICapabilityMessage(LivingEntity entity, boolean flag, Capability<?> capability) {
        entityID = entity.getId();
        this.flag = flag;
        this.capIndex = ArrayUtils.indexOf(CapabilityHandler.CAPABILITIES, capability);
    }

    public static void serialize(final ICapabilityMessage message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeVarInt(message.capIndex);
        buf.writeBoolean(message.flag);
    }

    public static ICapabilityMessage deserialize(final FriendlyByteBuf buf) {
        final ICapabilityMessage message = new ICapabilityMessage();
        message.entityID = buf.readVarInt();
        message.capIndex = buf.readVarInt();
        message.flag = buf.readBoolean();
        return message;
    }

    public static class Handler implements BiConsumer<ICapabilityMessage, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final ICapabilityMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (Minecraft.getInstance().level != null) {
                    Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
                    if (entity instanceof LivingEntity livingEntity) {
                        Capability<?> cap = CapabilityHandler.CAPABILITIES[message.capIndex];
                        if (CapabilityHandler.getCapability(livingEntity, cap) instanceof GeneralCapability iCapability) {
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
