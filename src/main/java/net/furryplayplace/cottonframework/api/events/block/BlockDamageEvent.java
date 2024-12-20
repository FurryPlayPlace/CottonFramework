package net.furryplayplace.cottonframework.api.events.block;

import lombok.Setter;
import net.furryplayplace.cottonframework.api.events.Cancellable;
import net.minecraft.block.Block;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BlockDamageEvent extends BlockEvent implements Cancellable {
    private final PlayerEntity player;
    @Setter
    private boolean instaBreak;
    private boolean cancel;
    private final ItemStack itemstack;
    private final BlockFace blockFace; // Paper - Expose BlockFace

    public BlockDamageEvent(@NotNull final PlayerEntity player, @NotNull final Block block, @NotNull final BlockFace blockFace, @NotNull final ItemStack itemInHand, final boolean instaBreak) { // Paper - Expose BlockFace
        super(block);
        this.blockFace = blockFace;
        this.instaBreak = instaBreak;
        this.player = player;
        this.itemstack = itemInHand;
        this.cancel = false;
    }

    /**
     * Gets the player damaging the block involved in this event.
     *
     * @return The player damaging the block involved in this event
     */
    @NotNull
    public PlayerEntity getPlayer() {
        return player;
    }

    public boolean getInstantBreak() {
        return instaBreak;
    }


    @NotNull
    public ItemStack getItemInHand() {
        return itemstack;
    }

    @NotNull
    public BlockFace getBlockFace() {
        if (this.blockFace == null) {
            throw new IllegalStateException("BlockFace is not available for this event, most likely due to a bad constructor call by a plugin");
        }
        return this.blockFace;
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
