package com.eeeab.eeeabsmobs.client.gui;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@OnlyIn(Dist.CLIENT)
public class BossBarRegistry {
    private static final Map<ResourceLocation, BossBarConfig> STATIC_CONFIGS = new ConcurrentHashMap<>();
    private static final Map<ResourceLocation, BossBarConfig> RESOURCEPACK_OVERRIDES = new ConcurrentHashMap<>();
    private static final Set<ResourceLocation> SCANNED_ENTITIES = ConcurrentHashMap.newKeySet();
    private static final BossBarConfig DEFAULT_CONFIG = new BossBarConfig(
            new ResourceLocation(EEEABMobs.MOD_ID, "textures/gui/boss_bar/default_base.png"),
            new ResourceLocation(EEEABMobs.MOD_ID, "textures/gui/boss_bar/default_overlay.png"),
            10, 32, 1, -4, -3, 256, 32, 25, ChatFormatting.WHITE);

    /**
     * 初始化注册表
     */
    public static void init() {
        clear();
        //registerSpecialBoss();
    }

    /**
     * 注册特殊Boss配置
     */
    public static void registerSpecialBoss(ResourceLocation entityId, BossBarConfig config) {
        STATIC_CONFIGS.put(entityId, config);
    }

    /**
     * 懒加载获取Boss血条配置
     * 优先级：资源包覆盖 > 静态配置 > 默认配置
     */
    public static BossBarConfig getBarForEntity(ResourceLocation entityId) {
        BossBarConfig resourcePackConfig = RESOURCEPACK_OVERRIDES.get(entityId);
        if (resourcePackConfig != null) {
            return resourcePackConfig;
        }
        if (!SCANNED_ENTITIES.contains(entityId)) {
            SCANNED_ENTITIES.add(entityId);
            ResourceLocation customBase = findTextureInResourcePacks(entityId, "base");
            ResourceLocation customOverlay = findTextureInResourcePacks(entityId, "overlay");
            if (customBase != null || customOverlay != null) {
                BossBarConfig customConfig = getConfig(customBase, customOverlay, STATIC_CONFIGS.getOrDefault(entityId, DEFAULT_CONFIG));
                RESOURCEPACK_OVERRIDES.put(entityId, customConfig);
                EEEABMobs.LOGGER.debug("The custom boss health bar texture overlay applied: {} [base: {}, overlay: {}]",
                        entityId,
                        customBase != null ? "custom" : "template",
                        customOverlay != null ? "custom" : "template");
                return customConfig;
            }
        }
        return STATIC_CONFIGS.getOrDefault(entityId, DEFAULT_CONFIG);
    }

    private static BossBarConfig getConfig(ResourceLocation base, ResourceLocation overlay, BossBarConfig templateConfig) {
        ResourceLocation finalBaseTexture = (base != null) ?
                base : templateConfig.getBaseTexture();
        ResourceLocation finalOverlayTexture = (overlay != null) ?
                overlay : templateConfig.getOverlayTexture();
        return new BossBarConfig(
                finalBaseTexture,
                finalOverlayTexture,
                templateConfig.getBaseHeight(),
                templateConfig.getBaseTextureHeight(),
                templateConfig.getBaseOffsetY(),
                templateConfig.getOverlayOffsetX(),
                templateConfig.getOverlayOffsetY(),
                templateConfig.getOverlayWidth(),
                templateConfig.getOverlayHeight(),
                templateConfig.getVerticalIncrement(),
                templateConfig.getTextColor()
        );
    }

    /**
     * 查找资源包纹理
     */
    private static ResourceLocation findTextureInResourcePacks(ResourceLocation entityId, String suffix) {
        String texturePath = String.format("textures/gui/boss_bar/%s_%s.png", entityId.getPath(), suffix);
        ResourceLocation targetLocation = new ResourceLocation(EEEABMobs.MOD_ID, texturePath);
        try {
            var resourceManager = Minecraft.getInstance().getResourceManager();
            if (resourceManager.getResource(targetLocation).isPresent()) return targetLocation;
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 清空缓存
     */
    public static void clear() {
        RESOURCEPACK_OVERRIDES.clear();
        SCANNED_ENTITIES.clear();
        STATIC_CONFIGS.clear();
    }

    /**
     * 重新加载资源包覆盖
     */
    public static void reloadResourcePackOverrides() {
        RESOURCEPACK_OVERRIDES.clear();
        SCANNED_ENTITIES.clear();
        EEEABMobs.LOGGER.debug("The custom boss health bar texture overlay cache has been reloaded.");
    }
}