/*
---------------------------------------------------------------------------------
File Name : PlayerChatMessageEvent

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
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

@Getter
public class PlayerChatMessageEvent extends PlayerEvent implements Cancellable {

    private final SignedMessage signedMessage;
    private final Text textMessage;

    private boolean cancel;

    public PlayerChatMessageEvent(@NotNull PlayerEntity who, SignedMessage signedMessage, Text textMessage) {
        super(who);

        this.signedMessage = signedMessage;
        this.textMessage = textMessage;
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