package net.furryplayplace.cottonframework.api.events.player;

import net.furryplayplace.cottonframework.api.events.Cancellable;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerItemConsumeEvent extends PlayerEvent implements Cancellable {
    private boolean isCancelled = false;
    private final ItemStack item;
    private final EquipmentSlot hand;
    @Nullable private ItemStack replacement; // Paper

    public PlayerItemConsumeEvent(@NotNull final PlayerEntity player, @NotNull final ItemStack item, @NotNull final EquipmentSlot hand) {
        super(player);

        this.item = item;
        this.hand = hand;
    }

    @NotNull
    public ItemStack getItem() {
        return item.copy();
    }

    @NotNull
    public EquipmentSlot getHand() {
        return hand;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
}
