package com.eeeab.animate.client.trail;

import com.eeeab.eeeabsmobs.client.particle.util.*;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import org.joml.Vector3f;

public class WeaponSwingTrailGenerator {
    /**
     * 生成武器挥动轨迹效果
     *
     * @param entity        实体
     * @param modelPart     模型部件
     * @param modelPartName 模型部件名称路径
     * @param color         轨迹颜色 (RGB)
     * @param alpha         透明度
     * @param length        轨迹长度
     * @param duration      轨迹持续时间
     */
    public static void generateSwingTrail(Entity entity, ModelPart modelPart, String[] modelPartName, Vector3f color, float alpha, int length, int duration) {
        if (entity.level().isClientSide) {
            // 创建轨迹组件
            ParticleComponent[] components = new ParticleComponent[]{
                    new RibbonComponent.PinLocationWithModelPart(entity, modelPart, modelPartName),
                    new RibbonComponent(ParticleInit.FLAT_RIBBON.get(), length,
                            0, 0, 0, 0.3,
                            color.x, color.y, color.z, alpha,
                            false, false,
                            new ParticleComponent[]{
                                    new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.ALPHA,
                                            AnimData.startAndEnd(alpha, 0)),
                                    new RibbonComponent.PropertyOverLength(RibbonComponent.PropertyOverLength.EnumRibbonProperty.SCALE,
                                            AnimData.startAndEnd(1F, 0.1F)),
                            }, false
                    ),
            };
            AdvancedParticleBase.spawnParticle(
                    entity.level(),
                    ParticleInit.ADV_ORB.get(),
                    entity.getX(), entity.getY(), entity.getZ(),
                    0, 0, 0,
                    false, 0, 0, 0,
                    0, 0.3F,
                    color.x, color.y, color.z, alpha,
                    1F, duration
                    , false, false, false,
                    components
            );
            // 生成 Ribbon 粒子
            //ParticleRibbon.spawnRibbon(
            //        world,
            //        ParticleInit.FLAT_RIBBON.get(),
            //        length,
            //        entity.getX(), entity.getY(), entity.getZ(),
            //        0, 0, 0,
            //        false, 0, 0, 0,
            //        0.3f, // 轨迹宽度
            //        color.x(), color.y(), color.z(),
            //        alpha,
            //        0,
            //        duration,
            //        false, // 是否发光
            //        components,
            //        false
            //);
        }
    }
}