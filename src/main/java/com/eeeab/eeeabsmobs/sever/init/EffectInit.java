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
    public static final RegistryObject<MobEffect> ERODE_EFFECT = EFFECTS.register("erode_effect", EffectErode::new);
    public static final RegistryObject<MobEffect> ARMOR_LOWER_EFFECT = EFFECTS.register("armor_lower_effect", EffectArmorLower::new);
    public static final RegistryObject<MobEffect> VERTIGO_EFFECT = EFFECTS.register("vertigo_effect", EffectVertigo::new);
    public static final RegistryObject<MobEffect> FRENZY_EFFECT = EFFECTS.register("frenzy_effect", EffectFrenzy::new);
    public static final RegistryObject<MobEffect> EM_OVERLOAD_EFFECT = EFFECTS.register("em_overload_effect", EffectOverload::new);

    public static void register(IEventBus bus) {
        EFFECTS.register(bus);
    }
}
