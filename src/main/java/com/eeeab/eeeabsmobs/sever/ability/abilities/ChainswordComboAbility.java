package com.eeeab.eeeabsmobs.sever.ability.abilities;

import com.eeeab.eeeabsmobs.sever.ability.Ability;
import com.eeeab.eeeabsmobs.sever.ability.AbilityPeriod;
import com.eeeab.eeeabsmobs.sever.ability.AbilityType;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.item.ItemChainsword;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class ChainswordComboAbility extends Ability<Player> {
    private UUID primary;
    private int primaryTick;
    private int comboCount;
    private String lastEntityType;
    private int lastAttackTime;

    public ChainswordComboAbility(AbilityType<Player, ? extends Ability<?>> type, Player user) {
        super(type, user, new AbilityPeriod[0], 0);
    }

    public void onAttack(LivingEntity target, boolean shouldUpdate) {
        Player player = getUser();
        if (player == null) return;
        ItemStack mainHand = player.getMainHandItem();
        if (!(mainHand.getItem() instanceof ItemChainsword)) return;

        String targetType = target.getType().getDescriptionId();
        int currentTick = player.tickCount;

        if (lastAttackTime > 0 && currentTick > lastAttackTime) {
            int ticksPassed = currentTick - lastAttackTime;
            int reduceBy = ticksPassed / 20;
            if (reduceBy > 0) {
                comboCount = Math.max(0, comboCount - reduceBy);
            }
        }

        if (shouldUpdate) {
            if (lastEntityType != null && lastEntityType.equals(targetType)) {
                comboCount = Math.min(comboCount + 1, ModConfigHandler.COMMON.items.chainswordConfig2.get() + 1);
            } else {
                comboCount = 1;
            }
            lastEntityType = targetType;
            lastAttackTime = currentTick;
        }
    }

    public float getExtraDamageMultiplier() {
        Player player = getUser();
        if (player == null) return 1;
        ItemStack mainHand = player.getMainHandItem();
        if (!(mainHand.getItem() instanceof ItemChainsword)) return 1;
        int count = comboCount;
        return 1 + (Math.max(count - 1, 0)) * (ModConfigHandler.COMMON.items.chainswordConfig1.get().floatValue() * 0.01F);
    }

    public int getComboCount() {
        return comboCount;
    }

    public UUID getPrimary() {
        return primary;
    }

    public void setPrimary(UUID primary) {
        this.primary = primary;
    }

    public int getPrimaryTick() {
        return primaryTick;
    }

    public void setPrimaryTick(int primaryTick) {
        this.primaryTick = primaryTick;
    }
}