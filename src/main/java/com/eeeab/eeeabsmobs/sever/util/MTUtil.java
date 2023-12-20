package com.eeeab.eeeabsmobs.sever.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.util.ArrayList;
import java.util.List;

/**
 * 翻译字段常量类:
 * M->Mod T->Translate
 *
 * @author EEEAB
 */
public class MTUtil {

    private MTUtil() {
    }

    //text style
    public static final Style STYLE_GRAY = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GRAY));

    public static final Style STYLE_GREEN = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GREEN));

    public static final Style STYLE_RED = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.RED));

    //general
    public static final String TIP_SUFFIX = ".tip";

    public static final String OTHER_PREFIX = "other.";

    public static final String MOD_ID = EEEABMobs.MOD_ID + ".";

    public static final Component UNABLE_BREAKS = simpleText(OTHER_PREFIX, "unable_breaks", MTUtil.STYLE_GRAY);//无法破坏

    //item
    public static final String ITEM_PREFIX = "item.";

    public static final String TOOLTIP_PREFIX = "tooltip.";

    public static final String ARMOR_PREFIX = TOOLTIP_PREFIX + "armor.";

    public static final String WEAPON_PREFIX = TOOLTIP_PREFIX + "weapon.";

    public static final String TOOLTIP_OTHER_PREFIX = TOOLTIP_PREFIX + OTHER_PREFIX;

    //keyboard Operation
    public static final String KEY_PREFIX = "keyboard.";

    public static final String SHIFT_DOWN = KEY_PREFIX + "shift_down.";

    public static final String RIGHT_CLICK = KEY_PREFIX + "right_click.";

    public static final String LEFT_CLICK = KEY_PREFIX + "left_click.";

    //other
    public static final String STRUCTURE_PREFIX = "structure.";


    public static Component simpleText(String prefix, String key, Style style) {
        MutableComponent component;
        String finalPrefix = prefix + MOD_ID;
        if (style == null) style = Style.EMPTY;
        if (key == null) {
            component = Component.translatable(finalPrefix + TIP_SUFFIX.substring(1)).setStyle(style);
        } else {
            component = Component.translatable(finalPrefix + key + TIP_SUFFIX).setStyle(style);
        }
        return component;
    }

    public static Component simpleItemText(String key, Style style) {
        return simpleText(ITEM_PREFIX, key, style);
    }

    public static Component simpleArmorText(String key, Style style) {
        return simpleText(ARMOR_PREFIX, key, style);
    }

    public static Component simpleWeaponText(String key, Style style) {
        return simpleText(WEAPON_PREFIX, key, style);
    }

    public static Component simpleOtherText(String key, Style style) {
        return simpleText(TOOLTIP_OTHER_PREFIX, key, style);
    }

    public static Component simpleShiftDownText(String key, Style style) {
        return simpleText(SHIFT_DOWN, key, style);
    }

    public static Component simpleRightClickText(String key, Style style) {
        return simpleText(RIGHT_CLICK, key, style);
    }

    public static Component simpleLeftClickText(String key, Style style) {
        return simpleText(LEFT_CLICK, key, style);
    }

    public static List<Component> complexText(String prefix, int count, Style style, String key) {
        List<Component> components = new ArrayList<>();
        if (style == null) style = Style.EMPTY;
        for (int i = 1; i <= count; i++) {
            StringBuilder sb = new StringBuilder();
            components.add(Component.translatable(sb
                    .append(prefix)
                    .append(MOD_ID)
                    .append(key)
                    .append(TIP_SUFFIX)
                    .append("_")
                    .append(i)
                    .toString()
            ).setStyle(style));
        }
        return components;
    }

    public static List<Component> complexText(String prefix, boolean hasIndex, Style style, String... keys) {
        List<Component> components = new ArrayList<>();
        if (style == null) style = Style.EMPTY;
        for (int i = 0; i < keys.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(prefix)
                    .append(MOD_ID)
                    .append(keys[i])
                    .append(TIP_SUFFIX);
            if (hasIndex) sb.append("_").append(i + 1);
            components.add(Component.translatable(sb.toString()).setStyle(style));
        }
        return components;
    }
}
