package com.eeeab.eeeabsmobs.sever.handler;

import com.eeeab.eeeabsmobs.sever.capability.VertigoCapability;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.effects.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

//客户端事件处理器
@OnlyIn(Dist.CLIENT)
public class HandlerClientEvent {
    //改变玩家相机角度
    @SubscribeEvent
    public void onSetupCamera(ViewportEvent.ComputeCameraAngles event) {
        Player player = Minecraft.getInstance().player;
        float delta = Minecraft.getInstance().getFrameTime();
        if (player != null) {
            float ticksExistedDelta = player.tickCount + delta;
            if (EMConfigHandler.COMMON.OTHER.enableCameraShake.get() && !Minecraft.getInstance().isPaused()) {
                float shakeAmplitude = 0;
                for (EntityCameraShake cameraShake : player.level().getEntitiesOfClass(EntityCameraShake.class, player.getBoundingBox().inflate(20, 20, 20))) {
                    if (cameraShake.distanceTo(player) < cameraShake.getRadius()) {
                        shakeAmplitude += cameraShake.getShakeAmount(player, delta);
                    }
                }
                if (shakeAmplitude > 1.0f) shakeAmplitude = 1.0f;
                if (player.hasEffect(EffectInit.VERTIGO_EFFECT.get())) shakeAmplitude = 0.015f;
                event.setPitch((float) (event.getPitch() + shakeAmplitude * Math.cos(ticksExistedDelta * 3 + 2) * 25));
                event.setYaw((float) (event.getYaw() + shakeAmplitude * Math.cos(ticksExistedDelta * 5 + 1) * 25));
                event.setRoll((float) (event.getRoll() + shakeAmplitude * Math.cos(ticksExistedDelta * 4) * 25));
            }
        }
    }

    //渲染刻
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        Player player = Minecraft.getInstance().player;
        VertigoCapability.IVertigoCapability capability = HandlerCapability.getCapability(player, HandlerCapability.MOVING_CONTROLLER_CAPABILITY);
        if (capability != null && capability.isVertigo()) {
            player.yHeadRot = capability.getYawHead();
            player.yRotO = player.getYRot();
            player.xRotO = player.getXRot();
            player.yHeadRotO = player.yHeadRot;
            player.setYRot(capability.getYaw());
            player.setXRot(capability.getPitch());
        }
    }

    //在渲染实体前触发
    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        LivingEntity entity = event.getEntity();
        VertigoCapability.IVertigoCapability capability = HandlerCapability.getCapability(entity, HandlerCapability.MOVING_CONTROLLER_CAPABILITY);
        if (capability != null && capability.isVertigo()) {
            entity.yHeadRot = entity.yHeadRotO = capability.getYawHead();
            entity.yBodyRot = entity.yBodyRotO = capability.getCRenderYawOffset();
            entity.attackAnim = entity.oAttackAnim = capability.getCSwingProgress();
            entity.walkAnimation.speed(capability.getCLimbSwingAmount());
            entity.setYRot(entity.yRotO = capability.getYaw());
            entity.setXRot(entity.xRotO = capability.getPitch());
        }
    }

    //渲染玩家
    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent event) {
        Player player = event.getEntity();
        ItemStack itemStack = player.getUseItem();
        if (event.isCancelable() && itemStack.is(ItemInit.GUARDIAN_CORE.get())) {
            PlayerRenderer renderer = event.getRenderer();
            renderer.getModel().leftArmPose = HumanoidModel.ArmPose.CROSSBOW_HOLD;
            renderer.getModel().rightArmPose = HumanoidModel.ArmPose.CROSSBOW_HOLD;
        }
    }

}
