package com.eeeab.animate.server.message;

import com.eeeab.animate.server.animation.Animation;
import com.eeeab.animate.server.animation.EMAnimatedEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 停止动画数据包
 */
public class StopAnimationMessage {
    private int entityID;
    //是否只停止叠加动画
    private boolean onlyStopSuperposition;

    public StopAnimationMessage() {

    }

    public StopAnimationMessage(int entityID, boolean onlyStopSuperposition) {
        this.entityID = entityID;
        this.onlyStopSuperposition = onlyStopSuperposition;
    }

    public static void serialize(final StopAnimationMessage message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeBoolean(message.onlyStopSuperposition);
    }

    public static StopAnimationMessage deserialize(final FriendlyByteBuf buf) {
        final StopAnimationMessage message = new StopAnimationMessage();
        message.entityID = buf.readVarInt();
        message.onlyStopSuperposition = buf.readBoolean();
        return message;
    }

    public static class Handler implements BiConsumer<StopAnimationMessage, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final StopAnimationMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (Minecraft.getInstance().level != null) {
                    Entity entity = Minecraft.getInstance().level.getEntity(message.entityID);
                    if (entity instanceof EMAnimatedEntity animationEntity && animationEntity.getAnimations() != null) {
                        Stream<Animation> stream = Arrays.stream(animationEntity.getAnimations());
                        if (message.onlyStopSuperposition) {
                            stream.filter(Animation::isSuperposition).forEach(AnimationState::stop);
                        } else {
                            stream.forEach(AnimationState::stop);
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
