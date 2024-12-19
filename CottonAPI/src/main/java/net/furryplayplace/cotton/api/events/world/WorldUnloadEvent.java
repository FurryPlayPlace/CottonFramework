package net.furryplayplace.cotton.api.events.world;

import net.furryplayplace.cotton.api.events.Cancellable;
import net.minecraft.world.World;

public class WorldUnloadEvent extends WorldEvent implements Cancellable {
    private boolean isCancelled;

    public WorldUnloadEvent(World world) {
        super(world);
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }
}
