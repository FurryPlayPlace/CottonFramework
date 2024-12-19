package net.furryplayplace.cotton.api.events.block;

import net.furryplayplace.cotton.api.events.Cancellable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlockDropItemEvent extends BlockEvent implements Cancellable {
    private final PlayerEntity player;
    private boolean cancel;
    private final BlockState blockState;
    private final List<Item> items;

    public BlockDropItemEvent(@NotNull Block block, @NotNull BlockState blockState, @NotNull PlayerEntity player, @NotNull List<Item> items) {
        super(block);
        this.blockState = blockState;
        this.player = player;
        this.items = items;
    }

    @NotNull
    public PlayerEntity getPlayer() {
        return player;
    }

    @NotNull
    public BlockState getBlockState() {
        return blockState;
    }

    @NotNull
    public List<Item> getItems() {
        return items;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
