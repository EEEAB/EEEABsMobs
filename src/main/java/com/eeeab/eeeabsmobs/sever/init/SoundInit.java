package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundInit {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EEEABMobs.MOD_ID);

    // Generic
    public static final RegistryObject<SoundEvent> LASER = create("laser");
    public static final RegistryObject<SoundEvent> GIANT_AXE_HIT = create("giant_axe_hit");

    //immortal skeleton
    public static final RegistryObject<SoundEvent> IMMORTAL_SKELETON_ROAR = create("immortal.skeleton.roar");
    public static final RegistryObject<SoundEvent> IMMORTAL_SKELETON_HURT = create("immortal.skeleton.hurt");
    public static final RegistryObject<SoundEvent> IMMORTAL_SKELETON_DEATH = create("immortal.skeleton.death");

    //immortal shaman
    //public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_DEFENSE = create("immortal.shaman_defense");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_PREPARE_SHOOT = create("immortal.shaman.prepare_shoot");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_PREPARE_SPELL_CASTING = create("immortal.shaman.prepare_spell_casting");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_HURT = create("immortal.shaman.hurt");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_DEATH = create("immortal.shaman.death");

    //immortal golem
    public static final RegistryObject<SoundEvent> IMMORTAL_GOLEM_HURT = create("immortal.golem.hurt");
    public static final RegistryObject<SoundEvent> IMMORTAL_GOLEM_DEATH = create("immortal.golem.death");

    //nameless guardian
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_STEP = create("nameless.guardian.step");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_IDLE = create("nameless.guardian.idle");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_CREAK = create("nameless.guardian.creak");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_PRE_SWING = create("nameless.guardian.pre_swing");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_ACCUMULATING = create("nameless.guardian.accumulating");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_WHOOSH = create("nameless.guardian.whoosh");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_PRE_POUNCE = create("nameless.guardian.pre_pounce");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_MADNESS = create("nameless.guardian.madness");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_HURT = create("nameless.guardian.hurt");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_DEATH = create("nameless.guardian.death");

    //Music
    public static final RegistryObject<SoundEvent> GUARDIANS = create("music.guardians");
    public static final RegistryObject<SoundEvent> GUARDIANS_CLIMAX = create("music.nameless_guardian_theme_2");
    public static final RegistryObject<SoundEvent> GUARDIANS_PRELUDE = create("music.nameless_guardian_theme_1");

    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }

    public static RegistryObject<SoundEvent> create(String name) {
        return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(EEEABMobs.MOD_ID, name)));
    }
}
