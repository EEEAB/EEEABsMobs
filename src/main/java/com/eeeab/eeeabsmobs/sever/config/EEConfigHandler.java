package com.eeeab.eeeabsmobs.sever.config;

import net.minecraftforge.common.ForgeConfigSpec;

//TODO 未完成
public class EEConfigHandler {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final Common COMMON;

    //必须使用静态代码块
    static {
        COMMON = new Common(BUILDER);
        SPEC = BUILDER.build();
    }

    public static class Common {
        public Common(final ForgeConfigSpec.Builder builder) {
            MOB = new Mob(builder);
            EXPERIMENTAL_ENTITY = new ExperimentalEntity(builder);
            OTHER = new Other(builder);
        }

        public final Mob MOB;
        public final ExperimentalEntity EXPERIMENTAL_ENTITY;
        public final Other OTHER;
    }

    public static class ExperimentalEntity {
        public ExperimentalEntity(final ForgeConfigSpec.Builder builder) {
            builder.push("Experimental Entity");
            GUARDIAN_LASER = new GuardianLaser(builder);
            //SCORCH = new Scorch(builder);
            builder.pop();
        }

        public final GuardianLaser GUARDIAN_LASER;
        //public final Scorch SCORCH;
    }

    public static class GuardianLaser {
        public GuardianLaser(final ForgeConfigSpec.Builder builder) {
            builder.push("Guardian Laser");
            this.enableGenerateScorchEntity = BUILDER.comment("If False disable scorch generate").
                    define("Enable scorch generate", true);
            builder.pop();
        }

        //生成烧焦的地面实体
        public final ForgeConfigSpec.BooleanValue enableGenerateScorchEntity;
    }

    //public static class Scorch {
    //    public Scorch(final ForgeConfigSpec.Builder builder) {
    //        builder.push("Scorch");
    //        this.existTimer = BUILDER.comment("Exist duration").
    //                defineInRange("Exist Seconds", 1, 1, 9);
    //        builder.pop();
    //    }
    //
    //    //存在时间
    //    public final ForgeConfigSpec.ConfigValue<Integer> existTimer;
    //}

    public static class Mob {
        public Mob(final ForgeConfigSpec.Builder builder) {
            builder.push("Mobs");
            IMMORTAL = new ImmortalMobs(builder);
            GUARDIAN = new Guardian(builder);
            builder.pop();
        }

        public final ImmortalMobs IMMORTAL;
        public final Guardian GUARDIAN;
    }

    public static class ImmortalMobs {
        public ImmortalMobs(final ForgeConfigSpec.Builder builder) {
            builder.push("Immortal Mobs");
            SKELETON = new Skeleton(builder);
            KNIGHT = new Knight(builder);
            SHAMAN = new Shaman(builder);
            GOLEM = new Golem(builder);
            THE_IMMORTAL = new Immortal(builder);
            builder.pop();
        }

        public final Skeleton SKELETON;
        public final Knight KNIGHT;
        public final Shaman SHAMAN;
        public final Golem GOLEM;
        public final Immortal THE_IMMORTAL;
    }

    //不朽骷髅
    public static class Skeleton {
        public Skeleton(final ForgeConfigSpec.Builder builder) {
            builder.push("Immortal Skeleton");
            combatConfig = new AttributeConfig(1, 1);
            builder.pop();
        }

        public final AttributeConfig combatConfig;
    }

    //不朽骷髅骑士
    public static class Knight {
        public Knight(final ForgeConfigSpec.Builder builder) {
            builder.push("Immortal Knight");
            combatConfig = new AttributeConfig(1, 1);
            builder.pop();
        }

        public final AttributeConfig combatConfig;
    }

    //不朽巫师
    public static class Shaman {
        public Shaman(final ForgeConfigSpec.Builder builder) {
            builder.push("Immortal Shaman");
            healValue = BUILDER.comment("Immortal Shaman heal values").
                    defineInRange("Heal values", 16, 0D, Double.MAX_VALUE);
            combatConfig = new AttributeConfig(1, 1);
            maximumDamageCap = new GeneralDamageCap(0.2F);
            builder.pop();
        }

        public final AttributeConfig combatConfig;
        public final GeneralDamageCap maximumDamageCap;
        public final ForgeConfigSpec.DoubleValue healValue;
    }

    //不朽傀儡
    public static class Golem {
        public Golem(final ForgeConfigSpec.Builder builder) {
            builder.push("Immortal Golem");
            combatConfig = new AttributeConfig(1, 1);
            builder.pop();
        }

        public final AttributeConfig combatConfig;
    }

    //无名守卫者
    public static class Guardian {
        public Guardian(final ForgeConfigSpec.Builder builder) {
            builder.push("The Nameless Guardian");
            enableNonCombatHeal = BUILDER.comment("If False disable non-combat heal").
                    define("Enable non-combat heal", true);
            suckBloodFactor = BUILDER.comment("The upper limit of vampire is based on the percentage of maximum health").
                    defineInRange("Suck blood factor", 0.05, 0, 1);
            combatConfig = new AttributeConfig(1, 1);
            maximumDamageCap = new GeneralDamageCap(0.05F);
            builder.pop();
        }

        //启用脱战治疗
        public final ForgeConfigSpec.BooleanValue enableNonCombatHeal;
        //吸血系数上限(基于最大生命值的百分比)
        public final ForgeConfigSpec.DoubleValue suckBloodFactor;
        public final AttributeConfig combatConfig;
        public final GeneralDamageCap maximumDamageCap;
    }

    //不朽
    public static class Immortal {
        public Immortal(final ForgeConfigSpec.Builder builder) {
            builder.push("The Immortal");
            combatConfig = new AttributeConfig(1, 1);
            builder.pop();
        }

        public final AttributeConfig combatConfig;
    }

    //其他设置
    public static class Other {
        public Other(final ForgeConfigSpec.Builder builder) {
            builder.push("Other");
            this.enableCameraShake = BUILDER.comment("If False disable camera shake").
                    define("Enable camera shake", true);
            this.enableShowBloodBars = BUILDER.comment("If False disable bosses blood bars").
                    define("Enable bosses blood bars", true);
            this.enableSameMobsTypeInjury = BUILDER.comment("If False able inflict damage between Mobs of the same type").
                    define("Mobs of the same type cannot cause harm", true);
            this.enableRenderFallingBlock = BUILDER.comment("If False disable falling block rendering").
                    define("Enable falling block rendering", true);
            this.enablePlayBossMusic = BUILDER.comment("If False disable play boss music").
                    define("Enable play boss music", true);
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
    }


    //属性倍率
    public static class AttributeConfig {
        public AttributeConfig(float healthMultiplier, float attackMultiplier) {
            this.healthMultiplier = BUILDER.comment("Set this mob health multiplier").
                    defineInRange("Health multiplier", healthMultiplier, 0D, Double.MAX_VALUE);

            this.attackMultiplier = BUILDER.comment("set this mob attack multiplier").
                    defineInRange("Attack multiplier", attackMultiplier, 0D, Double.MAX_VALUE);
        }

        public final ForgeConfigSpec.DoubleValue healthMultiplier;
        public final ForgeConfigSpec.DoubleValue attackMultiplier;
    }

    //通用伤害限制
    public static class GeneralDamageCap {
        public GeneralDamageCap(float damageCapPercentage) {
            this.damageCap = BUILDER.comment("Set damage cap percentage").
                    defineInRange("Damage cap percentage", damageCapPercentage, 0.01D, 1D);
        }

        public final ForgeConfigSpec.DoubleValue damageCap;
    }
}
