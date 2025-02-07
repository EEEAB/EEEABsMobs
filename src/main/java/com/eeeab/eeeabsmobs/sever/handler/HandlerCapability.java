package com.eeeab.eeeabsmobs.sever.handler;

import com.eeeab.eeeabsmobs.sever.capability.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nullable;

//自定义能力处理器
public class HandlerCapability {
    public static final Capability<VertigoCapability.IVertigoCapability> STUN_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<FrenzyCapability.IFrenzyCapability> FRENZY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<ElectricityCapability.IElectricityCapabilityImpl> ELECTRICITY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<AbilityCapability.IAbilityCapability> ABILITY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<PlayerCapability.IPlayerCapability> PLAYER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<?>[] CAPABILITIES = new Capability[]{
            STUN_CAPABILITY,
            FRENZY_CAPABILITY,
            ELECTRICITY_CAPABILITY,
            ABILITY_CAPABILITY,
            PLAYER_CAPABILITY,
    };

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(AbilityCapability.IAbilityCapability.class);
        event.register(PlayerCapability.IPlayerCapability.class);
        event.register(ElectricityCapability.IElectricityCapabilityImpl.class);
        event.register(VertigoCapability.IVertigoCapability.class);
        event.register(FrenzyCapability.IFrenzyCapability.class);
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
            event.addCapability(FrenzyCapability.ID, new FrenzyCapability.FrenzyCapabilityProvider());
            event.addCapability(ElectricityCapability.ID, new ElectricityCapability.ElectricityCapabilityProvider());
            if (entity instanceof Player) {
                event.addCapability(AbilityCapability.ID, new AbilityCapability.AbilityCapabilityProvider());
                event.addCapability(PlayerCapability.ID, new PlayerCapability.PlayerCapabilityProvider());
            }
        }
    }
}
