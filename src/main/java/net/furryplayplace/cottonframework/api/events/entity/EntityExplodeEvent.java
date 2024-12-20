package net.furryplayplace.cottonframework.api.events.entity;

import lombok.Getter;
import lombok.Setter;
import net.furryplayplace.cottonframework.api.Location;
import net.furryplayplace.cottonframework.api.events.Cancellable;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EntityExplodeEvent extends EntityEvent implements Cancellable {
    private boolean cancel;
    private final Location location;
    private final List<BlockPos> blocks;
    @Getter
    @Setter
    private float yield;

    public EntityExplodeEvent(@NotNull final Entity what, @NotNull final Location location, @NotNull final List<BlockPos> blocks, final float yield) {
        super(what);
        this.location = location;
        this.blocks = blocks;
        this.yield = yield;
        this.cancel = false;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @NotNull
    public List<BlockPos> blockList() {
        return blocks;
    }

    @NotNull
    public Location getLocation() {
        return location.clone();
    }
}
