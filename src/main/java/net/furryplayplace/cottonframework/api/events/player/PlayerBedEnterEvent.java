package net.furryplayplace.cottonframework.api.events.player;

import net.furryplayplace.cottonframework.api.events.Cancellable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;

public class PlayerBedEnterEvent extends PlayerEvent implements Cancellable {

    public enum BedEnterResult {
        OK,
        NOT_POSSIBLE_HERE,
        NOT_POSSIBLE_NOW,
        TOO_FAR_AWAY,
        OBSTRUCTED,
        NOT_SAFE,
        OTHER_PROBLEM;
    }

    private final Block bed;
    private final BedEnterResult bedEnterResult;
    private Result useBed = Result.DEFAULT;

    public PlayerBedEnterEvent(@NotNull PlayerEntity who, @NotNull Block bed, @NotNull BedEnterResult bedEnterResult) {
        super(who);
        this.bed = bed;
        this.bedEnterResult = bedEnterResult;
    }

    @Deprecated
    public PlayerBedEnterEvent(@NotNull PlayerEntity who, @NotNull Block bed) {
        this(who, bed, BedEnterResult.OK);
    }

    /**
     * This describes the default outcome of this event.
     *
     * @return the bed enter result representing the default outcome of this event
     */
    @NotNull
    public BedEnterResult getBedEnterResult() {
        return bedEnterResult;
    }

    @NotNull
    public Result useBed() {
        return useBed;
    }

    public void setUseBed(@NotNull Result useBed) {
        this.useBed = useBed;
    }

    @Override
    public boolean isCancelled() {
        return (useBed == Result.DENY || useBed == Result.DEFAULT && bedEnterResult != BedEnterResult.OK);
    }

    @Override
    public void setCancelled(boolean cancel) {
        setUseBed(cancel ? Result.DENY : useBed() == Result.DENY ? Result.DEFAULT : useBed());
    }

    /**
     * Returns the bed block involved in this event.
     *
     * @return the bed block involved in this event
     */
    @NotNull
    public Block getBed() {
        return bed;
    }
}
