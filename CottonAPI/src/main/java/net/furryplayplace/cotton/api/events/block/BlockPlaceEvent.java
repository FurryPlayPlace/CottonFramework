package net.furryplayplace.cotton.api.events.block;

import net.furryplayplace.cotton.api.events.Cancellable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BlockPlaceEvent extends BlockEvent implements Cancellable {
    protected boolean cancel;
    protected boolean canBuild;
    protected Block placedAgainst;
    protected BlockState replacedBlockState;
    protected ItemStack itemInHand;
    protected PlayerEntity player;
    protected EquipmentSlot hand;

    @Deprecated
    public BlockPlaceEvent(@NotNull final Block placedBlock, @NotNull final BlockState replacedBlockState, @NotNull final Block placedAgainst, @NotNull final ItemStack itemInHand, @NotNull final PlayerEntity thePlayer, final boolean canBuild) {
        this(placedBlock, replacedBlockState, placedAgainst, itemInHand, thePlayer, canBuild, EquipmentSlot.MAINHAND);
    }

    public BlockPlaceEvent(@NotNull final Block placedBlock, @NotNull final BlockState replacedBlockState, @NotNull final Block placedAgainst, @NotNull final ItemStack itemInHand, @NotNull final PlayerEntity thePlayer, final boolean canBuild, @NotNull final EquipmentSlot hand) {
        super(placedBlock);
        this.placedAgainst = placedAgainst;
        this.itemInHand = itemInHand;
        this.player = thePlayer;
        this.replacedBlockState = replacedBlockState;
        this.canBuild = canBuild;
        this.hand = hand;
        cancel = false;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @NotNull
    public PlayerEntity getPlayer() {
        return player;
    }

    @NotNull
    public Block getBlockPlaced() {
        return getBlock();
    }

    @NotNull
    public BlockState getBlockReplacedState() {
        return this.replacedBlockState;
    }

    @NotNull
    public Block getBlockAgainst() {
        return placedAgainst;
    }

    @NotNull
    public ItemStack getItemInHand() {
        return itemInHand;
    }

    @NotNull
    public EquipmentSlot getHand() {
        return this.hand;
    }

    public boolean canBuild() {
        return this.canBuild;
    }

    public void setBuild(boolean canBuild) {
        this.canBuild = canBuild;
    }
}
