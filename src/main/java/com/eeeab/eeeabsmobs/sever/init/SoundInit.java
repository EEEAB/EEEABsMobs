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
    public static final RegistryObject<SoundEvent> CORPSE_HURT = create("entity.corpse.hurt");
    public static final RegistryObject<SoundEvent> CORPSE_DEATH = create("entity.corpse.death");
    public static final RegistryObject<SoundEvent> CORPSE_IDLE = create("entity.corpse.idle");

    //Corpse Warlock
    public static final RegistryObject<SoundEvent> CORPSE_WARLOCK_HURT = create("entity.corpse_warlock.hurt");
    public static final RegistryObject<SoundEvent> CORPSE_WARLOCK_TEAR = create("entity.corpse_warlock.tear");

    public static final RegistryObject<SoundEvent> RELICRON_ACTIVE = create("entity.relicron.active");

    //Relic Observer
    public static final RegistryObject<SoundEvent> RELIC_OBSERVER_HURT = create("entity.relic_observer.hurt");
    public static final RegistryObject<SoundEvent> RELIC_OBSERVER_DEATH = create("entity.relic_observer.death");
    public static final RegistryObject<SoundEvent> RELIC_OBSERVER_ATTACK = create("entity.relic_observer.attack");
    public static final RegistryObject<SoundEvent> RELIC_OBSERVER_ELECTROMAGNETIC = create("entity.relic_observer.electromagnetic");
    public static final RegistryObject<SoundEvent> RELIC_OBSERVER_ELECTRIC_PULSE = create("entity.relic_observer.electric_pulse");

    //Relic Ripper
    public static final RegistryObject<SoundEvent> RELIC_RIPPER_STEP = create("entity.relic_ripper.step");
    public static final RegistryObject<SoundEvent> RELIC_RIPPER_HURT = create("entity.relic_ripper.hurt");
    public static final RegistryObject<SoundEvent> RELIC_RIPPER_DEATH = create("entity.relic_ripper.death");
    public static final RegistryObject<SoundEvent> RELIC_RIPPER_SHAKE_GROUND = create("entity.relic_ripper.shake_ground");
    public static final RegistryObject<SoundEvent> RELIC_RIPPER_SAW = create("entity.relic_ripper.saw");
    public static final RegistryObject<SoundEvent> RELIC_RIPPER_WHOOSH = create("entity.relic_ripper.whoosh");

    //Relic Earthshaker
    public static final RegistryObject<SoundEvent> RELIC_EARTHSHAKER_STEP = create("entity.relic_earthshaker.step");
    public static final RegistryObject<SoundEvent> RELIC_EARTHSHAKER_IDLE = create("entity.relic_earthshaker.idle");
    public static final RegistryObject<SoundEvent> RELIC_EARTHSHAKER_ELECTROMAGNETIC = create("entity.relic_earthshaker.electromagnetic");
    public static final RegistryObject<SoundEvent> RELIC_EARTHSHAKER_PRE_ATTACK = create("entity.relic_earthshaker.pre_attack");
    public static final RegistryObject<SoundEvent> RELIC_EARTHSHAKER_ATTACK = create("entity.relic_earthshaker.attack");
    public static final RegistryObject<SoundEvent> RELIC_EARTHSHAKER_LAUNCH_GRENADE = create("entity.relic_earthshaker.launch_grenade");
    public static final RegistryObject<SoundEvent> RELIC_EARTHSHAKER_HURT = create("entity.relic_earthshaker.hurt");
    public static final RegistryObject<SoundEvent> RELIC_EARTHSHAKER_DEATH = create("entity.relic_earthshaker.death");
    public static final RegistryObject<SoundEvent> RELIC_EARTHSHAKER_FALL = create("entity.relic_earthshaker.fall");

    //Relic Annihilator
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_ACTIVE = create("entity.relic_annihilator.active");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_IDLE = create("entity.relic_annihilator.idle");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_PRE_ATTACK = create("entity.relic_annihilator.pre_attack");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_ATTACK = create("entity.relic_annihilator.attack");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_HURT = create("entity.relic_annihilator.hurt");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_STUN = create("entity.relic_annihilator.stun");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_DEATH = create("entity.relic_annihilator.death");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_SAW = create("entity.relic_annihilator.saw");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_WHOOSH = create("entity.relic_annihilator.whoosh");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_SMASH = create("entity.relic_annihilator.smash");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_JUMP = create("entity.relic_annihilator.jump");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_ACTIVE_SCOPE = create("entity.relic_annihilator.active_scope");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_LAUNCH = create("entity.relic_annihilator.launch");
    public static final RegistryObject<SoundEvent> RELIC_ANNIHILATOR_SHOOT_LASER = create("entity.relic_annihilator.shoot_laser");

    //Nameless Guardian
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_STEP = create("entity.nameless_guardian.step");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_IDLE = create("entity.nameless_guardian.idle");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_CREAK = create("entity.nameless_guardian.creak");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_ACCUMULATING = create("entity.nameless_guardian.accumulating");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_ACCUMULATING_END = create("entity.nameless_guardian.accumulating_end");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_WHOOSH = create("entity.nameless_guardian.whoosh");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_PRE_POUNCE = create("entity.nameless_guardian.pre_pounce");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_MADNESS = create("entity.nameless_guardian.madness");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_HURT = create("entity.nameless_guardian.hurt");
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_DEATH = create("entity.nameless_guardian.death");

    //Realmwarden
    public static final RegistryObject<SoundEvent> REALM_WARDEN_IDLE = create("entity.realm_warden.idle");
    public static final RegistryObject<SoundEvent> REALM_WARDEN_STEP = create("entity.realm_warden.step");
    public static final RegistryObject<SoundEvent> REALM_WARDEN_HURT = create("entity.realm_warden.hurt");
    public static final RegistryObject<SoundEvent> REALM_WARDEN_DEATH = create("entity.realm_warden.death");
    public static final RegistryObject<SoundEvent> REALM_WARDEN_LAUNCH = create("entity.realm_warden.launch");
    public static final RegistryObject<SoundEvent> REALM_WARDEN_BLAST = create("entity.realm_warden.blast");
    public static final RegistryObject<SoundEvent> REALM_WARDEN_WHOOSH = create("entity.realm_warden.whoosh");
    public static final RegistryObject<SoundEvent> REALM_WARDEN_SHAKE_GROUND = create("entity.realm_warden.shake_ground");
    public static final RegistryObject<SoundEvent> REALM_WARDEN_SHOCK = create("entity.realm_warden.shock");
    public static final RegistryObject<SoundEvent> REALM_WARDEN_LEAP = create("entity.realm_warden.leap");
    public static final RegistryObject<SoundEvent> REALM_WARDEN_LANDING = create("entity.realm_warden.landing");
    public static final RegistryObject<SoundEvent> REALM_WARDEN_HUM = create("entity.realm_warden.hum");
    public static final RegistryObject<SoundEvent> REALM_WARDEN_SHORTHUM = create("entity.realm_warden.short_hum");
    public static final RegistryObject<SoundEvent> REALM_WARDEN_ATTACK = create("entity.realm_warden.attack");
    public static final RegistryObject<SoundEvent> REALM_WARDEN_AIRFLOW = create("entity.realm_warden.airflow");
    public static final RegistryObject<SoundEvent> REALM_WARDEN_VOICE1 = create("entity.realm_warden.voice1");
    public static final RegistryObject<SoundEvent> REALM_WARDEN_VOICE2 = create("entity.realm_warden.voice2");

    //Immortal Skeleton
    public static final RegistryObject<SoundEvent> IMMORTAL_SKELETON_HURT = create("entity.immortal_skeleton.hurt");
    public static final RegistryObject<SoundEvent> IMMORTAL_SKELETON_DEATH = create("entity.immortal_skeleton.death");

    //Immortal Shaman
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_SHOOT = create("entity.immortal_shaman.shoot");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_SPELL_CASTING = create("entity.immortal_shaman.spell_casting");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_HURT = create("entity.immortal_shaman.hurt");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAMAN_DEATH = create("entity.immortal_shaman.death");

    //Immortal Golem
    public static final RegistryObject<SoundEvent> IMMORTAL_GOLEM_HURT = create("entity.immortal_golem.hurt");
    public static final RegistryObject<SoundEvent> IMMORTAL_GOLEM_DEATH = create("entity.immortal_golem.death");

    //Immortal Executioner
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_HURT = create("entity.immortal_executioner.hurt");
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_DEATH = create("entity.immortal_executioner.death");
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_DASH = create("entity.immortal_executioner.dash");
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_BLOCK = create("entity.immortal_executioner.block");
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_SCRATCH = create("entity.immortal_executioner.scratch");
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_BURN = create("entity.immortal_executioner.burn");
    public static final RegistryObject<SoundEvent> IMMORTAL_EXECUTIONER_DETONATION = create("entity.immortal_executioner.detonation");

    //Immortal
    public static final RegistryObject<SoundEvent> IMMORTAL_ACCUMULATING = create("entity.immortal.accumulating");
    public static final RegistryObject<SoundEvent> IMMORTAL_ACCUMULATING_END = create("entity.immortal.accumulating_end");
    public static final RegistryObject<SoundEvent> IMMORTAL_ADAPT = create("entity.immortal.adapt");
    public static final RegistryObject<SoundEvent> IMMORTAL_ATTACK = create("entity.immortal.attack");
    public static final RegistryObject<SoundEvent> IMMORTAL_ATTACK2 = create("entity.immortal.attack2");
    public static final RegistryObject<SoundEvent> IMMORTAL_ATTRACT = create("entity.immortal.attract");
    public static final RegistryObject<SoundEvent> IMMORTAL_BLOCKING = create("entity.immortal.blocking");
    public static final RegistryObject<SoundEvent> IMMORTAL_BLOCKING_COUNTER = create("entity.immortal.blocking_counter");
    public static final RegistryObject<SoundEvent> IMMORTAL_DEATH = create("entity.immortal.death");
    public static final RegistryObject<SoundEvent> IMMORTAL_PUNCH_HIT = create("entity.immortal.punch_hit");
    public static final RegistryObject<SoundEvent> IMMORTAL_PUNCH_HARD_HIT = create("entity.immortal.punch_hard_hit");
    public static final RegistryObject<SoundEvent> IMMORTAL_HURT = create("entity.immortal.hurt");
    public static final RegistryObject<SoundEvent> IMMORTAL_IDLE = create("entity.immortal.idle");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHORYUKEN = create("entity.immortal.shoryuken");
    public static final RegistryObject<SoundEvent> IMMORTAL_STONE_CRACK = create("entity.immortal.stone_crack");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHAKE_GROUND = create("entity.immortal.shake_ground");
    public static final RegistryObject<SoundEvent> IMMORTAL_SUBSONIC = create("entity.immortal.subsonic");
    public static final RegistryObject<SoundEvent> IMMORTAL_TELEPORT = create("entity.immortal.teleport");

    //Misc
    public static final RegistryObject<SoundEvent> LASER = create("guardian_laser.shoot");
    public static final RegistryObject<SoundEvent> LASER2 = create("immortal_laser.shoot");
    public static final RegistryObject<SoundEvent> INFRARED_RAY = create("infrared_ray.target_lock");
    public static final RegistryObject<SoundEvent> CRIMSON_RAY = create("crimson_ray.shoot");
    public static final RegistryObject<SoundEvent> GIANT_AXE_HIT = create("giant_axe_hit");
    public static final RegistryObject<SoundEvent> CRIMSON_CRACK_BREAK = create("crimson_crack.break");
    public static final RegistryObject<SoundEvent> PULSED_GRENADE_LAUNCH = create("pulsed_grenade.launch");
    public static final RegistryObject<SoundEvent> MAGIC_MATRIX_OPEN = create("magic_matrix.open");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHURIKEN_SPIN = create("immortal_shuriken.spin");
    public static final RegistryObject<SoundEvent> IMMORTAL_SHURIKEN_EXPLODE = create("immortal_shuriken.explode");
    public static final RegistryObject<SoundEvent> SURGE = create("surge");
    public static final RegistryObject<SoundEvent> MAN = create("man");

    //Item
    public static final RegistryObject<SoundEvent> CHAINSWORD_HIT_CUT = create("item.chainsword.cut_hit");
    public static final RegistryObject<SoundEvent> DOOMBOLTAXE_THROW = create("item.doomboltaxe.throw");
    public static final RegistryObject<SoundEvent> DOOMBOLTAXE_RETURN = create("item.doomboltaxe.return");
    public static final RegistryObject<SoundEvent> DOOMBOLTAXE_HIT = create("item.doomboltaxe.hit");
    public static final RegistryObject<SoundEvent> DOOMBOLTAXE_THUNDER = create("item.doomboltaxe.thunder");
    public static final RegistryObject<SoundEvent> DOOMBOLTAXE_HIT_GROUND = create("item.doomboltaxe.hit_ground");

    //Music
    public static final RegistryObject<SoundEvent> NAMELESS_GUARDIAN_THEME = create("music.nameless_guardian_theme");
    public static final RegistryObject<SoundEvent> REALM_WARDEN_THEME = create("music.realm_warden_theme");
    public static final RegistryObject<SoundEvent> REALM_WARDEN_THEME_DISC = create("music.realm_warden_theme_disc");
    public static final RegistryObject<SoundEvent> THE_IMMORTAL_THEME = create("music.the_immortal_theme");


    public static void register(IEventBus bus) {
        SOUNDS.register(bus);
    }

    public static RegistryObject<SoundEvent> create(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(EEEABMobs.MOD_ID, name)));
    }
}
