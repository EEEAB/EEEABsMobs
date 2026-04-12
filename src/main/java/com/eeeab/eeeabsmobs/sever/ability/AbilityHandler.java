package com.eeeab.eeeabsmobs.sever.ability;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.ability.abilities.*;
import com.eeeab.eeeabsmobs.sever.capability.AbilityCapability;
import com.eeeab.eeeabsmobs.sever.handler.CapabilityHandler;
import com.eeeab.eeeabsmobs.sever.message.PlayerUseAbilityMessage;
import com.eeeab.eeeabsmobs.sever.message.UseAbilityMessage;
import com.eeeab.eeeabsmobs.sever.util.TranslateUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;

public enum AbilityHandler {
    INSTANCE;
    public static final AbilityType<Player, ChainswordComboAbility> CHAINSWORD_COMBO_ABILITY = new AbilityType<>(ChainswordComboAbility::new, "chainsword_combo_ability", true);
    public static final AbilityType<Player, SoulSummonNecklaceAbility> SOUL_SUMMON_NECKLACE_ABILITY = new AbilityType<>(SoulSummonNecklaceAbility::new, "soul_summon_necklace_ability", true);
    public static final AbilityType<Player, ImmortalStaffAbility> IMMORTAL_STAFF_ABILITY = new AbilityType<>(ImmortalStaffAbility::new, "immortal_staff_ability");
    public static final AbilityType<Player, GuardianLaserAbility> GUARDIAN_LASER_ABILITY = new AbilityType<>(GuardianLaserAbility::new, "guardian_laser_ability");
    public static final AbilityType<Player, SkyfallHammerAbility> SKYFALL_HAMMER_ABILITY = new AbilityType<>(SkyfallHammerAbility::new, "skyfall_hammer_ability");
    public static final AbilityType<Player, DoomboltAxeAbility> DOOMBOLT_AXE_ABILITY = new AbilityType<>(DoomboltAxeAbility::new, "doombolt_axe_ability");
    public static final AbilityType<Player, GuardianAxeAbility> GUARDIAN_AXE_ABILITY = new AbilityType<>(GuardianAxeAbility::new, "guardian_axe_ability");
    public static final AbilityType<Player, BusterGauntletAbility> BUSTER_GAUNTLET_ABILITY = new AbilityType<>(BusterGauntletAbility::new, "buster_gauntlet_ability");
    public static final AbilityType<Player, ? extends Ability<?>>[] PLAYER_ABILITY_TYPES = new AbilityType[]{
            CHAINSWORD_COMBO_ABILITY,
            SOUL_SUMMON_NECKLACE_ABILITY,
            IMMORTAL_STAFF_ABILITY,
            GUARDIAN_LASER_ABILITY,
            SKYFALL_HAMMER_ABILITY,
            GUARDIAN_AXE_ABILITY,
            DOOMBOLT_AXE_ABILITY,
            BUSTER_GAUNTLET_ABILITY
    };

    public AbilityCapability.IAbilityCapability getAbilityCapability(LivingEntity entity) {
        return CapabilityHandler.getCapability(entity, CapabilityHandler.ABILITY_CAPABILITY);
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
                player.displayClientMessage(TranslateUtils.simpleText(TranslateUtils.OTHER_PREFIX, "cooling", null), true);
            }
            if (instance.canUse()) {
                abilityCapability.onActive(entity, abilityType);
                EEEABMobs.NETWORK.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new UseAbilityMessage(entity, ArrayUtils.indexOf(abilityCapability.getAbilityTypeByEntity(entity), abilityType)));
            }
        }
    }

    public <T extends Player> void sendPlayerAbilityMessage(T entity, AbilityType<?, ?> ability) {
        if (!(entity.level().isClientSide && entity instanceof LocalPlayer)) {
            return;
        }
        AbilityCapability.IAbilityCapability abilityCapability = getAbilityCapability(entity);
        if (abilityCapability != null) {
            EEEABMobs.NETWORK.sendToServer(new PlayerUseAbilityMessage(ArrayUtils.indexOf(abilityCapability.getAbilityTypeByEntity(entity), ability)));
        }
    }
}
