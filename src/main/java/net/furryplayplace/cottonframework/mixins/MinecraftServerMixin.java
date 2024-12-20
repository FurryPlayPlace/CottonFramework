/*
---------------------------------------------------------------------------------
File Name : MinecraftServerMixin

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 19.12.2024
Last Modified : 19.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.mixins;

import net.furryplayplace.cottonframework.api.events.cotton.CottonPluginShutdown;
import net.furryplayplace.cottonframework.CottonFramework;
import net.furryplayplace.cottonframework.api.events.world.WorldLoadEvent;
import net.minecraft.server.*;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow public abstract ServerWorld getOverworld();

    @Inject(method = "stop", at = @At(value = "HEAD"))
    public void stopServer(CallbackInfo ci) {
        CottonFramework.getInstance().getApi().pluginManager()
                .getEventBus().post(new CottonPluginShutdown());
    }

    @Inject(method = "prepareStartRegion", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getOverworld()Lnet/minecraft/server/world/ServerWorld;"))
    public void prepareStartRegion(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci) {
        CottonFramework.getInstance().getApi().pluginManager()
                .getEventBus().post(new WorldLoadEvent(this.getOverworld()));
    }
}
