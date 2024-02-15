package com.eeeab.eeeabsmobs.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class ServerLoadMixin {
    @Inject(at = @At("HEAD"),method = "loadLevel")
    private void init(CallbackInfo info){
        //System.out.println("minix test...");
    }
}