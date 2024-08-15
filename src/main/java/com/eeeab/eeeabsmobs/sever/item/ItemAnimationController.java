package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.animate.server.inventory.AnimationControllerMenu;
import com.eeeab.eeeabsmobs.sever.util.EMTUtils;
import com.eeeab.eeeabsmobs.sever.util.EMTabGroup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemAnimationController extends Item {
    private static final String NBT_ENTITY_ID = "animationEntityId";

    public ItemAnimationController() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON).tab(EMTabGroup.TABS));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof EMAnimatedEntity) {
            CompoundTag compoundTag = stack.getOrCreateTag();
            compoundTag.putInt(NBT_ENTITY_ID, entity.getId());
            if (player.level.isClientSide) {
                player.displayClientMessage(EMTUtils.simpleOtherText(this.getDescriptionId(), null, entity.getName().getString()), true);
            }
        }
        return true;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return stack.hasTag() && stack.getTag().get(NBT_ENTITY_ID) != null || super.isFoil(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(EMTUtils.simpleItemText(this.getDescriptionId(), EMTUtils.STYLE_GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (itemStack.hasTag()) {
            Entity entity = level.getEntity(itemStack.getTag().getInt(NBT_ENTITY_ID));
            if (player instanceof ServerPlayer serverPlayer && entity instanceof LivingEntity livingEntity && entity instanceof EMAnimatedEntity && entity.isAlive()) {
                NetworkHooks.openScreen(serverPlayer, new MenuProvider() {
                    @Override
                    public @NotNull Component getDisplayName() {
                        return livingEntity.getName();
                    }

                    @Override
                    public @NotNull AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
                        return new AnimationControllerMenu(containerId, playerInventory, livingEntity);
                    }
                }, buf -> buf.writeInt(livingEntity.getId()));
            }
            if (entity == null || !entity.isAlive()) {
                itemStack.getTag().remove(NBT_ENTITY_ID);
            } else {
                player.getCooldowns().addCooldown(this, 20);
            }
        }
        return InteractionResultHolder.success(itemStack);
    }
}
