package net.furryplayplace.cottonframework.api.events.player;

import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

public class PlayerLoginEvent extends PlayerEvent {
    private final InetAddress address;
    private final InetAddress realAddress;
    private final String hostname;

    public PlayerLoginEvent(@NotNull final PlayerEntity player, @NotNull final String hostname, @NotNull final InetAddress address, final @NotNull InetAddress realAddress) {
        super(player);
        this.hostname = hostname;
        this.address = address;
        this.realAddress = realAddress;
    }

    public PlayerLoginEvent(@NotNull final PlayerEntity player, @NotNull final String hostname, @NotNull final InetAddress address) {
        this(player, hostname, address, address);
    }

    @NotNull
    public String getHostname() {
        return hostname;
    }

    @NotNull
    public InetAddress getAddress() {
        return address;
    }

    @NotNull
    public InetAddress getRealAddress() {
        return realAddress;
    }
}
