package net.furryplayplace.cottonframework.api.events.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player stops damaging a Block.
 * @see BlockDamageEvent
 */
public class BlockDamageAbortEvent extends BlockEvent {

    private final PlayerEntity player;
    private final ItemStack itemstack;

    public BlockDamageAbortEvent(@NotNull final PlayerEntity player, @NotNull final Block block, @NotNull final ItemStack itemInHand) {
        super(block);
        this.player = player;
        this.itemstack = itemInHand;
    }

    /**
     * Gets the player that stopped damaging the block involved in this event.
     *
     * @return The player that stopped damaging the block
     */
    @NotNull
    public PlayerEntity getPlayer() {
        return player;
    }

    /**
     * Gets the ItemStack for the item currently in the player's hand.
     *
     * @return The ItemStack for the item currently in the player's hand
     */
    @NotNull
    public ItemStack getItemInHand() {
        return itemstack;
    }
}
