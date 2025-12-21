package com.eeeab.eeeabsmobs.sever.message;

import com.eeeab.eeeabsmobs.client.gui.PromptNotificationHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PopupNotificationMessage {
    private ResourceLocation id;
    private int level;
    private boolean hint;

    public PopupNotificationMessage() {
    }

    public PopupNotificationMessage(ResourceLocation id, int level, boolean hint) {
        this.id = id;
        this.level = level;
        this.hint = hint;
    }

    public static void serialize(PopupNotificationMessage msg, FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(msg.id);
        buffer.writeInt(msg.level);
        buffer.writeBoolean(msg.hint);
    }

    public static PopupNotificationMessage deserialize(FriendlyByteBuf buffer) {
        PopupNotificationMessage message = new PopupNotificationMessage();
        message.id = buffer.readResourceLocation();
        message.level = buffer.readInt();
        message.hint = buffer.readBoolean();
        return message;
    }

    public static void handle(PopupNotificationMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            if (msg.hint) PromptNotificationHandler.showHint(msg.id, msg.level);
            else PromptNotificationHandler.showPrompt(msg.id, msg.level);
        });
        ctx.setPacketHandled(true);
    }
}