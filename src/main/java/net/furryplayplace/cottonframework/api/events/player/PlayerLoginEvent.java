package net.furryplayplace.cottonframework.api.events.player;

import net.furryplayplace.cottonframework.api.events.Event;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.ClientConnection;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

public class PlayerLoginEvent extends Event {
    private final ClientConnection connection;
    private final String hostname;

    public PlayerLoginEvent(@NotNull final ClientConnection connection, @NotNull final String hostname) {
        this.connection = connection;
        this.hostname = hostname;
    }

    @NotNull
    public String getHostname() {
        return hostname;
    }
}
