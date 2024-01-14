package com.eeeab.eeeabsmobs;

import com.eeeab.eeeabsmobs.client.ClientProxy;
import com.eeeab.eeeabsmobs.sever.ServerProxy;
import com.eeeab.eeeabsmobs.sever.handler.HandlerServerEvent;
import com.eeeab.eeeabsmobs.sever.util.EETabGroup;
import com.eeeab.eeeabsmobs.sever.init.*;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import com.eeeab.eeeabsmobs.sever.init.StructuresInit;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
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
        EETabGroup.register(bus);
        EffectInit.register(bus);
        ParticleInit.register(bus);
        SoundInit.register(bus);
        StructuresInit.register(bus);

        PROXY.init(bus);
        bus.<FMLCommonSetupEvent>addListener(this::init);
        bus.<FMLLoadCompleteEvent>addListener(this::init);
        bus.addListener(HandlerCapability::registerCapabilities);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new HandlerServerEvent());
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, HandlerCapability::attachEntityCapability);

    }

    public void init(final FMLCommonSetupEvent event) {
        PROXY.registerMessage();
    }


    private void init(FMLLoadCompleteEvent event) {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        PROXY.onLateInit(bus);
    }
}
