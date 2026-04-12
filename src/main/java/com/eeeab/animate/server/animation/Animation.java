package com.eeeab.animate.server.animation;

/**
 * 动画定义
 *
 * @author EEEAB
 */
public class Animation {
    /**
     * 动画时长
     */
    private final int duration;

    /**
     * 是否循环动画
     */
    private boolean looping;

    /**
     * 是否允许该动画可以与后续播放动画堆叠
     */
    private boolean overlap;

    /**
     * 动画基础速度
     */
    private float speed = 1.0F;

    /**
     * 动画基础幅度
     */
    private float scale = 1.0F;

    protected Animation(int duration) {
        this.duration = duration;
    }

    public Animation doesLoop() {
        looping = true;
        return this;
    }

    public Animation doesOverlap() {
        overlap = true;
        return this;
    }

    public Animation setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public Animation setScale(float scale) {
        this.scale = scale;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isOverlap() {
        return overlap;
    }

    public boolean isLooping() {
        return looping;
    }

    public float getSpeed() {
        return speed;
    }

    public float getScale() {
        return scale;
    }

    public static Animation create(int duration) {
        return new Animation(duration);
    }
}
