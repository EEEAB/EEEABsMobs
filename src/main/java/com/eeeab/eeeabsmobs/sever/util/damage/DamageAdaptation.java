package com.eeeab.eeeabsmobs.sever.util.damage;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 伤害适应
 *
 * @author EEEAB
 * @version 1.6
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
    private static final RandomSource random = RandomSource.create();
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
            }
            //当适应类型个数超出上限时 先尝试删除失效的适应伤害 再随机移除一种适应伤害
            if (adaptMap.size() >= adaptDamageTypesCount) {
                updateCache(entity);
                if (adaptMap.size() >= adaptDamageTypesCount) {
                    List<String> keys = adaptMap.keySet().stream().toList();
                    adaptMap.remove(keys.get(random.nextInt(keys.size())));
                }
            }
            adaptMap.put(key, new DamageInfo(tickStamp, 0));
        } catch (Exception e) {
            EEEABMobs.LOGGER.error("An unexpected exception occurred when calculating damage: {}", e.getMessage());
        }
        return amount;
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
        } else if (source.getEntity() == null && !source.is(EMTagKey.GENERAL_UNRESISTANT_TO)) {
            return spliceCharacters(source.type().msgId(), "unknown_entity");
        } else if (source.getEntity() != null) {
            Entity entity = source.getEntity();
            String id = adaptsSameTypeMobs ? entity.getType().getDescriptionId() : entity.getStringUUID();
            String key = id;
            if (entity instanceof Player player) {
                InteractionHand hand = player.getUsedItemHand();
                key = spliceCharacters(id, player.getItemInHand(hand).getItem().getDescriptionId());
            }
            return key;
        } else {
            return adaptBypassesDamage ? spliceCharacters(source.type().msgId(), "bypasses_source") : null;
        }
    }

    private static String spliceCharacters(String str1, String str2) {
        return str1 + ":" + str2;
    }
}
