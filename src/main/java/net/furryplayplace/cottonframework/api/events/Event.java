/*
---------------------------------------------------------------------------------
File Name : Event

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 19.12.2024
Last Modified : 19.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.api.events;

public abstract class Event {
    private final boolean async;

    public Event() {
        this(false);
    }

    public Event(boolean isAsync) {
        this.async = isAsync;
    }

    public final boolean isAsynchronous() {
        return async;
    }

    public enum Result {
        DENY,
        DEFAULT,
        ALLOW;
    }
}