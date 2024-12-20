package net.furryplayplace.cottonframework.api.events.world;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.furryplayplace.cottonframework.api.Location;
import net.furryplayplace.cottonframework.api.events.Cancellable;
import net.minecraft.entity.Entity;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class GenericGameEvent extends WorldEvent implements Cancellable {
    private final GameEvent event;
    private final Location location;
    private final Entity entity;

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

    @NotNull
    public GameEvent getEvent() {
        return event;
    }

    @NotNull
    public Location getLocation() {
        return location.clone(); // Paper - clone to avoid changes
    }

    @Nullable
    public Entity getEntity() {
        return entity;
    }

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
