package net.furryplayplace.cotton.api.events.world;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Chunk related event
 */
public abstract class ChunkEvent extends WorldEvent {
    protected Chunk chunk;

    protected ChunkEvent(@NotNull final WorldChunk chunk) {
        super(chunk.getWorld());
        this.chunk = chunk;
    }

    @NotNull
    public Chunk getChunk() {
        return chunk;
    }
}
