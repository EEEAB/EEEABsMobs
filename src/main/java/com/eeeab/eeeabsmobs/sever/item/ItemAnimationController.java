package com.eeeab.eeeabsmobs.sever.item;

import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.animate.server.inventory.AnimationControllerMenu;
import com.eeeab.eeeabsmobs.sever.item.util.EMItemStackUtils;
import com.eeeab.eeeabsmobs.sever.util.EMTUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
    private static final String NBT_ENTITY_UUID = "animationEntityUUID";

    public ItemAnimationController() {
        super(new Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof EMAnimatedEntity) {
            CompoundTag tag = new CompoundTag();
            tag.putUUID(NBT_ENTITY_UUID, entity.getUUID());
            EMItemStackUtils.putNBT(stack, NBT_ENTITY_UUID, tag);
            if (player.level().isClientSide) {
                player.displayClientMessage(EMTUtils.simpleOtherText(this.getDescriptionId(), null, entity.getName().getString()), true);
            }
        }
        return true;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return EMItemStackUtils.hasNBT(stack, NBT_ENTITY_UUID) || super.isFoil(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @org.jetbrains.annotations.Nullable Level level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        tooltip.add(EMTUtils.simpleItemText(this.getDescriptionId(), EMTUtils.STYLE_GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        if (level instanceof ServerLevel serverLevel && itemStack.hasTag()) {
            Entity entity = serverLevel.getEntity(EMItemStackUtils.getNBT(itemStack, NBT_ENTITY_UUID).getUUID(NBT_ENTITY_UUID));
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
                EMItemStackUtils.removeNbt(itemStack, NBT_ENTITY_UUID);
                return InteractionResultHolder.fail(itemStack);
            } else {
                player.getCooldowns().addCooldown(this, 20);
            }
        }
        return InteractionResultHolder.success(itemStack);
    }
}
