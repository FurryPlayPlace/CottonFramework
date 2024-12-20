package net.furryplayplace.cottonframework.api.events.block;

import lombok.Setter;
import net.furryplayplace.cottonframework.api.events.Event;
import net.minecraft.block.Block;

@Setter
public abstract class BlockEvent extends Event {
    protected Block block;

    public BlockEvent(Block theBlock) {
        block = theBlock;
    }

    public final Block getBlock() {
        return block;
    }
}
