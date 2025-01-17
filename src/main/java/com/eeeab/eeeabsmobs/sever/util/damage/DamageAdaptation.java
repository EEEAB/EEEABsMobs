package com.eeeab.eeeabsmobs.sever.util.damage;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 伤害适应
 *
 * @author EEEAB
 * @version 1.7
 */
public class DamageAdaptation {
    /**
     * 能够适应伤害类型数量
     */
    private final int adaptDamageTypesCount;
    /**
     * 重置适应倒计时 ms
     */
    private final int resetCountdown;
    /**
     * 每次受伤所适应值
     */
    private final float singleAdaptFactor;
    /**
     * 最大累计适应值
     */
    private final float maxAdaptFactor;
    /**
     * 是否适应相同类型生物
     */
    private final boolean adaptsSameTypeMobs;
    private final Map<String, DamageInfo> adaptMap = new ConcurrentHashMap<>();
    private boolean adaptBypassesDamage;

    public DamageAdaptation(int adaptDamageTypesCount, int resetCountdown, float singleAdaptFactor, float maxAdaptFactor, boolean adaptsSameTypeMobs) {
        this.adaptDamageTypesCount = adaptDamageTypesCount;
        this.resetCountdown = resetCountdown;
        this.singleAdaptFactor = singleAdaptFactor;
        this.maxAdaptFactor = maxAdaptFactor;
        this.adaptsSameTypeMobs = adaptsSameTypeMobs;
    }

    public DamageAdaptation(EMConfigHandler.DamageSourceAdaptConfig config) {
        this.adaptDamageTypesCount = config.maxDamageSourceAdaptCount.get();
        this.resetCountdown = config.resetCountdown.get() * 20;
        this.singleAdaptFactor = config.singleAdaptFactor.get().floatValue();
        this.maxAdaptFactor = config.maxAdaptFactor.get().floatValue();
        this.adaptBypassesDamage = config.adaptBypassesDamage.get();
        this.adaptsSameTypeMobs = config.adaptsSameTypeMobs;
    }

    public DamageAdaptation setAdaptBypassesDamage(boolean adaptBypassesDamage) {
        this.adaptBypassesDamage = adaptBypassesDamage;
        return this;
    }

    public float damageAfterAdaptingOnce(LivingEntity entity, @Nullable DamageSource source, float amount) {
        //检查是否适应伤害
        if (maxAdaptFactor <= 0F || singleAdaptFactor <= 0F) return amount;
        try {
            String key = getKey(source, adaptsSameTypeMobs, adaptBypassesDamage);
            if (key == null) return amount;

            DamageInfo info = adaptMap.getOrDefault(key, null);
            long tickStamp = entity.tickCount;
            if (info != null) {
                //判断适应伤害是否失效
                if (tickStamp > info.getTimestamp() + resetCountdown) {
                    adaptMap.remove(key);
                } else {
                    float newAdaptFactor = Math.min(info.getAdaptFactor() + singleAdaptFactor, maxAdaptFactor);
                    float adaptedAmount = amount * (1F - newAdaptFactor);
                    //累计适应因子 更新刻度戳
                    info.setAdaptFactor(newAdaptFactor);
                    info.setTimestamp(tickStamp);
                    adaptMap.put(key, info);
                    return Math.max(adaptedAmount, 0);
                }
            } else if (adaptMap.size() >= adaptDamageTypesCount) {
                updateCache(entity);
                return amount;
            }
            adaptMap.put(key, new DamageInfo(tickStamp, 0));
        } catch (Exception e) {
            EEEABMobs.LOGGER.error("An unexpected exception occurred when calculating damage: {}", e.getMessage());
        }
        return amount;
    }


    public float getAdaptFactorTotalBySource(LivingEntity entity, @Nullable DamageSource source) {
        String key = getKey(source, adaptsSameTypeMobs, adaptBypassesDamage);
        if (key == null) return -1F;
        DamageInfo damageInfo = adaptMap.get(key);
        if (damageInfo != null) {
            return entity.tickCount > damageInfo.getTimestamp() + resetCountdown ? -1F : damageInfo.getAdaptFactor();
        }
        return -1F;
    }

    public boolean isFullyAdapted(LivingEntity entity, @Nullable DamageSource source) {
        return maxAdaptFactor > 0 && getAdaptFactorTotalBySource(entity, source) >= maxAdaptFactor;
    }

    public void updateCache(LivingEntity entity) {
        adaptMap.entrySet().removeIf(entry -> entity.tickCount > entry.getValue().getTimestamp() + resetCountdown);
    }

    public void clearCache() {
        adaptMap.clear();
    }

    private static @Nullable String getKey(@Nullable DamageSource source, boolean adaptsSameTypeMobs, boolean adaptBypassesDamage) {
        if (source == null) {
            return "unknown_source";
        } else if (source.getEntity() == null && !(source == DamageSource.OUT_OF_WORLD || source == DamageSource.GENERIC)) {
            return spliceCharacters(source.msgId, "unknown_entity");
        } else if (source.getEntity() != null) {
            //避免荆棘伤害导致适应玩家持有武器问题
            if (source instanceof EntityDamageSource entityDamageSource && entityDamageSource.isThorns()) return null;
            Entity entity = source.getEntity();
            String id = adaptsSameTypeMobs ? entity.getType().getDescriptionId() : entity.getStringUUID();
            String key = id;
            if (entity instanceof Player player) {
                InteractionHand hand = player.getUsedItemHand();
                key = spliceCharacters(id, player.getItemInHand(hand).getItem().getDescriptionId());
            }
            return key;
        } else {
            return adaptBypassesDamage ? spliceCharacters(source.msgId, "bypasses_source") : null;
        }
    }

    private static String spliceCharacters(String str1, String str2) {
        return str1 + ":" + str2;
    }
}
