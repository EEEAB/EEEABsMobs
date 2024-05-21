package com.eeeab.animate.server.animation;

import org.jetbrains.annotations.NotNull;

/**
 * 动画实体
 *
 * @author EEEAB
 */
public interface EMAnimatedEntity {
    /**
     * @return 获取动画关键Tick
     */
    int getAnimationTick();

    /**
     * 设置动画Tick
     *
     * @param tick 游戏刻
     */
    void setAnimationTick(int tick);

    /**
     * 获取正在播放动画
     *
     * @return 当前动画
     */
    Animation getAnimation();

    /**
     * 设置动画
     *
     * @param animation 准备播放动画
     */
    void setAnimation(Animation animation);

    /**
     * 用于设置每个实体的闲置动画
     *
     * @return 闲置动画
     */
    @NotNull
    Animation getNoAnimation();

    /**
     * 通过数组索引作为网络传输的参数，保证客户端与服务端的数据一致
     *
     * @return 动画集合
     */
    Animation[] getAnimations();
}
