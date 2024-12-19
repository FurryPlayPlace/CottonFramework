package net.furryplayplace.cotton.api.events.world;

import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.NotNull;

public class ChunkPopulateEvent extends ChunkEvent {
    public ChunkPopulateEvent(@NotNull final WorldChunk chunk) {
        super(chunk);
    }
}
