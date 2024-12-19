package net.furryplayplace.cotton.api.events.block;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

public class BlockDispenseArmorEvent extends BlockDispenseEvent {

    private final LivingEntity target;

    public BlockDispenseArmorEvent(@NotNull Block block, @NotNull ItemStack dispensed, @NotNull LivingEntity target) {
        super(block, dispensed, new Vector3d(0, 0, 0));
        this.target = target;
    }

    @NotNull
    public LivingEntity getTargetEntity() {
        return target;
    }
}
