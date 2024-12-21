package net.furryplayplace.cottonframework.api.events.player;

import net.furryplayplace.cottonframework.api.Location;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerRespawnEvent extends PlayerEvent {
    private final Location respawnLocation;

    public PlayerRespawnEvent(@NotNull final PlayerEntity respawnPlayer, @NotNull final Location respawnLocation) {
        super(respawnPlayer);
        this.respawnLocation = respawnLocation;
    }

    @NotNull
    public Location getRespawnLocation() {
        return this.respawnLocation;
    }
}
