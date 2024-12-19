package net.furryplayplace.cotton.api.events.block;

import net.furryplayplace.cotton.api.events.Event;
import net.minecraft.block.Block;
import org.jetbrains.annotations.NotNull;

public abstract class BlockEvent extends Event {
    protected Block block;

    public BlockEvent(@NotNull final Block theBlock) {
        block = theBlock;
    }

    @NotNull
    public final Block getBlock() {
        return block;
    }
}
