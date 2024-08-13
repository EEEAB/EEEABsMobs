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

    //Generic
    public static final RegistryObject<SoundEvent> LASER = create("laser");
    public static final RegistryObject<SoundEvent> GIANT_AXE_HIT = create("giant_axe_hit");
    public static final RegistryObject<SoundEvent> CRIMSON_RAY = create("crimson_ray");
    public static final RegistryObject<SoundEvent> BREAK = create("break");

    //Immortal Skeleton
    public static final RegistryObject<SoundEvent> IMMORTAL_SKELETON_ROAR = create("immortal.skeleton.roar");
    public static final RegistryObject<SoundEvent> IMMORTAL_SKELETON_HURT = create("immortal.skeleton.hurt");
    public static final RegistryObject<SoundEvent> IMMORTAL_SKELETON_DEATH = create("immortal.skeleton.death");

    //Immortal Shaman
    //public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_DEFENSE = create("immortal.shaman_defense");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_PREPARE_SHOOT = create("immortal.shaman.prepare_shoot");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_PREPARE_SPELL_CASTING = create("immortal.shaman.prepare_spell_casting");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_HURT = create("immortal.shaman.hurt");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_DEATH = create("immortal.shaman.death");

    //Immortal Golem
    public static final RegistryObject<SoundEvent> IMMORTAL_GOLEM_HURT = create("immortal.golem.hurt");
    public static final RegistryObject<SoundEvent> IMMORTAL_GOLEM_DEATH = create("immortal.golem.death");

    //Immortal Executioner
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_HURT = create("immortal.executioner.hurt");
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_DEATH = create("immortal.executioner.death");
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_DASH = create("immortal.executioner.dash");
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_BLOCK = create("immortal.executioner.block");
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_SCRATCH = create("immortal.executioner.scratch");
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_BURN = create("immortal.executioner.burn");
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_DETONATION = create("immortal.executioner.detonation");

    //Guling Sentinel
    public static final RegistryObject<SoundEvent> GS_HURT = create("guling.sentinel.hurt");
    public static final RegistryObject<SoundEvent> GS_DEATH = create("guling.sentinel.death");
    public static final RegistryObject<SoundEvent> GS_ELECTROMAGNETIC = create("guling.sentinel.electromagnetic");

    //Guling Sentinel-Heavy
    public static final RegistryObject<SoundEvent> GSH_STEP = create("guling_sentinel_heavy.step");
    public static final RegistryObject<SoundEvent> GSH_IDLE = create("guling_sentinel_heavy.idle");
    public static final RegistryObject<SoundEvent> GSH_FRICTION = create("guling_sentinel_heavy.friction");
    public static final RegistryObject<SoundEvent> GSH_ELECTROMAGNETIC = create("guling_sentinel_heavy.electromagnetic");
    public static final RegistryObject<SoundEvent> GSH_SPARK = create("guling_sentinel_heavy.spark");
    public static final RegistryObject<SoundEvent> GSH_PRE_ATTACK = create("guling_sentinel_heavy.pre_attack");
    public static final RegistryObject<SoundEvent> GSH_ATTACK = create("guling_sentinel_heavy.attack");
    public static final RegistryObject<SoundEvent> GSH_HURT = create("guling_sentinel_heavy.hurt");
    public static final RegistryObject<SoundEvent> GSH_DEATH = create("guling_sentinel_heavy.death");

    //Nameless Guardian
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_STEP = create("nameless.guardian.step");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_IDLE = create("nameless.guardian.idle");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_CREAK = create("nameless.guardian.creak");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_ACCUMULATING = create("nameless.guardian.accumulating");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_ACCUMULATING_END = create("nameless.guardian.accumulating_end");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_WHOOSH = create("nameless.guardian.whoosh");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_PRE_POUNCE = create("nameless.guardian.pre_pounce");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_MADNESS = create("nameless.guardian.madness");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_HURT = create("nameless.guardian.hurt");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_DEATH = create("nameless.guardian.death");

    //Corpse
    public static final RegistryObject<SoundEvent> CORPSE_HURT = create("corpse.hurt");
    public static final RegistryObject<SoundEvent> CORPSE_DEATH = create("corpse.death");
    public static final RegistryObject<SoundEvent> CORPSE_IDLE = create("corpse.idle");

    //Corpse Warlock
    public static final RegistryObject<SoundEvent> CORPSE_WARLOCK_HURT = create("corpse_warlock.hurt");
    public static final RegistryObject<SoundEvent> CORPSE_WARLOCK_TEAR = create("corpse_warlock_tear");

    //Music
    public static final RegistryObject<SoundEvent> GUARDIANS = create("music.guardians");
    public static final RegistryObject<SoundEvent> GUARDIANS_CLIMAX = create("music.nameless_guardian_theme_2");
    public static final RegistryObject<SoundEvent> GUARDIANS_PRELUDE = create("music.nameless_guardian_theme_1");
    public static final RegistryObject<SoundEvent> THE_ARMY_OF_MINOTAUR = create("music.the_army_of_minotaur");

    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }

    public static RegistryObject<SoundEvent> create(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(EEEABMobs.MOD_ID, name)));
    }
}
