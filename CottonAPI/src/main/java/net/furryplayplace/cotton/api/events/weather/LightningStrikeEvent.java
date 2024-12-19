package net.furryplayplace.cotton.api.events.weather;

import net.furryplayplace.cotton.api.events.Cancellable;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.NotNull;

/**
 * Stores data for lightning striking
 */
public class LightningStrikeEvent extends WeatherEvent implements Cancellable {
    private boolean canceled;
    private final LightningEntity bolt;
    private final Cause cause;

    @Deprecated
    public LightningStrikeEvent(@NotNull final ServerWorld world, @NotNull final LightningEntity bolt) {
        this(world, bolt, Cause.UNKNOWN);
    }

    public LightningStrikeEvent(@NotNull final ServerWorld world, @NotNull final LightningEntity bolt, @NotNull final Cause cause) {
        super(world);
        this.bolt = bolt;
        this.cause = cause;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    @NotNull
    public LightningEntity getLightning() {
        return bolt;
    }

    @NotNull
    public Cause getCause() {
        return cause;
    }

    public enum Cause {
        COMMAND,
        CUSTOM,
        SPAWNER,
        TRIDENT,
        TRAP,
        WEATHER,
        ENCHANTMENT,
        UNKNOWN;
    }
}
