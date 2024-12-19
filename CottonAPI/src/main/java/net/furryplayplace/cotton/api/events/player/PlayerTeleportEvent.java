package net.furryplayplace.cotton.api.events.player;

import net.furryplayplace.cotton.api.Location;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerTeleportEvent extends PlayerMoveEvent {
    private TeleportCause cause = TeleportCause.UNKNOWN;

    public PlayerTeleportEvent(@NotNull final PlayerEntity player, @NotNull final Location from, @Nullable final Location to) {
        super(player, from, to);
    }

    public PlayerTeleportEvent(@NotNull final PlayerEntity player, @NotNull final Location from, @Nullable final Location to, @NotNull final TeleportCause cause) {
        this(player, from, to);
        this.cause = cause;
    }

    @NotNull
    public TeleportCause getCause() {
        return cause;
    }

    public enum TeleportCause {
        ENDER_PEARL,
        COMMAND,
        PLUGIN,
        NETHER_PORTAL,
        END_PORTAL,
        SPECTATE,
        END_GATEWAY,
        CHORUS_FRUIT,
        DISMOUNT,
        EXIT_BED,
        UNKNOWN;
    }
}
