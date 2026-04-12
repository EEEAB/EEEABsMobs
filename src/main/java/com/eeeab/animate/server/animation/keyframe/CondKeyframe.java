package com.eeeab.animate.server.animation.keyframe;

import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import net.minecraft.world.entity.Entity;

/**
 * 条件关键帧处理
 *
 * @author EEEAB
 */
public interface CondKeyframe<T extends Entity & AnimatedEntity> {
    boolean shouldHandle(T entity, Animation animation, int tick);

    void handle(T entity, Animation animation, int tick);
}