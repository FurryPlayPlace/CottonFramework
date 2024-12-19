package net.furryplayplace.cotton.api.events.server;


import net.furryplayplace.cotton.api.events.Event;

/**
 * Miscellaneous server events
 */
public abstract class ServerEvent extends Event {

    public ServerEvent(boolean isAsync) {
        super(isAsync);
    }
}
