package net.furryplayplace.cottonframework.api.events.player;

import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerQuitEvent extends PlayerEvent {
    public PlayerQuitEvent(@NotNull final PlayerEntity who) {
        super(who);
    }
}
