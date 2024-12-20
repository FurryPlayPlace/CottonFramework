package net.furryplayplace.cottonframework.api.events.player;

import net.furryplayplace.cottonframework.api.events.Cancellable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

/**
 * This event is fired when the player is leaving a bed.
 */
public class PlayerBedLeaveEvent extends PlayerEvent implements Cancellable {
    private boolean setBedSpawn;
    private boolean cancelled;

    public PlayerBedLeaveEvent(@NotNull final PlayerEntity who, boolean setBedSpawn) {
        super(who);
        this.setBedSpawn = setBedSpawn;
    }

    @Deprecated(forRemoval = true) // Paper - Unused
    public boolean shouldSetSpawnLocation() {
        return setBedSpawn;
    }

    @Deprecated(forRemoval = true) // Paper - Unused
    public void setSpawnLocation(boolean setBedSpawn) {
        this.setBedSpawn = setBedSpawn;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
