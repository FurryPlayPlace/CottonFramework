package net.furryplayplace.cottonframework.api.events.world;

import lombok.Getter;
import net.furryplayplace.cottonframework.api.events.Event;
import net.minecraft.world.World;

@Getter
public abstract class WorldEvent extends Event {
    private final World world;

    public WorldEvent(World world) {
        this(world, false);
    }

    public WorldEvent(World world, boolean isAsync) {
        super(isAsync);
        this.world = world;
    }
}
