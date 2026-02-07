package com.eeeab.eeeabsmobs.client.render.util;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class MultiLineTextRenderer {

    /**
     * 绘制支持换行符的多行文本
     *
     * @param guiGraphics GUI图形对象
     * @param font        字体
     * @param text        文本组件（可以包含换行符 \n）
     * @param x           起始X坐标
     * @param y           起始Y坐标
     * @param maxWidth    最大宽度（超过此宽度会自动换行）
     * @param color       文本颜色（ARGB格式）
     * @param lineSpacing 行间距
     */
    public static void drawWordWrapWithNewlines(GuiGraphics guiGraphics, Font font,
                                                Component text, int x, int y,
                                                int maxWidth, int color, int lineSpacing) {
        String textString = text.getString();
        String[] manualLines = textString.split("\n");
        int currentY = y;
        for (int i = 0; i < manualLines.length; i++) {
            String line = manualLines[i];
            Component lineComponent = createComponentFromLine(text, line);
            List<FormattedCharSequence> wrappedLines = font.split(lineComponent, maxWidth);
            for (int j = 0; j < wrappedLines.size(); j++) {
                FormattedCharSequence wrappedLine = wrappedLines.get(j);
                guiGraphics.drawString(font, wrappedLine, x, currentY, color, false);
                currentY += font.lineHeight;
                if (j < wrappedLines.size() - 1) {
                    currentY += lineSpacing;
                }
            }
            if (i < manualLines.length - 1) {
                currentY += lineSpacing;
            }
        }
    }

    /**
     * 从原始组件和行字符串创建新组件（保留样式）
     */
    private static Component createComponentFromLine(Component original, String line) {
        MutableComponent lineComponent = Component.literal(line);
        lineComponent.setStyle(original.getStyle());
        return lineComponent;
    }

    /**
     * 计算多行文本的总高度
     */
    public static int calculateTotalHeight(Font font, Component text,
                                           int maxWidth, int lineSpacing) {
        String textString = text.getString();
        String[] manualLines = textString.split("\n");
        int totalHeight = 0;
        for (int i = 0; i < manualLines.length; i++) {
            String line = manualLines[i];
            Component lineComponent = createComponentFromLine(text, line);
            List<FormattedCharSequence> wrappedLines = font.split(lineComponent, maxWidth);
            int blockHeight = 0;
            for (int j = 0; j < wrappedLines.size(); j++) {
                blockHeight += font.lineHeight;
                if (j < wrappedLines.size() - 1) {
                    blockHeight += lineSpacing;
                }
            }
            totalHeight += blockHeight;
            if (i < manualLines.length - 1) {
                totalHeight += lineSpacing;
            }
        }
        return totalHeight;
    }

    /**
     * 计算多行文本中最大行的宽度
     *
     * @param font        字体
     * @param text        文本组件
     * @param maxWidth    最大允许宽度
     * @return 最大行的宽度
     */
    public static int calculateMaxLineWidth(Font font, Component text, int maxWidth) {
        String textString = text.getString();
        String[] manualLines = textString.split("\n");
        int maxLineWidth = 0;

        for (String line : manualLines) {
            Component lineComponent = createComponentFromLine(text, line);
            List<FormattedCharSequence> wrappedLines = font.split(lineComponent, maxWidth);

            for (FormattedCharSequence wrappedLine : wrappedLines) {
                int lineWidth = font.width(wrappedLine);
                if (lineWidth > maxLineWidth) {
                    maxLineWidth = lineWidth;
                }
            }
        }

        return maxLineWidth;
    }
}