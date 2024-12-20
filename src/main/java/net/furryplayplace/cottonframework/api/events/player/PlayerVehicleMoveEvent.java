/*
---------------------------------------------------------------------------------
File Name : PlayerVehicleMoveEvent

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 20.12.2024
Last Modified : 20.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.api.events.player;

import net.furryplayplace.cottonframework.api.Location;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerVehicleMoveEvent extends PlayerMoveEvent {
    public PlayerVehicleMoveEvent(@NotNull PlayerEntity player, @NotNull Location from, @Nullable Location to) {
        super(player, from, to);
    }
}