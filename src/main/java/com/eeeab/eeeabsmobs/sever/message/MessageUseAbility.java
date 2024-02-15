package com.eeeab.eeeabsmobs.sever.message;

import com.eeeab.eeeabsmobs.sever.capability.AbilityCapability;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class MessageUseAbility {
    private int entityID;
    private int index;

    public MessageUseAbility() {
    }

    public MessageUseAbility(LivingEntity entity, int index) {
        entityID = entity.getId();
        this.index = index;
    }

    public static void serialize(final MessageUseAbility message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeVarInt(message.index);
    }

    public static MessageUseAbility deserialize(final FriendlyByteBuf buf) {
        final MessageUseAbility message = new MessageUseAbility();
        message.entityID = buf.readVarInt();
        message.index = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessageUseAbility, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageUseAbility message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (Minecraft.getInstance().level != null) {
                    Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
                    if (entity instanceof LivingEntity livingEntity) {
                        AbilityCapability.IAbilityCapability livingCapability = HandlerCapability.getCapability(livingEntity, HandlerCapability.CUSTOM_ABILITY_CAPABILITY);
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
