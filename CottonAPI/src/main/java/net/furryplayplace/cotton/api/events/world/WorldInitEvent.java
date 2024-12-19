package net.furryplayplace.cotton.api.events.world;

import net.minecraft.world.World;

public class WorldInitEvent extends WorldEvent {
    public WorldInitEvent(World world) {
        super(world);
    }
}
