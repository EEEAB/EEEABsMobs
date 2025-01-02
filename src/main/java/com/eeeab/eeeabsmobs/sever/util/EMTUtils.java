package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * 翻译字段工具类
 *
 * @author EEEAB
 * @version 1.5
 */
public class EMTUtils {
    public static boolean SHOW_ITEM_CD;

    private EMTUtils() {
    }

    //general
    public static final String TIP_SUFFIX = ".tip";

    public static final String CONFIG_SUFFIX = ".config";

    public static final String OTHER_PREFIX = "other.";

    public static final String MOD_ID = EEEABMobs.MOD_ID + ".";

    //item
    public static final String ITEM_PREFIX = "item.";

    public static final String ITEM_ARMOR_PREFIX = ITEM_PREFIX + "armor.";

    public static final String ITEM_OTHER_PREFIX = ITEM_PREFIX + OTHER_PREFIX;

    public static final String ITEM_STRUCTURE_PREFIX = ITEM_PREFIX + "structure.";

    //other
    public static final String KEY_PREFIX = "hotkeys.";

    //无法破坏
    public static final Component UNABLE_BREAKS = simpleText(ITEM_OTHER_PREFIX, "unable_breaks", ChatFormatting.BLUE);

    //按住Shift
    public static final Component HOLD_SHIFT_KEY = simpleText(ITEM_PREFIX + KEY_PREFIX, "shift_down", ChatFormatting.GREEN);

    public static Component simpleText(String prefix, String key, ChatFormatting format, @Nullable Object... args) {
        return simpleText(prefix, key, format, TIP_SUFFIX, args);
    }

    public static Component simpleText(String prefix, String key, ChatFormatting format, String suffix, @Nullable Object... args) {
        MutableComponent component;
        String finalPrefix = prefix + MOD_ID;
        if (args == null) args = new Object[0];
        if (key == null) {
            component = Component.translatable(finalPrefix + suffix.substring(1), args);
        } else {
            key = subDescriptionId(key);
            component = Component.translatable(finalPrefix + key + suffix, args);
        }
        if (format != null) component.withStyle(format);
        return component;
    }

    public static Component itemCoolTime(double args) {
        return simpleText(ITEM_OTHER_PREFIX, args == 0 ? "no_cd" : "cd", ChatFormatting.GREEN, args);
    }

    public static Component simpleItemText(String key, @Nullable Object... args) {
        return simpleText(ITEM_PREFIX, key, ChatFormatting.GRAY, args);
    }

    public static Component simpleArmorText(String key, ChatFormatting format, @Nullable Object... args) {
        return simpleText(ITEM_ARMOR_PREFIX, key, format, args);
    }

    public static Component simpleOtherText(String key, ChatFormatting format, @Nullable Object... args) {
        return simpleText(ITEM_OTHER_PREFIX, key, format, args);
    }

    public static Component simpleConfigText(String key, ChatFormatting format, @Nullable Object... args) {
        return simpleText(OTHER_PREFIX, key, format, CONFIG_SUFFIX, args);
    }

    /**
     * 批量生成键<br>
     * 示例:<br>
     * prefix.modId.descriptionId.tip_1<br>
     * prefix.modId.descriptionId.tip_2<br>
     * prefix.modId.descriptionId.tip_n<br>
     * ...
     */
    public static List<Component> complexText(String prefix, int count, ChatFormatting format, String key, @Nullable Object... args) {
        List<Component> components = new ArrayList<>();
        if (args == null) args = new Object[0];
        for (int i = 1; i <= count; i++) {
            StringBuilder sb = new StringBuilder();
            MutableComponent component = Component.translatable(sb
                            .append(prefix)
                            .append(MOD_ID)
                            .append(subDescriptionId(key))
                            .append(TIP_SUFFIX)
                            .append("_")
                            .append(i)
                            .toString()
                    , args);
            if (format != null) component.withStyle(format);
            components.add(component);
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
    public static List<Component> complexText(String prefix, boolean hasIndex, ChatFormatting format, String... keys) {
        List<Component> components = new ArrayList<>();
        for (int i = 0; i < keys.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(prefix)
                    .append(MOD_ID)
                    .append(subDescriptionId(keys[i]))
                    .append(TIP_SUFFIX);
            if (hasIndex) sb.append("_").append(i + 1);
            MutableComponent component = Component.translatable(sb.toString());
            if (format != null) component.withStyle(format);
            components.add(component);
        }
        return components;
    }

    public static String subDescriptionId(String descriptionId) {
        int i = descriptionId.lastIndexOf(".");
        if (i == -1) return descriptionId;
        return descriptionId.substring(i + 1);
    }
}
