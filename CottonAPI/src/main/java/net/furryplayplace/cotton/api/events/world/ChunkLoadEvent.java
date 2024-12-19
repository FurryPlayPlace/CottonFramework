package net.furryplayplace.cotton.api.events.world;

import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.NotNull;

public class ChunkLoadEvent extends ChunkEvent {
    private final boolean newChunk;

    public ChunkLoadEvent(@NotNull final WorldChunk chunk, final boolean newChunk) {
        super(chunk);
        this.newChunk = newChunk;
    }

    public boolean isNewChunk() {
        return newChunk;
    }
}
