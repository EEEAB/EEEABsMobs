package com.eeeab.eeeabsmobs.sever.handler;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.ClientProxy;
import com.eeeab.eeeabsmobs.client.gui.BossBarConfig;
import com.eeeab.eeeabsmobs.client.gui.BossBarHandler;
import com.eeeab.eeeabsmobs.client.gui.BossBarRegistry;
import com.eeeab.eeeabsmobs.client.gui.PromptNotificationHandler;
import com.eeeab.eeeabsmobs.sever.capability.FrenzyCapability;
import com.eeeab.eeeabsmobs.sever.entity.effect.EntityCameraShake;
import com.eeeab.eeeabsmobs.sever.init.EffectInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

//客户端事件处理器
@OnlyIn(Dist.CLIENT)
public class ClientEventHandler {
    private static final ResourceLocation FRENZY_OUTLINE_LOCATION = new ResourceLocation(EEEABMobs.MOD_ID, "textures/misc/frenzy_outline.png");

    //改变玩家相机角度
    @SubscribeEvent
    public void onSetupCamera(ViewportEvent.ComputeCameraAngles event) {
        Player player = Minecraft.getInstance().player;
        float delta = Minecraft.getInstance().getFrameTime();
        if (player != null) {
            float ticksExistedDelta = player.tickCount + delta;
            if (ModConfigHandler.CLIENT.enableCameraShake.get() && !Minecraft.getInstance().isPaused()) {
                float shakeAmplitude = 0;
                for (EntityCameraShake cameraShake : player.level().getEntitiesOfClass(EntityCameraShake.class, player.getBoundingBox().inflate(20, 20, 20))) {
                    if (cameraShake.distanceTo(player) < cameraShake.getRadius()) {
                        shakeAmplitude += cameraShake.getShakeAmount(player, delta);
                    }
                }
                if (shakeAmplitude > 1.0f) shakeAmplitude = 1.0f;
                if (player.hasEffect(EffectInit.STUN_EFFECT.get())) shakeAmplitude = 0.01f;
                event.setPitch((float) (event.getPitch() + shakeAmplitude * Math.cos(ticksExistedDelta * 3 + 2) * 25));
                event.setYaw((float) (event.getYaw() + shakeAmplitude * Math.cos(ticksExistedDelta * 5 + 1) * 25));
                event.setRoll((float) (event.getRoll() + shakeAmplitude * Math.cos(ticksExistedDelta * 4) * 25));
            }
        }
    }

    @SubscribeEvent
    public void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay().overlay() == VanillaGuiOverlay.FROSTBITE.type().overlay()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                FrenzyCapability.IFrenzyCapability capability = CapabilityHandler.getCapability(player, CapabilityHandler.FRENZY_CAPABILITY);
                if (capability != null && capability.flag()) {
                    RenderSystem.setShaderTexture(0, FRENZY_OUTLINE_LOCATION);
                    Window res = event.getWindow();
                    event.getGuiGraphics().blit(FRENZY_OUTLINE_LOCATION, 0, 0, 0, 0, res.getGuiScaledWidth(), res.getGuiScaledHeight(), res.getGuiScaledWidth(), res.getGuiScaledHeight());
                }
            }
        }
        if (event.getOverlay().overlay() == VanillaGuiOverlay.PLAYER_LIST.type().overlay()) {
            PromptNotificationHandler.renderOverlay(event.getGuiGraphics(), event.getPartialTick());
        }
    }

    //渲染刻
    @SubscribeEvent
    public void onRenderTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            PromptNotificationHandler.clientTick();
        }
    }

    //在渲染实体前触发
    //@SubscribeEvent
    //public void onRenderLiving(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
    //    LivingEntity entity = event.getEntity();
    //    StunCapability.IStunCapability capability = CapabilityHandler.getCapability(entity, CapabilityHandler.STUN_CAPABILITY);
    //    if (capability != null && capability.flag()) {
    //        entity.yHeadRot = entity.yHeadRotO = capability.getYawHead();
    //        entity.yBodyRot = entity.yBodyRotO = capability.getCRenderYawOffset();
    //        entity.attackAnim = entity.oAttackAnim = capability.getCSwingProgress();
    //        entity.walkAnimation.speed(capability.getCLimbSwingAmount());
    //        entity.setYRot(entity.yRotO = capability.getYaw());
    //        entity.setXRot(entity.xRotO = capability.getPitch());
    //    }
    //}

    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null) return;
        ItemStack useItem = player.getUseItem();
        InteractionHand hand = event.getHand();
        if (useItem.is(ItemInit.GUARDIAN_CORE.get()) && player.isUsingItem()) {
            event.setCanceled(true);
            renderChargedGuardianCore(event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), player, hand);
        }
    }

    private static void renderChargedGuardianCore(PoseStack poseStack, MultiBufferSource buffer,
                                                  int combinedLight, Player player, InteractionHand hand) {
        Minecraft minecraft = Minecraft.getInstance();
        ItemInHandRenderer itemRenderer = minecraft.getEntityRenderDispatcher().getItemInHandRenderer();
        ItemStack stack = player.getItemInHand(hand);
        boolean isMainHand = hand == InteractionHand.MAIN_HAND;
        HumanoidArm arm = isMainHand ? player.getMainArm() : player.getMainArm().getOpposite();
        boolean rightHand = arm == HumanoidArm.RIGHT;
        int direction = rightHand ? 1 : -1;
        poseStack.pushPose();
        poseStack.translate(direction * 0.5F, -0.52F, -0.72F);
        poseStack.translate(direction * -0.5F, 0F, 0.1F);
        poseStack.mulPose(Axis.YP.rotationDegrees(45F));
        ItemDisplayContext transformType = isMainHand ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND;
        itemRenderer.renderItem(player, stack, transformType, !isMainHand, poseStack, buffer, combinedLight);
        poseStack.popPose();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderBossBar(CustomizeGuiOverlayEvent.BossEventProgress event) {
        if (!ModConfigHandler.COMMON.others.enableCustomBossBars.get()) return;
        ResourceLocation bossRegistryName = ClientProxy.bossBarRegistryNames.getOrDefault(event.getBossEvent().getId(), null);
        if (bossRegistryName == null) return;
        BossBarConfig config = BossBarRegistry.getBarForEntity(bossRegistryName);
        event.setCanceled(true);
        BossBarHandler.renderBossBar(event, config);
    }
}
