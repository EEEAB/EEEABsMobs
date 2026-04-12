package com.eeeab.animate.server.animation;

import com.eeeab.animate.server.animation.keyframe.KeyframeManager;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;

/**
 * 动画实体
 *
 * @author EEEAB
 */
public interface AnimatedEntity {
    /**
     * 无动画(默认值)
     */
    Animation NO_ANIMATION = Animation.create(0);

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
     * 通过数组索引作为网络传输的参数，保证客户端与服务端的数据一致
     *
     * @return 动画集合
     */
    Animation[] getAnimations();

    /**
     * 获取动画状态
     *
     * @return 当前动画状态
     */
    AnimationState getAnimationState(Animation animation);

    /**
     * 可选-获取叠加动画状态
     */
    default AnimationState getOverlapAnimationState(Animation animation) {
        return null;
    }

    /**
     * 获取该动画播放速度
     *
     * @return 动画速度
     */
    default float getAnimationSpeed(Animation animation) {
        return animation.getSpeed();
    }

    /**
     * 获取该动画播放幅度
     *
     * @return 动画幅度
     */
    default float getAnimationScale(Animation animation) {
        return animation.getScale();
    }

    /**
     * 可选-获取关键帧触发器
     */
    default <T extends Entity & AnimatedEntity> KeyframeManager<T> getKeyframeManager() {
        return null;
    }
}
