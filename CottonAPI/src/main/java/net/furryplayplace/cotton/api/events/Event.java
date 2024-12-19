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

package net.furryplayplace.cotton.api.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Event {
    private String name;
    private final boolean async;

    public Event() {
        this(false);
    }

    public Event(boolean isAsync) {
        this.async = isAsync;
    }

    public String getEventName() {
        if (name == null) {
            name = getClass().getSimpleName();
        }

        return name;
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