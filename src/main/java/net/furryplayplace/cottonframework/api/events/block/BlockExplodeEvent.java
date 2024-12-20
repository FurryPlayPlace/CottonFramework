package net.furryplayplace.cottonframework.api.events.block;

import lombok.Getter;
import lombok.Setter;
import net.furryplayplace.cottonframework.api.events.Cancellable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlockExplodeEvent extends BlockEvent implements Cancellable {
    private boolean cancel;
    private final BlockState blockState;
    private final List<Block> blocks;

    @Getter
    @Setter
    private float yield;

    public BlockExplodeEvent(@NotNull final Block what, @NotNull final BlockState blockState, @NotNull final List<Block> blocks, final float yield) {
        super(what);
        this.blockState = blockState;
        this.blocks = blocks;
        this.yield = yield;
        this.cancel = false;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @NotNull
    public BlockState getExplodedBlockState() {
        return blockState;
    }

    @NotNull
    public List<Block> blockList() {
        return blocks;
    }
}
