package com.eeeab.eeeabsmobs.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;

public class BossBarHandler {

    public static void renderBossBar(CustomizeGuiOverlayEvent.BossEventProgress event, BossBarConfig config) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        int y = event.getY();
        int w = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int j = y - 9;
        //基础血条
        Minecraft.getInstance().getProfiler().push("modBossBarBase");
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, config.getBaseTexture());
        drawBar(guiGraphics, event.getX() + 1, y + config.getBaseOffsetY(), event.getBossEvent(), config);
        Component component = event.getBossEvent().getName().copy().withStyle(config.getTextColor());
        Minecraft.getInstance().getProfiler().pop();
        //文字
        int k = Minecraft.getInstance().font.width(component);
        int l = w / 2 - k / 2;
        guiGraphics.drawString(Minecraft.getInstance().font, component, l, j, 16777215);
        //血条外层
        if (config.hasOverlay()) {
            Minecraft.getInstance().getProfiler().push("modBossBarOverlay");
            RenderSystem.setShaderTexture(0, config.getOverlayTexture());
            event.getGuiGraphics().blit(config.getOverlayTexture(), event.getX() + 1 + config.getOverlayOffsetX(), y + config.getOverlayOffsetY() + config.getBaseOffsetY(),
                    0, 0, config.getOverlayWidth(), config.getOverlayHeight(), config.getOverlayWidth(), config.getOverlayHeight());
            Minecraft.getInstance().getProfiler().pop();
        }
        event.setIncrement(config.getVerticalIncrement());
    }

    private static void drawBar(GuiGraphics guiGraphics, int x, int y, BossEvent event, BossBarConfig config) {
        guiGraphics.blit(config.getBaseTexture(), x, y, 0, 0, 182, config.getBaseHeight(), 256, config.getBaseTextureHeight());
        int i = (int) (event.getProgress() * 183.0F);
        if (i > 0) {
            guiGraphics.blit(config.getBaseTexture(), x, y, 0, config.getBaseHeight(), i, config.getBaseHeight(), 256, config.getBaseTextureHeight());
        }
    }
}