package net.furryplayplace.cotton.api.events.world;

import net.minecraft.world.World;

public class WorldSaveEvent extends WorldEvent {
    public WorldSaveEvent(World world) {
        super(world);
    }
}
