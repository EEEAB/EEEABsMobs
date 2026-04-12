package com.eeeab.animate.server.animation.keyframe;

import com.eeeab.animate.server.animation.AnimatedEntity;
import com.eeeab.animate.server.animation.Animation;
import net.minecraft.world.entity.Entity;

/**
 * 关键帧处理
 *
 * @author EEEAB
 */
@FunctionalInterface
public interface Keyframe<T extends Entity & AnimatedEntity> {
    void handle(T entity, Animation animation, int tick);
}