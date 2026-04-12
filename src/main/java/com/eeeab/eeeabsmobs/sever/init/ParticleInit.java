package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.particle.ParticleDust;
import com.eeeab.eeeabsmobs.client.particle.ParticleOrb;
import com.eeeab.eeeabsmobs.client.particle.ParticleRing;
import com.eeeab.eeeabsmobs.client.particle.lib.data.AdvancedBlockParticleData;
import com.eeeab.eeeabsmobs.client.particle.lib.data.AdvancedParticleData;
import com.eeeab.eeeabsmobs.client.particle.lib.data.RibbonParticleData;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleInit {
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, EEEABMobs.MOD_ID);

    public static final RegistryObject<SimpleParticleType> HIT_CUT = PARTICLES.register("hit_cut", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> GUARDIAN_SPARK = PARTICLES.register("guardian_spark", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> POISON = PARTICLES.register("poison", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> WARLOCK_HEAL = PARTICLES.register("warlock_heal", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> IMMORTAL_EXPLOSION = PARTICLES.register("immortal_explosion", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BLAZE_EXPLOSION = PARTICLES.register("blaze_explosion", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> VOLT_EXPLOSION = PARTICLES.register("volt_explosion", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> BLAZE_EXPLOSION_EMITTER = PARTICLES.register("blaze_explosion_emitter", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> VOLT_EXPLOSION_EMITTER = PARTICLES.register("volt_explosion_emitter", () -> new SimpleParticleType(true));
    public static final RegistryObject<ParticleType<ParticleOrb.OrbData>> ORB = PARTICLES.register("orb", () -> new ParticleType<>(false, ParticleOrb.OrbData.DESERIALIZER) {
        public Codec<ParticleOrb.OrbData> codec() {
            return ParticleOrb.OrbData.CODEC(this);
        }
    });
    public static final RegistryObject<ParticleType<ParticleDust.DustData>> DUST = PARTICLES.register("dust", () -> new ParticleType<>(false, ParticleDust.DustData.DESERIALIZER) {
        public Codec<ParticleDust.DustData> codec() {
            return ParticleDust.DustData.CODEC(this);
        }
    });
    public static final RegistryObject<ParticleType<ParticleRing.RingData>> RING = PARTICLES.register("ring", () -> new ParticleType<>(false, ParticleRing.RingData.DESERIALIZER) {
        public Codec<ParticleRing.RingData> codec() {
            return ParticleRing.RingData.CODEC(this);
        }
    });
    public static final RegistryObject<ParticleType<ParticleRing.RingData>> BIG_RING = PARTICLES.register("big_ring", () -> new ParticleType<>(false, ParticleRing.RingData.DESERIALIZER) {
        public Codec<ParticleRing.RingData> codec() {
            return ParticleRing.RingData.CODEC(this);
        }
    });

    public static final RegistryObject<ParticleType<AdvancedParticleData>> SPELL_CASTING = ParticleInit.registerAdvancedParticle("spell_casting", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> ADV_ORB = ParticleInit.registerAdvancedParticle("adv_orb", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> GLOW = ParticleInit.registerAdvancedParticle("glow", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> ADV_RING = ParticleInit.registerAdvancedParticle("adv_ring", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> ADV_RING2 = ParticleInit.registerAdvancedParticle("adv_ring2", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> CRIMSON = ParticleInit.registerAdvancedParticle("crimson", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<RibbonParticleData>> FLAT_RIBBON = ParticleInit.registerRibbonParticle("flat_ribbon", RibbonParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> CRIMSON_EYE = ParticleInit.registerAdvancedParticle("crimson_eye", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> STRIP_SOUL_FIRE = ParticleInit.registerAdvancedParticle("strip_soul_fire", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> CROSS_FLASH = ParticleInit.registerAdvancedParticle("cross_flash", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> VORTEX = ParticleInit.registerAdvancedParticle("vortex", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> CRIT_RING = ParticleInit.registerAdvancedParticle("crit_ring", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> THUMP_RING = ParticleInit.registerAdvancedParticle("thump_ring", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> PUNCTURED_AIR_RING = ParticleInit.registerAdvancedParticle("punctured_air_ring", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> SURGE = ParticleInit.registerAdvancedParticle("surge", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> SPARK = ParticleInit.registerAdvancedParticle("spark", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedBlockParticleData>> BLOCK = PARTICLES.register("block", () -> new ParticleType<>(false, AdvancedBlockParticleData.DESERIALIZER) {
        public Codec<AdvancedBlockParticleData> codec() {
            return AdvancedBlockParticleData.CODEC_BLOCK();
        }
    });

    private static RegistryObject<ParticleType<AdvancedParticleData>> registerAdvancedParticle(String key, ParticleOptions.Deserializer<AdvancedParticleData> deserializer) {
        return PARTICLES.register(key, () -> new ParticleType<>(false, deserializer) {
            public Codec<AdvancedParticleData> codec() {
                return AdvancedParticleData.CODEC(this);
            }
        });
    }

    private static RegistryObject<ParticleType<RibbonParticleData>> registerRibbonParticle(String key, ParticleOptions.Deserializer<RibbonParticleData> deserializer) {
        return PARTICLES.register(key, () -> new ParticleType<>(false, deserializer) {
            public Codec<RibbonParticleData> codec() {
                return RibbonParticleData.CODEC_RIBBON(this);
            }
        });
    }

    public static void register(IEventBus bus) {
        PARTICLES.register(bus);
    }
}
