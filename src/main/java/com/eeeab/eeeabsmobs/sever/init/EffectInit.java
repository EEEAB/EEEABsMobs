package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.potion.EffectErode;
import com.eeeab.eeeabsmobs.sever.potion.EffectArmorLower;
import com.eeeab.eeeabsmobs.sever.potion.EffectVertigo;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EffectInit {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, EEEABMobs.MOD_ID);

    //掉护甲耐久
    public static final RegistryObject<MobEffect> ERODE_EFFECT = EFFECTS.register("erode_effect", EffectErode::new);

    //护甲无效化
    public static final RegistryObject<MobEffect> ARMOR_LOWER_EFFECT = EFFECTS.register("armor_lower_effect", () ->
            new EffectArmorLower(MobEffectCategory.HARMFUL, 13382297, false, -5.0D).
                    addAttributeModifier(Attributes.ARMOR, "0eacf76e-1e90-42e6-9479-2a5af12dd6cf",
                            0.0D, AttributeModifier.Operation.ADDITION));

    //眩晕
    public static final RegistryObject<MobEffect> VERTIGO_EFFECT = EFFECTS.register("vertigo_effect", () ->
            new EffectVertigo(MobEffectCategory.HARMFUL, -13382297, false));


    public static void register(IEventBus bus) {
        EFFECTS.register(bus);
    }
}
