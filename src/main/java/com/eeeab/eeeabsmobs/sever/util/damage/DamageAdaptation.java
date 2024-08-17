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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 伤害适应
 *
 * @author EEEAB
 * @version 1.3
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
    private final Map<String, DamageInfo> ADAPT_MAP = new HashMap<>();
    private boolean hurting;

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
        this.adaptsSameTypeMobs = config.adaptsSameTypeMobs;
    }

    public void tick(LivingEntity entity) {
        if (!this.hurting && entity.tickCount % resetCountdown == 0) {
            this.updateCache(entity);
        }
    }

    public float damageAfterAdaptingOnce(LivingEntity entity, @Nullable DamageSource source, float amount) {
        try {
            this.hurting = true;
            String key = getKey(source, this.adaptsSameTypeMobs);
            if (key == null) {
                return amount;
            }
            DamageInfo info = ADAPT_MAP.getOrDefault(key, null);
            long tickStamp = entity.tickCount;
            if (info != null) {
                //判断适应伤害是否失效
                if (tickStamp > info.getTimestamp() + resetCountdown) {
                    ADAPT_MAP.remove(key);
                } else {
                    float newAdaptFactor = Math.min(info.getAdaptFactor() + singleAdaptFactor, maxAdaptFactor);
                    float adaptedAmount = amount * (1F - newAdaptFactor);
                    //累计适应因子 更新刻度戳
                    info.setAdaptFactor(newAdaptFactor);
                    info.setTimestamp(tickStamp);
                    ADAPT_MAP.put(key, info);
                    return Math.max(adaptedAmount, 0);
                }
            }
            //当适应类型个数超出上限时 先尝试删除失效的适应伤害 再随机移除一种适应伤害
            if (ADAPT_MAP.size() >= adaptDamageTypesCount) {
                updateCache(entity);
                if (ADAPT_MAP.size() >= adaptDamageTypesCount) {
                    List<String> keys = ADAPT_MAP.keySet().stream().toList();
                    ADAPT_MAP.remove(keys.get(random.nextInt(keys.size())));
                }
            }
            ADAPT_MAP.put(key, new DamageInfo(tickStamp, singleAdaptFactor));
            return amount;
        } catch (Exception e) {
            EEEABMobs.LOGGER.error("An unexpected exception occurred when calculating damage:", e);
            return amount;
        } finally {
            this.hurting = false;
        }
    }

    public void updateCache(LivingEntity entity) {
        ADAPT_MAP.entrySet().removeIf(entry -> entity.tickCount > entry.getValue().getTimestamp() + resetCountdown);
    }

    public void clearCache() {
        ADAPT_MAP.clear();
    }

    private static @Nullable String getKey(@Nullable DamageSource source, boolean adaptsSameTypeMobs) {
        if (source == null) {
            return "unknown:source";
        } else if (source.getEntity() == null && !source.is(EMTagKey.GENERAL_UNRESISTANT_TO)) {
            return "unknown:entity";
        } else if (source.getEntity() != null) {
            Entity entity = source.getEntity();
            String id = adaptsSameTypeMobs ? entity.getClass().getName() : entity.getStringUUID();
            String key;
            if (entity instanceof Player player) {
                InteractionHand hand = player.getUsedItemHand();
                key = spliceCharacters(id, player.getItemInHand(hand).getItem().getDescriptionId());
            } else {
                key = spliceCharacters(id, source.getMsgId());
            }
            return key;
        } else {
            return null;
        }
    }

    private static String spliceCharacters(String str1, String str2) {
        return str1 + ":" + str2;
    }
}
