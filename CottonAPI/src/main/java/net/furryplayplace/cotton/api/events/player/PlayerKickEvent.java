package net.furryplayplace.cotton.api.events.player;

import net.furryplayplace.cotton.api.events.Cancellable;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerKickEvent extends PlayerEvent implements Cancellable {
    private boolean cancel;

    public PlayerKickEvent(@NotNull final PlayerEntity playerKicked) {
        super(playerKicked);
        this.cancel = false;
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
