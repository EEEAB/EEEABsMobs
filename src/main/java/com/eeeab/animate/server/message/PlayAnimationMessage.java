package com.eeeab.animate.server.message;

import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.animate.server.handler.EMAnimationHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * 客户端发起播放动画数据包
 */
public class PlayAnimationMessage {
    private int entityID;
    private int animationsIndex;

    public PlayAnimationMessage() {

    }

    public PlayAnimationMessage(int entityID, int animationsIndex) {
        this.entityID = entityID;
        this.animationsIndex = animationsIndex;
    }

    public static void serialize(final PlayAnimationMessage message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeVarInt(message.animationsIndex);
    }

    public static PlayAnimationMessage deserialize(final FriendlyByteBuf buf) {
        final PlayAnimationMessage message = new PlayAnimationMessage();
        message.entityID = buf.readVarInt();
        message.animationsIndex = buf.readVarInt();
        return message;
    }

    public static class Handler<T extends Entity & EMAnimatedEntity> implements BiConsumer<PlayAnimationMessage, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final PlayAnimationMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender != null) {
                    Level level = sender.level();
                    T entity = (T) level.getEntity(message.entityID);
                    if (entity != null && message.animationsIndex != -1) {
                        EMAnimationHandler.INSTANCE.sendEMAnimationMessage(entity, entity.getAnimations()[message.animationsIndex]);
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
