package com.eeeab.eeeabsmobs.sever.message;

import com.eeeab.eeeabsmobs.client.gui.NotificationHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShowHintMessage {
    private ResourceLocation id;
    private int level;
    private boolean hint;

    public ShowHintMessage() {
    }

    public ShowHintMessage(ResourceLocation id, int level, boolean hint) {
        this.id = id;
        this.level = level;
        this.hint = hint;
    }

    public static void serialize(ShowHintMessage msg, FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(msg.id);
        buffer.writeInt(msg.level);
        buffer.writeBoolean(msg.hint);
    }

    public static ShowHintMessage deserialize(FriendlyByteBuf buffer) {
        ShowHintMessage message = new ShowHintMessage();
        message.id = buffer.readResourceLocation();
        message.level = buffer.readInt();
        message.hint = buffer.readBoolean();
        return message;
    }

    public static void handle(ShowHintMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            if (msg.hint) NotificationHandler.showHint(msg.id, msg.level);
            else NotificationHandler.showPrompt(msg.id, msg.level);
        });
        ctx.setPacketHandled(true);
    }
}