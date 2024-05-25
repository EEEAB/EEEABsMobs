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
            enableImmortalArmorItemDurability = BUILDER.comment("If 'True' the 'Immortal Armor' will be damaged due to durability").define("Enable immortal armor durability", false);
            enableImmortalItemDurability = BUILDER.comment("If 'True' the 'Immortal Item' will be damaged due to durability").define("Enable immortal item durability", false);
            {
                builder.push("Guardian Axe");
                GUARDIAN_AXE_TOOL = new ToolConfig(15D, 0.9D);
                builder.pop();
            }
            builder.pop();
        }

        public final ForgeConfigSpec.BooleanValue enableImmortalArmorItemDurability;
        public final ForgeConfigSpec.BooleanValue enableImmortalItemDurability;
        public final ToolConfig GUARDIAN_AXE_TOOL;
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
            THE_IMMORTAL = new Immortal(builder);
            builder.pop();
        }

        public final ImmortalSkeleton IMMORTAL_SKELETON;
        public final ImmortalKnight IMMORTAL_KNIGHT;
        public final ImmortalShaman IMMORTAL_SHAMAN;
        public final ImmortalGolem IMMORTAL_GOLEM;
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
            maximumDamageCap = new GeneralDamageCap(0.25);
            builder.pop();
        }

        public final AttributeConfig combatConfig;
        public final GeneralDamageCap maximumDamageCap;
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

    //不朽
    public static class Immortal {
        public Immortal(final ForgeConfigSpec.Builder builder) {
            builder.push("Immortal Boss");
            combatConfig = new AttributeConfig();
            builder.pop();
        }

        public final AttributeConfig combatConfig;
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
            maximumDamageCap = new GeneralDamageCap(0.25);
            builder.pop();
        }

        public final ForgeConfigSpec.DoubleValue maxDistanceTakeDamage;
        public final AttributeConfig combatConfig;
        public final GeneralDamageCap maximumDamageCap;
    }

    public static class GulingMobs {
        public GulingMobs(final ForgeConfigSpec.Builder builder) {
            builder.push("Structure-Guling");
            GULING_SENTINEL_HEAVY = new GulingSentinelHeavy(builder);
            NAMELESS_GUARDIAN = new NamelessGuardian(builder);
            builder.pop();
        }

        public final NamelessGuardian NAMELESS_GUARDIAN;
        public final GulingSentinelHeavy GULING_SENTINEL_HEAVY;
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
            enableNonCombatHeal = BUILDER.comment("If 'False' disable non-combat heal").define("Enable non-combat heal", true);
            suckBloodFactor = BUILDER.comment("Max life steal coefficient (based on max health percentage)").defineInRange("Suck blood factor", 0.05, 0, 1);
            challengeMode = BUILDER.comment("Challenge mode!").define("Be careful! It's going to get tricky!", false);
            combatConfig = new AttributeConfig();
            maximumDamageCap = new GeneralDamageCap(0.05);
            builder.pop();
        }

        public final ForgeConfigSpec.BooleanValue enableNonCombatHeal;//启用脱战治疗
        public final ForgeConfigSpec.DoubleValue suckBloodFactor;//吸血系数上限(基于最大生命值的百分比)
        public final ForgeConfigSpec.BooleanValue challengeMode;//挑战模式
        public final AttributeConfig combatConfig;
        public final GeneralDamageCap maximumDamageCap;
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
            builder.pop();
        }

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
            builder.pop();
        }

        public final ForgeConfigSpec.BooleanValue enableCameraShake;//启用相机摇晃
        public final ForgeConfigSpec.BooleanValue enableShowBloodBars;//启用显示boss血条
        public final ForgeConfigSpec.BooleanValue enableSameMobsTypeInjury;//启用同类型生物之间无法造成伤害
        public final ForgeConfigSpec.BooleanValue enableRenderFallingBlock;//启用渲染掉落方块
        public final ForgeConfigSpec.BooleanValue enablePlayBossMusic;//启用播放boss战斗音乐
    }


    //属性倍率
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
    public static class GeneralDamageCap {
        public GeneralDamageCap(double damageCapPercentage) {
            this.damageCap = BUILDER.comment("Set damage cap percentage (based on max health)").defineInRange("Damage cap percentage", damageCapPercentage, 0.01D, 1D);
        }

        public final ForgeConfigSpec.DoubleValue damageCap;
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
