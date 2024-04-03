package com.eeeab.eeeabsmobs;

import com.eeeab.eeeabsmobs.client.ClientProxy;
import com.eeeab.eeeabsmobs.sever.ServerProxy;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import com.eeeab.eeeabsmobs.sever.handler.HandlerServerEvent;
import com.eeeab.eeeabsmobs.sever.init.*;
import com.eeeab.eeeabsmobs.sever.util.EMBrewingRecipe;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

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
        EffectInit.register(bus);
        PotionInit.register(bus);
        ParticleInit.register(bus);
        SoundInit.register(bus);
        StructuresInit.register(bus);

        bus.<FMLCommonSetupEvent>addListener(this::setup);
        bus.<InterModEnqueueEvent>addListener(this::modQueue);
        bus.<FMLLoadCompleteEvent>addListener(this::complete);

        PROXY.init(bus);
        bus.addListener(HandlerCapability::registerCapabilities);
        bus.addListener(PROXY::register);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new HandlerServerEvent());
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, HandlerCapability::attachEntityCapability);

    }

    private void setup(final FMLCommonSetupEvent event) {
        PROXY.registerMessage();
        event.enqueueWork(() -> {
            BrewingRecipeRegistry.addRecipe(new EMBrewingRecipe(Potions.STRONG_STRENGTH, ItemInit.HEART_OF_PAGAN.get(), PotionInit.FRENZY_POTION.get()));
        });
    }

    private void modQueue(final InterModEnqueueEvent event) {
        List<IModInfo> mods = ModList.get().getMods();
        //mods.stream().map(IModInfo::getModId).forEach(EEEABMobs.LOGGER::info);
    }

    private void complete(FMLLoadCompleteEvent event) {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        PROXY.loadComplete(bus);
    }
}
