package net.furryplayplace.cottonframework.api.events.player;

import net.furryplayplace.cottonframework.api.events.Cancellable;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerItemHeldEvent extends PlayerEvent implements Cancellable {
    private boolean cancel = false;
    private final int previous;
    private final int current;

    public PlayerItemHeldEvent(@NotNull final PlayerEntity player, final int previous, final int current) {
        super(player);
        this.previous = previous;
        this.current = current;
    }

    public int getPreviousSlot() {
        return previous;
    }

    public int getNewSlot() {
        return current;
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
