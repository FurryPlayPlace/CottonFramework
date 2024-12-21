package net.furryplayplace.cottonframework.api.events.player;

import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerDeathEvent extends PlayerEvent {
    public PlayerDeathEvent(final @NotNull PlayerEntity player) {
        super(player);
    }
}
