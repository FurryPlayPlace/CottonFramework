package net.furryplayplace.cotton.api.events.player;

import lombok.Getter;
import lombok.Setter;
import net.furryplayplace.cotton.api.Location;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerPortalEvent extends PlayerTeleportEvent {
    private int getSearchRadius = 128;
    @Setter
    private boolean canCreatePortal = true;
    @Getter
    @Setter
    private int creationRadius = 16;

    public PlayerPortalEvent(@NotNull final PlayerEntity player, @NotNull final Location from, @Nullable final Location to) {
        super(player, from, to);
    }

    public PlayerPortalEvent(@NotNull PlayerEntity player, @NotNull Location from, @Nullable Location to, @NotNull TeleportCause cause) {
        super(player, from, to, cause);
    }

    public PlayerPortalEvent(@NotNull PlayerEntity player, @NotNull Location from, @Nullable Location to, @NotNull TeleportCause cause, int getSearchRadius, boolean canCreatePortal, int creationRadius) {
        super(player, from, to, cause);
        this.getSearchRadius = getSearchRadius;
        this.canCreatePortal = canCreatePortal;
        this.creationRadius = creationRadius;
    }

    @Override
    public @NotNull Location getTo() {
        return super.getTo();
    }

    @Override
    public void setTo(@NotNull final Location to) {
        super.setTo(to);
    }

    public void setSearchRadius(int searchRadius) {
        this.getSearchRadius = searchRadius;
    }

    public int getSearchRadius() {
        return getSearchRadius;
    }

    public boolean getCanCreatePortal() {
        return canCreatePortal;
    }
}
