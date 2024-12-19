package net.furryplayplace.cotton.api.events.player;

import com.google.common.base.Preconditions;
import net.furryplayplace.cotton.api.Location;
import net.furryplayplace.cotton.api.events.Cancellable;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerMoveEvent extends PlayerEvent implements Cancellable {
    private boolean cancel = false;
    private Location from;
    private Location to;

    public PlayerMoveEvent(@NotNull final PlayerEntity player, @NotNull final Location from, @Nullable final Location to) {
        super(player);
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @NotNull
    public Location getFrom() {
        return from;
    }

    public void setFrom(@NotNull Location from) {
        validateLocation(from);
        this.from = from;
    }

    @NotNull // Paper
    public Location getTo() {
        return to;
    }

    public void setTo(@NotNull Location to) {
        validateLocation(to);
        this.to = to;
    }

    public boolean hasChangedPosition() {
        return hasExplicitlyChangedPosition() || !from.getWorld().equals(to.getWorld());
    }

    public boolean hasExplicitlyChangedPosition() {
        return from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ();
    }

    public boolean hasChangedBlock() {
        return hasExplicitlyChangedBlock() || !from.getWorld().equals(to.getWorld());
    }

    public boolean hasExplicitlyChangedBlock() {
        return from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ();
    }

    public boolean hasChangedOrientation() {
        return from.getPitch() != to.getPitch() || from.getYaw() != to.getYaw();
    }

    private void validateLocation(@NotNull Location loc) {
        Preconditions.checkArgument(loc != null, "Cannot use null location!");
        Preconditions.checkArgument(loc.getWorld() != null, "Cannot use null location with null world!");
    }
}
