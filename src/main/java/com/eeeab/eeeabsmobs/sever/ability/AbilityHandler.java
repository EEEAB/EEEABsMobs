package com.eeeab.eeeabsmobs.sever.ability;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.ability.abilities.GuardianAxeAbility;
import com.eeeab.eeeabsmobs.sever.ability.abilities.GuardianLaserAbility;
import com.eeeab.eeeabsmobs.sever.ability.abilities.HowitzerAbility;
import com.eeeab.eeeabsmobs.sever.ability.abilities.ImmortalStaffAbility;
import com.eeeab.eeeabsmobs.sever.capability.AbilityCapability;
import com.eeeab.eeeabsmobs.sever.handler.HandlerCapability;
import com.eeeab.eeeabsmobs.sever.message.MessagePlayerUseAbility;
import com.eeeab.eeeabsmobs.sever.message.MessageUseAbility;
import com.eeeab.eeeabsmobs.sever.util.EMTUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;

public enum AbilityHandler {
    INSTANCE;
    public static final AbilityType<Player, ImmortalStaffAbility> IMMORTAL_STAFF_ABILITY_TYPE = new AbilityType<>(ImmortalStaffAbility::new, "immortal_staff_ability");
    public static final AbilityType<Player, GuardianLaserAbility> GUARDIAN_LASER_ABILITY_TYPE = new AbilityType<>(GuardianLaserAbility::new, "guardian_laser_ability");
    public static final AbilityType<Player, GuardianAxeAbility> GUARDIAN_AXE_ABILITY_TYPE = new AbilityType<>(GuardianAxeAbility::new, "guardian_axe_ability");
    public static final AbilityType<Player, HowitzerAbility> HOWITZER_ABILITY_TYPE = new AbilityType<>(HowitzerAbility::new, "howitzer_ability");
    public static final AbilityType<Player, ? extends Ability<?>>[] PLAYER_ABILITY_TYPES = new AbilityType[]{
            IMMORTAL_STAFF_ABILITY_TYPE,
            GUARDIAN_LASER_ABILITY_TYPE,
            GUARDIAN_AXE_ABILITY_TYPE,
            HOWITZER_ABILITY_TYPE
    };

    public AbilityCapability.IAbilityCapability getAbilityCapability(LivingEntity entity) {
        return HandlerCapability.getCapability(entity, HandlerCapability.ABILITY_CAPABILITY);
    }

    public Ability<?> getAbility(Player player, AbilityType<?, ?> abilityType) {
        AbilityCapability.IAbilityCapability capability = getAbilityCapability(player);
        if (capability != null) {
            return capability.getAbilitiesMap().get(abilityType);
        }
        return null;
    }

    public <T extends LivingEntity> void sendAbilityMessage(T entity, AbilityType<?, ?> abilityType) {
        if (entity.level().isClientSide) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            Ability<?> instance = abilityCapability.getAbilitiesMap().get(abilityType);
            if (instance == null) return;
            if (instance.isCooling() && entity instanceof Player player) {
                player.displayClientMessage(EMTUtils.simpleText(EMTUtils.OTHER_PREFIX, "cooling", null), true);
            }
            if (instance.canUse()) {
                abilityCapability.onActive(entity, abilityType);
                EEEABMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MessageUseAbility(entity, ArrayUtils.indexOf(abilityCapability.getAbilityTypeByEntity(entity), abilityType)));
            }
        }
    }

    public <T extends Player> void sendPlayerAbilityMessage(T entity, AbilityType<?, ?> ability) {
        if (!(entity.level().isClientSide && entity instanceof LocalPlayer)) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            EEEABMobs.NETWORK.sendToServer(new MessagePlayerUseAbility(ArrayUtils.indexOf(abilityCapability.getAbilityTypeByEntity(entity), ability)));
        }
    }
}
