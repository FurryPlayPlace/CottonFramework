package net.furryplayplace.cotton.api.events.weather;

import net.furryplayplace.cotton.api.events.Cancellable;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

/**
 * Stores data for thunder state changing in a world
 */
public class ThunderChangeEvent extends WeatherEvent implements Cancellable {
    private boolean canceled;
    private final boolean to;
    private final Cause cause;

    public ThunderChangeEvent(@NotNull final ServerWorld world, final boolean to, @NotNull final Cause cause) {
        super(world);
        this.to = to;
        this.cause = cause;
    }

    @Deprecated
    public ThunderChangeEvent(@NotNull final ServerWorld world, final boolean to) {
        super(world);
        this.to = to;
        this.cause = Cause.UNKNOWN;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    public boolean toThunderState() {
        return to;
    }

    @NotNull
    public Cause getCause() {
        return this.cause;
    }

    public enum Cause {
        COMMAND,
        NATURAL,
        SLEEP,
        PLUGIN,
        UNKNOWN
    }
}
