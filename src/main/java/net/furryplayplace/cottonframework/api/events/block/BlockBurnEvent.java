package net.furryplayplace.cottonframework.api.events.block;

import net.furryplayplace.cottonframework.api.events.Cancellable;
import net.minecraft.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockBurnEvent extends BlockEvent implements Cancellable {
    private boolean cancelled;
    private final Block ignitingBlock;

    @Deprecated
    public BlockBurnEvent(@NotNull final Block block) {
        this(block, null);
    }

    public BlockBurnEvent(@NotNull final Block block, @Nullable final Block ignitingBlock) {
        super(block);
        this.ignitingBlock = ignitingBlock;
    }

    @Nullable
    public Block getIgnitingBlock() {
        return ignitingBlock;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
