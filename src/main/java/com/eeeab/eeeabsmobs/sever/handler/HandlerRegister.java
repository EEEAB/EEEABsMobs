package com.eeeab.eeeabsmobs.sever.handler;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.EMItemModels;
import com.eeeab.eeeabsmobs.client.model.armor.ModelGhostWarriorArmor;
import com.eeeab.eeeabsmobs.client.model.effects.ModelBloodBall;
import com.eeeab.eeeabsmobs.client.model.effects.ModelGrenade;
import com.eeeab.eeeabsmobs.client.model.effects.ModelGuardianBlade;
import com.eeeab.eeeabsmobs.client.model.entity.*;
import com.eeeab.eeeabsmobs.client.model.item.ModelTheNetherworldKatana;
import com.eeeab.eeeabsmobs.client.model.layer.EMModelLayer;
import com.eeeab.eeeabsmobs.client.particle.ParticleDust;
import com.eeeab.eeeabsmobs.client.particle.ParticleGuardianSpark;
import com.eeeab.eeeabsmobs.client.particle.ParticlePoison;
import com.eeeab.eeeabsmobs.client.particle.ParticleVerticalLine;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleOrb;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleRing;
import com.eeeab.eeeabsmobs.client.particle.util.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.util.ParticleRibbon;
import com.eeeab.eeeabsmobs.client.render.EmptyRender;
import com.eeeab.eeeabsmobs.client.render.effects.*;
import com.eeeab.eeeabsmobs.client.render.entity.*;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

//渲染处理器
@Mod.EventBusSubscriber(modid = EEEABMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class HandlerRegister {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(EMModelLayer.UNKNOWN, ModelUnKnown::createBodyLayer);
        event.registerLayerDefinition(EMModelLayer.TESTER, ModelTester::createBodyLayer);
        event.registerLayerDefinition(EMModelLayer.CORPSE, ModelCorpse::createBodyLayer);
        event.registerLayerDefinition(EMModelLayer.CORPSE_VILLAGER, ModelCorpse::createVillagerBodyLayer);
        event.registerLayerDefinition(EMModelLayer.CORPSE_SLAVERY, ModelCorpseWarlock::createBodyLayer);
        event.registerLayerDefinition(EMModelLayer.BLOOD_BALL, ModelBloodBall::createBodyLayer);
        event.registerLayerDefinition(EMModelLayer.GULING_SENTINEL, ModelGulingSentinel::createBodyLayer);
        event.registerLayerDefinition(EMModelLayer.GULING_SENTINEL_HEAVY, ModelGulingSentinelHeavy::createBodyLayer);
        event.registerLayerDefinition(EMModelLayer.GRENADE, ModelGrenade::createBodyLayer);
        event.registerLayerDefinition(EMModelLayer.NAMELESS_GUARDIAN, ModelNamelessGuardian::createBodyLayer);
        event.registerLayerDefinition(EMModelLayer.GUARDIAN_BLADE, ModelGuardianBlade::createBodyLayer);
        event.registerLayerDefinition(EMModelLayer.IMMORTAL_GOLEM, ModelImmortalGolem::createBodyLayer);
        event.registerLayerDefinition(EMModelLayer.IMMORTAL_SKELETON, ModelAbsImmortalSkeleton::createBodyLayer);
        event.registerLayerDefinition(EMModelLayer.IMMORTAL_SHAMAN, ModelImmortalShaman::createBodyLayer);
        event.registerLayerDefinition(EMModelLayer.IMMORTAL, ModelTheImmortal::createBodyLayer);

        event.registerLayerDefinition(EMModelLayer.GHOST_WARRIOR_ARMOR, () -> ModelGhostWarriorArmor.createBodyLayer(new CubeDeformation(0.5F)));
        event.registerLayerDefinition(EMModelLayer.GHOST_WARRIOR_ARMOR_LEGS, () -> ModelGhostWarriorArmor.createBodyLayer(new CubeDeformation(0.2F)));
        event.registerLayerDefinition(EMModelLayer.THE_NETHERWORLD_KATANA, ModelTheNetherworldKatana::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.IMMORTAL_SKELETON.get(), RenderAbsImmortalSkeleton::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_KNIGHT.get(), RenderAbsImmortalSkeleton::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_SHAMAN.get(), RenderImmortalShaman::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_GOLEM.get(), RenderImmortalGolem::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_EXECUTIONER.get(), (context) -> new RenderUnknown<>(context, 1.4F, 0.6F));
        event.registerEntityRenderer(EntityInit.IMMORTAL_BOSS.get(), RenderTheImmortal::new);
        event.registerEntityRenderer(EntityInit.CORPSE.get(), RenderCorpse::new);
        event.registerEntityRenderer(EntityInit.CORPSE_VILLAGER.get(), RenderCorpseVillager::new);
        event.registerEntityRenderer(EntityInit.CORPSE_WARLOCK.get(), RenderCorpseWarlock::new);
        event.registerEntityRenderer(EntityInit.CORPSE_TO_PLAYER.get(), RenderCorpseToPlayer::new);
        event.registerEntityRenderer(EntityInit.NAMELESS_GUARDIAN.get(), RenderNamelessGuardian::new);
        event.registerEntityRenderer(EntityInit.GULING_SENTINEL.get(), RenderGulingSentinel::new);
        event.registerEntityRenderer(EntityInit.GULING_SENTINEL_HEAVY.get(), RenderGulingSentinelHeavy::new);
        event.registerEntityRenderer(EntityInit.TESTER.get(), RenderTester::new);

        event.registerEntityRenderer(EntityInit.GUARDIAN_BLADE.get(), RenderGuardianBlade::new);
        event.registerEntityRenderer(EntityInit.SHAMAN_BOMB.get(), RenderShamanBomb::new);
        event.registerEntityRenderer(EntityInit.CAMERA_SHAKE.get(), EmptyRender::new);
        event.registerEntityRenderer(EntityInit.EXPLODE.get(), EmptyRender::new);
        event.registerEntityRenderer(EntityInit.MOVING_CONTROLLER.get(), EmptyRender::new);
        event.registerEntityRenderer(EntityInit.FALLING_BLOCK.get(), RenderFallingBlock::new);
        event.registerEntityRenderer(EntityInit.GUARDIAN_LASER.get(), RenderGuardianLaser::new);
        event.registerEntityRenderer(EntityInit.SCORCH.get(), RenderScorch::new);
        event.registerEntityRenderer(EntityInit.EYE_OF_STRUCTURE.get(), (context) -> new ThrownItemRenderer<>(context, 1.5F, true));
        event.registerEntityRenderer(EntityInit.POISON_ARROW.get(), RenderPoisonArrow::new);
        event.registerEntityRenderer(EntityInit.BLOOD_BALL.get(), RendererBloodBall::new);
        event.registerEntityRenderer(EntityInit.CRIMSON_CRACK.get(), RenderCrimsonCrack::new);
        event.registerEntityRenderer(EntityInit.CRIMSON_RAY.get(), RenderCrimsonRay::new);
        event.registerEntityRenderer(EntityInit.CRIMSON_RAY_PRE.get(), RenderCrimsonRay.RenderPreAttack::new);
        event.registerEntityRenderer(EntityInit.GRENADE.get(), RenderGrenade::new);
        event.registerEntityRenderer(EntityInit.ELECTROMAGNETIC.get(), EmptyRender::new);
        event.registerEntityRenderer(EntityInit.ALIEN_PORTAL.get(), RenderAlienPortal::new);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void onRegisterParticleProvidersEvent(final RegisterParticleProvidersEvent event) {
        event.register(ParticleInit.SPELL_CASTING.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleInit.ADV_ORB.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleInit.CRIMSON.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleInit.CRIMSON_EYE.get(), AdvancedParticleBase.Factory::new);
        event.register(ParticleInit.FLAT_RIBBON.get(), ParticleRibbon.Factory::new);
        event.register(ParticleInit.DUST.get(), ParticleDust.DustFactory::new);
        event.register(ParticleInit.ORB.get(), ParticleOrb.OrbFactory::new);
        event.register(ParticleInit.GUARDIAN_SPARK.get(), ParticleGuardianSpark.GuardianSparkFactory::new);
        event.register(ParticleInit.POISON.get(), ParticlePoison.PoisonFactory::new);
        event.register(ParticleInit.RING.get(), ParticleRing.RingFactory::new);
        event.register(ParticleInit.VERTICAL_LINE.get(), ParticleVerticalLine.VerticalLineFactory::new);
    }

    @SubscribeEvent
    public static void onRegisterModels(ModelEvent.RegisterAdditional event) {
        for (String item : EMItemModels.HAND_MODEL_ITEMS) {
            event.register(new ModelResourceLocation(new ResourceLocation(EEEABMobs.MOD_ID, item + "_in_hand"), "inventory"));
        }
    }
}

