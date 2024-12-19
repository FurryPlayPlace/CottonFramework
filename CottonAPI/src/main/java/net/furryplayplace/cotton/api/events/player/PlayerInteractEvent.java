package net.furryplayplace.cotton.api.events.player;

import net.furryplayplace.cotton.api.events.Cancellable;
import net.furryplayplace.cotton.api.events.block.Action;
import net.minecraft.block.Block;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

public class PlayerInteractEvent extends PlayerEvent implements Cancellable {
    protected ItemStack item;
    protected Action action;
    protected Block blockClicked;
    protected BlockFace blockFace;
    private Result useClickedBlock;
    private Result useItemInHand;
    private EquipmentSlot hand;
    private Vector3d clickedPosistion;

    public PlayerInteractEvent(@NotNull final PlayerEntity who, @NotNull final Action action, @Nullable final ItemStack item, @Nullable final Block clickedBlock, @NotNull final BlockFace clickedFace) {
        this(who, action, item, clickedBlock, clickedFace, EquipmentSlot.MAINHAND);
    }

    public PlayerInteractEvent(@NotNull final PlayerEntity who, @NotNull final Action action, @Nullable final ItemStack item, @Nullable final Block clickedBlock, @NotNull final BlockFace clickedFace, @Nullable final EquipmentSlot hand) {
        this(who, action, item, clickedBlock, clickedFace, hand, null);
    }

    public PlayerInteractEvent(@NotNull final PlayerEntity who, @NotNull final Action action, @Nullable final ItemStack item, @Nullable final Block clickedBlock, @NotNull final BlockFace clickedFace, @Nullable final EquipmentSlot hand, @Nullable final Vector3d clickedPosition) {
        super(who);
        this.action = action;
        this.item = item;
        this.blockClicked = clickedBlock;
        this.blockFace = clickedFace;
        this.hand = hand;
        this.clickedPosistion = clickedPosition;

        useItemInHand = Result.DEFAULT;
        useClickedBlock = clickedBlock == null ? Result.DENY : Result.ALLOW;
    }

    @NotNull
    public Action getAction() {
        return action;
    }

    @Deprecated
    @Override
    public boolean isCancelled() {
        return useInteractedBlock() == Result.DENY;
    }

    @Override
    public void setCancelled(boolean cancel) {
        setUseInteractedBlock(cancel ? Result.DENY : useInteractedBlock() == Result.DENY ? Result.DEFAULT : useInteractedBlock());
        setUseItemInHand(cancel ? Result.DENY : useItemInHand() == Result.DENY ? Result.DEFAULT : useItemInHand());
    }

    @Nullable
    public ItemStack getItem() {
        return this.item;
    }

    public boolean hasBlock() {
        return this.blockClicked != null;
    }

    public boolean hasItem() {
        return this.item != null;
    }

    @Nullable
    public Block getClickedBlock() {
        return blockClicked;
    }

    @NotNull
    public BlockFace getBlockFace() {
        return blockFace;
    }

    @NotNull
    public Result useInteractedBlock() {
        return useClickedBlock;
    }

    /**
     * @param useInteractedBlock the action to take with the interacted block
     */
    public void setUseInteractedBlock(@NotNull Result useInteractedBlock) {
        this.useClickedBlock = useInteractedBlock;
    }

    @NotNull
    public Result useItemInHand() {
        return useItemInHand;
    }

    public void setUseItemInHand(@NotNull Result useItemInHand) {
        this.useItemInHand = useItemInHand;
    }

    @Nullable
    public EquipmentSlot getHand() {
        return hand;
    }

    @Nullable
    @Deprecated
    public Vector3d getClickedPosition() {
        return clickedPosistion;
    }
}
