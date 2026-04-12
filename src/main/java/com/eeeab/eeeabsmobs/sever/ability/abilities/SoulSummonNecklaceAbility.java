package com.eeeab.eeeabsmobs.sever.ability.abilities;

import com.eeeab.eeeabsmobs.sever.ability.Ability;
import com.eeeab.eeeabsmobs.sever.ability.AbilityPeriod;
import com.eeeab.eeeabsmobs.sever.ability.AbilityType;
import com.eeeab.eeeabsmobs.sever.entity.mob.corpse.EntityCorpseToPlayer;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.EntityInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import com.eeeab.eeeabsmobs.sever.integration.curios.ICuriosApi;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class SoulSummonNecklaceAbility extends Ability<Player> {
    private float cumulativeDamage;
    private int coolingTime;

    public SoulSummonNecklaceAbility(AbilityType<Player, ? extends Ability<?>> abilityType, Player user) {
        super(abilityType, user, new AbilityPeriod[0], 0);
    }

    @Override
    public void tick() {
        if (this.coolingTime > 0) {
            this.coolingTime--;
        }
    }

    public void hurt(Player player, DamageSource source, float damage) {
        Entity entity = source.getEntity();
        if (this.coolingTime <= 0 && entity != null) {
            Item item = ItemInit.SOUL_SUMMON_NECKLACE.get();
            if (ICuriosApi.isLoaded()) {
                if (ICuriosApi.INSTANCE.isPresentInventory(player, item)) {
                    this.doSSNCapability(player, entity, item, damage);
                }
            } else if (this.ifPresentItem(player, item.getDefaultInstance())) {
                this.doSSNCapability(player, entity, item, damage);
            }
        }
    }

    private void doSSNCapability(Player player, Entity target, Item item, float damage) {
        if (!player.getCooldowns().isOnCooldown(item)) {
            this.cumulativeDamage += damage;
            if (this.cumulativeDamage >= ModConfigHandler.COMMON.items.summoningSoulNecklaceConfig1.get().floatValue()) {
                player.getCooldowns().addCooldown(item, (int) (ModConfigHandler.COMMON.items.summoningSoulNecklaceConfig2.get() * 20));
                if (!player.level().isClientSide) {
                    Vec3 vec3 = player.position();
                    EntityCorpseToPlayer entity = EntityInit.CORPSE_TO_PLAYER.get().create(player.level());
                    if (entity != null) {
                        entity.finalizeSpawn((ServerLevel) player.level(), player.level().getCurrentDifficultyAt(BlockPos.containing(vec3.x, vec3.y, vec3.z)), MobSpawnType.MOB_SUMMONED, null, null);
                        entity.moveTo(vec3);
                        entity.setOwner(player);
                        if (player != target && target instanceof LivingEntity livingEntity)
                            entity.setTarget(livingEntity);
                        player.level().addFreshEntity(entity);
                        entity.afterSpawn();
                    }
                }
                this.coolingTime = 10;
                this.cumulativeDamage = 0;
            }
        }
    }

    private boolean ifPresentItem(Player player, ItemStack itemStack) {
        return player.getInventory().contains(itemStack);
    }

    @Override
    public CompoundTag writeNBT() {
        CompoundTag tag = super.writeNBT();
        tag.putFloat("cumulativeDamage", this.cumulativeDamage);
        tag.putInt("coolingTime", this.coolingTime);
        return tag;
    }

    @Override
    public void readNBT(Tag nbt) {
        super.readNBT(nbt);
        CompoundTag compoundTag = (CompoundTag) nbt;
        this.cumulativeDamage = compoundTag.getFloat("cumulativeDamage");
        this.coolingTime = compoundTag.getInt("coolingTime");
    }
}
