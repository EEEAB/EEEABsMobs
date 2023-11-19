package com.eeeab.eeeabsmobs.sever.handler;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.particle.ParticleDust;
import com.eeeab.eeeabsmobs.client.particle.ParticleGuardianSpark;
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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

//渲染处理器
@Mod.EventBusSubscriber(modid = EEEABMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class HandlerRegister {
    //@SubscribeEvent
    //public static void setEntityAttributeCreationEvent(EntityAttributeCreationEvent event) {
    //    event.put(EntityInit.IMMORTAL_SKELETON.get(), EntityImmortalSkeleton.setAttributes().build());
    //    event.put(EntityInit.IMMORTAL_KNIGHT.get(), EntityImmortalKnight.setAttributes().build());
    //    event.put(EntityInit.IMMORTAL_SHAMAN.get(), EntityImmortalShaman.setAttributes().build());
    //    event.put(EntityInit.IMMORTAL_GOLEM.get(), EntityImmortalGolem.setAttributes().build());
    //    event.put(EntityInit.NAMELESS_GUARDIAN.get(), EntityNamelessGuardian.setAttributes().build());
    //    event.put(EntityInit.TESTLLAGER.get(), EntityTestllager.setAttributes().build());
    //    event.put(EntityInit.TEST.get(), setCommonAttributes());
    //}

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.IMMORTAL_SKELETON.get(), RenderImmortalSkeleton::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_KNIGHT.get(), RenderImmortalSkeleton::new);
        //event.registerEntityRenderer(EntityInit.IMMORTAL_SKELETON.get(), GeoRenderImmortalSkeleton::new);
        //event.registerEntityRenderer(EntityInit.IMMORTAL_KNIGHT.get(), GeoRenderImmortalKnight::new);
        //event.registerEntityRenderer(EntityInit.IMMORTAL_SHAMAN.get(), GeoRenderImmortalShaman::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_SHAMAN.get(), RenderImmortalShaman::new);
        event.registerEntityRenderer(EntityInit.IMMORTAL_GOLEM.get(), RenderImmortalGolem::new);
        event.registerEntityRenderer(EntityInit.NAMELESS_GUARDIAN.get(), RenderNamelessGuardian::new);
        //event.registerEntityRenderer(EntityInit.IMMORTAL.get(), RenderTheImmortal::new);
        //event.registerEntityRenderer(EntityInit.TESTLLAGER.get(), GeoRenderTestllager::new);
        event.registerEntityRenderer(EntityInit.TESTLLAGER.get(), RenderTestllager::new);

//        event.registerEntityRenderer(EntityInit.GUARDIAN_BLADE.get(),EmptyRender::new);
        event.registerEntityRenderer(EntityInit.GUARDIAN_BLADE.get(),RenderGuardianBlade::new);//TODO

        event.registerEntityRenderer(EntityInit.SHAMAN_BOMB.get(), RenderShamanBomb::new);
        event.registerEntityRenderer(EntityInit.CAMERA_SHAKE.get(), EmptyRender::new);
        event.registerEntityRenderer(EntityInit.MOVING_CONTROLLER.get(), EmptyRender::new);
        //event.registerEntityRenderer(EntityInit.FALLING_BLOCK.get(), EEFallingBlockRender::new);
        event.registerEntityRenderer(EntityInit.FALLING_BLOCK.get(), RenderFallingBlock::new);
        event.registerEntityRenderer(EntityInit.GUARDIAN_LASER.get(), RenderGuardianLaser::new);
        event.registerEntityRenderer(EntityInit.SCORCH.get(), RenderEntityScorch::new);
        event.registerEntityRenderer(EntityInit.EYE_OF_STRUCTURE.get(), (context) -> new ThrownItemRenderer<>(context, 1.5F, true));
        event.registerEntityRenderer(EntityInit.TEST.get(), RenderTest::new);


        event.registerBlockEntityRenderer(BlockEntityInit.ENTITY_TOMBSTONE.get(), RenderBlockTombstone::new);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @OnlyIn(Dist.CLIENT)
    public static void onRegisterParticleProvidersEvent(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleInit.SPELL_CASTING.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.ADV_ORB.get(), AdvancedParticleBase.Factory::new);
        event.registerSpriteSet(ParticleInit.DUST.get(), ParticleDust.DustFactory::new);
        event.registerSpriteSet(ParticleInit.ORB.get(), ParticleOrb.OrbFactory::new);
        event.registerSpriteSet(ParticleInit.GUARDIAN_SPARK.get(), ParticleGuardianSpark.GuardianSparkFactory::new);
        event.registerSpriteSet(ParticleInit.RING.get(), ParticleRing.RingFactory::new);
    }

    public static AttributeSupplier setCommonAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).
                add(Attributes.MOVEMENT_SPEED, 0.3D).
                add(Attributes.ATTACK_DAMAGE, 1D).build();
    }
}

