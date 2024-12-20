package net.furryplayplace.cottonframework.api.events.player;

import net.minecraft.block.Block;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player fills a bucket
 */
public class PlayerBucketFillEvent extends PlayerBucketEvent {

    public PlayerBucketFillEvent(@NotNull final PlayerEntity who, @NotNull final Block block, @NotNull final Block blockClicked, @NotNull final BlockFace blockFace, @NotNull final Item bucket, @NotNull final ItemStack itemInHand, @NotNull final EquipmentSlot hand) {
        super(who, block, blockClicked, blockFace, bucket, itemInHand, hand);
    }
}
