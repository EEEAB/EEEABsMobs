package com.eeeab.eeeabsmobs.client.model.util;

import com.eeeab.eeeabsmobs.EEEABMobs;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModModelLayer {
    //实体
    public static final ModelLayerLocation UNKNOWN = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "unknown"), "main");
    public static final ModelLayerLocation TESTER = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "tester"), "main");
    public static final ModelLayerLocation CORPSE = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "corpse"), "main");
    public static final ModelLayerLocation CORPSE_VILLAGER = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "corpse_villager"), "main");
    public static final ModelLayerLocation CORPSE_SLAVERY = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "corpse_slavery"), "main");
    public static final ModelLayerLocation RELIC_OBSERVER = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "relic_observer"), "main");
    public static final ModelLayerLocation RELIC_RIPPER = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "relic_ripper"), "main");
    public static final ModelLayerLocation RELIC_EARTHSHAKER = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "relic_earthshaker"), "main");
    public static final ModelLayerLocation RELIC_ANNIHILATOR = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "relic_annihilator"), "main");
    public static final ModelLayerLocation REALM_WARDEN = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "relic_domain_warden"), "main");
    public static final ModelLayerLocation NAMELESS_GUARDIAN = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "nameless_guardian"), "main");
    public static final ModelLayerLocation MAGIC_GOLEM = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "immortal_golem"), "main");
    public static final ModelLayerLocation IMMORTAL_SKELETON = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "immortal_skeleton"), "main");
    public static final ModelLayerLocation IMMORTAL_SHAMAN = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "immortal_shaman"), "main");
    public static final ModelLayerLocation IMMORTAL_EXECUTIONER = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "immortal_executioner"), "main");
    public static final ModelLayerLocation IMMORTAL = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "immortal_boss"), "main");

    //非生物实体
    public static final ModelLayerLocation BLOOD_BALL = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "blood_ball"), "main");
    public static final ModelLayerLocation PULSED_GRENADE = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "pulsed_grenade"), "main");
    public static final ModelLayerLocation GUARDIAN_BLADE = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "guardian_blade"), "main");
    public static final ModelLayerLocation IMMORTAL_SHURIKEN = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "immortal_shuriken"), "main");
    public static final ModelLayerLocation ANNIHILATOR_MISSILE = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "annihilator_missile"), "main");
    public static final ModelLayerLocation DOOMBOLT_AXE = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "doombolt_axe"), "main");

    //盔甲
    public static final ModelLayerLocation GHOST_WARRIOR_ARMOR = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "ghost_warrior_armor"), "main");
    public static final ModelLayerLocation GHOST_WARRIOR_ARMOR_LEGS = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "ghost_warrior_armor_legs"), "main");
    public static final ModelLayerLocation SOUL_SUMMONING_NECKLACE = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "soul_summoning_necklace"), "main");

    //物品
    public static final ModelLayerLocation THE_NETHERWORLD_KATANA = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "netherworld_katana"), "main");
    public static final ModelLayerLocation THE_GUARDIAN_BATTLEAXE = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "guardian_axe"), "main");
    public static final ModelLayerLocation BUSTER_GAUNTLET = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "buster_gauntlet"), "main");

    //方块
    public static final ModelLayerLocation SLIDING_DOOR = new ModelLayerLocation(new ResourceLocation(EEEABMobs.MOD_ID, "sliding_door"), "main");
}
