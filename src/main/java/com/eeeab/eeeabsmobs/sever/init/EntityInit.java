package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpse;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpseWarlock;
import com.eeeab.eeeabsmobs.sever.entity.corpse.EntityCorpseVillager;
import com.eeeab.eeeabsmobs.sever.entity.effects.*;
import com.eeeab.eeeabsmobs.sever.entity.immortal.*;
import com.eeeab.eeeabsmobs.sever.entity.namelessguardian.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.projectile.EntityBloodBall;
import com.eeeab.eeeabsmobs.sever.entity.projectile.EntityPoisonArrow;
import com.eeeab.eeeabsmobs.sever.entity.projectile.EntityShamanBomb;
import com.eeeab.eeeabsmobs.sever.entity.test.EntityTester;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = EEEABMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, EEEABMobs.MOD_ID);

    public static final RegistryObject<EntityType<EntityImmortalSkeleton>> IMMORTAL_SKELETON =
            ENTITIES.register("immortal_skeleton",
                    () -> EntityType.Builder.<EntityImmortalSkeleton>of(EntityImmortalSkeleton::new, MobCategory.MONSTER)
                            .sized(0.6f, 2.0f).clientTrackingRange(8)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "immortal_skeleton").toString()));


    public static final RegistryObject<EntityType<EntityImmortalKnight>> IMMORTAL_KNIGHT =
            ENTITIES.register("immortal_knight",
                    () -> EntityType.Builder.<EntityImmortalKnight>of(EntityImmortalKnight::new, MobCategory.MONSTER)
                            .sized(0.7f, 2.4f).clientTrackingRange(8)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "immortal_knight").toString()));

    public static final RegistryObject<EntityType<EntityImmortalShaman>> IMMORTAL_SHAMAN =
            ENTITIES.register("immortal_shaman",
                    () -> EntityType.Builder.<EntityImmortalShaman>of(EntityImmortalShaman::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.8f).clientTrackingRange(8)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "immortal_shaman").toString()));


    public static final RegistryObject<EntityType<EntityImmortalGolem>> IMMORTAL_GOLEM =
            ENTITIES.register("immortal_golem",
                    () -> EntityType.Builder.<EntityImmortalGolem>of(EntityImmortalGolem::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.2f).clientTrackingRange(8)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "immortal_golem").toString()));

    public static final RegistryObject<EntityType<EntityCorpse>> CORPSE =
            ENTITIES.register("corpse",
                    () -> EntityType.Builder.<EntityCorpse>of(EntityCorpse::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.95f).clientTrackingRange(8)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "corpse").toString()));

    public static final RegistryObject<EntityType<EntityCorpseVillager>> CORPSE_VILLAGER =
            ENTITIES.register("corpse_villager",
                    () -> EntityType.Builder.<EntityCorpseVillager>of(EntityCorpseVillager::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.95f).clientTrackingRange(8)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "corpse_villager").toString()));

    public static final RegistryObject<EntityType<EntityCorpseWarlock>> CORPSE_WARLOCK =
            ENTITIES.register("corpse_warlock",
                    () -> EntityType.Builder.<EntityCorpseWarlock>of(EntityCorpseWarlock::new, MobCategory.MONSTER)
                            .sized(0.7f, 2.2f).fireImmune().clientTrackingRange(8)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "corpse_warlock").toString()));


    public static final RegistryObject<EntityType<EntityNamelessGuardian>> NAMELESS_GUARDIAN =
            ENTITIES.register("nameless_guardian",
                    () -> EntityType.Builder.<EntityNamelessGuardian>of(EntityNamelessGuardian::new, MobCategory.MONSTER)
                            .sized(2.0f, 4.0f).fireImmune().clientTrackingRange(10)
                            .setShouldReceiveVelocityUpdates(true)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "nameless_guardian").toString()));

    //public static final RegistryObject<EntityType<EntityTheImmortal>> IMMORTAL =
    //        ENTITIES.register("immortal",
    //                () -> EntityType.Builder.<EntityTheImmortal>of(EntityTheImmortal::new, MobCategory.MONSTER)
    //                        .sized(0.6f, 2.0f).fireImmune().updateInterval(1)
    //                        .build(new ResourceLocation(EEEABMobs.MOD_ID, "immortal").toString()));

    //test entity
    public static final RegistryObject<EntityType<EntityTester>> TESTER =
            ENTITIES.register("tester",
                    () -> EntityType.Builder.<EntityTester>of(EntityTester::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f).clientTrackingRange(10)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "tester").toString()));


    //非生物实体
    //巫师炸弹
    public static final RegistryObject<EntityType<EntityShamanBomb>> SHAMAN_BOMB =
            ENTITIES.register("shaman_bomb",
                    () -> EntityType.Builder.<EntityShamanBomb>of(EntityShamanBomb::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f).clientTrackingRange(4).updateInterval(10)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "shaman_bomb").toString()));

    //相机摇晃
    public static final RegistryObject<EntityType<EntityCameraShake>> CAMERA_SHAKE =
            ENTITIES.register("camera_shake",
                    () -> EntityType.Builder.<EntityCameraShake>of(EntityCameraShake::new, MobCategory.MISC).sized(1.0f, 1.0f)
                            .setUpdateInterval(Integer.MAX_VALUE).build(new ResourceLocation(EEEABMobs.MOD_ID, "camera_shake").toString()));

    //移动控制器
    public static final RegistryObject<EntityType<EntityMovingController>> MOVING_CONTROLLER =
            ENTITIES.register("moving_controller",
                    () -> EntityType.Builder.<EntityMovingController>of(EntityMovingController::new, MobCategory.MISC).
                            sized(0f, 0f).noSummon().build(new ResourceLocation(EEEABMobs.MOD_ID, "moving_controller").toString()));

    //下落的方块
    public static final RegistryObject<EntityType<EntityFallingBlock>> FALLING_BLOCK =
            ENTITIES.register("falling_block",
                    () -> EntityType.Builder.<EntityFallingBlock>of(EntityFallingBlock::new, MobCategory.MISC)
                            .sized(0.99f, 0.99f)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "falling_block").toString()));

    //守卫者激光
    public static final RegistryObject<EntityType<EntityGuardianLaser>> GUARDIAN_LASER =
            ENTITIES.register("guardian_laser",
                    () -> EntityType.Builder.<EntityGuardianLaser>of(EntityGuardianLaser::new, MobCategory.MISC)
                            .sized(0.1f, 0.1f).setUpdateInterval(1)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "guardian_laser").toString()));

    //守卫者之刃
    public static final RegistryObject<EntityType<EntityGuardianBlade>> GUARDIAN_BLADE =
            ENTITIES.register("guardian_blade",
                    () -> EntityType.Builder.<EntityGuardianBlade>of(EntityGuardianBlade::new, MobCategory.MISC)
                            .sized(0.4f, 3f).setUpdateInterval(1)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "guardian_blade").toString()));

    //烧焦的地面
    public static final RegistryObject<EntityType<EntityScorch>> SCORCH =
            ENTITIES.register("scorch",
                    () -> EntityType.Builder.<EntityScorch>of(EntityScorch::new, MobCategory.MISC)
                            .sized(0.1f, 0.1f).setUpdateInterval(1)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "scorch").toString()));

    //结构之眼
    public static final RegistryObject<EntityType<EntityEyeOfStructure>> EYE_OF_STRUCTURE =
            ENTITIES.register("eye_of_structure",
                    () -> EntityType.Builder.<EntityEyeOfStructure>of(EntityEyeOfStructure::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(4)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "eye_of_structure").toString()));

    //毒箭
    public static final RegistryObject<EntityType<EntityPoisonArrow>> POISON_ARROW =
            ENTITIES.register("poison_arrow",
                    () -> EntityType.Builder.<EntityPoisonArrow>of(EntityPoisonArrow::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "poison_arrow").toString()));

    //血球
    public static final RegistryObject<EntityType<EntityBloodBall>> BLOOD_BALL =
            ENTITIES.register("blood_ball",
                    () -> EntityType.Builder.<EntityBloodBall>of(EntityBloodBall::new, MobCategory.MISC)
                            .sized(1F, 1F).clientTrackingRange(4).updateInterval(10)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "blood_ball").toString()));

    //腥红裂缝
    public static final RegistryObject<EntityType<EntityCrimsonCrack>> CRIMSON_CRACK =
            ENTITIES.register("crimson_crack",
                    () -> EntityType.Builder.<EntityCrimsonCrack>of(EntityCrimsonCrack::new, MobCategory.MISC)
                            .sized(0.1f, 0.1f).setUpdateInterval(1)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "crimson_crack").toString()));

    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
    }

    @SubscribeEvent
    public static void setEntityAttributeCreationEvent(EntityAttributeCreationEvent event) {
        event.put(EntityInit.IMMORTAL_SKELETON.get(), EntityImmortalSkeleton.setAttributes().build());
        event.put(EntityInit.IMMORTAL_KNIGHT.get(), EntityImmortalKnight.setAttributes().build());
        event.put(EntityInit.IMMORTAL_SHAMAN.get(), EntityImmortalShaman.setAttributes().build());
        event.put(EntityInit.IMMORTAL_GOLEM.get(), EntityImmortalGolem.setAttributes().build());
        event.put(EntityInit.CORPSE.get(), EntityCorpse.setAttributes().build());
        event.put(EntityInit.CORPSE_VILLAGER.get(), EntityCorpse.setAttributes().build());
        event.put(EntityInit.CORPSE_WARLOCK.get(), EntityCorpseWarlock.setAttributes().build());
        event.put(EntityInit.NAMELESS_GUARDIAN.get(), EntityNamelessGuardian.setAttributes().build());
        event.put(EntityInit.TESTER.get(), EntityTester.setAttributes().build());
        //event.put(EntityInit.IMMORTAL.get(), EntityTheImmortal.setAttributes().build());
    }
}
