package net.furryplayplace.cottonframework.api.events.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

public class PlayerInteractAtEntityEvent extends PlayerInteractEntityEvent {
    private final Vector3d position;

    public PlayerInteractAtEntityEvent(@NotNull PlayerEntity who, @NotNull Entity clickedEntity, @NotNull Vector3d position) {
        this(who, clickedEntity, position, EquipmentSlot.MAINHAND);
    }

    public PlayerInteractAtEntityEvent(@NotNull PlayerEntity who, @NotNull Entity clickedEntity, @NotNull Vector3d position, @NotNull EquipmentSlot hand) {
        super(who, clickedEntity, hand);
        this.position = position;
    }

    @NotNull
    public Vector3d getClickedPosition() {
        return position;
    }
}
