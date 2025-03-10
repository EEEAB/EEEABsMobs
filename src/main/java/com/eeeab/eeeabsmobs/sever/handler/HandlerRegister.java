package com.eeeab.eeeabsmobs.sever.handler;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.effects.ModelImmortalShuriken;
import com.eeeab.eeeabsmobs.client.model.item.ModelDemolisher;
import com.eeeab.eeeabsmobs.client.model.item.ModelGuardianBattleaxe;
import com.eeeab.eeeabsmobs.client.model.util.EMItemModels;
import com.eeeab.eeeabsmobs.client.model.armor.ModelGhostWarriorArmor;
import com.eeeab.eeeabsmobs.client.particle.*;
import com.eeeab.eeeabsmobs.client.particle.ParticleHugeEMExplosionSeed;
import com.eeeab.eeeabsmobs.sever.integration.curios.model.ModelSoulSummoningNecklace;
import com.eeeab.eeeabsmobs.client.model.effects.ModelBloodBall;
import com.eeeab.eeeabsmobs.client.model.effects.ModelGrenade;
import com.eeeab.eeeabsmobs.client.model.effects.ModelGuardianBlade;
import com.eeeab.eeeabsmobs.client.model.entity.*;
import com.eeeab.eeeabsmobs.client.model.item.ModelTheNetherworldKatana;
import com.eeeab.eeeabsmobs.client.model.util.EMModelLayer;
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
import net.minecraft.client.particle.SuspendedTownParticle;
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
        event.registerLayerDefinition(EMModelLayer.IMMORTAL_EXECUTIONER, ModelImmortalExecutioner::createBodyLayer);
        event.registerLayerDefinition(EMModelLayer.IMMORTAL, ModelImmortal::createBodyLayer);
        event.registerLayerDefinition(EMModelLayer.IMMORTAL_FIREBALL, ModelImmortalShuriken::createBodyLayer);

        event.registerLayerDefinition(EMModelLayer.GHOST_WARRIOR_ARMOR, () -> ModelGhostWarriorArmor.createBodyLayer(new CubeDeformation(0.5F)));
        event.registerLayerDefinition(EMModelLayer.GHOST_WARRIOR_ARMOR_LEGS, () -> ModelGhostWarriorArmor.createBodyLayer(new CubeDeformation(0.2F)));
        event.registerLayerDefinition(EMModelLayer.SOUL_SUMMONING_NECKLACE, ModelSoulSummoningNecklace::createBodyLayer);
        event.registerLayerDefinition(EMModelLayer.THE_NETHERWORLD_KATANA, ModelTheNetherworldKatana::createBodyLayer);
        event.registerLayerDefinition(EMModelLayer.THE_GUARDIAN_BATTLEAXE, ModelGuardianBattleaxe::createBodyLayer);
        event.registerLayerDefinition(EMModelLayer.DEMOLISHER, ModelDemolisher::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.IMMORTAL_SKELETON.get(), RenderAbsImmortalSkeleton::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_ARCHER.get(), RenderAbsImmortalSkeleton::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_KNIGHT.get(), RenderAbsImmortalSkeleton::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_MAGE.get(), RenderAbsImmortalSkeleton::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_WARRIOR.get(), RenderAbsImmortalSkeleton::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_SHAMAN.get(), RenderImmortalShaman::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_GOLEM.get(), RenderImmortalGolem::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_EXECUTIONER.get(), RenderImmortalExecutioner::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_BOSS.get(), RenderImmortal::new);
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
        event.registerEntityRenderer(EntityInit.OVERLOAD_EXPLODE.get(), EmptyRender::new);
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
        event.registerEntityRenderer(EntityInit.MAGIC_CIRCLE.get(), RenderImmortalMagicCircle::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_SHURIKEN.get(), RenderImmortalShuriken::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_LASER.get(), RenderImmortalLaser::new);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void onRegisterParticleProvidersEvent(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleInit.SPELL_CASTING.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.ADV_ORB.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.CRIMSON.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.CRIMSON_EYE.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.STRIP_SOUL_FIRE.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.GLOW.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.ADV_RING.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.BIG_RING.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.CRIT_RING.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.THUMP_RING.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.PUNCTURED_AIR_RING.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.FLAT_RIBBON.get(), ParticleRibbon.Factory::new);
        event.registerSpriteSet(ParticleInit.DUST.get(), ParticleDust.Factory::new);
        event.registerSpriteSet(ParticleInit.ORB.get(), ParticleOrb.Factory::new);
        event.registerSpriteSet(ParticleInit.GUARDIAN_SPARK.get(), ParticleGuardianSpark.Factory::new);
        event.registerSpriteSet(ParticleInit.POISON.get(), ParticlePoison.Factory::new);
        event.registerSpriteSet(ParticleInit.WARLOCK_HEAL.get(), SuspendedTownParticle.HappyVillagerProvider::new);
        event.registerSpriteSet(ParticleInit.IMMORTAL_EXPLOSION.get(), ParticleImmortalExplosion.Factory::new);
        event.registerSpriteSet(ParticleInit.OVERLOAD_EXPLOSION.get(), ParticleOverloadExplosion.Factory::new);
        event.registerSpriteSet(ParticleInit.RING.get(), ParticleRing.Factory::new);
        event.registerSpecial(ParticleInit.OVERLOAD_EXPLOSION_EMITTER.get(), new ParticleHugeEMExplosionSeed.Factory());
    }

    @SubscribeEvent
    public static void onRegisterModels(ModelEvent.RegisterAdditional event) {
        for (String item : EMItemModels.HAND_MODEL_ITEMS) {
            event.register(new ModelResourceLocation(new ResourceLocation(EEEABMobs.MOD_ID, item + "_in_hand"), "inventory"));
        }
    }
}

