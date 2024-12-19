package net.furryplayplace.cotton.api.events.block;


import lombok.Getter;
import lombok.Setter;
import net.furryplayplace.cotton.api.events.Cancellable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class BlockBreakEvent extends BlockExpEvent implements Cancellable {
    private final PlayerEntity player;
    @Getter
    @Setter
    private boolean dropItems;
    private boolean cancel;

    public BlockBreakEvent(@NotNull final Block theBlock, @NotNull final PlayerEntity player) {
        super(theBlock, 0);

        this.player = player;
        this.dropItems = true;
    }

    @NotNull
    public PlayerEntity getPlayer() {
        return player;
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
