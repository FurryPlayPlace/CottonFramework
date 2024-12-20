package net.furryplayplace.cottonframework.api.events.player;

import net.furryplayplace.cottonframework.api.events.Cancellable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerInteractEntityEvent extends PlayerEvent implements Cancellable {
    protected Entity clickedEntity;
    boolean cancelled = false;
    private EquipmentSlot hand;

    public PlayerInteractEntityEvent(@NotNull final PlayerEntity who, @NotNull final Entity clickedEntity) {
        this(who, clickedEntity, EquipmentSlot.MAINHAND);
    }

    public PlayerInteractEntityEvent(@NotNull final PlayerEntity who, @NotNull final Entity clickedEntity, @NotNull final EquipmentSlot hand) {
        super(who);
        this.clickedEntity = clickedEntity;
        this.hand = hand;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @NotNull
    public Entity getRightClicked() {
        return this.clickedEntity;
    }

    @NotNull
    public EquipmentSlot getHand() {
        return hand;
    }
}
