package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.potion.*;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EffectInit {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, EEEABMobs.MOD_ID);
    public static final RegistryObject<MobEffect> ERODE_EFFECT = EFFECTS.register("erode_effect", ErodeEffect::new);
    public static final RegistryObject<MobEffect> ARMOR_LOWER_EFFECT = EFFECTS.register("armor_lower_effect", ArmorLowerEffect::new);
    public static final RegistryObject<MobEffect> STUN_EFFECT = EFFECTS.register("stun_effect", StunEffect::new);
    public static final RegistryObject<MobEffect> FRENZY_EFFECT = EFFECTS.register("frenzy_effect", FrenzyEffect::new);
    public static final RegistryObject<MobEffect> ELECTRIFIED_EFFECT = EFFECTS.register("electrified_effect", OverloadEffect::new);

    public static void register(IEventBus bus) {
        EFFECTS.register(bus);
    }
}
