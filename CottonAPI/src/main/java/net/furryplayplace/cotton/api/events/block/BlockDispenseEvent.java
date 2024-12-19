package net.furryplayplace.cotton.api.events.block;

import net.furryplayplace.cotton.api.events.Cancellable;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

/**
 * Called when an item is dispensed from a block.
 * <p>
 * If a Block Dispense event is cancelled, the block will not dispense the
 * item.
 */
public class BlockDispenseEvent extends BlockEvent implements Cancellable {
    private boolean cancelled = false;
    private ItemStack item;
    private Vector3d velocity;

    public BlockDispenseEvent(@NotNull final Block block, @NotNull final ItemStack dispensed, @NotNull final Vector3d velocity) {
        super(block);
        this.item = dispensed;
        this.velocity = velocity;
    }

    @NotNull
    public ItemStack getItem() {
        return item.copy();
    }

    public void setItem(@NotNull ItemStack item) {
        this.item = item;
    }

    @NotNull
    public Vector3d getVelocity() {
        return velocity;
    }

    public void setVelocity(@NotNull Vector3d vel) {
        velocity = vel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
