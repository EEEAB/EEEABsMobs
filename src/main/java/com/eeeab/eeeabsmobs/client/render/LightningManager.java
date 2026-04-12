package com.eeeab.eeeabsmobs.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.joml.Matrix4f;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

//参考自: https://github.com/AlexModGuy/Ice_and_Fire/blob/1.20/src/main/java/com/github/alexthe666/iceandfire/client/particle/LightningRender.java
public class LightningManager {
    private static final float REFRESH_INTERVAL = 3F;
    private static final double MAX_OWNER_TRACK_TIME = 100;
    private double refreshTime = 0.0;
    private final Minecraft minecraft = Minecraft.getInstance();
    private final Map<Object, BoltOwnerData> boltOwners = new ConcurrentHashMap<>();

    /**
     * 更新与渲染所有闪电实例
     */
    public void render(float partialTicks, PoseStack matrixStack, MultiBufferSource bufferSource) {
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.lightning());
        Matrix4f matrix = matrixStack.last().pose();
        long renderTicks = minecraft.levelRenderer.getTicks();
        double now = renderTicks + partialTicks;
        boolean refresh = now - refreshTime >= 1.0 / REFRESH_INTERVAL;
        if (refresh) refreshTime = now;
        Iterator<Map.Entry<Object, BoltOwnerData>> iter = boltOwners.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Object, BoltOwnerData> entry = iter.next();
            BoltOwnerData data = entry.getValue();

            if (refresh) data.bolts.removeIf(bolt -> bolt.tick(now));

            if (data.bolts.isEmpty() && data.lastBolt != null && data.lastBolt.isConsecutive()) {
                data.addBolt(new BoltInstance(data.lastBolt, now), now);
            }

            data.bolts.forEach(bolt -> bolt.render(matrix, buffer, now));

            if (data.bolts.isEmpty() && now - data.lastUpdateTimestamp >= MAX_OWNER_TRACK_TIME) {
                iter.remove();
            }
        }
    }

    /**
     * 更新或新建某个所有者的闪电
     *
     * @param owner       用于区分闪电所有者
     * @param newBolt 新的闪电
     */
    public void update(Object owner, LightningBolt newBolt) {
        if (minecraft.level == null) return;

        BoltOwnerData data = boltOwners.computeIfAbsent(owner, k -> new BoltOwnerData());
        data.lastBolt = newBolt;
        long renderTicks = minecraft.levelRenderer.getTicks();
        double now = renderTicks + minecraft.getPartialTick();

        if ((!newBolt.isConsecutive() || data.bolts.isEmpty()) && now - data.lastBoltTimestamp >= data.lastBoltDelay) {
            data.addBolt(new BoltInstance(newBolt, now), now);
        }
        data.lastUpdateTimestamp = now;
    }

    private static class BoltOwnerData {
        private final Set<BoltInstance> bolts = ConcurrentHashMap.newKeySet();
        private LightningBolt lastBolt;
        private double lastBoltTimestamp = 0.0;
        private double lastUpdateTimestamp = 0.0;
        private double lastBoltDelay;

        void addBolt(BoltInstance instance, double now) {
            bolts.add(instance);
            lastBoltDelay = instance.bolt.getSpawnDelay();
            lastBoltTimestamp = now;
        }
    }

    //单个闪电实例
    private static class BoltInstance {
        private final LightningBolt bolt;
        private final List<LightningBolt.BoltQuads> renderQuads;
        private final double createdAt;

        BoltInstance(LightningBolt bolt, double now) {
            this.bolt = bolt;
            this.renderQuads = bolt.generate();
            this.createdAt = now;
        }

        //渲染闪电
        void render(Matrix4f matrix, VertexConsumer buffer, double now) {
            float lifeScale = (float) ((now - createdAt) / bolt.getLifespan());
            lifeScale = Math.min(1, lifeScale);
            Pair<Integer, Integer> bounds = bolt.getFadeFunction().getRenderBounds(renderQuads.size(), lifeScale);
            for (int i = bounds.getLeft(); i < bounds.getRight(); i++) {
                for (Vec3 v : renderQuads.get(i).getVecs()) {
                    buffer.vertex(matrix, (float) v.x, (float) v.y, (float) v.z)
                            .color(bolt.getColor().x(), bolt.getColor().y(), bolt.getColor().z(), bolt.getColor().w())
                            .endVertex();
                }
            }
        }

        boolean tick(double now) {
            return now - createdAt >= bolt.getLifespan();
        }
    }
}

