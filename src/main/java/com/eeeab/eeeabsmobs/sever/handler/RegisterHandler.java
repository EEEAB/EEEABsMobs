package com.eeeab.eeeabsmobs.sever.handler;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.block.SlidingDoorModel;
import com.eeeab.eeeabsmobs.client.model.entity.effect.*;
import com.eeeab.eeeabsmobs.client.model.item.ModelBusterGauntlet;
import com.eeeab.eeeabsmobs.client.model.item.ModelDemolisher;
import com.eeeab.eeeabsmobs.client.model.item.ModelGuardianBattleaxe;
import com.eeeab.eeeabsmobs.client.model.util.ModItemModels;
import com.eeeab.eeeabsmobs.client.model.armor.ModelGhostWarriorArmor;
import com.eeeab.eeeabsmobs.client.particle.*;
import com.eeeab.eeeabsmobs.client.particle.ParticleBlazeExplosionSeed;
import com.eeeab.eeeabsmobs.client.particle.lib.AdvancedBlockParticle;
import com.eeeab.eeeabsmobs.client.render.block.RenderSlidingDoor;
import com.eeeab.eeeabsmobs.client.render.entity.effect.*;
import com.eeeab.eeeabsmobs.sever.init.BlockEntityInit;
import com.eeeab.eeeabsmobs.sever.integration.curios.model.ModelSoulSummoningNecklace;
import com.eeeab.eeeabsmobs.client.model.entity.*;
import com.eeeab.eeeabsmobs.client.model.item.ModelTheNetherworldKatana;
import com.eeeab.eeeabsmobs.client.model.util.ModModelLayer;
import com.eeeab.eeeabsmobs.client.particle.ParticleOrb;
import com.eeeab.eeeabsmobs.client.particle.ParticleRing;
import com.eeeab.eeeabsmobs.client.particle.lib.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.particle.lib.ParticleRibbon;
import com.eeeab.eeeabsmobs.client.render.EmptyRender;
import com.eeeab.eeeabsmobs.client.render.entity.*;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.particle.HugeExplosionParticle;
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
public class RegisterHandler {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayer.UNKNOWN, ModelUnKnown::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.TESTER, ModelTester::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.CORPSE, ModelCorpse::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.CORPSE_VILLAGER, ModelCorpse::createVillagerBodyLayer);
        event.registerLayerDefinition(ModModelLayer.CORPSE_SLAVERY, ModelCorpseWarlock::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.BLOOD_BALL, ModelBloodBall::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.RELIC_OBSERVER, ModelRelicObserver::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.RELIC_RIPPER, ModelRelicRipper::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.RELIC_EARTHSHAKER, ModelRelicEarthshaker::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.PULSED_GRENADE, ModelPulsedGrenade::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.RELIC_ANNIHILATOR, ModelRelicAnnihilator::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.REALM_WARDEN, ModelRealmWarden::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.NAMELESS_GUARDIAN, ModelNamelessGuardian::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.GUARDIAN_BLADE, ModelGuardianBlade::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.MAGIC_GOLEM, ModelMagicGolem::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.IMMORTAL_SKELETON, ModelAbsImmortalSkeleton::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.IMMORTAL_SHAMAN, ModelImmortalShaman::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.IMMORTAL_EXECUTIONER, ModelImmortalExecutioner::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.IMMORTAL, ModelImmortal::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.IMMORTAL_SHURIKEN, ModelImmortalShuriken::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.ANNIHILATOR_MISSILE, ModelAnnihilatorMissile::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.DOOMBOLT_AXE, ModelDoomboltAxe::createBodyLayer);

        event.registerLayerDefinition(ModModelLayer.GHOST_WARRIOR_ARMOR, () -> ModelGhostWarriorArmor.createBodyLayer(new CubeDeformation(0.5F)));
        event.registerLayerDefinition(ModModelLayer.GHOST_WARRIOR_ARMOR_LEGS, () -> ModelGhostWarriorArmor.createBodyLayer(new CubeDeformation(0.2F)));
        event.registerLayerDefinition(ModModelLayer.SOUL_SUMMONING_NECKLACE, ModelSoulSummoningNecklace::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.THE_NETHERWORLD_KATANA, ModelTheNetherworldKatana::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.THE_GUARDIAN_BATTLEAXE, ModelGuardianBattleaxe::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.DEMOLISHER, ModelDemolisher::createBodyLayer);
        event.registerLayerDefinition(ModModelLayer.BUSTER_GAUNTLET, ModelBusterGauntlet::createBodyLayer);

        event.registerLayerDefinition(ModModelLayer.SLIDING_DOOR, SlidingDoorModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.TESTER.get(), RenderTester::new);
        event.registerEntityRenderer(EntityInit.CORPSE.get(), RenderCorpse::new);
        event.registerEntityRenderer(EntityInit.CORPSE_VILLAGER.get(), RenderCorpseVillager::new);
        event.registerEntityRenderer(EntityInit.CORPSE_WARLOCK.get(), RenderCorpseWarlock::new);
        event.registerEntityRenderer(EntityInit.CORPSE_TO_PLAYER.get(), RenderCorpse::new);
        event.registerEntityRenderer(EntityInit.RELIC_OBSERVER.get(), RenderRelicObserver::new);
        event.registerEntityRenderer(EntityInit.RELIC_RIPPER.get(), RenderRelicRipper::new);
        event.registerEntityRenderer(EntityInit.RELIC_EARTHSHAKER.get(), RenderRelicEarthshaker::new);
        event.registerEntityRenderer(EntityInit.RELIC_ANNIHILATOR.get(), RenderRelicAnnihilator::new);
        event.registerEntityRenderer(EntityInit.REALM_WARDEN.get(), RenderRealmWarden::new);
        event.registerEntityRenderer(EntityInit.NAMELESS_GUARDIAN.get(), RenderNamelessGuardian::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_SKELETON.get(), RenderAbsImmortalSkeleton::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_ARCHER.get(), RenderAbsImmortalSkeleton::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_KNIGHT.get(), RenderAbsImmortalSkeleton::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_MAGE.get(), RenderAbsImmortalSkeleton::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_WARRIOR.get(), RenderAbsImmortalSkeleton::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_SHAMAN.get(), RenderImmortalShaman::new);
        event.registerEntityRenderer(EntityInit.MAGIC_GOLEM.get(), RenderMagicGolem::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_EXECUTIONER.get(), RenderImmortalExecutioner::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_BOSS.get(), RenderImmortal::new);

        event.registerEntityRenderer(EntityInit.TELEGRAPH.get(), RendererTelegraph::new);
        event.registerEntityRenderer(EntityInit.GUARDIAN_BLADE.get(), RenderGuardianBlade::new);
        event.registerEntityRenderer(EntityInit.SHAMAN_BOMB.get(), RenderShamanBomb::new);
        event.registerEntityRenderer(EntityInit.CAMERA_SHAKE.get(), EmptyRender::new);
        event.registerEntityRenderer(EntityInit.EXPLODE.get(), EmptyRender::new);
        event.registerEntityRenderer(EntityInit.OVERLOAD_EXPLODE.get(), EmptyRender::new);
        event.registerEntityRenderer(EntityInit.FALLING_BLOCK.get(), RenderFallingBlock::new);
        event.registerEntityRenderer(EntityInit.DOOMBOLT_AXE.get(), RenderDoomboltAxe::new);
        event.registerEntityRenderer(EntityInit.INFRARED_RAY.get(), RenderInfraredRay::new);
        event.registerEntityRenderer(EntityInit.GUARDIAN_LASER.get(), RenderGuardianLaser::new);
        event.registerEntityRenderer(EntityInit.EYE_OF_STRUCTURE.get(), (context) -> new ThrownItemRenderer<>(context, 1.5F, true));
        event.registerEntityRenderer(EntityInit.POISON_ARROW.get(), RenderPoisonArrow::new);
        event.registerEntityRenderer(EntityInit.BLOOD_BALL.get(), RendererBloodBall::new);
        event.registerEntityRenderer(EntityInit.CRIMSON_CRACK.get(), RenderCrimsonCrack::new);
        event.registerEntityRenderer(EntityInit.CRIMSON_RAY.get(), RenderCrimsonRay::new);
        event.registerEntityRenderer(EntityInit.CRIMSON_RAY_PRE.get(), RenderCrimsonRay.RenderPreAttack::new);
        event.registerEntityRenderer(EntityInit.PULSED_GRENADE.get(), RenderPulsedGrenade::new);
        event.registerEntityRenderer(EntityInit.ELECTROMAGNETIC.get(), EmptyRender::new);
        event.registerEntityRenderer(EntityInit.ANNIHILATOR_MISSILE.get(), RenderAnnihilatorMissile::new);
        event.registerEntityRenderer(EntityInit.SURGE.get(), EmptyRender::new);
        //event.registerEntityRenderer(EntityInit.ALIEN_PORTAL.get(), RenderAlienPortal::new);
        event.registerEntityRenderer(EntityInit.MAGIC_CIRCLE.get(), RenderImmortalMagicCircle::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_SHURIKEN.get(), RenderImmortalShuriken::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_LASER.get(), RenderImmortalLaser::new);

        event.registerBlockEntityRenderer(BlockEntityInit.SLIDING_DOOR_BE.get(), RenderSlidingDoor::new);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void onRegisterParticleProvidersEvent(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleInit.SPELL_CASTING.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.ADV_ORB.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.GLOW.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.ADV_RING.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.ADV_RING2.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.ADV_RING3.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.CRIMSON.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.CRIMSON_EYE.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.STRIP_SOUL_FIRE.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.CROSS_FLASH.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.VORTEX.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.CRIT_RING.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.THUMP_RING.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.PUNCTURED_AIR_RING.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.SURGE.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.SPARK.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.FLAT_RIBBON.get(), ParticleRibbon.Factory::new);
        event.registerSpriteSet(ParticleInit.BLOCK.get(), AdvancedBlockParticle.Factory::new);
        event.registerSpriteSet(ParticleInit.DUST.get(), ParticleDust.Factory::new);
        event.registerSpriteSet(ParticleInit.ORB.get(), ParticleOrb.Factory::new);
        event.registerSpriteSet(ParticleInit.GUARDIAN_SPARK.get(), ParticleGuardianSpark.Factory::new);
        event.registerSpriteSet(ParticleInit.HIT_CUT.get(), ParticleHitCut.Factory::new);
        event.registerSpriteSet(ParticleInit.POISON.get(), ParticlePoison.Factory::new);
        event.registerSpriteSet(ParticleInit.WARLOCK_HEAL.get(), SuspendedTownParticle.HappyVillagerProvider::new);
        event.registerSpriteSet(ParticleInit.IMMORTAL_EXPLOSION.get(), ParticleImmortalExplosion.Factory::new);
        event.registerSpriteSet(ParticleInit.BLAZE_EXPLOSION.get(), HugeExplosionParticle.Provider::new);
        event.registerSpriteSet(ParticleInit.VOLT_EXPLOSION.get(), HugeExplosionParticle.Provider::new);
        event.registerSpriteSet(ParticleInit.RING.get(), ParticleRing.Factory::new);
        event.registerSpriteSet(ParticleInit.BIG_RING.get(), ParticleRing.Factory::new);
        event.registerSpriteSet(ParticleInit.RADIAL_OPACITY_RING.get(), ParticleRing.Factory::new);
        event.registerSpecial(ParticleInit.BLAZE_EXPLOSION_EMITTER.get(), new ParticleBlazeExplosionSeed.Factory());
        event.registerSpecial(ParticleInit.VOLT_EXPLOSION_EMITTER.get(), new ParticleVoltExplosionSeed.Factory());
    }

    @SubscribeEvent
    public static void onRegisterModels(ModelEvent.RegisterAdditional event) {
        for (String item : ModItemModels.HAND_MODEL_ITEMS) {
            event.register(new ModelResourceLocation(new ResourceLocation(EEEABMobs.MOD_ID, item + "_in_hand"), "inventory"));
        }
    }
}

