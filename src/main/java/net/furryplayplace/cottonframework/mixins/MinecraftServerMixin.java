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

import net.furryplayplace.cotton.api.events.cotton.CottonPluginShutdown;
import net.furryplayplace.cottonframework.CottonFramework;
import net.minecraft.server.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "stop", at = @At(value = "HEAD"))
    public void stopServer(CallbackInfo ci) {
        CottonFramework.getInstance().getPluginManager()
                .getEventBus().post(new CottonPluginShutdown());
    }
}
