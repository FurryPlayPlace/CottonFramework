package net.furryplayplace.cottonframework.api.events.player;

import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinEvent extends PlayerEvent {
    public PlayerJoinEvent(@NotNull final PlayerEntity playerJoined) {
        super(playerJoined);
    }
}
