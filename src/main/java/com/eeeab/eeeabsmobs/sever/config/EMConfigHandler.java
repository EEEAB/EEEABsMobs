package com.eeeab.eeeabsmobs.sever.config;

import com.eeeab.eeeabsmobs.EEEABMobs;
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

    public static class Common {
        public Common(final ForgeConfigSpec.Builder builder) {
            MOB = new Mob(builder);
            ENTITY = new Entity(builder);
            ITEM = new Item(builder);
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
            {
                builder.push("Summoning Soul Necklace");
                SSNCumulativeMaximumDamage = BUILDER.comment("Set the maximum amount of damage a player can take while holding this item").defineInRange("Set the maximum cumulative damage taken", 50, 1, Float.MAX_VALUE);
                SSNCoolingTime = BUILDER.comment("Set item cool down time after player summons (seconds)").defineInRange("Set item cool down time", 20, 1, Integer.MAX_VALUE);
                builder.pop();
            }
            {
                builder.push("Howitzer");
                itemHowitzerCoolingTime = BUILDER.comment("Set item cool down time after player on use (seconds)").defineInRange("Set item cool down time", 2D, 0.5D, 60D);
                itemHowitzerGrenadeDamage = BUILDER.comment("Set Grenade maximum explosion damage(damage to the center of the explosion)").defineInRange("Set damage cap", 10D, 1D, 128D);
                itemHowitzerGrenadeExplosionRadius = BUILDER.comment("Set Grenade explosion radius(the bigger the blast radius, the higher the damage)").defineInRange("Set explosion radius", 2.5D, 1D, 10D);
                builder.pop();
            }
            {
                builder.push("Guardian Axe");
                GUARDIAN_AXE_TOOL = new ToolConfig(15D, 0.9D);
                builder.pop();
            }
            {
                builder.push("Immortal Staff");
                itemImmortalStaffCoolingTime = BUILDER.comment("Set the cool down time after using the weapon (seconds)").defineInRange("Set item cool down time", 1.5, 0.5, 60);
                builder.pop();
            }
            {
                builder.push("Ghost Warrior Armor & Weapons");
                enableGhostWarriorArmorItemDurability = BUILDER.comment("If 'True' the 'Ghost Warrior Armor' will be damaged due to durability").define("Enable armor durability", false);
                NETHERWORLD_KATANA_TOOL = new ToolConfig(14D, 1.4D);
                builder.pop();
            }
            builder.pop();
        }

        public final ForgeConfigSpec.BooleanValue enableGhostWarriorArmorItemDurability;
        public final ForgeConfigSpec.DoubleValue itemImmortalStaffCoolingTime;
        public final ToolConfig GUARDIAN_AXE_TOOL;
        public final ToolConfig NETHERWORLD_KATANA_TOOL;
        public final ForgeConfigSpec.DoubleValue itemHowitzerCoolingTime;
        public final ForgeConfigSpec.DoubleValue itemHowitzerGrenadeDamage;
        public final ForgeConfigSpec.DoubleValue itemHowitzerGrenadeExplosionRadius;
        public final ForgeConfigSpec.DoubleValue SSNCumulativeMaximumDamage;
        public final ForgeConfigSpec.IntValue SSNCoolingTime;
    }

    public static class Mob {
        public Mob(final ForgeConfigSpec.Builder builder) {
            builder.push("Mobs");
            IMMORTAL = new ImmortalMobs(builder);
            CORPSES = new CorpseMobs(builder);
            GULING = new GulingMobs(builder);
            builder.pop();
        }

        public final CorpseMobs CORPSES;
        public final ImmortalMobs IMMORTAL;
        public final GulingMobs GULING;
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
            healPercentage = BUILDER.comment("Immortal Shaman heal values (based on max health percentage)").defineInRange("Heal percentage", 0.5D, 0D, 1D);
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
            maximumDetonationCount = BUILDER.comment("Set the number of times to strengthen this mob").defineInRange("Detonation Count", 3, 0, 1024);
            builder.pop();
        }

        public final AttributeConfig combatConfig;
        public final ForgeConfigSpec.IntValue maximumDetonationCount;
    }

    //不朽
    public static class Immortal {
        public Immortal(final ForgeConfigSpec.Builder builder) {
            builder.push("Immortal Boss");
            combatConfig = new AttributeConfig();
            maximumDamageCap = new DamageCapConfig(20);
            adaptConfig = new DamageSourceAdaptConfig(builder, 100, 30, 0.1D, 0.8D, true);
            builder.pop();
        }

        public final AttributeConfig combatConfig;
        public final DamageCapConfig maximumDamageCap;
        public final DamageSourceAdaptConfig adaptConfig;
    }

    public static class CorpseMobs {
        public CorpseMobs(final ForgeConfigSpec.Builder builder) {
            builder.push("Structure-Bloody Altar");
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
            enableConvertToCorpse = BUILDER.comment("If 'False', it will not be converted to corpse").define("Converted to corpse", true);
            builder.pop();
        }

        public final AttributeConfig combatConfig;
        public final ForgeConfigSpec.BooleanValue enableConvertToCorpse;
    }

    //死尸术士
    public static class CorpseWarlock {
        public CorpseWarlock(final ForgeConfigSpec.Builder builder) {
            builder.push("Corpse Warlock");
            maxDistanceTakeDamage = BUILDER.comment("Set the distance to take damage from projectiles").defineInRange("Attack distance", 12D, 1D, 32D);
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
            builder.push("Structure-Guling");
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
            enableNonCombatHeal = BUILDER.comment("If 'False' disable non-combat heal").define("Enable non-combat heal", true);
            combatConfig = new AttributeConfig();
            builder.pop();
        }

        public final ForgeConfigSpec.BooleanValue enableNonCombatHeal;//启用脱战治疗
        public final AttributeConfig combatConfig;
    }

    //古陵哨兵-重型
    public static class GulingSentinelHeavy {
        public GulingSentinelHeavy(final ForgeConfigSpec.Builder builder) {
            builder.push("Guling Sentinel-Heavy");
            enableNonCombatHeal = BUILDER.comment("If 'False' disable non-combat heal").define("Enable non-combat heal", true);
            combatConfig = new AttributeConfig();
            builder.pop();
        }

        public final ForgeConfigSpec.BooleanValue enableNonCombatHeal;//启用脱战治疗
        public final AttributeConfig combatConfig;
    }

    //无名守卫者
    public static class NamelessGuardian {
        public NamelessGuardian(final ForgeConfigSpec.Builder builder) {
            builder.push("Nameless Guardian");
            suckBloodMultiplier = BUILDER.comment("Set suck blood multiplier").defineInRange("Suck blood multiplier", 1D, 0D, 1024D);
            enableNonCombatHeal = BUILDER.comment("If 'False' disable non-combat heal").define("Enable non-combat heal", true);
            enableForcedSuckBlood = BUILDER.comment("If 'False' disable forced suck blood on power status(Does not take effect in Challenge mode)").define("Enable forced suck blood", true);
            challengeMode = BUILDER.comment("Be careful! It's going to get tricky!").define("Challenge mode!", false);
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
        public final AttributeConfig combatConfig;
        public final DamageCapConfig maximumDamageCap;
    }

    public static class Entity {
        public Entity(final ForgeConfigSpec.Builder builder) {
            builder.push("Entity");
            GUARDIAN_LASER = new GuardianLaser(builder);
            builder.pop();
        }

        public final GuardianLaser GUARDIAN_LASER;
    }

    public static class GuardianLaser {
        public GuardianLaser(final ForgeConfigSpec.Builder builder) {
            builder.push("Guardian Laser");
            this.enableGenerateScorchEntity = BUILDER.comment("If 'False' disable scorch generate on the ground").define("Enable scorch generate", true);
            this.playerShootRadius = BUILDER.comment("Set the maximum shooting distance when the user is a player").defineInRange("Set attack radius", 16D, 1D, 64D);
            builder.pop();
        }

        //当使用者是玩家时的最大射击距离
        public final ForgeConfigSpec.DoubleValue playerShootRadius;
        //生成烧焦的地面实体
        public final ForgeConfigSpec.BooleanValue enableGenerateScorchEntity;
    }

    //其他设置
    public static class Other {
        public Other(final ForgeConfigSpec.Builder builder) {
            builder.push("Other");
            this.enableCameraShake = BUILDER.comment("If 'False' disable camera shake").define("Enable camera shake", true);
            this.enableShowBloodBars = BUILDER.comment("If 'False' disable bosses blood bars").define("Enable bosses blood bars", true);
            this.enableSameMobsTypeInjury = BUILDER.comment("If 'False' able inflict damage between mobs of the same type").define("Mobs of the same type cannot cause harm", true);
            this.enableRenderFallingBlock = BUILDER.comment("If 'False' disable falling block rendering").define("Enable falling block rendering", true);
            this.enablePlayBossMusic = BUILDER.comment("If 'False' disable play boss music").define("Enable play boss music", true);
            this.enableFrenzyDestroyBlock = BUILDER.comment("If 'False' disable frenzy potion destroy block").define("Enable frenzy potion destroy block", true);
            this.enableAnimationLegalityLogPrint = BUILDER.comment("If 'True' enable print illegal animation logs(For developers only)").define("Enable print illegal logs", false);
            builder.pop();
        }

        //启用相机摇晃
        public final ForgeConfigSpec.BooleanValue enableCameraShake;
        //启用显示boss血条
        public final ForgeConfigSpec.BooleanValue enableShowBloodBars;
        //启用同类型生物之间无法造成伤害
        public final ForgeConfigSpec.BooleanValue enableSameMobsTypeInjury;
        //启用渲染掉落方块
        public final ForgeConfigSpec.BooleanValue enableRenderFallingBlock;
        //启用播放boss战斗音乐
        public final ForgeConfigSpec.BooleanValue enablePlayBossMusic;
        //启用狂暴药水冲刺时破坏方块效果
        public final ForgeConfigSpec.BooleanValue enableFrenzyDestroyBlock;
        //启用记录错误动作日志
        public final ForgeConfigSpec.BooleanValue enableAnimationLegalityLogPrint;
    }


    //通用属性倍率
    public static class AttributeConfig {
        public AttributeConfig() {
            this(1.0F, 1.0F);
        }

        public AttributeConfig(float healthMultiplier, float attackMultiplier) {
            this.healthMultiplier = BUILDER.comment("Set this mob health multiplier").defineInRange("Health multiplier", healthMultiplier, 0D, Double.MAX_VALUE);
            this.attackMultiplier = BUILDER.comment("Set this mob attack multiplier").defineInRange("Attack multiplier", attackMultiplier, 0D, Double.MAX_VALUE);
        }

        public final ForgeConfigSpec.DoubleValue healthMultiplier;
        public final ForgeConfigSpec.DoubleValue attackMultiplier;
    }

    //通用伤害限制
    public static class DamageCapConfig {
        public DamageCapConfig(double damageCapPercentage) {
            this.damageCap = BUILDER.comment("Set this mob damageCap").defineInRange("DamageCap", damageCapPercentage, 0D, 1024D);
        }

        public final ForgeConfigSpec.DoubleValue damageCap;
    }

    //通用伤害源适应
    public static class DamageSourceAdaptConfig {
        public DamageSourceAdaptConfig(final ForgeConfigSpec.Builder builder, int maxDamageSourceAdaptCount, int resetCountdown, double singleAdaptFactor, double maxAdaptFactor, boolean adaptsSameTypeMobs) {
            builder.push("Damage Adapt");
            this.maxDamageSourceAdaptCount = BUILDER.comment("Set this mob max damage source adapt count").defineInRange("Max damageSource adapt count", maxDamageSourceAdaptCount, 10, 1024);
            this.resetCountdown = BUILDER.comment("Set the effective time for a single adapt damage source(second)").defineInRange("Effective time", resetCountdown, 10, 1024);
            this.singleAdaptFactor = BUILDER.comment("Set the factor of each reduction of the same damage source").defineInRange("Single adapt factor", singleAdaptFactor, 0D, 1D);
            this.maxAdaptFactor = BUILDER.comment("Set the max damage reduction factor").defineInRange("Max adapt factor", maxAdaptFactor, 0D, 1D);
            this.adaptsSameTypeMobs = adaptsSameTypeMobs;
            builder.pop();
        }

        public final ForgeConfigSpec.IntValue maxDamageSourceAdaptCount;
        public final ForgeConfigSpec.IntValue resetCountdown;
        public final ForgeConfigSpec.DoubleValue singleAdaptFactor;
        public final ForgeConfigSpec.DoubleValue maxAdaptFactor;
        public final boolean adaptsSameTypeMobs;
    }

    //通用物品配置
    public static class ToolConfig {
        ToolConfig(double attackDamage, double attackSpeed) {
            this.attackSpeedValue = attackSpeed;
            this.attackDamageValue = attackDamage;
            this.attackDamage = BUILDER.comment("Set tool attack damage").defineInRange("Attack damage", attackDamage, 0d, Float.MAX_VALUE);
            this.attackSpeed = BUILDER.comment("Set tool attack speed").defineInRange("Attack speed", attackSpeed, 0d, Float.MAX_VALUE);
        }

        public final ForgeConfigSpec.DoubleValue attackDamage;
        public final ForgeConfigSpec.DoubleValue attackSpeed;

        public double attackDamageValue;
        public double attackSpeedValue;
    }
}
