package net.furryplayplace.cotton.api.events.block;

import net.minecraft.block.Block;
import org.jetbrains.annotations.NotNull;

/**
 * An event that's called when a block yields experience.
 */
public class BlockExpEvent extends BlockEvent {
    private int exp;

    public BlockExpEvent(@NotNull Block block, int exp) {
        super(block);

        this.exp = exp;
    }

    public int getExpToDrop() {
        return exp;
    }

    public void setExpToDrop(int exp) {
        this.exp = exp;
    }
}
