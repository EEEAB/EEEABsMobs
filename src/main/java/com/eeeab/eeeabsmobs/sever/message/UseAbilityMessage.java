package com.eeeab.eeeabsmobs.sever.message;

import com.eeeab.eeeabsmobs.sever.capability.AbilityCapability;
import com.eeeab.eeeabsmobs.sever.handler.CapabilityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class UseAbilityMessage {
    private int entityID;
    private int index;

    public UseAbilityMessage() {
    }

    public UseAbilityMessage(LivingEntity entity, int index) {
        entityID = entity.getId();
        this.index = index;
    }

    public static void serialize(final UseAbilityMessage message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeVarInt(message.index);
    }

    public static UseAbilityMessage deserialize(final FriendlyByteBuf buf) {
        final UseAbilityMessage message = new UseAbilityMessage();
        message.entityID = buf.readVarInt();
        message.index = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<UseAbilityMessage, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final UseAbilityMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (Minecraft.getInstance().level != null) {
                    Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
                    if (entity instanceof LivingEntity livingEntity) {
                        AbilityCapability.IAbilityCapability livingCapability = CapabilityHandler.getCapability(livingEntity, CapabilityHandler.ABILITY_CAPABILITY);
                        if (livingCapability != null) {
                            livingCapability.onActive(livingEntity, livingCapability.getAbilityTypeByEntity(livingEntity)[message.index]);
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
