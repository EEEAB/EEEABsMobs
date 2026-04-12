package com.eeeab.animate.server.message;

import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 停止动画数据包
 */
public class MessageStopAnimation {
    private int entityID;
    //是否只停止叠加动画
    private boolean onlyStopOverlapAnimation;

    public MessageStopAnimation() {
    }

    public MessageStopAnimation(int entityID, boolean onlyStopOverlapAnimation) {
        this.entityID = entityID;
        this.onlyStopOverlapAnimation = onlyStopOverlapAnimation;
    }

    public static void serialize(final MessageStopAnimation message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeBoolean(message.onlyStopOverlapAnimation);
    }

    public static MessageStopAnimation deserialize(final FriendlyByteBuf buf) {
        final MessageStopAnimation message = new MessageStopAnimation();
        message.entityID = buf.readVarInt();
        message.onlyStopOverlapAnimation = buf.readBoolean();
        return message;
    }

    public static class Handler implements BiConsumer<MessageStopAnimation, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final MessageStopAnimation message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (Minecraft.getInstance().level != null) {
                    Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
                    if (entity instanceof AnimatedEntity animationEntity && animationEntity.getAnimations() != null) {
                        Stream<Animation> stream = Arrays.stream(animationEntity.getAnimations());
                        if (!message.onlyStopOverlapAnimation) {
                            animationEntity.getAnimationState(AnimatedEntity.NO_ANIMATION).stop();
                        }
                        stream.filter(Animation::isOverlap).forEach(a -> animationEntity.getAnimationState(a).stop());
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
