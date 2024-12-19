package net.furryplayplace.cotton.api.events.player;

import net.furryplayplace.cotton.api.Location;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerRespawnEvent extends PlayerEvent {
    private final Location respawnLocation;
    private final boolean isBedSpawn;
    private final boolean isAnchorSpawn;

    public PlayerRespawnEvent(@NotNull final PlayerEntity respawnPlayer, @NotNull final Location respawnLocation, final boolean isBedSpawn, final boolean isAnchorSpawn) {
        super(respawnPlayer);
        this.respawnLocation = respawnLocation;
        this.isBedSpawn = isBedSpawn;
        this.isAnchorSpawn = isAnchorSpawn;
    }

    @NotNull
    public Location getRespawnLocation() {
        return this.respawnLocation;
    }


    public boolean isBedSpawn() {
        return this.isBedSpawn;
    }

    public boolean isAnchorSpawn() {
        return isAnchorSpawn;
    }
}
