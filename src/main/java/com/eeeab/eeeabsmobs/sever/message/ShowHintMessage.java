package com.eeeab.eeeabsmobs.sever.message;

import com.eeeab.eeeabsmobs.client.gui.NotificationHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShowHintMessage {
    private ResourceLocation hintId;
    private int hintLevel;

    public ShowHintMessage() {
    }

    public ShowHintMessage(ResourceLocation hintId, int hintLevel) {
        this.hintId = hintId;
        this.hintLevel = hintLevel;
    }

    public static void serialize(ShowHintMessage msg, FriendlyByteBuf buffer) {
        buffer.writeResourceLocation(msg.hintId);
        buffer.writeInt(msg.hintLevel);
    }

    public static ShowHintMessage deserialize(FriendlyByteBuf buffer) {
        ShowHintMessage message = new ShowHintMessage();
        message.hintId = buffer.readResourceLocation();
        message.hintLevel = buffer.readInt();
        return message;
    }

    public static void handle(ShowHintMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> NotificationHandler.showHint(msg.hintId, msg.hintLevel));
        ctx.setPacketHandled(true);
    }
}