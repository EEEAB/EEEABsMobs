package com.eeeab.animate.server.message;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.AnimatedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * 动画数据包
 */
public class MessageAnimation {
    private int entityID;
    private int animationsIndex;

    public MessageAnimation() {

    }

    public MessageAnimation(int entityID, int animationsIndex) {
        this.entityID = entityID;
        this.animationsIndex = animationsIndex;
    }

    public static void serialize(final MessageAnimation message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeVarInt(message.animationsIndex);
    }

    public static MessageAnimation deserialize(final FriendlyByteBuf buf) {
        final MessageAnimation message = new MessageAnimation();
        message.entityID = buf.readVarInt();
        message.animationsIndex = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<MessageAnimation, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageAnimation message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (Minecraft.getInstance().level != null) {
                    Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
                    if (entity instanceof AnimatedEntity animatedEntity) {
                        //判断当前动画是否是可堆叠的 是可堆叠则由客户端单独计算结束时间并停止动画
                        if (!animatedEntity.getAnimation().isOverlap()) animatedEntity.getAnimation().stop();
                        if (message.animationsIndex == -1) {
                            animatedEntity.setAnimation(AnimatedEntity.NO_ANIMATION);
                        } else {
                            Animation animation = animatedEntity.getAnimations()[message.animationsIndex];
                            animatedEntity.setAnimation(animation);
                            //会覆盖可叠加动画
                            animation.start(entity.tickCount);
                        }
                        animatedEntity.setAnimationTick(0);
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
