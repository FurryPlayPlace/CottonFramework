/*
---------------------------------------------------------------------------------
File Name : PlayerSetBeaconEffectEvent

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
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PlayerSetBeaconEffectEvent extends PlayerEvent implements Cancellable {

    @Getter
    private final Optional<RegistryEntry<StatusEffect>> primaryEffect;
    @Getter
    private final Optional<RegistryEntry<StatusEffect>> secondaryEffect;

    private boolean cancel;

    public PlayerSetBeaconEffectEvent(@NotNull PlayerEntity who, Optional<RegistryEntry<StatusEffect>> primaryEffect, Optional<RegistryEntry<StatusEffect>> secondaryEffect) {
        super(who);

        this.primaryEffect = primaryEffect;
        this.secondaryEffect = secondaryEffect;
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