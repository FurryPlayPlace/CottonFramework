package net.furryplayplace.cotton.api.events.world;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.furryplayplace.cotton.api.Location;
import net.furryplayplace.cotton.api.events.Cancellable;
import net.minecraft.entity.Entity;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a generic Mojang game event.
 *
 * Specific Bukkit events should be used where possible, this event is mainly
 * used internally by Sculk sensors.
 */
public class GenericGameEvent extends WorldEvent implements Cancellable {

    private final GameEvent event;
    private final Location location;
    private final Entity entity;
    /**
     * -- GETTER --
     *  Get the block radius to which this event will be broadcast.
     *
     * @return broadcast radius
     */
    @Getter
    private int radius;
    private boolean cancelled;

    public GenericGameEvent(@NotNull GameEvent event, @NotNull Location location, @Nullable Entity entity, int radius, boolean isAsync) {
        super(location.getWorld(), isAsync);
        this.event = event;
        this.location = location;
        this.entity = entity;
        this.radius = radius;
    }

    /**
     * Get the underlying event.
     *
     * @return the event
     */
    @NotNull
    public GameEvent getEvent() {
        return event;
    }

    /**
     * Get the location where the event occurred.
     *
     * @return event location
     */
    @NotNull
    public Location getLocation() {
        return location.clone(); // Paper - clone to avoid changes
    }

    /**
     * Get the entity which triggered this event, if present.
     *
     * @return triggering entity or null
     */
    @Nullable
    public Entity getEntity() {
        return entity;
    }

    /**
     * Set the radius to which the event should be broadcast.
     *
     * @param radius radius, must be greater than or equal to 0
     */
    public void setRadius(int radius) {
        Preconditions.checkArgument(radius >= 0, "Radius must be >= 0");
        this.radius = radius;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
