package net.furryplayplace.cottonframework.api.events.player;

import net.furryplayplace.cottonframework.api.events.Cancellable;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

public class PlayerVelocityEvent extends PlayerEvent implements Cancellable {
    private boolean cancel = false;
    private Vector3d velocity;

    public PlayerVelocityEvent(@NotNull final PlayerEntity player, @NotNull final Vector3d velocity) {
        super(player);
        this.velocity = velocity;
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
    public Vector3d getVelocity() {
        return velocity;
    }

    public void setVelocity(@NotNull Vector3d velocity) {
        this.velocity = velocity;
    }
}
