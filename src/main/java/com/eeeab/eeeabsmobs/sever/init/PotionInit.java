package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PotionInit {
    private static final DeferredRegister<Potion> POTION = DeferredRegister.create(ForgeRegistries.POTIONS, EEEABMobs.MOD_ID);

    public static final RegistryObject<Potion> FRENZY_POTION = POTION.register("frenzy_potion",
            () -> new Potion(new MobEffectInstance(EffectInit.FRENZY_EFFECT.get(), 1000, 4)));

    public static void register(IEventBus bus) {
        POTION.register(bus);
    }
}
