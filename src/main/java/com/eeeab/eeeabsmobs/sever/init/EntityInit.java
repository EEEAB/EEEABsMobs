package com.eeeab.eeeabsmobs.sever.init;


import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.entity.impl.effect.*;
import com.eeeab.eeeabsmobs.sever.entity.impl.namelessguardian.EntityNamelessGuardian;
import com.eeeab.eeeabsmobs.sever.entity.impl.immortal.*;
import com.eeeab.eeeabsmobs.sever.entity.impl.projectile.EntityShamanBomb;
import com.eeeab.eeeabsmobs.sever.entity.impl.test.EntityTest;
import com.eeeab.eeeabsmobs.sever.entity.impl.test.EntityTestllager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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


    public static final RegistryObject<EntityType<EntityNamelessGuardian>> NAMELESS_GUARDIAN =
            ENTITIES.register("nameless_guardian",
                    () -> EntityType.Builder.<EntityNamelessGuardian>of(EntityNamelessGuardian::new, MobCategory.MONSTER)
                            .sized(2.25f, 4.0f).fireImmune().clientTrackingRange(5).updateInterval(1)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "nameless_guardian").toString()));

    //test entity
    public static final RegistryObject<EntityType<EntityTestllager>> TESTLLAGER =
            ENTITIES.register("testllager",
                    () -> EntityType.Builder.<EntityTestllager>of(EntityTestllager::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.95f).clientTrackingRange(10)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "testllager").toString()));


//    public static final RegistryObject<EntityType<EntityTheImmortal>> IMMORTAL =
//            ENTITIES.register("immortal",
//                    () -> EntityType.Builder.<EntityTheImmortal>of(EntityTheImmortal::new, MobCategory.MONSTER)
//                            .sized(0.6f, 2.0f).fireImmune().updateInterval(1)
//                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "immortal").toString()));


    public static final RegistryObject<EntityType<EntityTest>> TEST =
            ENTITIES.register("test",
                    () -> EntityType.Builder.<EntityTest>of(EntityTest::new, MobCategory.MONSTER)
                            .sized(1f, 2f).clientTrackingRange(8)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "test").toString()));


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
                    () -> EntityType.Builder.<EntityFallingBlock>of(EntityFallingBlock::new, MobCategory.MISC).sized(0.99f, 0.99f)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "falling_block").toString()));

    //守护者激光
    public static final RegistryObject<EntityType<EntityGuardianLaser>> GUARDIAN_LASER =
            ENTITIES.register("guardian_laser",
                    () -> EntityType.Builder.<EntityGuardianLaser>of(EntityGuardianLaser::new, MobCategory.MISC).sized(0.1f, 0.1f).setUpdateInterval(1)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "guardian_laser").toString()));

    public static final RegistryObject<EntityType<EntityGuardianBlade>> GUARDIAN_BLADE=
            ENTITIES.register("guardian_blade",
                    ()->EntityType.Builder.<EntityGuardianBlade>of(EntityGuardianBlade::new,MobCategory.MISC).sized(0.3f,3f).setUpdateInterval(1)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID,"guardian_blade").toString()));

    //烧焦的地面
    public static final RegistryObject<EntityType<EntityScorch>> SCORCH =
            ENTITIES.register("scorch",
                    () -> EntityType.Builder.<EntityScorch>of(EntityScorch::new, MobCategory.MISC).sized(0.1f, 0.1f).setUpdateInterval(1)
                            .build(new ResourceLocation(EEEABMobs.MOD_ID, "scorch").toString()));

    //结构之眼
    public static final RegistryObject<EntityType<EntityEyeOfStructure>> EYE_OF_STRUCTURE =
            ENTITIES.register("eye_of_structure", () -> EntityType.Builder.<EntityEyeOfStructure>of(EntityEyeOfStructure::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(4)
                    .build(new ResourceLocation(EEEABMobs.MOD_ID, "eye_of_structure").toString()));


    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
    }

    @SubscribeEvent
    public static void setEntityAttributeCreationEvent(EntityAttributeCreationEvent event) {
        event.put(EntityInit.IMMORTAL_SKELETON.get(), EntityImmortalSkeleton.setAttributes().build());
        event.put(EntityInit.IMMORTAL_KNIGHT.get(), EntityImmortalKnight.setAttributes().build());
        event.put(EntityInit.IMMORTAL_SHAMAN.get(), EntityImmortalShaman.setAttributes().build());
        event.put(EntityInit.IMMORTAL_GOLEM.get(), EntityImmortalGolem.setAttributes().build());
        event.put(EntityInit.NAMELESS_GUARDIAN.get(), EntityNamelessGuardian.setAttributes().build());
        event.put(EntityInit.TESTLLAGER.get(), EntityTestllager.setAttributes().build());
//        event.put(EntityInit.IMMORTAL.get(), EntityTheImmortal.setAttributes().build());
//        event.put(EntityInit.TEST.get(), setCommonAttributes());
    }

    public static AttributeSupplier setCommonAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).
                add(Attributes.MOVEMENT_SPEED, 0.3D).
                add(Attributes.ATTACK_DAMAGE, 1D).build();
    }
}
