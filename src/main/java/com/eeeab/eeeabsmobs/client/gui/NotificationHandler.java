package com.eeeab.eeeabsmobs.client.gui;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.util.ControlledAnimation;
import com.eeeab.eeeabsmobs.client.util.MultiLineTextRenderer;
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

public class NotificationHandler {
    private static ModConfigHandler.CombatPrompt.DisplayMode currentMode = ModConfigHandler.CombatPrompt.DisplayMode.FADE_IN_OUT;
    private static ControlledAnimation alphaAnimation;
    private static ResourceLocation promptIcons;
    private static Component currentPrompt;
    private static boolean isShowing = false;
    private static int hintLevel = 0;
    private static int targetY = 30;

    public static void init() {
        promptIcons = new ResourceLocation(EEEABMobs.MOD_ID, "textures/gui/msg_levels.png");
        alphaAnimation = new ControlledAnimation(ModConfigHandler.CLIENT.combatPrompt.displayDuration.get());
    }

    /**
     * 间接提示使用
     */
    public static void showHint(ResourceLocation location, int level) {
        init("hint.", location, level);
    }

    /**
     * 直接提示使用
     */
    public static void showPrompt(ResourceLocation location, int level) {
        init("prompt.", location, level);
    }

    public static void clientTick() {
        if (!isShowing) return;

        alphaAnimation.updatePrevTimer();

        if (!ModConfigHandler.CLIENT.combatPrompt.enabled.get() || alphaAnimation.isEnd()) {
            isShowing = false;
            currentPrompt = null;
            return;
        }

        alphaAnimation.increaseTimer();
    }

    public static void renderOverlay(GuiGraphics guiGraphics, float partialTick) {
        if (!ModConfigHandler.CLIENT.combatPrompt.enabled.get()) return;
        if (currentPrompt == null || !isShowing) return;

        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        Window window = minecraft.getWindow();

        float alpha = getAlphaByMode(partialTick);
        //if (alpha <= 0.001f) return;

        //参数
        int maxTextWidth = ModConfigHandler.CLIENT.combatPrompt.textMaxWidth.get();
        int lineSpacing = ModConfigHandler.CLIENT.combatPrompt.lineSpacing.get();
        float backgroundOpacity = ModConfigHandler.CLIENT.combatPrompt.backgroundOpacity.get().floatValue();
        int textBlockHeight = MultiLineTextRenderer.calculateTotalHeight(font, currentPrompt, maxTextWidth, lineSpacing);
        int textBlockWidth = MultiLineTextRenderer.calculateMaxLineWidth(font, currentPrompt, maxTextWidth);
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
        if (alpha > 0 && promptIcons != null) {
            int iconX = groupX;
            int iconY = groupY + (contentHeight - iconSize) / 2;
            guiGraphics.setColor(1F, 1F, 1F, alpha);
            guiGraphics.blit(promptIcons, iconX, iconY, 0, hintLevel * iconSize, iconSize, iconSize, 16, 48);
            guiGraphics.setColor(1F, 1F, 1F, 1F);
        }

        //绘制多行文本
        if (alpha > 0) {
            int textX = groupX + iconSize + iconSpacing;
            int textY = groupY + (contentHeight - textBlockHeight) / 2;
            int textAlpha = (int) (alpha * 255);
            int textColor = 0xFFFFFF | (textAlpha << 24);
            MultiLineTextRenderer.drawWordWrapWithNewlines(guiGraphics, font, currentPrompt, textX, textY, textBlockWidth, textColor, lineSpacing);
        }
    }

    private static void init(String prefix, ResourceLocation location, int level) {
        if (!ModConfigHandler.CLIENT.combatPrompt.enabled.get()) return;
        hintLevel = Mth.clamp(level, 0, 2);
        currentPrompt = TranslateUtils.simpleText(location.getNamespace(), prefix, location.getPath(), getTextStyle(), "");
        currentMode = ModConfigHandler.CLIENT.combatPrompt.displayMode.get();
        targetY = ModConfigHandler.CLIENT.combatPrompt.hudPositionY.get();
        alphaAnimation.setDuration(ModConfigHandler.CLIENT.combatPrompt.displayDuration.get());
        alphaAnimation.resetTimer();
        isShowing = true;
    }

    private static ChatFormatting getTextStyle() {
        return switch (hintLevel) {
            case 1 -> ChatFormatting.YELLOW;
            case 2 -> ChatFormatting.RED;
            default -> ChatFormatting.WHITE;
        };
    }

    private static float getAlphaByMode(float partialTick) {
        float progress = alphaAnimation.getAnimationFraction(partialTick);
        if (alphaAnimation.isEnd()) return 0.0F;
        if (currentMode == ModConfigHandler.CombatPrompt.DisplayMode.FADE_IN_OUT) {
            if (progress < 0.125F) {
                return Mth.clamp(progress * 8F, 0F, 1F);
            } else if (progress > 0.875f) {
                float fadeOutProgress = (progress - 0.875F) / 0.125F;
                return Mth.clamp(1.0F - fadeOutProgress, 0F, 1F);
            } else return 1.0F;
        } else return 1F;
    }
}