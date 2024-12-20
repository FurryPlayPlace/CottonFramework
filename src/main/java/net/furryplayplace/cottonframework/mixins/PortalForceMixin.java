/*
---------------------------------------------------------------------------------
File Name : PortalForcerMixin

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 19.12.2024
Last Modified : 19.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.mixins;

import net.furryplayplace.cottonframework.api.events.world.PortalCreateEvent;
import net.furryplayplace.cottonframework.CottonFramework;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockLocating;
import net.minecraft.world.dimension.PortalForcer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(PortalForcer.class)
public class PortalForceMixin {
    @Shadow @Final private ServerWorld world;

    @Inject(method = "createPortal", at = @At("HEAD"), cancellable = true)
    public void createPortal(BlockPos pos, Direction.Axis axis, CallbackInfoReturnable<Optional<BlockLocating.Rectangle>> cir) {
        PortalCreateEvent portalCreateEvent = new PortalCreateEvent(List.of(
                Blocks.NETHER_PORTAL.getDefaultState().with(NetherPortalBlock.AXIS, axis)
        ), world, PortalCreateEvent.CreateReason.FIRE);

        ThreadLocal.withInitial(() -> portalCreateEvent);

        CottonFramework.getInstance().getApi().pluginManager()
                        .getEventBus().post(portalCreateEvent);

        cir.setReturnValue(Optional.of(new BlockLocating.Rectangle(pos.toImmutable(), 2, 3)));
    }
}