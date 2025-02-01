package com.eeeab.eeeabsmobs.sever.capability;

import net.minecraft.world.entity.LivingEntity;

/**
 * 通用能力接口
 *
 * @author EEEAB
 */
public interface GeneralCapability {
    boolean flag();

    void tick(LivingEntity entity);

    void onStart(LivingEntity entity);

    void onEnd(LivingEntity entity);
}
