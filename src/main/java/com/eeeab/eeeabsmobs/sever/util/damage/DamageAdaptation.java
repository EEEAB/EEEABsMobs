package com.eeeab.eeeabsmobs.sever.util.damage;

import com.eeeab.eeeabsmobs.sever.util.EMTagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 伤害适应
 *
 * @author EEEAB
 * @version 1.1
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

    public DamageAdaptation(int adaptDamageTypesCount, int resetCountdown, float singleAdaptFactor, float maxAdaptFactor, boolean adaptsSameTypeMobs) {
        this.adaptDamageTypesCount = adaptDamageTypesCount;
        this.resetCountdown = resetCountdown;
        this.singleAdaptFactor = singleAdaptFactor;
        this.maxAdaptFactor = maxAdaptFactor;
        this.adaptsSameTypeMobs = adaptsSameTypeMobs;
    }

    public void tick(LivingEntity entity) {
        if (entity.tickCount % (this.resetCountdown / 1000 * 20) == 0) {
            this.updateCache();
        }
    }

    public float damageAfterAdaptingOnce(@Nullable DamageSource source, float amount) {
        String key = getKey(source, this.adaptsSameTypeMobs);
        if (key == null) {
            return amount;
        }
        DamageInfo info = ADAPT_MAP.getOrDefault(key, null);
        long systemMs = System.currentTimeMillis();
        if (info != null) {
            // 如果当前时间超过最后受伤时间加上重置倒计时，则重置该伤害类型
            if (systemMs - info.getTimestamp() > resetCountdown) {
                ADAPT_MAP.remove(key);
            } else {
                float newAdaptFactor = Math.min(info.getAdaptFactor() + singleAdaptFactor, maxAdaptFactor);
                float adaptedAmount = amount * (1 - newAdaptFactor);
                //累计适应因子 更新时间戳
                info.setAdaptFactor(newAdaptFactor);
                info.setTimestamp(systemMs);
                ADAPT_MAP.put(key, info);
                return Math.max(adaptedAmount, 0);
            }
        }
        if (ADAPT_MAP.size() > adaptDamageTypesCount) {
            List<String> keys = ADAPT_MAP.keySet().stream().toList();
            ADAPT_MAP.remove(keys.get(random.nextInt(keys.size())));
            return amount;
        }
        ADAPT_MAP.put(key, new DamageInfo(systemMs, singleAdaptFactor));
        return amount;
    }

    public void updateCache() {
        Iterator<Map.Entry<String, DamageInfo>> iterator = ADAPT_MAP.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, DamageInfo> entry = iterator.next();
            long systemMs = System.currentTimeMillis();
            if (systemMs - entry.getValue().getTimestamp() > resetCountdown) {
                iterator.remove();
            }
        }
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
