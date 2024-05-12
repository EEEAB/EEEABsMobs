package com.eeeab.lib.server.animation;

import net.minecraft.world.entity.AnimationState;

/**
 * 动画对象
 * <br>
 * 注意：<br>
 * *该类不能使用static修饰(会使播放动画出现意想不到的问题)，需要注意加载顺序问题，避免在使用该对象时出现空值问题，
 * 比如Mob.registerGoals()优先级是要高于实体类的实例代码块，所以建议使用懒加载或提供初始化方法
 * @author EEEAB
 */
public class EMAnimation extends AnimationState {
    private final int duration;
    private boolean looping;

    private EMAnimation(int duration) {
        this.duration = duration;
    }

    public static EMAnimation create(int duration) {
        return new EMAnimation(duration);
    }

    public int getDuration() {
        return duration;
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }
}
