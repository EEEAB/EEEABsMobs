package com.eeeab.animate.server.message;

import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.handler.AnimationHandler;
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
public class MessagePlayAnimation {
    private int entityID;
    private int animationsIndex;

    public MessagePlayAnimation() {

    }

    public MessagePlayAnimation(int entityID, int animationsIndex) {
        this.entityID = entityID;
        this.animationsIndex = animationsIndex;
    }

    public static void serialize(final MessagePlayAnimation message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeVarInt(message.animationsIndex);
    }

    public static MessagePlayAnimation deserialize(final FriendlyByteBuf buf) {
        final MessagePlayAnimation message = new MessagePlayAnimation();
        message.entityID = buf.readVarInt();
        message.animationsIndex = buf.readVarInt();
        return message;
    }

    public static class Handler<T extends Entity & AnimatedEntity> implements BiConsumer<MessagePlayAnimation, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessagePlayAnimation message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender != null) {
                    Level level = sender.level();
                    T entity = (T) level.getEntity(message.entityID);
                    if (entity != null && message.animationsIndex != -1) {
                        AnimationHandler.INSTANCE.sendEMAnimationMessage(entity, entity.getAnimations()[message.animationsIndex]);
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
