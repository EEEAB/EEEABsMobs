package com.eeeab.eeeabsmobs.client.gui;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.render.util.MultiLineTextRenderer;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.util.TranslateUtils;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TipNotificationHandler {
    private static ModConfigHandler.TipNotification.DisplayMode currentMode = ModConfigHandler.TipNotification.DisplayMode.FADE_IN_OUT;
    private static ControlledAnimation alphaAControlled;
    private static ResourceLocation icons;
    private static Component current;
    private static boolean isShowing = false;
    private static int level = 0;
    private static int targetY = 30;

    public static void init() {
        icons = new ResourceLocation(EEEABMobs.MOD_ID, "textures/gui/msg_levels.png");
        alphaAControlled = new ControlledAnimation(ModConfigHandler.CLIENT.tipNotification.displayDuration.get());
    }

    public static void clientTick() {
        if (!isShowing) return;

        alphaAControlled.updatePrevTimer();

        if (!ModConfigHandler.CLIENT.tipNotification.enabled.get() || alphaAControlled.isEnd()) {
            isShowing = false;
            current = null;
            return;
        }

        alphaAControlled.increaseTimer();
    }

    public static void renderOverlay(GuiGraphics guiGraphics, float partialTick) {
        if (!ModConfigHandler.CLIENT.tipNotification.enabled.get()) return;
        if (current == null || !isShowing) return;

        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        Window window = minecraft.getWindow();

        float alpha = getAlphaByMode(partialTick);
        //if (alpha <= 0.001f) return;

        //参数
        int maxTextWidth = ModConfigHandler.CLIENT.tipNotification.textMaxWidth.get();
        int lineSpacing = ModConfigHandler.CLIENT.tipNotification.lineSpacing.get();
        float backgroundOpacity = ModConfigHandler.CLIENT.tipNotification.backgroundOpacity.get().floatValue();
        int textBlockHeight = MultiLineTextRenderer.calculateTotalHeight(font, current, maxTextWidth, lineSpacing);
        int textBlockWidth = MultiLineTextRenderer.calculateMaxLineWidth(font, current, maxTextWidth);
        int iconSize = 16;
        int iconSpacing = 6;
        int padding = 8;
        int contentHeight = Math.max(iconSize, textBlockHeight);
        int totalHeight = contentHeight + padding * 2;
        int groupWidth = iconSize + iconSpacing + textBlockWidth;
        int totalWidth = groupWidth + padding * 2;
        int screenWidth = window.getGuiScaledWidth();
        int x = (screenWidth - totalWidth) / 2;
        int y = targetY;

        //绘制背景
        int bgAlpha = (int) (backgroundOpacity * alpha * 255);
        if (bgAlpha > 0) {
            guiGraphics.fill(x, y, x + totalWidth, y + totalHeight, (bgAlpha << 24));
        }

        //计算整体在背景框内的居中位置
        int groupX = x + padding;
        int groupY = y + padding;

        //绘制图标
        if (alpha > 0 && icons != null) {
            int iconX = groupX;
            int iconY = groupY + (contentHeight - iconSize) / 2;
            guiGraphics.setColor(1F, 1F, 1F, alpha);
            guiGraphics.blit(icons, iconX, iconY, 0, level * iconSize, iconSize, iconSize, 16, 48);
            guiGraphics.setColor(1F, 1F, 1F, 1F);
        }

        //绘制多行文本
        if (alpha > 0 && bgAlpha > 0) {
            int textX = groupX + iconSize + iconSpacing;
            int textY = groupY + (contentHeight - textBlockHeight) / 2;
            int textAlpha = (int) (alpha * 255);
            int textColor = 0xFFFFFF | (textAlpha << 24);
            MultiLineTextRenderer.drawWordWrapWithNewlines(guiGraphics, font, current, textX, textY, textBlockWidth, textColor, lineSpacing);
        }
    }

    public static void init(ResourceLocation location, int level) {
        if (!ModConfigHandler.CLIENT.tipNotification.enabled.get()) return;
        TipNotificationHandler.level = Mth.clamp(level, 0, 2);
        current = TranslateUtils.simpleText(location.getNamespace(), "tip.", location.getPath(), getTextStyle(), "");
        currentMode = ModConfigHandler.CLIENT.tipNotification.displayMode.get();
        targetY = ModConfigHandler.CLIENT.tipNotification.hudPositionY.get();
        alphaAControlled.setDuration(ModConfigHandler.CLIENT.tipNotification.displayDuration.get());
        alphaAControlled.resetTimer();
        isShowing = true;
    }

    private static ChatFormatting getTextStyle() {
        return switch (level) {
            case 1 -> ChatFormatting.YELLOW;
            case 2 -> ChatFormatting.RED;
            default -> ChatFormatting.WHITE;
        };
    }

    private static float getAlphaByMode(float partialTick) {
        float progress = alphaAControlled.getAnimationFraction(partialTick);
        if (alphaAControlled.isEnd()) return 0.0F;
        if (currentMode == ModConfigHandler.TipNotification.DisplayMode.FADE_IN_OUT) {
            if (progress < 0.125F) {
                return Mth.clamp(progress * 8F, 0F, 1F);
            } else if (progress > 0.875f) {
                float fadeOutProgress = (progress - 0.875F) / 0.125F;
                return Mth.clamp(1.0F - fadeOutProgress, 0F, 1F);
            } else return 1.0F;
        } else return 1F;
    }
}