package net.furryplayplace.cottonframework.api.events.server;

import com.google.common.base.Preconditions;
import lombok.Getter;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.util.Iterator;

public class ServerListPingEvent extends ServerEvent implements Iterable<PlayerEntity> {
    private final String hostname;
    private final InetAddress address;
    @Getter
    private int maxPlayers;

    public ServerListPingEvent(@NotNull final String hostname, @NotNull final InetAddress address, final int numPlayers, final int maxPlayers) {
        super(true);
        Preconditions.checkArgument(numPlayers >= 0, "Cannot have negative number of players online", numPlayers);
        this.hostname = hostname;
        this.address = address;
        this.maxPlayers = maxPlayers;
    }

    @NotNull
    public String getHostname() {
        return hostname;
    }

    @NotNull
    public InetAddress getAddress() {
        return address;
    }

    @Deprecated
    public boolean shouldSendChatPreviews() {
        return false;
    }

    @Override
    public @NotNull Iterator<PlayerEntity> iterator() {
        throw new UnsupportedOperationException();
    }
}
