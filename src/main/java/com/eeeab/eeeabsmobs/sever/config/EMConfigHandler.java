package com.eeeab.eeeabsmobs.sever.config;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.util.EMTUtils;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EEEABMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class EMConfigHandler {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final Common COMMON;

    private EMConfigHandler() {
    }

    //必须使用静态代码块
    static {
        COMMON = new Common(BUILDER);
        SPEC = BUILDER.build();
    }

    private static String getTranslationKey(String key) {
        return EMTUtils.simpleConfigText(key, null).getString();
    }

    public static class Common {
        public Common(final ForgeConfigSpec.Builder builder) {
            ITEM = new Item(builder);
            ENTITY = new Entity(builder);
            MOB = new Mob(builder);
            OTHER = new Other(builder);
        }

        public final Mob MOB;
        public final Entity ENTITY;
        public final Item ITEM;
        public final Other OTHER;
    }

    public static class Item {
        public Item(final ForgeConfigSpec.Builder builder) {
            builder.push("Items");
            String CDComment = "Set item cool down time after player on use (seconds)";
            String CDPath = "Set item cool down time";
            String CDKey = "item_cd";
            {
                builder.push("Summoning Soul Necklace");
                SSNCumulativeMaximumDamage = BUILDER.comment("Set maximum amount of damage a player can take while holding this item")
                        .translation(getTranslationKey("summoning_soul_necklace"))
                        .defineInRange("Set maximum cumulative damage taken", 50, 1, Float.MAX_VALUE);
                SSNCoolingTime = BUILDER.comment(CDComment)
                        .translation(getTranslationKey(CDKey))
                        .defineInRange(CDPath, 20, 1, Integer.MAX_VALUE);
                builder.pop();
            }
            {
                builder.push("Howitzer");
                itemHowitzerCoolingTime = BUILDER.comment(CDComment)
                        .translation(getTranslationKey(CDKey))
                        .defineInRange(CDPath, 2D, 0.5D, 60D);
                itemHowitzerGrenadeDamage = BUILDER.comment("Set Grenade maximum explosion damage(damage to the center of the explosion)")
                        .translation(getTranslationKey("howitzer_1"))
                        .defineInRange("Set explosion damage cap", 10D, 1D, 128D);
                itemHowitzerGrenadeExplosionRadius = BUILDER.comment("Set Grenade explosion radius(the bigger the blast radius, the higher the damage)")
                        .translation(getTranslationKey("howitzer_2"))
                        .defineInRange("Set explosion radius", 2.5D, 1D, 10D);
                builder.pop();
            }
            {
                builder.push("Guardian Battleaxe");
                GUARDIAN_AXE_TOOL = new ToolConfig(15D, 0.9D);
                builder.pop();
            }
            {
                builder.push("Immortal Staff");
                itemImmortalStaffCoolingTime = BUILDER.comment(CDComment)
                        .translation(getTranslationKey(CDKey))
                        .defineInRange(CDPath, 1.5, 0.5, 60);
                builder.pop();
            }
            {
                builder.push("Ghost Warrior Series");
                enableGhostWarriorSeriesItemDurability = BUILDER.comment("If 'True' armor or weapon will be depleted")
                        .translation(getTranslationKey("series_depleted"))
                        .define("Enable armor or weapon can be depleted", false);
                NETHERWORLD_KATANA_TOOL = new ToolConfig(14D, 1.4D);
                builder.pop();
            }
            builder.pop();
        }

        public final ForgeConfigSpec.BooleanValue enableGhostWarriorSeriesItemDurability;
        public final ForgeConfigSpec.DoubleValue itemImmortalStaffCoolingTime;
        public final ToolConfig GUARDIAN_AXE_TOOL;
        public final ToolConfig NETHERWORLD_KATANA_TOOL;
        public final ForgeConfigSpec.DoubleValue itemHowitzerCoolingTime;
        public final ForgeConfigSpec.DoubleValue itemHowitzerGrenadeDamage;
        public final ForgeConfigSpec.DoubleValue itemHowitzerGrenadeExplosionRadius;
        public final ForgeConfigSpec.DoubleValue SSNCumulativeMaximumDamage;
        public final ForgeConfigSpec.IntValue SSNCoolingTime;
    }

    public static class Entity {
        public Entity(final ForgeConfigSpec.Builder builder) {
            builder.push("Entities");
            {
                builder.push("Guardian Laser");
                this.enableGenerateScorchEntity = BUILDER.comment("If 'False' disable scorch generate on the ground")
                        .translation(getTranslationKey("guardian_laser"))
                        .define("Enable scorch generate", true);
                this.guardianLaserShootRadius = BUILDER.comment("Set the maximum shooting distance when the user is a player")
                        .translation(getTranslationKey("attack_radius"))
                        .defineInRange("Set attack radius", 16D, 1D, 64D);
                builder.pop();
            }
            {
                builder.push("Falling Block");
                this.enableSpawnFallingBlock = BUILDER.comment("If 'False' disable falling block spawn")
                        .translation(getTranslationKey("falling_block_1")).define("Enable falling block spawn", true);
                this.enableRenderFallingBlock = BUILDER.comment("If 'False' disable falling block rendering")
                        .translation(getTranslationKey("falling_block_2")).define("Enable falling block render", true);
                this.fallingBlockBelowCheckRange = BUILDER.comment("Set the maximum range for downward block detection to determine the Y-axis position where the entity spawns")
                        .translation(getTranslationKey("falling_block_3")).defineInRange("Set below check range", 3, 1, 10);
                builder.pop();
            }
            {
                builder.push("Tester");
                this.testerMaxHealth = BUILDER.translation(getTranslationKey("tester_1")).defineInRange("Set max health", 20D, 1D, 1024D);
                this.immuneToEnvironmentalOrStatusDamage = BUILDER.translation(getTranslationKey("tester_2")).define("Enable immune to environmental or status damage", true);
                builder.pop();
            }
            builder.pop();
        }

        //当守卫者激光使用者是玩家时的最大射击距离
        public final ForgeConfigSpec.DoubleValue guardianLaserShootRadius;
        //生成烧焦的地面实体
        public final ForgeConfigSpec.BooleanValue enableGenerateScorchEntity;
        //启用生成掉落方块
        public final ForgeConfigSpec.BooleanValue enableSpawnFallingBlock;
        //启用渲染掉落方块
        public final ForgeConfigSpec.BooleanValue enableRenderFallingBlock;
        //下探检查下落方块生成位置最大范围
        public final ForgeConfigSpec.IntValue fallingBlockBelowCheckRange;
        //测试者最大生命值
        public final ForgeConfigSpec.DoubleValue testerMaxHealth;
        //测试者免疫由环境或者状态因素造成的伤害
        public final ForgeConfigSpec.BooleanValue immuneToEnvironmentalOrStatusDamage;
    }

    public static class Mob {
        public Mob(final ForgeConfigSpec.Builder builder) {
            builder.push("Mobs");
            IMMORTAL = new ImmortalMobs(builder);
            CORPSES = new CorpseMobs(builder);
            GULING = new GulingMobs(builder);
            MINION = new MinionMobs(builder);
            builder.pop();
        }

        public final CorpseMobs CORPSES;
        public final ImmortalMobs IMMORTAL;
        public final GulingMobs GULING;
        public final MinionMobs MINION;
    }

    public static class ImmortalMobs {
        public ImmortalMobs(final ForgeConfigSpec.Builder builder) {
            builder.push("Immortal Mobs");
            IMMORTAL_SKELETON = new ImmortalSkeleton(builder);
            IMMORTAL_KNIGHT = new ImmortalKnight(builder);
            IMMORTAL_SHAMAN = new ImmortalShaman(builder);
            IMMORTAL_GOLEM = new ImmortalGolem(builder);
            IMMORTAL_EXECUTIONER = new ImmortalExecutioner(builder);
            THE_IMMORTAL = new Immortal(builder);
            builder.pop();
        }

        public final ImmortalSkeleton IMMORTAL_SKELETON;
        public final ImmortalKnight IMMORTAL_KNIGHT;
        public final ImmortalShaman IMMORTAL_SHAMAN;
        public final ImmortalGolem IMMORTAL_GOLEM;
        public final ImmortalExecutioner IMMORTAL_EXECUTIONER;
        public final Immortal THE_IMMORTAL;
    }

    //不朽骷髅
    public static class ImmortalSkeleton {
        public ImmortalSkeleton(final ForgeConfigSpec.Builder builder) {
            builder.push("Immortal Skeleton");
            combatConfig = new AttributeConfig();
            builder.pop();
        }

        public final AttributeConfig combatConfig;
    }

    //不朽骷髅骑士
    public static class ImmortalKnight {
        public ImmortalKnight(final ForgeConfigSpec.Builder builder) {
            builder.push("Immortal Knight");
            combatConfig = new AttributeConfig();
            builder.pop();
        }

        public final AttributeConfig combatConfig;
    }

    //不朽巫师
    public static class ImmortalShaman {
        public ImmortalShaman(final ForgeConfigSpec.Builder builder) {
            builder.push("Immortal Shaman");
            healPercentage = BUILDER.comment("Immortal Shaman heal values (based on max health percentage)")
                    .translation(getTranslationKey("heal_percentage")).defineInRange("Heal percentage", 0.5D, 0D, 1D);
            combatConfig = new AttributeConfig();
            builder.pop();
        }

        public final AttributeConfig combatConfig;
        public final ForgeConfigSpec.DoubleValue healPercentage;
    }

    //不朽傀儡
    public static class ImmortalGolem {
        public ImmortalGolem(final ForgeConfigSpec.Builder builder) {
            builder.push("Immortal Golem");
            combatConfig = new AttributeConfig();
            builder.pop();
        }

        public final AttributeConfig combatConfig;
    }

    //不朽斩魂者
    public static class ImmortalExecutioner {
        public ImmortalExecutioner(final ForgeConfigSpec.Builder builder) {
            builder.push("Immortal Executioner");
            combatConfig = new AttributeConfig();
            maximumDetonationCount = BUILDER.comment("Set the number of times to strengthen this mob")
                    .translation(getTranslationKey("immortal_executioner")).defineInRange("Ignite Count", 3, 0, 1024);
            builder.pop();
        }

        public final AttributeConfig combatConfig;
        public final ForgeConfigSpec.IntValue maximumDetonationCount;
    }

    //不朽
    public static class Immortal {
        public Immortal(final ForgeConfigSpec.Builder builder) {
            builder.push("Immortal");
            intervalProtect = BUILDER.translation(getTranslationKey("interval_protect")).define("Frame Damage Protection", true);
            maxDistanceTakeDamage = BUILDER.comment("Set the effective distance at which projectiles can deal damage")
                    .translation(getTranslationKey("effective_range")).defineInRange("Set projectile damage range", 15D, 1D, 32D);
            combatConfig = new AttributeConfig();
            maximumDamageCap = new DamageCapConfig(25);
            adaptConfig = new DamageSourceAdaptConfig(builder, 100, 30, 0.1D, 0.7D, true, true);
            builder.pop();
        }

        //帧伤保护机制
        public final ForgeConfigSpec.BooleanValue intervalProtect;
        public final ForgeConfigSpec.DoubleValue maxDistanceTakeDamage;
        public final AttributeConfig combatConfig;
        public final DamageCapConfig maximumDamageCap;
        public final DamageSourceAdaptConfig adaptConfig;
    }

    public static class CorpseMobs {
        public CorpseMobs(final ForgeConfigSpec.Builder builder) {
            builder.push("Corpse Mobs");
            CORPSE = new Corpse(builder);
            CORPSE_WARLOCK = new CorpseWarlock(builder);
            builder.pop();
        }

        public final Corpse CORPSE;
        public final CorpseWarlock CORPSE_WARLOCK;
    }

    //死尸&死尸村民
    public static class Corpse {
        public Corpse(final ForgeConfigSpec.Builder builder) {
            builder.push("Corpse & Corpse Villager");
            combatConfig = new AttributeConfig();
            enableConvertToCorpse = BUILDER.comment("If 'False', When zombies or villagers are killed by corpses, they will not converted into corpses themselves")
                    .translation(getTranslationKey("corpse")).define("Converted to corpse", true);
            builder.pop();
        }

        public final AttributeConfig combatConfig;
        public final ForgeConfigSpec.BooleanValue enableConvertToCorpse;
    }

    //死尸术士
    public static class CorpseWarlock {
        public CorpseWarlock(final ForgeConfigSpec.Builder builder) {
            builder.push("Corpse Warlock");
            maxDistanceTakeDamage = BUILDER.comment("Set the effective distance at which projectiles can deal damage")
                    .translation(getTranslationKey("effective_range")).defineInRange("Set projectile damage range", 12D, 1D, 32D);
            combatConfig = new AttributeConfig();
            maximumDamageCap = new DamageCapConfig(22);
            builder.pop();
        }

        public final ForgeConfigSpec.DoubleValue maxDistanceTakeDamage;
        public final AttributeConfig combatConfig;
        public final DamageCapConfig maximumDamageCap;
    }

    public static class GulingMobs {
        public GulingMobs(final ForgeConfigSpec.Builder builder) {
            builder.push("Guling Mobs");
            GULING_SENTINEL = new GulingSentinel(builder);
            GULING_SENTINEL_HEAVY = new GulingSentinelHeavy(builder);
            NAMELESS_GUARDIAN = new NamelessGuardian(builder);
            builder.pop();
        }

        public final NamelessGuardian NAMELESS_GUARDIAN;
        public final GulingSentinel GULING_SENTINEL;
        public final GulingSentinelHeavy GULING_SENTINEL_HEAVY;
    }

    //古陵哨兵
    public static class GulingSentinel {
        public GulingSentinel(final ForgeConfigSpec.Builder builder) {
            builder.push("Guling Sentinel");
            enableNonCombatHeal = BUILDER.comment("If 'False' disable out-of-combat heal")
                    .translation(getTranslationKey("out_of_combat")).define("Enable out-of-combat healing", true);
            combatConfig = new AttributeConfig();
            builder.pop();
        }

        //启用脱战治疗
        public final ForgeConfigSpec.BooleanValue enableNonCombatHeal;
        public final AttributeConfig combatConfig;
    }

    //古陵哨兵-重型
    public static class GulingSentinelHeavy {
        public GulingSentinelHeavy(final ForgeConfigSpec.Builder builder) {
            builder.push("Guling Sentinel-Heavy");
            enableNonCombatHeal = BUILDER.comment("If 'False' disable out-of-combat heal")
                    .translation(getTranslationKey("out_of_combat")).define("Enable out-of-combat healing", true);
            combatConfig = new AttributeConfig();
            builder.pop();
        }

        //启用脱战治疗
        public final ForgeConfigSpec.BooleanValue enableNonCombatHeal;
        public final AttributeConfig combatConfig;
    }

    //无名守卫者
    public static class NamelessGuardian {
        public NamelessGuardian(final ForgeConfigSpec.Builder builder) {
            builder.push("Nameless Guardian");
            suckBloodMultiplier = BUILDER.comment("Set suck blood multiplier")
                    .translation(getTranslationKey("suck_blood")).defineInRange("Suck blood multiplier", 1D, 0D, 1024D);
            extraInvulnerableTick = BUILDER.comment("Set extra invulnerable tick(This setting does not take effect in Challenge Mode)")
                    .translation(getTranslationKey("invulnerable_tick")).defineInRange("Extra invulnerable tick", 20, 0, 1200);
            enableNonCombatHeal = BUILDER.comment("If 'False' disable out-of-combat heal")
                    .translation(getTranslationKey("out_of_combat")).define("Enable out-of-combat healing", true);
            enableForcedSuckBlood = BUILDER.comment("If 'False' disable forced suck blood on power status(This setting does not take effect in Challenge Mode)")
                    .translation(getTranslationKey("forced_suck_blood")).define("Enable forced suck blood", true);
            challengeMode = BUILDER.comment("Be careful! It's going to get tricky!")
                    .translation(getTranslationKey("challenge_mode")).define("Challenge Mode", false);
            intervalProtect = BUILDER.comment("This setting does not take effect in Challenge Mode")
                    .translation(getTranslationKey("interval_protect")).define("Frame Damage Protection", true);
            combatConfig = new AttributeConfig();
            maximumDamageCap = new DamageCapConfig(20);
            builder.pop();
        }

        //启用脱战治疗
        public final ForgeConfigSpec.BooleanValue enableNonCombatHeal;
        //吸血倍率
        public final ForgeConfigSpec.DoubleValue suckBloodMultiplier;
        //启用强制吸血
        public final ForgeConfigSpec.BooleanValue enableForcedSuckBlood;
        //挑战模式
        public final ForgeConfigSpec.BooleanValue challengeMode;
        //额外的无敌刻
        public final ForgeConfigSpec.IntValue extraInvulnerableTick;
        //帧伤保护机制
        public final ForgeConfigSpec.BooleanValue intervalProtect;
        public final AttributeConfig combatConfig;
        public final DamageCapConfig maximumDamageCap;
    }

    //召唤类生物
    public static class MinionMobs {
        public MinionMobs(final ForgeConfigSpec.Builder builder) {
            builder.push("Minion Mobs");
            CORPSE_MINION = new CorpseToPlayer(builder);
            builder.pop();
        }

        public final CorpseToPlayer CORPSE_MINION;
    }

    public static class CorpseToPlayer {
        public CorpseToPlayer(final ForgeConfigSpec.Builder builder) {
            builder.push("Corpse Minion");
            combatConfig = new AttributeConfig();
            minionDeathHealAmount = BUILDER.comment("Set corpse minion to restore its owner's health")
                    .translation(getTranslationKey("corpse_to_player")).defineInRange("Death Heal Amount", 5D, 0D, 1024D);
            builder.pop();
        }

        public final AttributeConfig combatConfig;
        public final ForgeConfigSpec.DoubleValue minionDeathHealAmount;
    }

    //其他设置
    public static class Other {
        public Other(final ForgeConfigSpec.Builder builder) {
            builder.push("Others");
            {
                builder.push("Camera");
                this.enableCameraShake = BUILDER.comment("If 'False' disable camera shake")
                        .translation(getTranslationKey("camera_1")).define("Enable camera shake", true);
                builder.pop();
            }
            {
                builder.push("Bosses");
                this.enableShowBloodBars = BUILDER.comment("If 'False' disable bosses blood bars")
                        .translation(getTranslationKey("bosses_1")).define("Enable bosses blood bars", true);
                this.enablePlayBossMusic = BUILDER.comment("If 'False' disable play boss music")
                        .translation(getTranslationKey("bosses_2")).define("Enable play bosses musics", true);
                this.enableBossCanBreakingBlockDropItem = BUILDER.comment("If 'False' disable bosses breaking blocks drop items")
                        .translation(getTranslationKey("bosses_3")).define("Enable bosses breaking blocks drop items", false);
                builder.pop();
            }
            {
                builder.push("Misc");
                this.enableSameMobsTypeInjury = BUILDER.comment("If 'False' able inflict damage between mobs of the same team")
                        .translation(getTranslationKey("misc_1")).define("Friendly fire among allies", true);
                this.enableFrenzyDestroyBlock = BUILDER.comment("If 'False' disable frenzy potion destroy block")
                        .translation(getTranslationKey("misc_2")).define("Enable frenzy potion destroy block", false);
                builder.pop();
            }
            builder.pop();
        }

        //启用相机摇晃
        public final ForgeConfigSpec.BooleanValue enableCameraShake;
        //启用显示boss血条
        public final ForgeConfigSpec.BooleanValue enableShowBloodBars;
        //启用播放boss战斗音乐
        public final ForgeConfigSpec.BooleanValue enablePlayBossMusic;
        //启用boss破坏方块掉落对应方块物品
        public final ForgeConfigSpec.BooleanValue enableBossCanBreakingBlockDropItem;
        //启用同类型生物之间无法造成伤害
        public final ForgeConfigSpec.BooleanValue enableSameMobsTypeInjury;
        //启用狂暴药水冲刺时破坏方块效果
        public final ForgeConfigSpec.BooleanValue enableFrenzyDestroyBlock;
    }


    //通用属性倍率
    public static class AttributeConfig {
        public AttributeConfig() {
            this(1.0F, 1.0F);
        }

        public AttributeConfig(float healthMultiplier, float attackMultiplier) {
            this.healthMultiplier = BUILDER.comment("Set this mob health multiplier")
                    .translation(getTranslationKey("mob_health")).defineInRange("Health multiplier", healthMultiplier, 0D, Double.MAX_VALUE);
            this.attackMultiplier = BUILDER.comment("Set this mob attack multiplier")
                    .translation(getTranslationKey("mob_attack")).defineInRange("Attack multiplier", attackMultiplier, 0D, Double.MAX_VALUE);
        }

        public final ForgeConfigSpec.DoubleValue healthMultiplier;
        public final ForgeConfigSpec.DoubleValue attackMultiplier;
    }

    //通用伤害限制
    public static class DamageCapConfig {
        public DamageCapConfig(double damageCapPercentage) {
            this.damageCap = BUILDER.comment("Set this mob damageCap")
                    .translation(getTranslationKey("damage_cap"))
                    .defineInRange("DamageCap", damageCapPercentage, 0D, 1024D);
        }

        public final ForgeConfigSpec.DoubleValue damageCap;
    }

    //通用伤害源适应
    public static class DamageSourceAdaptConfig {
        public DamageSourceAdaptConfig(final ForgeConfigSpec.Builder builder, int maxDamageSourceAdaptCount, int resetCountdown, double singleAdaptFactor, double maxAdaptFactor, boolean adaptsSameTypeMobs, boolean adaptBypassesDamage) {
            builder.push("Damage Adapt");
            this.maxDamageSourceAdaptCount = BUILDER.comment("Set this mob maximum adaptation damage source count")
                    .translation(getTranslationKey("damage_adapt_1")).defineInRange("Maximum adaptation damage source count", maxDamageSourceAdaptCount, 10, 1024);
            this.resetCountdown = BUILDER.comment("Set the effective time for a single adaptation damage source(second)")
                    .translation(getTranslationKey("damage_adapt_2")).defineInRange("Effective time", resetCountdown, 10, 1024);
            this.singleAdaptFactor = BUILDER.comment("Set the factor of each reduction of the same damage source")
                    .translation(getTranslationKey("damage_adapt_3")).defineInRange("Single adapt factor", singleAdaptFactor, 0D, 1D);
            this.maxAdaptFactor = BUILDER.comment("Set the max damage reduction factor")
                    .translation(getTranslationKey("damage_adapt_4")).defineInRange("Max adapt factor", maxAdaptFactor, 0D, 1D);
            this.adaptsSameTypeMobs = adaptsSameTypeMobs;
            this.adaptBypassesDamage = BUILDER.comment("If 'False' disable adaptation to out of world and generic kill damage source")
                    .translation(getTranslationKey("damage_adapt_5")).define("Adaptation bypasses damage source", adaptBypassesDamage);
            builder.pop();
        }

        public final ForgeConfigSpec.IntValue maxDamageSourceAdaptCount;
        public final ForgeConfigSpec.IntValue resetCountdown;
        public final ForgeConfigSpec.DoubleValue singleAdaptFactor;
        public final ForgeConfigSpec.DoubleValue maxAdaptFactor;
        public final ForgeConfigSpec.BooleanValue adaptBypassesDamage;
        public final boolean adaptsSameTypeMobs;
    }

    //通用物品配置
    public static class ToolConfig {
        ToolConfig(double attackDamage, double attackSpeed) {
            this.attackSpeedValue = attackSpeed;
            this.attackDamageValue = attackDamage;
            this.attackDamage = BUILDER.comment("Set tool attack damage")
                    .translation(getTranslationKey("tool_damage")).defineInRange("Attack damage", attackDamage, 0d, Float.MAX_VALUE);
            this.attackSpeed = BUILDER.comment("Set tool attack speed")
                    .translation(getTranslationKey("tool_speed")).defineInRange("Attack speed", attackSpeed, 0d, Float.MAX_VALUE);
        }

        public final ForgeConfigSpec.DoubleValue attackDamage;
        public final ForgeConfigSpec.DoubleValue attackSpeed;

        public double attackDamageValue;
        public double attackSpeedValue;
    }
}
