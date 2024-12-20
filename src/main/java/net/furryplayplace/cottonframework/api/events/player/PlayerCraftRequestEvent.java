/*
---------------------------------------------------------------------------------
File Name : PlayerCraftRequestEvent

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 20.12.2024
Last Modified : 20.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.api.events.player;

import lombok.Getter;
import net.furryplayplace.cottonframework.api.events.Cancellable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerCraftRequestEvent extends PlayerEvent implements Cancellable {

    private final Identifier recipeId;
    private final boolean craftAll;

    private boolean cancel;

    public PlayerCraftRequestEvent(@NotNull PlayerEntity who, Identifier recipeId, boolean craftAll) {
        super(who);
        this.recipeId = recipeId;
        this.craftAll = craftAll;
    }

    /**
     * Gets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return this.cancel;
    }

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancel true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}