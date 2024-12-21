package net.furryplayplace.cottonframework.api.events.player;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerItemBreakEvent extends PlayerEvent {
    private final ItemStack brokenItem;

    public PlayerItemBreakEvent(@NotNull final PlayerEntity player, @NotNull final ItemStack brokenItem) {
        super(player);
        this.brokenItem = brokenItem;
    }

    @NotNull
    public ItemStack getBrokenItem() {
        return brokenItem;
    }
}
