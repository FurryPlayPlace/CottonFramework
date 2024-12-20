package net.furryplayplace.cottonframework.api.events.player;

import net.furryplayplace.cottonframework.api.events.Cancellable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;

public class PlayerDropItemEvent extends PlayerEvent implements Cancellable {
    private final Item drop;
    private boolean cancel = false;

    public PlayerDropItemEvent(@NotNull final PlayerEntity player, @NotNull final Item drop) {
        super(player);
        this.drop = drop;
    }

    @NotNull
    public Item getItemDrop() {
        return drop;
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
