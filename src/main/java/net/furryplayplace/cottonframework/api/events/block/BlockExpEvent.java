package net.furryplayplace.cottonframework.api.events.block;

import net.minecraft.block.Block;

/**
 * An event that's called when a block yields experience.
 */

public class BlockExpEvent extends BlockEvent {
    private int exp;

    public BlockExpEvent(Block block, int exp) {
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
