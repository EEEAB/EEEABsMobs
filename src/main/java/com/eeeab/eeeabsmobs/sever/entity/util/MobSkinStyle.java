package com.eeeab.eeeabsmobs.sever.entity.util;

public enum MobSkinStyle {
    DEFAULT_STYLE(0),
    STYLE_1(1),
    STYLE_2(2);

    private final int id;

    MobSkinStyle(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static MobSkinStyle byId(int id) {
        for (MobSkinStyle mobSkinStyle : values()) {
            if (id == mobSkinStyle.id) {
                return mobSkinStyle;
            }
        }
        return DEFAULT_STYLE;
    }

}
