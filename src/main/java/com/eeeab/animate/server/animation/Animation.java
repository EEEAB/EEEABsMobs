package com.eeeab.animate.server.animation;

import net.minecraft.world.entity.AnimationState;

/**
 * 动画对象
 * <br>
 *
 * @author EEEAB
 * @说明: 该类不能使用static修饰(会使播放动画出现意想不到的问题)，需要注意加载顺序问题，避免在使用该对象时出现空值问题，
 * 比如Mob.registerGoals()优先级是要高于实体类的实例代码块，所以建议使用懒加载或提供初始化方法
 */
public class Animation extends AnimationState {
    /**
     * 动画时长
     */
    private final int duration;
    /**
     * 是否循环
     */
    private final boolean looping;

    private Animation(int duration, boolean looping) {
        this.duration = duration;
        this.looping = looping;
    }

    public static Animation create(int duration) {
        return new Animation(duration, false);
    }

    public static Animation createLoop(int duration) {
        return new Animation(duration, true);
    }

    public int getDuration() {
        return duration;
    }

    public boolean isLooping() {
        return looping;
    }

    @Override
    public String toString() {
        return "{" +
                "duration=" + duration +
                ", looping=" + looping +
                '}';
    }
}
