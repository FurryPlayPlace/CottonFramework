package net.furryplayplace.cottonframework.api.events.server;


import net.furryplayplace.cottonframework.api.events.Event;

/**
 * Miscellaneous server events
 */
public abstract class ServerEvent extends Event {

    public ServerEvent(boolean isAsync) {
        super(isAsync);
    }
}
