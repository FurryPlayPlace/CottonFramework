package net.furryplayplace.cotton.api.events.player;

import net.furryplayplace.cotton.api.events.Cancellable;
import net.minecraft.block.Block;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PlayerBucketEvent extends PlayerEvent implements Cancellable {
    private ItemStack itemStack;
    private boolean cancelled = false;
    private final Block block;
    private final Block blockClicked;
    private final BlockFace blockFace;
    private final Item bucket;
    private final EquipmentSlot hand;

    public PlayerBucketEvent(@NotNull final PlayerEntity who, @NotNull final Block block, @NotNull final Block blockClicked, @NotNull final BlockFace blockFace, @NotNull final Item bucket, @NotNull final ItemStack itemInHand, @NotNull final EquipmentSlot hand) {
        super(who);
        this.block = block;
        this.blockClicked = blockClicked;
        this.blockFace = blockFace;
        this.itemStack = itemInHand;
        this.bucket = bucket;
        this.hand = hand;
    }

    @NotNull
    public Item getBucket() {
        return bucket;
    }

    @Nullable
    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(@Nullable ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @NotNull
    public final Block getBlock() {
        return block;
    }

    @NotNull
    public Block getBlockClicked() {
        return blockClicked;
    }

    @NotNull
    public BlockFace getBlockFace() {
        return blockFace;
    }

    @NotNull
    public EquipmentSlot getHand() {
        return hand;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
