package com.eeeab.eeeabsmobs.sever.handler;

import com.eeeab.eeeabsmobs.sever.capability.AbilityCapability;
import com.eeeab.eeeabsmobs.sever.capability.VertigoCapability;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nullable;

//自定义能力处理器
public class HandlerCapability {

    public static final Capability<VertigoCapability.IVertigoCapability> MOVING_CONTROLLER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<AbilityCapability.IAbilityCapability> CUSTOM_ABILITY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(VertigoCapability.IVertigoCapability.class);
        event.register(AbilityCapability.IAbilityCapability.class);
    }

    @Nullable
    public static <T> T getCapability(Entity entity, Capability<T> capability) {
        if (entity == null) return null;
        if (!entity.isAlive()) return null;
        return entity.getCapability(capability).isPresent() ? entity.getCapability(capability).orElseThrow(() -> new IllegalArgumentException("Lazy optional must not be empty")) : null;
    }

    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof LivingEntity) {
            event.addCapability(VertigoCapability.ID, new VertigoCapability.VertigoCapabilityProvider());
            if (entity instanceof Player) {
                event.addCapability(AbilityCapability.ID, new AbilityCapability.AbilityCapabilityProvider());
            }
        }
    }
}
