package com.eeeab.eeeabsmobs.sever.message;

import com.eeeab.eeeabsmobs.client.ClientProxy;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class UpdateBossBarMessage {
    private ResourceLocation registryName;
    private UUID bossID;
    private boolean remove;

    public UpdateBossBarMessage() {

    }

    public UpdateBossBarMessage(UUID bossID, LivingEntity entity) {
        this.bossID = bossID;
        if (entity != null) {
            this.registryName = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
            this.remove = false;
        } else {
            this.registryName = null;
            this.remove = true;
        }
    }

    public static void serialize(final UpdateBossBarMessage message, final FriendlyByteBuf buf) {
        buf.writeUUID(message.bossID);
        buf.writeBoolean(message.remove);
        if (!message.remove && message.registryName != null) buf.writeResourceLocation(message.registryName);
    }

    public static UpdateBossBarMessage deserialize(final FriendlyByteBuf buf) {
        final UpdateBossBarMessage message = new UpdateBossBarMessage();
        message.bossID = buf.readUUID();
        message.remove = buf.readBoolean();
        if (!message.remove) message.registryName = buf.readResourceLocation();
        return message;
    }

    public static class Handler implements BiConsumer<UpdateBossBarMessage, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final UpdateBossBarMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (message.registryName == null) ClientProxy.bossBarRegistryNames.remove(message.bossID);
                else ClientProxy.bossBarRegistryNames.put(message.bossID, message.registryName);
            });
            context.setPacketHandled(true);
        }
    }
}
