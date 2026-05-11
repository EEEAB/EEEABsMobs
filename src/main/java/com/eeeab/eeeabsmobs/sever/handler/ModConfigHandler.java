package com.eeeab.eeeabsmobs.sever.handler;

import com.eeeab.eeeabsmobs.EEEABMobs;
import com.eeeab.eeeabsmobs.sever.util.TranslateUtils;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EEEABMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModConfigHandler {
    public static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Common COMMON;
    public static final Client CLIENT;

    private ModConfigHandler() {
    }

    static {
        COMMON = new Common(COMMON_BUILDER);
        CLIENT = new Client(CLIENT_BUILDER);
        COMMON_SPEC = COMMON_BUILDER.build();
        CLIENT_SPEC = CLIENT_BUILDER.build();
    }

    private static String getTranslationKey(String key) {
        return TranslateUtils.simpleConfigText(key, null).getString();
    }

    public static class Common {
        public Common(final ForgeConfigSpec.Builder builder) {
            items = new Item(builder);
            entities = new Entity(builder);
            mobs = new Mob(builder);
            potionEffects = new PotionEffect(builder);
            others = new Other(builder);
        }

        public final Mob mobs;
        public final Entity entities;
        public final Item items;
        public final PotionEffect potionEffects;
        public final Other others;
    }

    public static class Item {
        public Item(final ForgeConfigSpec.Builder builder) {
            builder.push("Items");
            {
                builder.push("Eye Of Structure");
                eyeOfStructureConfig1 = builder.comment("If set to 'True' eye of structure will be consume item on release")
                        .translation(getTranslationKey("eye_of_structure"))
                        .define("Consume Item On Release", false);
                eyeOfStructureConfig2 = itemCD(4D);
                builder.pop();
            }

            builder.push("Weapon");
            {
                builder.push("Doombolt Battleaxe");
                doomboltAxeConfig = itemCD(3D);
                doomboltAxe = new ToolConfig(15D, 0.9D);
                builder.push("Surge");
                surge = new AttributeConfig(7.5F);
                builder.pop();
                builder.pop();
            }
            {
                builder.push("Annihilator Sawblade");
                chainsword = new ToolConfig(10D, 1.6D);
                chainswordConfig1 = builder.comment("Set the percentage of damage increase per stack")
                        .translation(getTranslationKey("chainsword_1"))
                        .defineInRange("Set Damage Bonus Per Stack", 5D, 1D, 10D);
                chainswordConfig2 = builder.comment("Set the maximum number of stacks for the damage buff")
                        .translation(getTranslationKey("chainsword_2"))
                        .defineInRange("Set Max Buff Stacks", 5, 0, 10);
                builder.pop();
            }
            {
                builder.push("Guardian Core");
                guardianCoreConfig1 = builder.comment("Set the maximum shooting distance for players")
                        .translation(getTranslationKey("attack_radius"))
                        .defineInRange("Set Attack Radius", 16D, 1D, 64D);
                guardianCoreConfig2 = builder.comment("If set to 'False' disable block destruction by players")
                        .translation(getTranslationKey("guardian_core"))
                        .define("Enable Destroy Block", false);
                guardianLaser = new AttributeConfig(5F);
                builder.pop();
            }
            {
                builder.push("Buster Gauntlet");
                busterGauntletConfig1 = builder.translation(getTranslationKey("projectile_damage"))
                        .defineInRange("Set Projectile Damage", 10D, 1D, Float.MAX_VALUE);
                busterGauntletConfig2 = builder.comment("Set Grenade explosion radius")
                        .translation(getTranslationKey("attack_radius"))
                        .defineInRange("Set Attack Radius", 2.5D, 1D, 5D);
                busterGauntletConfig3 = itemCD(4D);
                busterGauntlet = new ToolConfig(8D, 1.6D);
                builder.pop();
            }
            //{
            //    builder.push("Ghost Warrior Series");
            //    ghostWarriorSeriesConfig1 = builder.comment("If set to 'True' armor or weapon will be depleted")
            //            .translation(getTranslationKey("series_depleted"))
            //            .define("Enable Armor Or Weapon Can Be Depleted", false);
            //    netherworldKatana = new ToolConfig(14D, 1.4D);
            //    builder.pop();
            //}
            //{
            //    builder.push("Immortal Staff");
            //    immortalStaffConfig1 = itemCD(3D);
            //    immortalStaffConfig2 = builder.translation(getTranslationKey("projectile_damage"))
            //            .defineInRange("Set Projectile Damage", 8D, 1D, Float.MAX_VALUE);
            //    builder.pop();
            //}
            builder.pop();
            builder.push("Accessories");
            {
                builder.push("Summoning Soul Necklace");
                summoningSoulNecklaceConfig1 = builder.comment("Set maximum amount of damage a player can take while holding this item")
                        .translation(getTranslationKey("summoning_soul_necklace"))
                        .defineInRange("Set Maximum Cumulative Damage Taken", 50D, 1D, Float.MAX_VALUE);
                summoningSoulNecklaceConfig2 = itemCD(20D);
                builder.pop();
            }
            builder.pop();
            builder.pop();
        }

        public final ForgeConfigSpec.BooleanValue eyeOfStructureConfig1;
        public final ForgeConfigSpec.DoubleValue eyeOfStructureConfig2;
        public final ForgeConfigSpec.DoubleValue summoningSoulNecklaceConfig1;
        public final ForgeConfigSpec.DoubleValue summoningSoulNecklaceConfig2;
        public final ForgeConfigSpec.DoubleValue busterGauntletConfig1;
        public final ForgeConfigSpec.DoubleValue busterGauntletConfig2;
        public final ForgeConfigSpec.DoubleValue busterGauntletConfig3;
        public final ToolConfig busterGauntlet;
        public final ForgeConfigSpec.DoubleValue doomboltAxeConfig;
        public final ToolConfig doomboltAxe;
        public final AttributeConfig surge;
        public final ToolConfig chainsword;
        public final ForgeConfigSpec.IntValue chainswordConfig2;
        public final ForgeConfigSpec.DoubleValue chainswordConfig1;
        public final ForgeConfigSpec.DoubleValue guardianCoreConfig1;
        public final ForgeConfigSpec.BooleanValue guardianCoreConfig2;
        public final AttributeConfig guardianLaser;
        //public final ForgeConfigSpec.DoubleValue immortalStaffConfig1;
        //public final ForgeConfigSpec.DoubleValue immortalStaffConfig2;
        //public final ForgeConfigSpec.BooleanValue ghostWarriorSeriesConfig1;
        //public final ToolConfig netherworldKatana;

        private static ForgeConfigSpec.DoubleValue itemCD(double defaultCD) {
            String CDComment = "Set item cool down time after player on use (in seconds)";
            String CDPath = "Set Item Cool Down Time";
            String CDKey = "item_cd";
            return COMMON_BUILDER.comment(CDComment)
                    .translation(getTranslationKey(CDKey))
                    .defineInRange(CDPath, defaultCD, 0D, 60D);
        }
    }

    public static class Entity {
        public Entity(final ForgeConfigSpec.Builder builder) {
            builder.push("Entities");
            {
                builder.push("Falling Block");
                fallingBlockConfig1 = builder.comment("If set to 'False' disable falling block spawn")
                        .translation(getTranslationKey("falling_block_1")).define("Enable Falling Block Spawn", true);
                fallingBlockConfig2 = builder.comment("Set the maximum range for downward block detection to determine the Y-axis position where the entity spawns")
                        .translation(getTranslationKey("falling_block_2")).defineInRange("Set Below Check Range", 3, 1, 10);
                builder.pop();
            }
            {
                builder.push("Tester");
                testerConfig1 = builder.translation(getTranslationKey("tester_1")).defineInRange("Set Max Health", 20D, 1D, 1024D);
                testerConfig2 = builder.translation(getTranslationKey("tester_2")).define("Enable Immune To Environmental Or Status Damage", true);
                builder.pop();
            }
            builder.pop();
        }

        public final ForgeConfigSpec.BooleanValue fallingBlockConfig1;
        public final ForgeConfigSpec.IntValue fallingBlockConfig2;
        public final ForgeConfigSpec.DoubleValue testerConfig1;
        public final ForgeConfigSpec.BooleanValue testerConfig2;
    }

    public static class Mob {
        public Mob(final ForgeConfigSpec.Builder builder) {
            builder.push("Mobs");
            immortals = new ImmortalMobs(builder);
            corpses = new CorpseMobs(builder);
            relicrons = new Relicrons(builder);
            minions = new MinionMobs(builder);
            builder.pop();
        }

        public final CorpseMobs corpses;
        public final ImmortalMobs immortals;
        public final Relicrons relicrons;
        public final MinionMobs minions;
    }

    public static class ImmortalMobs {
        public ImmortalMobs(final ForgeConfigSpec.Builder builder) {
            builder.push("Immortal Mobs");
            immortalSkeleton = new ImmortalSkeleton(builder);
            immortalShaman = new ImmortalShaman(builder);
            immortalGolem = new ImmortalGolem(builder);
            immortalExecutioner = new ImmortalExecutioner(builder);
            immortal = new Immortal(builder);
            builder.pop();
        }

        public final ImmortalSkeleton immortalSkeleton;
        public final ImmortalShaman immortalShaman;
        public final ImmortalGolem immortalGolem;
        public final ImmortalExecutioner immortalExecutioner;
        public final Immortal immortal;
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

    //不朽巫师
    public static class ImmortalShaman {
        public ImmortalShaman(final ForgeConfigSpec.Builder builder) {
            builder.push("Immortal Shaman");
            healPercentage = builder.comment("Immortal Shaman heal values (based on max health percentage)")
                    .translation(getTranslationKey("heal_percentage")).defineInRange("Heal Percentage", 0.5D, 0D, 1D);
            combatConfig = new AttributeConfig();
            {
                builder.push("Shaman Bomb").comment("BASE DAMAGE");
                shamanBomb = new AttributeConfig(8F);
                builder.pop();
            }
            builder.pop();
        }

        public final ForgeConfigSpec.DoubleValue healPercentage;
        public final AttributeConfig combatConfig;
        public final AttributeConfig shamanBomb;
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
            maximumDetonationCount = builder.comment("Set the number of times to strengthen this mob")
                    .translation(getTranslationKey("immortal_executioner")).defineInRange("Ignite Count", 3, 0, 1024);
            builder.pop();
        }

        public final AttributeConfig combatConfig;
        public final ForgeConfigSpec.IntValue maximumDetonationCount;
    }

    //不朽
    public static class Immortal {
        public Immortal(final ForgeConfigSpec.Builder builder) {
            builder.push("The Immortal");
            {
                builder.push("Immortal Shuriken").comment("BASE DAMAGE");
                immortalShuriken = new AttributeConfig(5);
                builder.pop();
            }
            {
                builder.push("Immortal Laser").comment("BASE DAMAGE");
                immortalLaser = new AttributeConfig(5);
                builder.pop();
            }
            combatConfig = new AttributeConfig();
            bossConfig = new BossConfig(30D, 16D, 0.25D);
            adaptConfig = new DamageSourceAdaptConfig(15, 60, 1.5D, 2D, 0.9D);
            builder.pop();
        }

        public final AttributeConfig combatConfig;
        public final AttributeConfig immortalShuriken;
        public final AttributeConfig immortalLaser;
        public final BossConfig bossConfig;
        public final DamageSourceAdaptConfig adaptConfig;
    }

    public static class CorpseMobs {
        public CorpseMobs(final ForgeConfigSpec.Builder builder) {
            builder.push("Corpse Mobs");
            corpse = new Corpse(builder);
            corpseWarlock = new CorpseWarlock(builder);
            builder.pop();
        }

        public final Corpse corpse;
        public final CorpseWarlock corpseWarlock;
    }

    //死尸&死尸村民
    public static class Corpse {
        public Corpse(final ForgeConfigSpec.Builder builder) {
            builder.push("Corpse & Corpse Villager");
            combatConfig = new AttributeConfig();
            enableConvertToCorpse = builder.comment("If set to 'False', When zombies or villagers are killed by corpses, they will not converted into corpses themselves")
                    .translation(getTranslationKey("corpse")).define("Converted To Corpse", true);
            builder.pop();
        }

        public final AttributeConfig combatConfig;
        public final ForgeConfigSpec.BooleanValue enableConvertToCorpse;
    }

    //死尸术士
    public static class CorpseWarlock {
        public CorpseWarlock(final ForgeConfigSpec.Builder builder) {
            builder.push("Corpse Warlock");
            combatConfig = new AttributeConfig();
            {
                builder.push("Crimson Ray");
                crimsonRay = new AttributeConfig(5F);
                builder.pop();
            }
            {
                builder.push("Crimson Crack");
                crimsonCrack = new AttributeConfig(5F);
                builder.pop();
            }
            {
                builder.push("Blood Ball").comment("BASE DAMAGE");
                bloodBall = new AttributeConfig(5F);
                builder.pop();
            }
            builder.pop();
        }

        public final AttributeConfig combatConfig;
        public final AttributeConfig crimsonRay;
        public final AttributeConfig crimsonCrack;
        public final AttributeConfig bloodBall;
    }

    public static class Relicrons {
        public Relicrons(final ForgeConfigSpec.Builder builder) {
            builder.push("Relicrons");
            relicObserver = new RelicObserver(builder);
            relicRipper = new RelicRipper(builder);
            relicEarthshaker = new RelicEarthshaker(builder);
            relicAnnihilator = new RelicAnnihilator(builder);
            realmwarden = new Realmwarden(builder);
            namelessGuardian = new NamelessGuardian(builder);
            outOfBattleHeal = builder.comment("If set to 'False' disable healing while not active")
                    .translation(getTranslationKey("out_of_battle")).define("Enable healing while not active", true);
            builder.pop();
        }

        public final RelicObserver relicObserver;
        public final RelicRipper relicRipper;
        public final RelicEarthshaker relicEarthshaker;
        public final RelicAnnihilator relicAnnihilator;
        public final Realmwarden realmwarden;
        public final NamelessGuardian namelessGuardian;
        public final ForgeConfigSpec.BooleanValue outOfBattleHeal;
    }

    //遗迹观察者
    public static class RelicObserver {
        public RelicObserver(final ForgeConfigSpec.Builder builder) {
            builder.push("Relic Observer");
            combatConfig = new AttributeConfig();
            {
                builder.push("Guardian Laser");
                guardianLaser = new AttributeConfig(6F);
                builder.pop();
            }
            builder.pop();
        }

        public final AttributeConfig combatConfig;
        public final AttributeConfig guardianLaser;
    }

    //遗迹撕裂者
    public static class RelicRipper {
        public RelicRipper(final ForgeConfigSpec.Builder builder) {
            builder.push("Relic Ripper");
            combatConfig = new AttributeConfig();
            builder.pop();
        }

        public final AttributeConfig combatConfig;
    }

    //遗迹撼地者
    public static class RelicEarthshaker {
        public RelicEarthshaker(final ForgeConfigSpec.Builder builder) {
            builder.push("Relic Earthshaker");
            combatConfig = new AttributeConfig();
            {
                builder.push("Electromagnetic");
                electromagnetic = new AttributeConfig(8F);
                builder.pop();
            }
            {
                builder.push("Pulsed Grenade");
                pulsedGrenade = new AttributeConfig(6F);
                builder.pop();
            }
            builder.pop();
        }

        public final AttributeConfig combatConfig;
        public final AttributeConfig electromagnetic;
        public final AttributeConfig pulsedGrenade;
    }

    //遗迹歼击者
    public static class RelicAnnihilator {
        public RelicAnnihilator(final ForgeConfigSpec.Builder builder) {
            builder.push("Relic Annihilator");
            combatConfig = new AttributeConfig();
            bossConfig = new BossConfig(20D, 14D, 0.75D);
            {
                builder.push("Guardian Laser");
                guardianLaser = new AttributeConfig(8F);
                builder.pop();
            }
            {
                builder.push("Annihilator Missile");
                annihilatorMissile = new AttributeConfig(8F);
                builder.pop();
            }
            {
                builder.push("Electromagnetic");
                electromagnetic = new AttributeConfig(10F);
                builder.pop();
            }
            builder.pop();
        }

        public final AttributeConfig combatConfig;
        public final BossConfig bossConfig;
        public final AttributeConfig guardianLaser;
        public final AttributeConfig annihilatorMissile;
        public final AttributeConfig electromagnetic;
    }

    public static class Realmwarden {
        public Realmwarden(final ForgeConfigSpec.Builder builder) {
            builder.push("Realmwarden");
            combatConfig = new AttributeConfig();
            bossConfig = new BossConfig(20D, 14D, 1D);
            {
                builder.push("Annihilator Missile");
                annihilatorMissile = new AttributeConfig(15F);
                builder.pop();
            }
            {
                builder.push("Surge");
                surge = new AttributeConfig(10F);
                builder.pop();
            }
            builder.pop();
        }

        public final AttributeConfig combatConfig;
        public final BossConfig bossConfig;
        public final AttributeConfig annihilatorMissile;
        public final AttributeConfig surge;
    }

    //无名守卫者
    public static class NamelessGuardian {
        public NamelessGuardian(final ForgeConfigSpec.Builder builder) {
            builder.push("Nameless Guardian");
            suckBloodMultiplier = builder.comment("Set suck blood multiplier")
                    .translation(getTranslationKey("suck_blood")).defineInRange("Suck Blood Multiplier", 1D, 0D, 1024D);
            enableForcedSuckBlood = builder.comment("If set to 'False' disable forced suck blood on power status(This setting does not take effect in Challenge Mode)")
                    .translation(getTranslationKey("forced_suck_blood")).define("Enable Forced Suck Blood", true);
            challengeMode = builder.comment("Be careful! It's going to get tricky!")
                    .translation(getTranslationKey("challenge_mode")).define("Challenge Mode", false);
            combatConfig = new AttributeConfig();
            bossConfig = new BossConfig(20D, 18D, 0.5D);
            {
                builder.push("Guardian Laser");
                guardianLaser = new AttributeConfig(5F);
                builder.pop();
            }
            {
                builder.push("Guardian Blade");
                guardianBlade = new AttributeConfig(15F);
                builder.pop();
            }
            builder.pop();
        }

        //吸血倍率
        public final ForgeConfigSpec.DoubleValue suckBloodMultiplier;
        //启用强制吸血
        public final ForgeConfigSpec.BooleanValue enableForcedSuckBlood;
        //挑战模式
        public final ForgeConfigSpec.BooleanValue challengeMode;
        public final AttributeConfig combatConfig;
        public final BossConfig bossConfig;
        public final AttributeConfig guardianLaser;
        public final AttributeConfig guardianBlade;
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
            minionDeathHealAmount = builder.comment("Set corpse minion to restore its owner's health")
                    .translation(getTranslationKey("corpse_to_player")).defineInRange("Death Heal Amount", 5D, 0D, 1024D);
            builder.pop();
        }

        public final AttributeConfig combatConfig;
        public final ForgeConfigSpec.DoubleValue minionDeathHealAmount;
    }

    //药水效果
    public static class PotionEffect {
        public PotionEffect(final ForgeConfigSpec.Builder builder) {
            builder.push("Potion Effects");
            {
                builder.push("Electromagnetic Overload");
                electrifiedConfig1 = builder.comment("Base damage dealt by the electromagnetic overload effect")
                        .translation(getTranslationKey("electrified_effect_1"))
                        .defineInRange("Set Base Damage", 8D, 0D, Float.MAX_VALUE);
                electrifiedConfig2 = builder.comment("Additional damage as a percentage of target's max health")
                        .translation(getTranslationKey("electrified_effect_2"))
                        .defineInRange("Set Max Health Ratio", 0.05D, 0D, 0.2D);
                builder.pop();
            }
            builder.pop();
        }

        public final ForgeConfigSpec.DoubleValue electrifiedConfig1;
        public final ForgeConfigSpec.DoubleValue electrifiedConfig2;
    }

    //其他设置
    public static class Other {
        public Other(final ForgeConfigSpec.Builder builder) {
            builder.push("Others");
            {
                builder.push("Bosses");
                enableShowBossBars = builder.comment("If set to 'False' disable show boss bar")
                        .translation(getTranslationKey("show_boss_bar")).define("Enable Show Boss Health Bars", true);
                bossBarMaxDist = builder.comment("Set maximum render distance for boss health bar")
                        .translation(getTranslationKey("boss_bar_render_dist")).defineInRange("Set Render Distance For Boss Bars", 48D, 32D, 128D);
                builder.pop();
            }
            enableMobsCanBreakingBlockDropItem = builder.comment("If set to 'False' disable mobs breaking blocks drop items")
                    .translation(getTranslationKey("break_block_drop")).define("Enable Mobs Breaking Blocks Drop Items", false);
            enableShowItemCD = builder.comment("If set to 'False' disable showing cooldown time in tooltip")
                    .translation(getTranslationKey("show_item_cd")).define("Enable Show Item Cd", false);
            enableCombatPrompts = builder.comment("If set to 'False' disable all combat encounter prompts")
                    .translation(getTranslationKey("combat_prompt")).define("Enable Combat Prompt Trigger Detection", true);
            builder.pop();
        }

        //启用显示boss血条
        public final ForgeConfigSpec.BooleanValue enableShowBossBars;
        //boss血条显示距离
        public final ForgeConfigSpec.DoubleValue bossBarMaxDist;
        //启用生物破坏方块掉落对应方块物品
        public final ForgeConfigSpec.BooleanValue enableMobsCanBreakingBlockDropItem;
        //启用显示物品冷却时间
        public final ForgeConfigSpec.BooleanValue enableShowItemCD;
        //启用战斗提示
        public final ForgeConfigSpec.BooleanValue enableCombatPrompts;
    }

    //通用属性倍率
    public static class AttributeConfig {
        public AttributeConfig() {
            this(1.0F, 1.0F);
        }

        public AttributeConfig(float healthMultiplier, float attackMultiplier) {
            this.healthMultiplier = COMMON_BUILDER.comment("Set this mob health multiplier")
                    .translation(getTranslationKey("mob_health")).defineInRange("Health Multiplier", healthMultiplier, 0D, Float.MAX_VALUE);
            this.attackMultiplier = COMMON_BUILDER.comment("Set this mob attack multiplier")
                    .translation(getTranslationKey("mob_attack")).defineInRange("Attack Multiplier", attackMultiplier, 0D, Float.MAX_VALUE);
            damage = null;
        }

        public AttributeConfig(float damageAmount) {
            damage = COMMON_BUILDER.comment("Set this entity attack damage")
                    .translation(getTranslationKey("entity_damage")).defineInRange("Attack Damage", damageAmount, 1D, Float.MAX_VALUE);
            attackMultiplier = null;
            healthMultiplier = null;
        }

        public final ForgeConfigSpec.DoubleValue healthMultiplier;
        public final ForgeConfigSpec.DoubleValue attackMultiplier;
        public final ForgeConfigSpec.DoubleValue damage;
    }

    //Boss通用配置
    public static class BossConfig {
        public BossConfig(double damageCap, double damageDist, double damageReductDur) {
            this.damageCap = COMMON_BUILDER.comment("Set this mob damage cap")
                    .translation(getTranslationKey("damage_cap"))
                    .defineInRange("Damage Cap", damageCap, 0D, Float.MAX_VALUE);
            this.canDamageDist = COMMON_BUILDER.comment("Set this mob max effective damage distance")
                    .translation(getTranslationKey("max_damage_dist")).defineInRange("Effective Damage Distance", damageDist, 0D, 64D);
            this.damageReductDur = COMMON_BUILDER.comment("Set this mob's damage reduction duration after being damaged (seconds)")
                    .translation(getTranslationKey("damage_reduction_duration"))
                    .defineInRange("Damage Reduction Duration", damageReductDur, 0D, 5D);
        }

        public final ForgeConfigSpec.DoubleValue damageCap;
        public final ForgeConfigSpec.DoubleValue canDamageDist;
        public final ForgeConfigSpec.DoubleValue damageReductDur;
    }

    //伤害源适应
    public static class DamageSourceAdaptConfig {
        public DamageSourceAdaptConfig(int attenuatedDuration, int fullAdaptDuration, double singleAdaptFactor, double playerSingleAdaptFactor, double maxAdaptFactor) {
            COMMON_BUILDER.push("Damage Adapt V1");
            this.enabled = COMMON_BUILDER.comment("If set to 'False' disable Damage Adaptation")
                    .translation(getTranslationKey("damage_adapt_enabled")).define("Enabled", true);
            this.debug = COMMON_BUILDER.comment("If set to 'False' disable print logs")
                    .translation(getTranslationKey("damage_adapt_debug")).define("Enabled Debug Mode", false);
            this.attenuatedDuration = COMMON_BUILDER.comment("Duration for adaptation to decay to zero when not fully adapted (in seconds)")
                    .translation(getTranslationKey("damage_adapt_0")).defineInRange("Decay Duration", attenuatedDuration, 1, 300);
            this.fullAdaptDuration = COMMON_BUILDER.comment("Set the effective duration for a single adaptation damage source (in seconds)")
                    .translation(getTranslationKey("damage_adapt_1")).defineInRange("Adaptation Duration", fullAdaptDuration, 1, 300);
            this.singleAdaptFactor = COMMON_BUILDER.comment("Adaptation increment per hit for mob-caused damage, scaled by damage/maxHealth. Higher = faster buildup")
                    .translation(getTranslationKey("damage_adapt_2")).defineInRange("Mob Damage Adaptation Factor", singleAdaptFactor, 1D, 10D);
            this.playerSingleAdaptFactor = COMMON_BUILDER.comment("Adaptation increment per hit for player-caused damage, scaled by damage/maxHealth. Higher = faster buildup")
                    .translation(getTranslationKey("damage_adapt_2_player")).defineInRange("Player-Caused Damage Adaptation Factor", playerSingleAdaptFactor, 1D, 10D);
            this.maxAdaptFactor = COMMON_BUILDER.comment("Set the maximum damage reduction factor")
                    .translation(getTranslationKey("damage_adapt_3")).defineInRange("Same Damage Max Adaptation Factor", maxAdaptFactor, 0.1D, 1D);
            COMMON_BUILDER.pop();
        }

        public final ForgeConfigSpec.BooleanValue enabled;
        public final ForgeConfigSpec.BooleanValue debug;
        public final ForgeConfigSpec.IntValue fullAdaptDuration;
        public final ForgeConfigSpec.IntValue attenuatedDuration;
        public final ForgeConfigSpec.DoubleValue playerSingleAdaptFactor;
        public final ForgeConfigSpec.DoubleValue singleAdaptFactor;
        public final ForgeConfigSpec.DoubleValue maxAdaptFactor;
    }

    //通用物品配置
    public static class ToolConfig {
        ToolConfig(double d0, double d1) {
            attackSpeedValue = d1;
            attackDamageValue = d0;
            this.attackDamage = COMMON_BUILDER.comment("Set tool attack damage(Restart required)")
                    .translation(getTranslationKey("tool_damage")).defineInRange("Attack Damage", d0, 0D, Float.MAX_VALUE);
            this.attackSpeed = COMMON_BUILDER.comment("Set tool attack speed(Restart required)")
                    .translation(getTranslationKey("tool_speed")).defineInRange("Attack Speed", d1, 0D, Float.MAX_VALUE);
        }

        public final ForgeConfigSpec.DoubleValue attackDamage;
        public final ForgeConfigSpec.DoubleValue attackSpeed;

        public double attackDamageValue;
        public double attackSpeedValue;
    }

    /* ↓↓↓ 客户端配置 ↓↓↓ */

    public static class Client {
        public Client(final ForgeConfigSpec.Builder builder) {
            tipNotification = new TipNotification(builder);
            enableCameraShake = builder.comment("If set to 'False' disable camera shake")
                    .translation(getTranslationKey("camera_shake")).define("Enable Camera Shake", true);
            enableCustomBossBars = builder.comment("If set to 'False' disable custom bosse health bar")
                    .translation(getTranslationKey("custom_boss_bar")).define("Enable Custom Boss Health Bars", true);
            enablePlayBossMusic = builder.comment("If set to 'False' disable play boss music")
                    .translation(getTranslationKey("play_boss_music")).define("Enable Play Bosses Musics", true);
            enableFallingBlockRender = builder.comment("If set to 'False' disable falling block rendering")
                    .translation(getTranslationKey("falling_block_render")).define("Enable Falling Block Render", true);
        }

        public final TipNotification tipNotification;
        public final ForgeConfigSpec.BooleanValue enableCameraShake;
        public final ForgeConfigSpec.BooleanValue enablePlayBossMusic;
        public final ForgeConfigSpec.BooleanValue enableFallingBlockRender;
        public final ForgeConfigSpec.BooleanValue enableCustomBossBars;
    }

    public static class TipNotification {
        public TipNotification(final ForgeConfigSpec.Builder builder) {
            builder.push("Popup Notification");
            enabled = builder.comment("If set to 'False' disable popup notification")
                    .translation(getTranslationKey("notification_enabled")).define("Enabled", true);
            displayMode = builder
                    .comment("HUD display mode: FADE_IN_OUT (fade in/out), IMMEDIATE (show immediately)")
                    .translation(getTranslationKey("notification_display_mode")).defineEnum("Display Mode", DisplayMode.FADE_IN_OUT);
            displayDuration = builder
                    .comment("HUD display duration in ticks").translation(getTranslationKey("notification_display_duration"))
                    .defineInRange("Display Duration", 120, 20, 1200);
            backgroundOpacity = builder
                    .comment("HUD background opacity").translation(getTranslationKey("notification_background_opacity"))
                    .defineInRange("Background Opacity", 0.25D, 0.0D, 1.0D);
            textMaxWidth = builder
                    .comment("Maximum width for text lines in pixels").translation(getTranslationKey("notification_text_max_width"))
                    .defineInRange("Text Max Width", 300, 100, 400);
            lineSpacing = builder.comment("Spacing between text lines in pixels")
                    .translation(getTranslationKey("notification_line_spacing")).defineInRange("Line Spacing", 2, 0, 10);
            hudPositionY = builder.comment("Vertical position of HUD from top of screen in pixels")
                    .translation(getTranslationKey("notification_hud_position_y")).defineInRange("Hud Position Y", 30, 0, 100);
            builder.pop();
        }

        public final ForgeConfigSpec.BooleanValue enabled;
        public final ForgeConfigSpec.EnumValue<DisplayMode> displayMode;
        public final ForgeConfigSpec.IntValue displayDuration;
        public final ForgeConfigSpec.DoubleValue backgroundOpacity;
        public final ForgeConfigSpec.IntValue textMaxWidth;
        public final ForgeConfigSpec.IntValue lineSpacing;
        public final ForgeConfigSpec.IntValue hudPositionY;

        public enum DisplayMode {
            FADE_IN_OUT,
            IMMEDIATE
        }
    }
}
