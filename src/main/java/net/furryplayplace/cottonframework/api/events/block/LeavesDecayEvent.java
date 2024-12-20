package net.furryplayplace.cottonframework.api.events.block;

import net.furryplayplace.cottonframework.api.events.Cancellable;
import net.minecraft.block.Block;
import org.jetbrains.annotations.NotNull;

public class LeavesDecayEvent extends BlockEvent implements Cancellable {
    private boolean cancel = false;

    public LeavesDecayEvent(@NotNull final Block block) {
        super(block);
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
