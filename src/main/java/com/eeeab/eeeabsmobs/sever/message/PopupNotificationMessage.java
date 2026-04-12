package com.eeeab.eeeabsmobs.sever.message;

import com.eeeab.eeeabsmobs.client.gui.TipNotificationHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PopupNotificationMessage {
    private ResourceLocation id;
    private int level;

    public PopupNotificationMessage() {
    }

    public PopupNotificationMessage(ResourceLocation id, int level) {
        this.id = id;
        this.level = level;
    }

    public static void serialize(PopupNotificationMessage msg, FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(msg.id);
        buffer.writeInt(msg.level);
    }

    public static PopupNotificationMessage deserialize(FriendlyByteBuf buffer) {
        PopupNotificationMessage message = new PopupNotificationMessage();
        message.id = buffer.readResourceLocation();
        message.level = buffer.readInt();
        return message;
    }

    public static void handle(PopupNotificationMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> TipNotificationHandler.init(msg.id, msg.level));
        ctx.setPacketHandled(true);
    }
}