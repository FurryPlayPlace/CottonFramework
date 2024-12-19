package net.furryplayplace.cotton.api.events.player;

import net.furryplayplace.cotton.api.events.Cancellable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerBucketEntityEvent extends PlayerEvent implements Cancellable {
    private boolean cancelled;
    private final Entity entity;
    private final ItemStack originalBucket;
    private final ItemStack entityBucket;
    private final EquipmentSlot hand;

    public PlayerBucketEntityEvent(@NotNull PlayerEntity player, @NotNull Entity entity, @NotNull ItemStack originalBucket, @NotNull ItemStack entityBucket, @NotNull EquipmentSlot hand) {
        super(player);
        this.entity = entity;
        this.originalBucket = originalBucket;
        this.entityBucket = entityBucket;
        this.hand = hand;
    }

    @NotNull
    public Entity getEntity() {
        return entity;
    }

    @NotNull
    public ItemStack getOriginalBucket() {
        return originalBucket;
    }

    @NotNull
    public ItemStack getEntityBucket() {
        return entityBucket;
    }

    @NotNull
    public EquipmentSlot getHand() {
        return hand;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
