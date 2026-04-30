package com.eeeab.eeeabsmobs.sever.world.datagen.loot;

import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.stream.Stream;

public class ModEntityLootTables extends EntityLootSubProvider {
    public ModEntityLootTables() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
        add(EntityInit.RELIC_OBSERVER.get(), LootTable.lootTable().withPool(getBoundaryBrickLootTable(0, 1, 0.75F)));
        add(EntityInit.RELIC_RIPPER.get(), LootTable.lootTable().withPool(getBoundaryBrickLootTable(0, 1, 1)));
        LootPoolSingletonContainer.Builder<?> ancientDriveCrystalBuilder = LootItem.lootTableItem(ItemInit.ANCIENT_DRIVE_CRYSTAL.get());
        add(EntityInit.RELIC_EARTHSHAKER.get(), LootTable.lootTable().withPool(getBoundaryBrickLootTable(1, 2, 1))
                .withPool(LootPool.lootPool().add(ancientDriveCrystalBuilder
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                )));
        add(EntityInit.RELIC_ANNIHILATOR.get(), LootTable.lootTable().withPool(getBoundaryBrickLootTable(1, 3, 1))
                .withPool(LootPool.lootPool().add(ancientDriveCrystalBuilder
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 0.5F)))
                ))
                .withPool(LootPool.lootPool().add(LootItem.lootTableItem(ItemInit.CHAIN_GEAR.get())
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1F)))
                )).withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ItemInit.RELICRON_UPGRADE_SMITHING_TEMPLATE.get()))
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.01F))
                )
        );
        add(EntityInit.REALM_WARDEN.get(), LootTable.lootTable().withPool(getBoundaryBrickLootTable(2, 3, 1))
                .withPool(LootPool.lootPool().add(ancientDriveCrystalBuilder
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))
                ))
                .withPool(LootPool.lootPool().add(LootItem.lootTableItem(ItemInit.GUARDIAN_CUBE.get())
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1F)))
                ))
                .withPool(LootPool.lootPool().add(LootItem.lootTableItem(ItemInit.THE_ONLY_EDICT_MUSIC_DISC.get())
                        .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.5F, 0.1F))
                )).withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(ItemInit.RELICRON_UPGRADE_SMITHING_TEMPLATE.get()))
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.025F, 0.01F))
                )
        );


    }

    public LootPool.Builder getBoundaryBrickLootTable(float minCount, float maxCount, float maxEnchantMultiplier) {
        return LootPool.lootPool()
                .add(LootItem.lootTableItem(ItemInit.BOUNDARY_BRICK.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, maxEnchantMultiplier)))
                );
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return Stream.of(
                EntityInit.RELIC_OBSERVER.get(),
                EntityInit.RELIC_RIPPER.get(),
                EntityInit.RELIC_EARTHSHAKER.get(),
                EntityInit.RELIC_ANNIHILATOR.get(),
                EntityInit.REALM_WARDEN.get()
        );
    }
}
