package com.eeeab.eeeabsmobs.server.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.server.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.server.message.UpdateBossBarMessage;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ModBossInfoServer extends ServerBossEvent {
    private final Set<ServerPlayer> pendingPlayers = new HashSet<>();
    private final EEEABMobEntity boss;
    private final double maxRenderDistSq;

    public ModBossInfoServer(EEEABMobEntity entity) {
        super(entity.getDisplayName(), entity.bossBarColor(), BossBarOverlay.PROGRESS);
        this.setVisible(entity.canLoadBossBar());
        this.boss = entity;
        this.maxRenderDistSq = Math.pow(ModConfigHandler.COMMON.others.bossBarMaxDist.get(), 2);
    }

    public void update() {
        if (!this.isVisible() || !boss.canShowBossBar()) {
            if (!this.getPlayers().isEmpty()) {
                for (ServerPlayer player : Lists.newArrayList(this.getPlayers())) {
                    super.removePlayer(player);
                    pendingPlayers.add(player);
                }
            }
            return;
        }

        this.setProgress(boss.getHealth() / boss.getMaxHealth());
        this.setDarkenScreen(boss.setDarkenScreen());

        for (ServerPlayer player : new ArrayList<>(this.getPlayers())) {
            double distanceSq = player.distanceToSqr(boss);
            if (distanceSq > maxRenderDistSq) {
                super.removePlayer(player);
                pendingPlayers.add(player);
            }
        }
        Iterator<ServerPlayer> iterator = pendingPlayers.iterator();
        while (iterator.hasNext()) {
            ServerPlayer player = iterator.next();
            double distanceSq = player.distanceToSqr(boss);
            if (distanceSq <= maxRenderDistSq && boss.getSensing().hasLineOfSight(player)) {
                super.addPlayer(player);
                iterator.remove();
            }
        }
    }

    @Override
    public void addPlayer(ServerPlayer player) {
        if (!this.isVisible()) return;
        EEEABMobs.NETWORK.sendTo(new UpdateBossBarMessage(this.getId(), boss), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        if (boss.canShowBossBar() && boss.getSensing().hasLineOfSight(player)) {
            super.addPlayer(player);
        } else {
            pendingPlayers.add(player);
        }
    }

    @Override
    public void removePlayer(ServerPlayer player) {
        if (this.isVisible()) EEEABMobs.NETWORK.sendTo(new UpdateBossBarMessage(this.getId(), null), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        super.removePlayer(player);
        pendingPlayers.remove(player);
    }
}
