package com.eeeab.eeeabsmobs.sever.handler;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.model.EMItemModels;
import com.eeeab.eeeabsmobs.client.particle.ParticleDust;
import com.eeeab.eeeabsmobs.client.particle.ParticleGuardianSpark;
import com.eeeab.eeeabsmobs.client.particle.ParticlePoison;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleOrb;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleRing;
import com.eeeab.eeeabsmobs.client.particle.util.AdvancedParticleBase;
import com.eeeab.eeeabsmobs.client.render.*;
import com.eeeab.eeeabsmobs.client.render.block.RenderBlockTombstone;
import com.eeeab.eeeabsmobs.client.render.effects.*;
import com.eeeab.eeeabsmobs.client.render.entity.*;
import com.eeeab.eeeabsmobs.sever.init.BlockEntityInit;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ParticleInit;
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

    //@SubscribeEvent
    //public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event){
    //
    //}

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.IMMORTAL_SKELETON.get(), RenderAbsImmortalSkeleton::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_KNIGHT.get(), RenderAbsImmortalSkeleton::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_SHAMAN.get(), RenderImmortalShaman::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_GOLEM.get(), RenderImmortalGolem::new);
        event.registerEntityRenderer(EntityInit.CORPSE.get(), RenderCorpse::new);
        event.registerEntityRenderer(EntityInit.CORPSE_VILLAGER.get(), RenderCorpseVillager::new);
        event.registerEntityRenderer(EntityInit.CORPSE_WARLOCK.get(), RenderCorpseWarlock::new);
        event.registerEntityRenderer(EntityInit.NAMELESS_GUARDIAN.get(), RenderNamelessGuardian::new);
        event.registerEntityRenderer(EntityInit.TESTER.get(), RenderTester::new);

        event.registerEntityRenderer(EntityInit.GUARDIAN_BLADE.get(), RenderGuardianBlade::new);
        event.registerEntityRenderer(EntityInit.SHAMAN_BOMB.get(), RenderShamanBomb::new);
        event.registerEntityRenderer(EntityInit.CAMERA_SHAKE.get(), EmptyRender::new);
        event.registerEntityRenderer(EntityInit.MOVING_CONTROLLER.get(), EmptyRender::new);
        event.registerEntityRenderer(EntityInit.FALLING_BLOCK.get(), RenderFallingBlock::new);
        event.registerEntityRenderer(EntityInit.GUARDIAN_LASER.get(), RenderGuardianLaser::new);
        event.registerEntityRenderer(EntityInit.SCORCH.get(), RenderScorch::new);
        event.registerEntityRenderer(EntityInit.EYE_OF_STRUCTURE.get(), (context) -> new ThrownItemRenderer<>(context, 1.5F, true));
        event.registerEntityRenderer(EntityInit.POISON_ARROW.get(), RenderPoisonArrow::new);
        event.registerEntityRenderer(EntityInit.BLOOD_BALL.get(), RendererBloodBall::new);
        event.registerEntityRenderer(EntityInit.CRIMSON_CRACK.get(), RenderCrimsonCrack::new);

        event.registerBlockEntityRenderer(BlockEntityInit.ENTITY_TOMBSTONE.get(), RenderBlockTombstone::new);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void onRegisterParticleProvidersEvent(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleInit.SPELL_CASTING.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.ADV_ORB.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.CRIMSON.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.CRIMSON_EYE.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.DUST.get(), ParticleDust.DustFactory::new);
        event.registerSpriteSet(ParticleInit.ORB.get(), ParticleOrb.OrbFactory::new);
        event.registerSpriteSet(ParticleInit.GUARDIAN_SPARK.get(), ParticleGuardianSpark.GuardianSparkFactory::new);
        event.registerSpriteSet(ParticleInit.POISON.get(), ParticlePoison.PoisonFactory::new);
        event.registerSpriteSet(ParticleInit.RING.get(), ParticleRing.RingFactory::new);
    }

    @SubscribeEvent
    public static void onRegisterModels(ModelEvent.RegisterAdditional event) {
        for (String item : EMItemModels.HAND_MODEL_ITEMS) {
            event.register(new ModelResourceLocation(new ResourceLocation(EEEABMobs.MOD_ID, item + "_in_hand"), "inventory"));
        }
    }
}

