package net.furryplayplace.cottonframework.api.events.server;

import org.jetbrains.annotations.NotNull;

/**
 * This event is called when either the server startup or reload has completed.
 */
public class ServerLoadEvent extends ServerEvent {

    /**
     * Represents the context in which the enclosing event has been completed.
     */
    public enum LoadType {
        STARTUP, RELOAD;
    }

    private final LoadType type;

    /**
     * Creates a {@code ServerLoadEvent} with a given loading type.
     *
     * @param type the context in which the server was loaded
     */
    public ServerLoadEvent(LoadType type) {
        super(false);
        this.type = type;
    }

    /**
     * Gets the context in which the server was loaded.
     *
     * @return the context in which the server was loaded
     */
    @NotNull
    public LoadType getType() {
        return type;
    }
}
