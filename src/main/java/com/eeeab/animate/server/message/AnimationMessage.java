package com.eeeab.animate.server.message;

import com.eeeab.animate.server.animation.EMAnimatedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * 动画数据包
 */
public class AnimationMessage {
    private int entityID;
    private int animationsIndex;

    public AnimationMessage() {

    }

    public AnimationMessage(int entityID, int animationsIndex) {
        this.entityID = entityID;
        this.animationsIndex = animationsIndex;
    }

    public static void serialize(final AnimationMessage message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeVarInt(message.animationsIndex);
    }

    public static AnimationMessage deserialize(final FriendlyByteBuf buf) {
        final AnimationMessage message = new AnimationMessage();
        message.entityID = buf.readVarInt();
        message.animationsIndex = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<AnimationMessage, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final AnimationMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (Minecraft.getInstance().level != null) {
                    Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
                    if (entity instanceof EMAnimatedEntity animationEntity) {
                        if (message.animationsIndex == -1) {
                            animationEntity.setAnimation(animationEntity.getNoAnimation());
                        } else {
                            //判断当前动画是否是可堆叠的 是可堆叠则由客户端单独计算结束时间并停止动画
                            if (!animationEntity.getAnimation().isSuperposition()) animationEntity.getAnimation().stop();
                            animationEntity.setAnimation(animationEntity.getAnimations()[message.animationsIndex]);
                        }
                        animationEntity.setAnimationTick(0);
                        animationEntity.getAnimation().start(entity.tickCount);
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
