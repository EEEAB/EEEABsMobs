package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.eeeabsmobs.sever.ability.Ability;
import com.eeeab.eeeabsmobs.sever.ability.AbilityHandler;
import com.eeeab.eeeabsmobs.sever.block.BlockVoidCrackPortalLock;
import com.eeeab.eeeabsmobs.sever.capability.AbilityCapability;
import com.eeeab.eeeabsmobs.sever.handler.ModConfigHandler;
import com.eeeab.eeeabsmobs.sever.init.SoundInit;
import com.eeeab.eeeabsmobs.sever.util.TranslateUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.List;
import java.util.Optional;

public class ItemGuardianCore extends Item {

    public ItemGuardianCore() {
        super(new Item.Properties().rarity(Rarity.EPIC).defaultDurability(100).fireResistant());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        AbilityCapability.IAbilityCapability capability = AbilityHandler.INSTANCE.getAbilityCapability(player);
        ItemStack itemStack = player.getItemInHand(hand);
        if (capability != null) {
            player.startUsingItem(hand);
            if (itemStack.getDamageValue() + 1 < itemStack.getMaxDamage()) {
                if (!level.isClientSide)
                    AbilityHandler.INSTANCE.sendAbilityMessage(player, AbilityHandler.GUARDIAN_LASER_ABILITY);
                itemStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                player.playSound(SoundInit.NAMELESS_GUARDIAN_ACCUMULATING.get(), 1.5F, (player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.2F + 3.5F);
                player.startUsingItem(hand);
                return InteractionResultHolder.consume(player.getItemInHand(hand));
            } else {
                try {
                    Optional.ofNullable(capability.getAbilitiesMap().get(AbilityHandler.GUARDIAN_LASER_ABILITY)).orElseThrow().end();
                } catch (Exception ignored) {
                    //EEEABMobs.LOGGER.error("An unforeseen error has occurred! Item Id: '{}', Exception Stack Msg: ", this.getDescriptionId(itemStack), e);
                }
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeCharged) {
        if (livingEntity instanceof Player player) {
            Ability<?> guardianLaserAbility = AbilityHandler.INSTANCE.getAbility(player, AbilityHandler.GUARDIAN_LASER_ABILITY);
            if (guardianLaserAbility != null && guardianLaserAbility.isUsing()) {
                guardianLaserAbility.end();
            }
        }
        super.releaseUsing(stack, level, livingEntity, timeCharged);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player entity = context.getPlayer();
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        ItemStack itemstack = context.getItemInHand();
        Level world = context.getLevel();
        if (entity != null && entity.mayUseItemAt(pos, context.getClickedFace(), itemstack) && itemstack.getDamageValue() == 0) {
            BlockState clickedState = world.getBlockState(pos);
            if (clickedState.getBlock() instanceof BlockVoidCrackPortalLock) {
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public boolean canBeDepleted() {
        return true;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(TranslateUtils.simpleItemText(this.getDescriptionId(), Component.literal(ModConfigHandler.COMMON.items.guardianCoreConfig1.get() + "").withStyle(ChatFormatting.YELLOW)));
        //tooltip.addAll(TranslateUtils.complexText(TranslateUtils.ITEM_PREFIX, 2, ChatFormatting.GRAY, this.getDescriptionId()));
        tooltip.addAll(TranslateUtils.complexText(TranslateUtils.ITEM_PREFIX, 1, ChatFormatting.GRAY, this.getDescriptionId()));
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                if (entityLiving.isUsingItem() && entityLiving.getUsedItemHand() == hand) {
                    return HumanoidModel.ArmPose.CROSSBOW_HOLD;
                }
                return HumanoidModel.ArmPose.ITEM;
            }
        });
    }
}
