package net.furryplayplace.cotton.api.events.player;

import net.furryplayplace.cotton.api.events.Cancellable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

/**
 * This event is fired when the player is leaving a bed.
 */
public class PlayerBedLeaveEvent extends PlayerEvent implements Cancellable {
    private final Block bed;
    private boolean setBedSpawn;
    private boolean cancelled;

    public PlayerBedLeaveEvent(@NotNull final PlayerEntity who, @NotNull final Block bed, boolean setBedSpawn) {
        super(who);
        this.bed = bed;
        this.setBedSpawn = setBedSpawn;
    }

    @NotNull
    public Block getBed() {
        return bed;
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
