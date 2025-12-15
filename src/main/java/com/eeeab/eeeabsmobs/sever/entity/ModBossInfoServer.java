package com.eeeab.eeeabsmobs.sever.entity;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.mob.IBoss;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.message.UpdateBossBarMessage;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;

import java.util.*;

public class ModBossInfoServer extends ServerBossEvent {
    private final Set<ServerPlayer> pendingPlayers = new HashSet<>();
    private final EEEABMobEntity boss;
    private final double maxRenderDistSq;

    public ModBossInfoServer(EEEABMobEntity entity) {
        super(entity.getDisplayName(), entity.bossBarColor(), BossBarOverlay.PROGRESS);
        this.setVisible(entity instanceof IBoss && ModConfigHandler.COMMON.others.enableShowBossBars.get());
        this.boss = entity;
        this.maxRenderDistSq = Math.pow(ModConfigHandler.COMMON.others.bossBarMaxDist.get(), 2);
    }

    public void update() {
        if (!this.isVisible() || !boss.canShowBossBar()) {
            for (ServerPlayer player : new ArrayList<>(this.getPlayers())) {
                super.removePlayer(player);
                pendingPlayers.add(player);
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
        EEEABMobs.NETWORK.sendTo(new UpdateBossBarMessage(this.getId(), boss), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        if (boss.canShowBossBar() && boss.getSensing().hasLineOfSight(player)) {
            super.addPlayer(player);
        } else {
            pendingPlayers.add(player);
        }
    }

    @Override
    public void removePlayer(ServerPlayer player) {
        EEEABMobs.NETWORK.sendTo(new UpdateBossBarMessage(this.getId(), null), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        super.removePlayer(player);
        pendingPlayers.remove(player);
    }
}
