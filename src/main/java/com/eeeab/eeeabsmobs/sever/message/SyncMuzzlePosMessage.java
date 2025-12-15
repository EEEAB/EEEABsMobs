package com.eeeab.eeeabsmobs.sever.message;

import com.eeeab.eeeabsmobs.sever.entity.mob.relicron.EntityRelicAnnihilator;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * 客户端发起同步炮口坐标并发射弹丸
 */
public class SyncMuzzlePosMessage {
    private int entityID;
    private double x, y, z;

    public SyncMuzzlePosMessage() {

    }

    public SyncMuzzlePosMessage(int entityID, double x, double y, double z) {
        this.entityID = entityID;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void serialize(final SyncMuzzlePosMessage message, final FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityID);
        buf.writeDouble(message.x);
        buf.writeDouble(message.y);
        buf.writeDouble(message.z);
    }

    public static SyncMuzzlePosMessage deserialize(final FriendlyByteBuf buf) {
        final SyncMuzzlePosMessage message = new SyncMuzzlePosMessage();
        message.entityID = buf.readVarInt();
        message.x = buf.readDouble();
        message.y = buf.readDouble();
        message.z = buf.readDouble();
        return message;
    }

    public static class Handler implements BiConsumer<SyncMuzzlePosMessage, Supplier<NetworkEvent.Context>> {
        @Override
        public void accept(final SyncMuzzlePosMessage message, final Supplier<NetworkEvent.Context> contextSupplier) {
            final NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                ServerPlayer sender = context.getSender();
                if (sender != null) {
                    Level level = sender.level();
                    if (level.getEntity(message.entityID) instanceof EntityRelicAnnihilator annihilator) {
                        if (!annihilator.serverSideVerified()) return;
                        annihilator.performRangedAttack(new Vec3(message.x, message.y, message.z));
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}
