package com.eeeab.eeeabsmobs.client;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.client.render.util.EEItemStackRenderProperties;
import com.eeeab.eeeabsmobs.sever.ServerProxy;
import com.eeeab.eeeabsmobs.sever.handler.HandlerClientEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends ServerProxy {

    @Override
    public void init(IEventBus bus) {
        super.init(bus);
        MinecraftForge.EVENT_BUS.register(new HandlerClientEvent());
    }

    @Override
    public Object getISTERProperties() {
        return new EEItemStackRenderProperties();
    }
}
