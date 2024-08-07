package com.eeeab.eeeabsmobs;

import com.eeeab.eeeabsmobs.client.ClientProxy;
import com.eeeab.animate.client.gui.AnimationControllerScreen;
import com.eeeab.eeeabsmobs.sever.ServerProxy;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import com.eeeab.eeeabsmobs.sever.handler.HandlerServerEvent;
import com.eeeab.eeeabsmobs.sever.init.*;
import com.eeeab.eeeabsmobs.sever.integration.curios.CuriosRegistry;
import com.eeeab.eeeabsmobs.sever.util.EMBrewingRecipe;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EEEABMobs.MOD_ID)
@Mod.EventBusSubscriber(modid = EEEABMobs.MOD_ID)
public class EEEABMobs {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "eeeabsmobs";
    public static SimpleChannel NETWORK;
    public static ServerProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public EEEABMobs() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.register(bus);
        BlockInit.register(bus);
        BlockEntityInit.register(bus);
        EntityInit.register(bus);
        CreativeTabInit.register(bus);
        EffectInit.register(bus);
        PotionInit.register(bus);
        ParticleInit.register(bus);
        SoundInit.register(bus);
        MenuInit.register(bus);
        StructuresInit.register(bus);
        bus.<FMLCommonSetupEvent>addListener(this::setup);
        bus.<FMLClientSetupEvent>addListener(this::clientSetup);
        bus.<FMLLoadCompleteEvent>addListener(this::complete);
        PROXY.init(bus);
    }

    private void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new HandlerServerEvent());
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, HandlerCapability::attachEntityCapability);
        PROXY.registerMessage();
        event.enqueueWork(() -> {
            CuriosRegistry.register();
            BrewingRecipeRegistry.addRecipe(new EMBrewingRecipe(Potions.STRONG_STRENGTH, ItemInit.HEART_OF_PAGAN.get(), PotionInit.FRENZY_POTION.get()));
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            CuriosRegistry.clientRegister();
            MenuScreens.register(MenuInit.ANIMATION_CONTROLLER.get(), AnimationControllerScreen::new);
        });
    }

    private void complete(final FMLLoadCompleteEvent event) {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        PROXY.loadComplete(bus);
    }
}
