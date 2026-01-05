package com.eeeab.eeeabsmobs;

import com.eeeab.animate.client.gui.AnimationControllerScreen;
import com.eeeab.eeeabsmobs.client.ClientProxy;
import com.eeeab.eeeabsmobs.client.gui.BossBarRegistry;
import com.eeeab.eeeabsmobs.client.gui.PromptNotificationHandler;
import com.eeeab.eeeabsmobs.sever.ServerProxy;
import com.eeeab.eeeabsmobs.sever.advancements.ModCriteriaTriggers;
import com.eeeab.eeeabsmobs.sever.handler.CapabilityHandler;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.handler.ServerEventHandler;
import com.eeeab.eeeabsmobs.sever.init.*;
import com.eeeab.eeeabsmobs.sever.integration.curios.CuriosRegistry;
import com.eeeab.eeeabsmobs.sever.trigger.CombatTriggerEvent;
import com.eeeab.eeeabsmobs.sever.trigger.CombatTriggerHandler;
import com.eeeab.eeeabsmobs.sever.util.ModBrewingRecipe;
import com.eeeab.eeeabsmobs.sever.util.TranslateUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EEEABMobs.MOD_ID)
@Mod.EventBusSubscriber(modid = EEEABMobs.MOD_ID)
public class EEEABMobs {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "eeeabsmobs";
    public static SimpleChannel NETWORK;
    public static ServerProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public EEEABMobs() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.register(bus);
        BlockInit.register(bus);
        BlockEntityInit.register(bus);
        EntityInit.register(bus);
        CreativeTabInit.register(bus);
        AttributeInit.register(bus);
        EffectInit.register(bus);
        PotionInit.register(bus);
        ParticleInit.register(bus);
        SoundInit.register(bus);
        MenuInit.register(bus);
        StructuresInit.register(bus);
        WorldGenInit.register(bus);
        PROXY.initMod(bus);
        PROXY.initForge(MinecraftForge.EVENT_BUS);
        bus.addListener(this::setup);
        bus.addListener(this::clientSetup);
        bus.addListener(this::complete);
        bus.addListener(this::onModConfigInit);
    }

    private void setup(final FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        MinecraftForge.EVENT_BUS.register(new CombatTriggerEvent());
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, CapabilityHandler::attachEntityCapability);
        PROXY.initNetwork();
        event.enqueueWork(() -> {
            CuriosRegistry.init();
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(BlockInit.BONE_BUSH.getId(), BlockInit.POTTED_BONE_BUSH);
            BrewingRecipeRegistry.addRecipe(new ModBrewingRecipe(Potions.STRONG_STRENGTH, ItemInit.HEART_OF_PAGAN.get(), PotionInit.FRENZY_POTION.get()));
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            PromptNotificationHandler.init();
            BossBarRegistry.init();
            CuriosRegistry.initClient();
            MenuScreens.register(MenuInit.ANIMATION_CONTROLLER.get(), AnimationControllerScreen::new);
        });
    }

    private void complete(final FMLLoadCompleteEvent event) {
        CombatTriggerHandler.init();
        ModCriteriaTriggers.init();
        ItemInit.initializeAttributes();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        PROXY.loadComplete(bus);
    }

    @SubscribeEvent
    public void onModConfigInit(final ModConfigEvent event) {
        ModConfig config = event.getConfig();
        if (config.getSpec() == ModConfigHandler.COMMON_SPEC) {
            ModConfigHandler.Item item = ModConfigHandler.COMMON.items;
            item.guardianBattleaxe.attackSpeedValue = item.guardianBattleaxe.attackSpeed.get().floatValue();
            item.guardianBattleaxe.attackDamageValue = item.guardianBattleaxe.attackDamage.get().floatValue();
            item.netherworldKatana.attackSpeedValue = item.netherworldKatana.attackSpeed.get().floatValue();
            item.netherworldKatana.attackDamageValue = item.netherworldKatana.attackDamage.get().floatValue();
            TranslateUtils.SHOW_ITEM_CD = ModConfigHandler.COMMON.others.enableShowItemCD.get();
        }
    }
}
