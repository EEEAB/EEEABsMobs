package com.eeeab.eeeabsmobs.sever.entity;

import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class EMBossInfoServer extends ServerBossEvent {
    private final EEEABMobEntity boss;
    private final Set<ServerPlayer> playerSet = new HashSet<>();

    public EMBossInfoServer(EEEABMobEntity entity) {
        super(entity.getDisplayName(), entity.bossBloodBarsColor(), BossBarOverlay.PROGRESS);
        this.setVisible(entity.showBossBloodBars());
        this.boss = entity;
    }

    public void update() {
        if (!this.boss.showBossBloodBars()) return;
        this.setProgress(boss.getHealth() / boss.getMaxHealth());
        this.setDarkenScreen(this.boss.setDarkenScreen());
        Iterator<ServerPlayer> iterator = playerSet.iterator();
        while (iterator.hasNext()) {
            ServerPlayer player = iterator.next();
            if (this.boss.getSensing().hasLineOfSight(player)) {
                super.addPlayer(player);
                iterator.remove();
            }
        }
    }

    @Override
    public void addPlayer(ServerPlayer player) {
        if (!this.boss.showBossBloodBars()) return;
        if (this.boss.getSensing().hasLineOfSight(player)) {
            super.addPlayer(player);
        } else {
            this.playerSet.add(player);
        }
    }

    @Override
    public void removePlayer(ServerPlayer player) {
        super.removePlayer(player);
        this.playerSet.remove(player);
    }
}
