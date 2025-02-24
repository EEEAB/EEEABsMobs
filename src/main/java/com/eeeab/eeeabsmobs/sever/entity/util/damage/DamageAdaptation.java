package com.eeeab.eeeabsmobs.sever.entity.util.damage;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.config.EMConfigHandler;
import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
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
 * @version 1.8
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
     * 适应绕过伤害上限的伤害(例如:虚空与指令伤害)
     */
    private boolean adaptBypassesDamage;
    /**
     * 是否为帧伤保护
     */
    private boolean intervalProtectorFlag;
    private final Map<String, DamageInfo> adaptMap = new ConcurrentHashMap<>();

    public DamageAdaptation(int adaptDamageTypesCount, int resetCountdown, float singleAdaptFactor, float maxAdaptFactor) {
        this.adaptDamageTypesCount = adaptDamageTypesCount;
        this.resetCountdown = resetCountdown;
        this.singleAdaptFactor = singleAdaptFactor;
        this.maxAdaptFactor = maxAdaptFactor;
    }

    public DamageAdaptation(EMConfigHandler.DamageSourceAdaptConfig config) {
        this.adaptDamageTypesCount = config.maxDamageSourceAdaptCount.get();
        this.resetCountdown = config.resetCountdown.get() * 20;
        this.singleAdaptFactor = config.singleAdaptFactor.get().floatValue();
        this.maxAdaptFactor = config.maxAdaptFactor.get().floatValue();
        this.adaptBypassesDamage = config.adaptBypassesDamage.get();
    }

    public DamageAdaptation adaptBypassesDamage() {
        this.adaptBypassesDamage = true;
        return this;
    }

    public DamageAdaptation intervalProtector() {
        this.intervalProtectorFlag = true;
        return this;
    }

    public float damageAfterAdaptingOnce(LivingEntity entity, @Nullable DamageSource source, float amount) {
        //检查是否适应伤害
        if (maxAdaptFactor <= 0F || singleAdaptFactor <= 0F) return amount;
        String key;
        if (intervalProtectorFlag) {
            key = "interval_protector";
        } else {
            key = getKey(source, adaptBypassesDamage);
        }
        if (key == null) return amount;
        try {
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
        String key = getKey(source, adaptBypassesDamage);
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

    private static @Nullable String getKey(@Nullable DamageSource source, boolean adaptBypassesDamage) {
        if (source == null) {
            return "unknown_source";
        } else {
            //避免荆棘伤害导致适应玩家持有武器问题
            if (source.is(DamageTypes.THORNS)) return "thorns";
            if (source.getEntity() == null && !source.is(EMTagKey.BYPASSES_DAMAGE_CAP)) {
                return spliceCharacters(source.type().msgId(), "unknown_entity");
            } else if (source.getEntity() != null) {
                Entity entity = source.getEntity();
                String id = entity.getType().getDescriptionId();
                String key = id;
                if (entity instanceof Player player) {
                    if (source.getEntity() == source.getDirectEntity()) {
                        key = spliceCharacters(id, player.getMainHandItem().getDescriptionId());
                    } else {
                        key = spliceCharacters(id, player.getItemInHand(player.getUsedItemHand()).getDescriptionId());
                    }
                }
                return key;
            } else {
                return adaptBypassesDamage ? spliceCharacters(source.type().msgId(), "bypasses_source") : null;
            }
        }
    }

    private static String spliceCharacters(String str1, String str2) {
        return str1 + ":" + str2;
    }
}
