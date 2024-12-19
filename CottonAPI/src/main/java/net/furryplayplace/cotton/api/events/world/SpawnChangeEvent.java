package net.furryplayplace.cotton.api.events.world;

import net.furryplayplace.cotton.api.Location;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class SpawnChangeEvent extends WorldEvent {
    private final Location previousLocation;

    public SpawnChangeEvent(@NotNull final World world, @NotNull final Location previousLocation) {
        super(world);
        this.previousLocation = previousLocation;
    }

    @NotNull
    public Location getPreviousLocation() {
        return previousLocation.clone(); // Paper - clone to avoid changes
    }
}
