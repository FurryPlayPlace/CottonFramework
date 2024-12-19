package net.furryplayplace.cotton.api.events.world;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a chunk is unloaded
 */
@Setter
@Getter
public class ChunkUnloadEvent extends ChunkEvent {
    private boolean saveChunk;

    public ChunkUnloadEvent(@NotNull final WorldChunk chunk) {
        this(chunk, true);
    }

    public ChunkUnloadEvent(@NotNull WorldChunk chunk, boolean save) {
        super(chunk);
        this.saveChunk = save;
    }

}
