package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * 翻译字段工具类
 *
 * @author EEEAB
 * @version 1.4
 */
public class EMTUtils {

    private EMTUtils() {
    }

    //text style
    public static final Style STYLE_BLUE = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.BLUE));

    public static final Style STYLE_GRAY = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GRAY));

    public static final Style STYLE_GREEN = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GREEN));

    public static final Style STYLE_RED = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.RED));

    //general
    public static final String TIP_SUFFIX = ".tip";

    public static final String CONFIG_SUFFIX = ".config";

    public static final String OTHER_PREFIX = "other.";

    public static final String MOD_ID = EEEABMobs.MOD_ID + ".";

    public static final Component UNABLE_BREAKS = simpleText(OTHER_PREFIX, "unable_breaks", STYLE_BLUE);//无法破坏

    //item
    public static final String ITEM_PREFIX = "item.";

    public static final String ITEM_ARMOR_PREFIX = ITEM_PREFIX + "armor.";

    public static final String ITEM_OTHER_PREFIX = ITEM_PREFIX + OTHER_PREFIX;

    //keyboard Operation
    public static final String KEY_PREFIX = "keyboard.";

    public static final String SHIFT_DOWN = KEY_PREFIX + "shift_down.";

    public static final String RIGHT_CLICK = KEY_PREFIX + "right_click.";

    public static final String LEFT_CLICK = KEY_PREFIX + "left_click.";

    //other
    public static final String STRUCTURE_PREFIX = "structure.";

    public static Component simpleText(String prefix, String key, Style style, @Nullable Object... args) {
        return simpleText(prefix, key, style, TIP_SUFFIX, args);
    }

    public static Component simpleText(String prefix, String key, Style style, String suffix, @Nullable Object... args) {
        MutableComponent component;
        String finalPrefix = prefix + MOD_ID;
        if (style == null) style = Style.EMPTY;
        if (args == null) args = new Object[0];
        if (key == null) {
            component = Component.translatable(finalPrefix + suffix.substring(1), args).setStyle(style);
        } else {
            key = subDescriptionId(key);
            component = Component.translatable(finalPrefix + key + suffix, args).setStyle(style);
        }
        return component;
    }

    public static Component itemCoolTime(double args) {
        return simpleText(ITEM_OTHER_PREFIX, args == 0 ? "no_cd" : "cd", STYLE_GREEN, args);
    }

    public static Component simpleItemText(String key, Style style, @Nullable Object... args) {
        return simpleText(ITEM_PREFIX, key, style, args);
    }

    public static Component simpleArmorText(String key, Style style, @Nullable Object... args) {
        return simpleText(ITEM_ARMOR_PREFIX, key, style, args);
    }

    public static Component simpleOtherText(String key, Style style, @Nullable Object... args) {
        return simpleText(ITEM_OTHER_PREFIX, key, style, args);
    }

    public static Component simpleShiftDownText(String key, Style style, @Nullable Object... args) {
        return simpleText(SHIFT_DOWN, key, style, args);
    }

    public static Component simpleRightClickText(String key, Style style, @Nullable Object... args) {
        return simpleText(RIGHT_CLICK, key, style, args);
    }

    public static Component simpleLeftClickText(String key, Style style, @Nullable Object... args) {
        return simpleText(LEFT_CLICK, key, style, args);
    }

    public static Component simpleConfigText(String key, Style style, @Nullable Object... args) {
        return simpleText(OTHER_PREFIX, key, style, CONFIG_SUFFIX, args);
    }

    /**
     * 批量生成键<br>
     * 示例:<br>
     * prefix.modId.descriptionId.tip_1<br>
     * prefix.modId.descriptionId.tip_2<br>
     * prefix.modId.descriptionId.tip_n<br>
     * ...
     */
    public static List<Component> complexText(String prefix, int count, Style style, String key, @Nullable Object... args) {
        List<Component> components = new ArrayList<>();
        if (style == null) style = Style.EMPTY;
        if (args == null) args = new Object[0];
        for (int i = 1; i <= count; i++) {
            StringBuilder sb = new StringBuilder();
            components.add(Component.translatable(sb
                            .append(prefix)
                            .append(MOD_ID)
                            .append(subDescriptionId(key))
                            .append(TIP_SUFFIX)
                            .append("_")
                            .append(i)
                            .toString()
                    , args).setStyle(style));
        }
        return components;
    }

    /**
     * 批量生成键(不支持插入自定义数据)<br>
     * 示例:<br>
     * prefix.modId.AAA.tip<br>
     * prefix.modId.BBB.tip<br>
     * ...
     */
    public static List<Component> complexText(String prefix, boolean hasIndex, Style style, String... keys) {
        List<Component> components = new ArrayList<>();
        if (style == null) style = Style.EMPTY;
        for (int i = 0; i < keys.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(prefix)
                    .append(MOD_ID)
                    .append(subDescriptionId(keys[i]))
                    .append(TIP_SUFFIX);
            if (hasIndex) sb.append("_").append(i + 1);
            components.add(Component.translatable(sb.toString()).setStyle(style));
        }
        return components;
    }

    public static String subDescriptionId(String descriptionId) {
        int i = descriptionId.lastIndexOf(".");
        if (i == -1) return descriptionId;
        return descriptionId.substring(i + 1);
    }
}
