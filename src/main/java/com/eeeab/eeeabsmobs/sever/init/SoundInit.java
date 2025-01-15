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

    //Misc
    public static final RegistryObject<SoundEvent> LASER = create("guardian_laser");
    public static final RegistryObject<SoundEvent> LASER2 = create("immortal_laser");
    public static final RegistryObject<SoundEvent> CRIMSON_RAY = create("crimson_ray");
    public static final RegistryObject<SoundEvent> GIANT_AXE_HIT = create("giant_axe_hit");
    public static final RegistryObject<SoundEvent> UNDAMAGED = create("undamaged");
    public static final RegistryObject<SoundEvent> CRIMSON_CRACK_BREAK = create("crimson_crack.break");
    public static final RegistryObject<SoundEvent> LAUNCH_GRENADE = create("grenade.launch");
    public static final RegistryObject<SoundEvent> MAGIC_MATRIX_OPEN = create("magic_matrix.open");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHURIKEN_SPIN = create("immortal_shuriken.spin");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHURIKEN_EXPLODE = create("immortal_shuriken.explode");

    //Immortal Skeleton
    public static final RegistryObject<SoundEvent> IMMORTAL_SKELETON_ROAR = create("immortal_skeleton.roar");
    public static final RegistryObject<SoundEvent> IMMORTAL_SKELETON_HURT = create("immortal_skeleton.hurt");
    public static final RegistryObject<SoundEvent> IMMORTAL_SKELETON_DEATH = create("immortal_skeleton.death");

    //Immortal Shaman
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_PREPARE_SHOOT = create("immortal_shaman.prepare_shoot");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_PREPARE_SPELL_CASTING = create("immortal_shaman.prepare_spell_casting");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_HURT = create("immortal_shaman.hurt");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_DEATH = create("immortal_shaman.death");

    //Immortal Golem
    public static final RegistryObject<SoundEvent> IMMORTAL_GOLEM_HURT = create("immortal_golem.hurt");
    public static final RegistryObject<SoundEvent> IMMORTAL_GOLEM_DEATH = create("immortal_golem.death");

    //Immortal Executioner
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_HURT = create("immortal_executioner.hurt");
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_DEATH = create("immortal_executioner.death");
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_DASH = create("immortal_executioner.dash");
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_BLOCK = create("immortal_executioner.block");
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_SCRATCH = create("immortal_executioner.scratch");
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_BURN = create("immortal_executioner.burn");
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_DETONATION = create("immortal_executioner.detonation");

    //Immortal
    public static final RegistryObject<SoundEvent> IMMORTAL_ACCUMULATING = create("immortal.accumulating");
    public static final RegistryObject<SoundEvent> IMMORTAL_ACCUMULATING_END = create("immortal.accumulating_end");
    public static final RegistryObject<SoundEvent> IMMORTAL_ADAPT = create("immortal.adapt");
    public static final RegistryObject<SoundEvent> IMMORTAL_ATTACK = create("immortal.attack");
    public static final RegistryObject<SoundEvent> IMMORTAL_ATTACK2 = create("immortal.attack2");
    public static final RegistryObject<SoundEvent> IMMORTAL_ATTRACT = create("immortal.attract");
    public static final RegistryObject<SoundEvent> IMMORTAL_BLOCKING = create("immortal.blocking");
    public static final RegistryObject<SoundEvent> IMMORTAL_BLOCKING_COUNTER = create("immortal.blocking_counter");
    public static final RegistryObject<SoundEvent> IMMORTAL_DEATH = create("immortal.death");
    public static final RegistryObject<SoundEvent> IMMORTAL_PUNCH_HIT = create("immortal.punch_hit");
    public static final RegistryObject<SoundEvent> IMMORTAL_PUNCH_HARD_HIT = create("immortal.punch_hard_hit");
    public static final RegistryObject<SoundEvent> IMMORTAL_HURT = create("immortal.hurt");
    public static final RegistryObject<SoundEvent> IMMORTAL_IDLE = create("immortal.idle");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHORYUKEN = create("immortal.shoryuken");
    public static final RegistryObject<SoundEvent> IMMORTAL_STONE_CRACK = create("immortal.stone_crack");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAKE_GROUND = create("immortal.shake_ground");
    public static final RegistryObject<SoundEvent> IMMORTAL_SUBSONIC = create("immortal.subsonic");
    public static final RegistryObject<SoundEvent> IMMORTAL_TELEPORT = create("immortal.teleport");

    //Guling Sentinel
    public static final RegistryObject<SoundEvent> GS_HURT = create("guling_sentinel.hurt");
    public static final RegistryObject<SoundEvent> GS_DEATH = create("guling_sentinel.death");
    public static final RegistryObject<SoundEvent> GS_ELECTROMAGNETIC = create("guling_sentinel.electromagnetic");

    //Guling Sentinel-Heavy
    public static final RegistryObject<SoundEvent> GSH_STEP = create("guling_sentinel_heavy.step");
    public static final RegistryObject<SoundEvent> GSH_IDLE = create("guling_sentinel_heavy.idle");
    public static final RegistryObject<SoundEvent> GSH_FRICTION = create("guling_sentinel_heavy.friction");
    public static final RegistryObject<SoundEvent> GSH_ELECTROMAGNETIC = create("guling_sentinel_heavy.electromagnetic");
    public static final RegistryObject<SoundEvent> GSH_PRE_ATTACK = create("guling_sentinel_heavy.pre_attack");
    public static final RegistryObject<SoundEvent> GSH_ATTACK = create("guling_sentinel_heavy.attack");
    public static final RegistryObject<SoundEvent> GSH_HURT = create("guling_sentinel_heavy.hurt");
    public static final RegistryObject<SoundEvent> GSH_DEATH = create("guling_sentinel_heavy.death");

    //Nameless Guardian
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_STEP = create("nameless_guardian.step");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_IDLE = create("nameless_guardian.idle");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_CREAK = create("nameless_guardian.creak");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_ACCUMULATING = create("nameless_guardian.accumulating");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_ACCUMULATING_END = create("nameless_guardian.accumulating_end");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_WHOOSH = create("nameless_guardian.whoosh");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_PRE_POUNCE = create("nameless_guardian.pre_pounce");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_MADNESS = create("nameless_guardian.madness");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_HURT = create("nameless_guardian.hurt");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_DEATH = create("nameless_guardian.death");

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
    //Record
    public static final RegistryObject<SoundEvent> MONO_GUARDIANS = create("music.guardians.mono");
    public static final RegistryObject<SoundEvent> MONO_THE_ARMY_OF_MINOTAUR = create("music.the_army_of_minotaur.mono");

    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }

    public static RegistryObject<SoundEvent> create(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(EEEABMobs.MOD_ID, name)));
    }
}
