/*
---------------------------------------------------------------------------------
File Name : BlockMixin

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 19.12.2024
Last Modified : 19.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.mixins.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.furryplayplace.cottonframework.api.events.block.BlockBreakEvent;
import net.furryplayplace.cottonframework.CottonFramework;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.SERVER)
@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "onBreak", at = @At("RETURN"), cancellable = true)
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<BlockState> cir) {
        BlockBreakEvent blockBreakEvent = new BlockBreakEvent(state.getBlock(), player);
        blockBreakEvent.setCancelled(cir.isCancelled());

        if (blockBreakEvent.isCancelled())
            return;

        CottonFramework.getInstance().getApi()
                .pluginManager().getEventBus().post(blockBreakEvent);
    }
}
