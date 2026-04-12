package com.eeeab.animate.server.animation;

import net.minecraft.util.Mth;

import javax.annotation.Nullable;

/**
 * 带有提示信息的动画定义
 *
 * @author EEEAB
 */
public class AnimationNotification extends Animation {
    @Nullable
    private final String msgId;
    /**
     * 提示框等级 (0-2)
     */
    private int level;

    private AnimationNotification(int duration, @Nullable String msgId) {
        super(duration);
        this.msgId = msgId;
    }

    public AnimationNotification level(int level) {
        this.level = Mth.clamp(level, 0, 2);
        return this;
    }

    @Nullable
    public String getMsgId() {
        return msgId;
    }

    public int getLevel() {
        return level;
    }

    public static AnimationNotification create(int duration, @Nullable String msg) {
        return new AnimationNotification(duration, msg);
    }
}
