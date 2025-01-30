package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.particle.ParticleDust;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleOrb;
import com.eeeab.eeeabsmobs.client.particle.base.ParticleRing;
import com.eeeab.eeeabsmobs.client.particle.util.AdvancedParticleData;
import com.eeeab.eeeabsmobs.client.particle.util.RibbonParticleData;
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

    public static final RegistryObject<SimpleParticleType> GUARDIAN_SPARK = PARTICLES.register("guardian_spark", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> POISON = PARTICLES.register("poison", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> WARLOCK_HEAL = PARTICLES.register("warlock_heal", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> IMMORTAL_EXPLOSION = PARTICLES.register("immortal_explosion", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> OVERLOAD_EXPLOSION = PARTICLES.register("overload_explosion", () -> new SimpleParticleType(true));
    public static final RegistryObject<SimpleParticleType> OVERLOAD_EXPLOSION_EMITTER = PARTICLES.register("overload_explosion_emitter", () -> new SimpleParticleType(true));
    public static final RegistryObject<ParticleType<ParticleOrb.OrbData>> ORB = PARTICLES.register("orb", () -> new ParticleType<ParticleOrb.OrbData>(false, ParticleOrb.OrbData.DESERIALIZER) {
        @Override
        public Codec<ParticleOrb.OrbData> codec() {
            return ParticleOrb.OrbData.CODEC(ORB.get());
        }
    });
    public static final RegistryObject<ParticleType<ParticleDust.DustData>> DUST = PARTICLES.register("dust", () -> new ParticleType<ParticleDust.DustData>(false, ParticleDust.DustData.DESERIALIZER) {
        @Override
        public Codec<ParticleDust.DustData> codec() {
            return ParticleDust.DustData.CODEC(DUST.get());
        }
    });
    public static final RegistryObject<ParticleType<ParticleRing.RingData>> RING = PARTICLES.register("ring", () -> new ParticleType<ParticleRing.RingData>(false, ParticleRing.RingData.DESERIALIZER) {
        @Override
        public Codec<ParticleRing.RingData> codec() {
            return ParticleRing.RingData.CODEC(RING.get());
        }
    });

    public static final RegistryObject<ParticleType<AdvancedParticleData>> SPELL_CASTING = ParticleInit.registerAdvancedParticle("spell_casting", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> ADV_ORB = ParticleInit.registerAdvancedParticle("adv_orb", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> CRIMSON = ParticleInit.registerAdvancedParticle("crimson", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> CRIMSON_EYE = ParticleInit.registerAdvancedParticle("crimson_eye", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> STRIP_SOUL_FIRE = ParticleInit.registerAdvancedParticle("strip_soul_fire", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> GLOW = ParticleInit.registerAdvancedParticle("glow", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> ADV_RING = ParticleInit.registerAdvancedParticle("adv_ring", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> BIG_RING = ParticleInit.registerAdvancedParticle("big_ring", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> CRIT_RING = ParticleInit.registerAdvancedParticle("crit_ring", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> THUMP_RING = ParticleInit.registerAdvancedParticle("thump_ring", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<AdvancedParticleData>> PUNCTURED_AIR_RING = ParticleInit.registerAdvancedParticle("punctured_air_ring", AdvancedParticleData.DESERIALIZER);
    public static final RegistryObject<ParticleType<RibbonParticleData>> FLAT_RIBBON = ParticleInit.registerRibbonParticle("flat_ribbon", RibbonParticleData.DESERIALIZER);

    private static RegistryObject<ParticleType<AdvancedParticleData>> registerAdvancedParticle(String key, ParticleOptions.Deserializer<AdvancedParticleData> deserializer) {
        return PARTICLES.register(key, () -> new ParticleType<AdvancedParticleData>(false, deserializer) {
            public Codec<AdvancedParticleData> codec() {
                return AdvancedParticleData.CODEC(this);
            }
        });
    }

    private static RegistryObject<ParticleType<RibbonParticleData>> registerRibbonParticle(String key, ParticleOptions.Deserializer<RibbonParticleData> deserializer) {
        return PARTICLES.register(key, () -> new ParticleType<RibbonParticleData>(false, deserializer) {
            public Codec<RibbonParticleData> codec() {
                return RibbonParticleData.CODEC_RIBBON(this);
            }
        });
    }

    public static void register(IEventBus bus) {
        PARTICLES.register(bus);
    }
}
