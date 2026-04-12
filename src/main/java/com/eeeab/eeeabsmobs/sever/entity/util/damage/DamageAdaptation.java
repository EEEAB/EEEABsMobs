package com.eeeab.eeeabsmobs.sever.entity.util.damage;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.util.ModTagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 伤害适应(未完成)
 *
 * @author EEEAB
 */
public class DamageAdaptation {
    /**
     * 是否开启伤害适应
     */
    private final boolean enabled;
    /**
     * 调试模式
     */
    private final boolean debug;
    /**
     * 在没有完全适应时衰减至0的时间
     */
    private final int attenuatedDuration;
    /**
     * 完全适应后的持续时间
     */
    private final int fullAdaptDuration;
    /**
     * 由实体对其每次伤害所适应值增加系数
     */
    private final float singleAdaptFactor;
    /**
     * 由玩家对其每次伤害所适应值增加系数
     */
    private final float playerSingleAdaptFactor;
    /**
     * 最大累计适应值
     */
    private final float maxAdaptFactor;
    /**
     * 生命值比例峰值
     */
    private final float healthRatioPeak;
    /**
     * 减伤曲线指数
     */
    private static final float CURVE_EXPONENT = 1.5F;
    /**
     * 伤害频率阈值
     */
    private static final int FREQUENCY_THRESHOLD = 20;
    private long lastHitTick;
    private final Map<String, DamageInfo> adaptMap = new ConcurrentHashMap<>();

    public DamageAdaptation(boolean enabled, boolean debug, int fullAdaptDuration, int attenuatedDuration,
                            float singleAdaptFactor, float playerSingleAdaptFactor,
                            float maxAdaptFactor, float healthRatioPeak) {
        this.enabled = enabled;
        this.debug = enabled && debug;
        this.fullAdaptDuration = fullAdaptDuration;
        this.attenuatedDuration = attenuatedDuration;
        this.singleAdaptFactor = singleAdaptFactor;
        this.playerSingleAdaptFactor = playerSingleAdaptFactor;
        this.maxAdaptFactor = maxAdaptFactor;
        this.healthRatioPeak = healthRatioPeak;
    }

    public DamageAdaptation(ModConfigHandler.DamageSourceAdaptConfig config) {
        this(config.enabled.get(), config.debug.get(), config.fullAdaptDuration.get() * 20, config.attenuatedDuration.get() * 20,
                config.singleAdaptFactor.get().floatValue(), config.playerSingleAdaptFactor.get().floatValue(),
                config.maxAdaptFactor.get().floatValue(), 0.5F);
    }

    /**
     * 尝试累计适应值并根据适应值减免伤害
     */
    public float adaptToDamage(LivingEntity entity, @Nullable DamageSource source, float amount) {
        if (!enabled || amount <= 0) return amount;
        String key = getKey(source);
        if (key == null) return amount;

        boolean isPlayer = source != null && (source.getEntity() instanceof Player || source.getDirectEntity() instanceof Player);
        float baseIncrement = (amount / entity.getMaxHealth()) * (isPlayer ? playerSingleAdaptFactor : singleAdaptFactor);
        if (baseIncrement <= 0) return amount;

        float ratioMultiplier = getHealthRatioMultiplier(entity.getHealth() / entity.getMaxHealth()) * 0.5F;
        baseIncrement += ratioMultiplier * baseIncrement;
        float totalIncrement = baseIncrement;

        long currentTick = entity.tickCount;
        long interval = currentTick - lastHitTick;
        totalIncrement += (maxAdaptFactor * (isPlayer ? 0.2F : 0.1F)) * (1 - Math.min(interval / FREQUENCY_THRESHOLD, 1));

        DamageInfo info = adaptMap.get(key);
        if (info != null) {
            if (currentTick - info.getTimestamp() >= fullAdaptDuration) {
                adaptMap.remove(key);
                info = null;
            } else {
                float adaptFactor = info.getAdaptFactor();
                if (adaptFactor < maxAdaptFactor) {
                    float decayedAdapt = getDecayedAdapt(adaptFactor, info.getTimestamp(), currentTick);
                    info.setAdaptFactor(Math.min(decayedAdapt + totalIncrement, maxAdaptFactor));
                }
                lastHitTick = currentTick;
            }
        }

        if (info == null) {
            info = new DamageInfo(currentTick, Math.min(totalIncrement, maxAdaptFactor));
            lastHitTick = currentTick;
        } else {
            info.setTimestamp(currentTick);
        }
        adaptMap.put(key, info);

        float reduction = (float) Math.pow(info.getAdaptFactor() / maxAdaptFactor, CURVE_EXPONENT);
        reduction = Math.min(reduction, maxAdaptFactor);
        if (reduction == maxAdaptFactor) info.setAdaptFactor(reduction);
        if (debug) EEEABMobs.LOGGER.info("伤害：{} 累计适应值：{} 总累计适应值：{} 适应减伤系数：{} 间隔适应值：{} 生命值系数：{}",
                amount, totalIncrement, info.getAdaptFactor(), reduction, totalIncrement - baseIncrement, ratioMultiplier);
        return amount * (1F - reduction);
    }

    //不会处理是否存在过期适应值也不会累计新的适应值
    public void adaptToDamage(int currentTick, @Nullable DamageSource source, float multiplier) {
        if (!enabled) return;
        String key = getKey(source);
        if (key == null) return;
        DamageInfo info = adaptMap.get(key);
        if (info == null) return;
        info.setTimestamp(currentTick);
        info.setAdaptFactor(Math.min(info.getAdaptFactor() + maxAdaptFactor * multiplier, maxAdaptFactor));
    }

    /**
     * @return 根据生命值比例计算额外系数
     */
    private float getHealthRatioMultiplier(float healthRatio) {
        if (healthRatio <= healthRatioPeak) {
            float k = 2F;
            return (float) (Math.log(1 + k * healthRatio) / Math.log(1 + k * healthRatioPeak));
        } else {
            return (1F - healthRatio) / (1F - healthRatioPeak);
        }
    }

    /**
     * @return 在未完全适应时，计算随时间自然衰减的适应值
     */
    private float getDecayedAdapt(float originalAdapt, long lastTick, long currentTick) {
        if (originalAdapt <= 0) return 0F;
        long delta = currentTick - lastTick;
        if (delta <= 0) return originalAdapt;
        //float decay = Math.min(1.0f, (float) delta / attenuatedDuration);
        float decay = Math.min(1F, (float) Math.pow(delta / (double) attenuatedDuration, 1.5F));
        float newAdapt = originalAdapt * (1F - decay);
        return Math.max(0, newAdapt);
    }

    /**
     * @return 获取当前伤害源的适应值
     */
    public float getAdaptFactorTotalBySource(int currentTick, @Nullable DamageSource source) {
        if (!enabled) return 0F;
        String key = getKey(source);
        if (key == null) return 0F;
        DamageInfo info = adaptMap.get(key);
        if (info == null) return 0F;
        if (currentTick - info.getTimestamp() >= fullAdaptDuration) {
            adaptMap.remove(key);
            return 0F;
        }
        return info.getAdaptFactor();
    }

    /**
     * @return 检查当前伤害源是否被完全适应
     */
    public boolean isFullyAdapted(LivingEntity entity, @Nullable DamageSource source) {
        float adapt = getAdaptFactorTotalBySource(entity.tickCount, source);
        return adapt >= maxAdaptFactor;
    }

    public void updateCache(LivingEntity entity) {
        if (!enabled) return;
        long currentTick = entity.tickCount;
        adaptMap.entrySet().removeIf(entry -> currentTick - entry.getValue().getTimestamp() >= fullAdaptDuration);
    }

    public void clearCache() {
        adaptMap.clear();
    }

    private static @Nullable String getKey(@Nullable DamageSource source) {
        if (source == null) return "unknown_source";
        if (source.is(ModTagKey.BYPASSES_DAMAGE_ADAPT)) return null;
        if (source.is(DamageTypes.THORNS)) return "thorns";
        Entity entity = source.getEntity();
        if (entity == null) {
            return spliceCharacters(source.type().msgId(), "unknown_entity");
        }
        String descId = entity.getType().getDescriptionId();
        if (entity instanceof Player player) {
            Entity directEntity = source.getDirectEntity();
            return spliceCharacters(descId, getSourceForDamage(player, directEntity));
        }
        return descId;
        //return adaptBypassesDamage ? spliceCharacters(source.type().msgId(), "bypasses_source") : null;
    }

    private static String getSourceForDamage(Player player, Entity direct) {
        if (direct == null) return "unknown_entity";
        if (player == direct) return player.getMainHandItem().getDescriptionId();
        return direct.getType().getDescriptionId();
    }

    private static String spliceCharacters(String str1, String str2) {
        return str1 + ":" + str2;
    }

    private static class DamageInfo {
        private long timestamp;
        private float adaptFactor;

        public DamageInfo(long timestamp, float adaptFactor) {
            this.timestamp = timestamp;
            this.adaptFactor = adaptFactor;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public float getAdaptFactor() {
            return adaptFactor;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public void setAdaptFactor(float adaptFactor) {
            this.adaptFactor = adaptFactor;
        }
    }
}
