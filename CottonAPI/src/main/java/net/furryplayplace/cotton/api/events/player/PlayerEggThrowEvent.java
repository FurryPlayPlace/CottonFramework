package net.furryplayplace.cotton.api.events.player;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EggItem;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class PlayerEggThrowEvent extends PlayerEvent {
    private final EggItem egg;
    private boolean hatching;
    private EntityType hatchType;
    private byte numHatches;

    public PlayerEggThrowEvent(@NotNull final PlayerEntity player, @NotNull final EggItem egg, final boolean hatching, final byte numHatches, @NotNull final EntityType hatchingType) {
        super(player);
        this.egg = egg;
        this.hatching = hatching;
        this.numHatches = numHatches;
        this.hatchType = hatchingType;
    }

    /**
     * Gets the egg involved in this event.
     *
     * @return the egg involved in this event
     */
    @NotNull
    public EggItem getEgg() {
        return egg;
    }


    @NotNull
    public EntityType getHatchingType() {
        return hatchType;
    }
}
