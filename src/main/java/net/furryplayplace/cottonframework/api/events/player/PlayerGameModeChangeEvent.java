package net.furryplayplace.cottonframework.api.events.player;

import net.furryplayplace.cottonframework.api.events.Cancellable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameMode;
import org.jetbrains.annotations.NotNull;

public class PlayerGameModeChangeEvent extends PlayerEvent implements Cancellable {
    private boolean cancelled;
    private final GameMode newGameMode;

    public PlayerGameModeChangeEvent(@NotNull final PlayerEntity player, @NotNull final GameMode newGameMode) {
        super(player);
        this.newGameMode = newGameMode;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    public GameMode getNewGameMode() {
        return newGameMode;
    }
}
