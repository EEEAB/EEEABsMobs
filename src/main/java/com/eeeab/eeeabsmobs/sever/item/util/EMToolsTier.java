package com.eeeab.eeeabsmobs.sever.item.util;


import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;

import java.util.function.Supplier;

//工具级别
public enum EMToolsTier implements Tier {
    IMMORTAL_TIER(2, 300, 5.0F, 0.0F, 10, () -> Ingredient.of(ItemInit.IMMORTAL_INGOT.get())),
    NETHERWORLD_KATANA_TIER(4, 3000, 9.0F, 5.0F, 20, () -> Ingredient.of(ItemInit.GHOST_STEEL_INGOT.get())),
    GUARDIAN_AXE_TIER(4, 3000, 9.0F, 4.0F, 15, () -> Ingredient.of(Tags.Items.INGOTS_IRON)),
    DEMOLISHER_TIER(4, 3000, 8.0F, 3.0F, 15, () -> Ingredient.of(Tags.Items.INGOTS_IRON));

    private final int harvestLevel;//挖掘等级
    private final int maxUses;//最大耐久
    private final float efficiency;//效率
    private final float attackDamage;//攻击力:材料级别攻击力+武器本身攻击力+玩家攻击力
    private final int enchantability;//附魔亲和力
    private final LazyLoadedValue<Ingredient> repairMaterial;//修复材料

    EMToolsTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
        this.harvestLevel = harvestLevelIn;
        this.maxUses = maxUsesIn;
        this.efficiency = efficiencyIn;
        this.attackDamage = attackDamageIn;
        this.enchantability = enchantabilityIn;
        this.repairMaterial = new LazyLoadedValue<>(repairMaterialIn);
    }

    public int getUses() {
        return this.maxUses;
    }

    public float getSpeed() {
        return this.efficiency;
    }

    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    public int getLevel() {
        return this.harvestLevel;
    }

    public int getEnchantmentValue() {
        return this.enchantability;
    }

    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }
}
