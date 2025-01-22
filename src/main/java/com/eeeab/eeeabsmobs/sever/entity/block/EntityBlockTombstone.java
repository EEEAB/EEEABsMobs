package com.eeeab.eeeabsmobs.sever.entity.block;

import com.eeeab.eeeabsmobs.sever.init.BlockEntityInit;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

//TODO 未完成方块
//参考自: package net.minecraft.world.level.block.entity.JukeboxBlockEntity
public class EntityBlockTombstone extends BaseContainerBlockEntity {
    public int tick;
    private NonNullList<ItemStack> items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

    public EntityBlockTombstone(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityInit.ENTITY_TOMBSTONE.get(), blockPos, blockState);
    }

    public static void Ticker(Level level, BlockPos blockPos, BlockState blockState, EntityBlockTombstone tombstone) {
        tombstone.Tick(level, blockPos, blockState);
    }

    private void Tick(Level level, BlockPos blockPos, BlockState blockState) {
        tick++;
        if (!this.getItem(0).isEmpty() && this.getItem(0).getItem() == ItemInit.LOGO_ITEM.get()) {

        }
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.summoner.tombstone");
    }

    @Override
    protected AbstractContainerMenu createMenu(int p_58627_, Inventory inventory) {
        return null;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int size) {
        return ContainerHelper.removeItem(this.items, index, size);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack itemstack = this.items.get(index);
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.items.set(index, ItemStack.EMPTY);
            return itemstack;
        }
    }

    @Override
    public void setItem(int index, ItemStack itemStack) {
        if (level != null) {
            this.items.set(index, itemStack);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.tick = compoundTag.getInt("tickCount");
        ContainerHelper.loadAllItems(compoundTag, this.items);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ContainerHelper.saveAllItems(compoundTag, this.items);
        compoundTag.putInt("tickCount", this.tick);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        if (packet != null && packet.getTag() != null) {
            this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
            ContainerHelper.loadAllItems(packet.getTag(), this.items);
        }
    }
}
