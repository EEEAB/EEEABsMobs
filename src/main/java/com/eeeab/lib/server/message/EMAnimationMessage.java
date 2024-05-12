package com.eeeab.lib.server.message;

import com.eeeab.lib.server.animation.EMAnimatedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * 动画数据包
 */
public class EMAnimationMessage {
    private int entityID;
    private int animationsIndex;

    public EMAnimationMessage() {

    }

    public EMAnimationMessage(int entityID, int animationsIndex) {
        this.entityID = entityID;
        this.animationsIndex = animationsIndex;
    }

    public static void serialize(final EMAnimationMessage message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeVarInt(message.animationsIndex);
    }

    public static EMAnimationMessage deserialize(final FriendlyByteBuf buf) {
        final EMAnimationMessage message = new EMAnimationMessage();
        message.entityID = buf.readVarInt();
        message.animationsIndex = buf.readVarInt();
        return message;
    }

    public static class Handler implements BiConsumer<EMAnimationMessage, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final EMAnimationMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (Minecraft.getInstance().level != null) {
                    Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
                    if (entity instanceof EMAnimatedEntity animationEntity) {
                        if (message.animationsIndex == -1) {
                            animationEntity.setEMAnimation(EMAnimatedEntity.NO_EMANIMATION);
                            animationEntity.getEMAnimation().stop();
                        } else {
                            animationEntity.setEMAnimation(animationEntity.getEMAnimations()[message.animationsIndex]);
                        }
                        animationEntity.getEMAnimation().start(entity.tickCount);
                        animationEntity.setEMAnimationTick(0);
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
