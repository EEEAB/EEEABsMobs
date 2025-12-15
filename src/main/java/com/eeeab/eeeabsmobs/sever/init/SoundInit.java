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

    //Corpse
    public static final RegistryObject<SoundEvent> CORPSE_HURT = create("corpse.hurt");
    public static final RegistryObject<SoundEvent> CORPSE_DEATH = create("corpse.death");
    public static final RegistryObject<SoundEvent> CORPSE_IDLE = create("corpse.idle");

    //Corpse Warlock
    public static final RegistryObject<SoundEvent> CORPSE_WARLOCK_HURT = create("corpse_warlock.hurt");
    public static final RegistryObject<SoundEvent> CORPSE_WARLOCK_TEAR = create("corpse_warlock_tear");
    
    public static final RegistryObject<SoundEvent> RELICRON_FRICTION = create("relicron.friction");

    //Relic Observer
    public static final RegistryObject<SoundEvent> RELIC_OBSERVER_HURT = create("relic_observer.hurt");
    public static final RegistryObject<SoundEvent> RELIC_OBSERVER_DEATH = create("relic_observer.death");
    public static final RegistryObject<SoundEvent> RELIC_OBSERVER_ELECTROMAGNETIC = create("relic_observer.electromagnetic");
    public static final RegistryObject<SoundEvent> RELIC_OBSERVER_ELECTRIC_PULSE = create("relic_observer.electric_pulse");

    //Relic Ripper
    public static final RegistryObject<SoundEvent> RELIC_RIPPER_STEP = create("relic_ripper.step");
    public static final RegistryObject<SoundEvent> RELIC_RIPPER_HURT = create("relic_ripper.hurt");
    public static final RegistryObject<SoundEvent> RELIC_RIPPER_DEATH = create("relic_ripper.death");
    public static final RegistryObject<SoundEvent> RELIC_RIPPER_SHAKE_GROUND = create("relic_ripper.shake_ground");
    public static final RegistryObject<SoundEvent> RELIC_RIPPER_SAW = create("relic_ripper.saw");
    public static final RegistryObject<SoundEvent> RELIC_RIPPER_WHOOSH = create("relic_ripper.whoosh");

    //Relic Annihilator
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_PRE_ATTACK = create("relic_annihilator.pre_attack");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_ATTACK = create("relic_annihilator.attack");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_HURT = create("relic_annihilator.hurt");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_STUN = create("relic_annihilator.stun");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_DEATH = create("relic_annihilator.death");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_SAW = create("relic_annihilator.saw");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_WHOOSH = create("relic_annihilator.whoosh");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_SMASH = create("relic_annihilator.smash");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_JUMP = create("relic_annihilator.jump");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_ACTIVE_SCOPE = create("relic_annihilator.active_scope");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_LAUNCH = create("relic_annihilator.launch");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_SHOOT_LASER = create("relic_annihilator.shoot_laser");

    //Relic Earthshaker
    public static final RegistryObject<SoundEvent> RELIC_EARTHSHAKER_STEP = create("relic_earthshaker.step");
    public static final RegistryObject<SoundEvent> RELIC_EARTHSHAKER_IDLE = create("relic_earthshaker.idle");
    public static final RegistryObject<SoundEvent> RELIC_EARTHSHAKER_ELECTROMAGNETIC = create("relic_earthshaker.electromagnetic");
    public static final RegistryObject<SoundEvent> RELIC_EARTHSHAKER_PRE_ATTACK = create("relic_earthshaker.pre_attack");
    public static final RegistryObject<SoundEvent> RELIC_EARTHSHAKER_ATTACK = create("relic_earthshaker.attack");
    public static final RegistryObject<SoundEvent> RELIC_EARTHSHAKER_HURT = create("relic_earthshaker.hurt");
    public static final RegistryObject<SoundEvent> RELIC_EARTHSHAKER_DEATH = create("relic_earthshaker.death");

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

    //Immortal Skeleton
    public static final RegistryObject<SoundEvent> IMMORTAL_SKELETON_HURT = create("immortal_skeleton.hurt");
    public static final RegistryObject<SoundEvent> IMMORTAL_SKELETON_DEATH = create("immortal_skeleton.death");

    //Immortal Shaman
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_SHOOT = create("immortal_shaman.shoot");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_SPELL_CASTING = create("immortal_shaman.spell_casting");
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

    //Misc
    public static final RegistryObject<SoundEvent> LASER = create("guardian_laser.shoot");
    public static final RegistryObject<SoundEvent> LASER2 = create("immortal_laser.shoot");
    public static final RegistryObject<SoundEvent> INFRARED_RAY = create("infrared_ray.target_lock");
    public static final RegistryObject<SoundEvent> CRIMSON_RAY = create("crimson_ray.shoot");
    public static final RegistryObject<SoundEvent> GIANT_AXE_HIT = create("giant_axe_hit");
    public static final RegistryObject<SoundEvent> CRIMSON_CRACK_BREAK = create("crimson_crack.break");
    //TODO 待优化
    public static final RegistryObject<SoundEvent> LAUNCH_GRENADE = create("grenade.launch");
    public static final RegistryObject<SoundEvent> MAGIC_MATRIX_OPEN = create("magic_matrix.open");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHURIKEN_SPIN = create("immortal_shuriken.spin");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHURIKEN_EXPLODE = create("immortal_shuriken.explode");

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
