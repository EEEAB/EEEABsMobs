package com.eeeab.eeeabsmobs.sever.init;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.animate.server.inventory.AnimationControllerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuInit {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, EEEABMobs.MOD_ID);

    public static final RegistryObject<MenuType<AnimationControllerMenu>> ANIMATION_CONTROLLER = REGISTRY.register("animation_controller", () -> IForgeMenuType.create(AnimationControllerMenu::new));

    public static void register(IEventBus bus) {
        REGISTRY.register(bus);
    }
}
