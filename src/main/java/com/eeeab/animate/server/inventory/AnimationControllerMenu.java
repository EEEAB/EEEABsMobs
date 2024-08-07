package com.eeeab.animate.server.inventory;

import com.eeeab.eeeabsmobs.sever.init.MenuInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;


public class AnimationControllerMenu extends AbstractContainerMenu {
    public final Entity animationEntity;

    public AnimationControllerMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getEntity(extraData.readInt()));
    }

    public AnimationControllerMenu(int id, Inventory inv, Entity entity) {
        super(MenuInit.ANIMATION_CONTROLLER.get(), id);
        animationEntity = entity;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        return ItemStack.EMPTY;
    }
}
