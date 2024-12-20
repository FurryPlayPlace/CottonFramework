package net.furryplayplace.cottonframework.api.events.weather;

import net.furryplayplace.cottonframework.api.events.Cancellable;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

/**
 * Stores data for weather changing in a world
 */
public class WeatherChangeEvent extends WeatherEvent implements Cancellable {
    private boolean canceled;
    private final boolean to;
    private final Cause cause;

    public WeatherChangeEvent(@NotNull final ServerWorld world, final boolean to, @NotNull Cause cause) {
        super(world);
        this.to = to;
        this.cause = cause;
    }

    @Deprecated
    public WeatherChangeEvent(@NotNull final ServerWorld world, final boolean to) {
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

    public boolean toWeatherState() {
        return to;
    }

    @NotNull
    public Cause getCause() {
        return cause;
    }

    public enum Cause {
        COMMAND,
        NATURAL,
        SLEEP,
        PLUGIN,
        UNKNOWN
    }
}
