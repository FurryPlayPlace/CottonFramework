package net.furryplayplace.cottonframework.api.events.entity;

import net.furryplayplace.cottonframework.api.events.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an Entity-related event
 */
public abstract class EntityEvent extends Event {
    protected Entity entity;

    public EntityEvent(@NotNull final Entity what) {
        entity = what;
    }

    @NotNull
    public Entity getEntity() {
        return entity;
    }

    @NotNull
    public EntityType getEntityType() {
        return entity.getType();
    }
}
