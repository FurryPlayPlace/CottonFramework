package net.furryplayplace.cottonframework.api.events.player;

import net.furryplayplace.cottonframework.api.events.Event;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a player related event
 */
public abstract class PlayerEvent extends Event {
    protected PlayerEntity player;

    public PlayerEvent(@NotNull final PlayerEntity who) {
        player = who;
    }

    public PlayerEvent(@NotNull final PlayerEntity who, boolean async) { // Paper - public
        super(async);
        player = who;

    }

    @NotNull
    public final PlayerEntity getPlayer() {
        return player;
    }
}
