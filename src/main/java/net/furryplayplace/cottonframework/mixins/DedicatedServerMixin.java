/*
---------------------------------------------------------------------------------
File Name : DedicatedServerMixin

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 19.12.2024
Last Modified : 19.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.mixins;

import net.furryplayplace.cottonframework.api.events.cotton.CottonPluginInitialize;
import net.furryplayplace.cottonframework.CottonFramework;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftDedicatedServer.class)
public class DedicatedServerMixin {
    @Inject(method = "setupServer", at = @At(value = "NEW", target = "(Lnet/minecraft/server/dedicated/MinecraftDedicatedServer;Ljava/lang/String;)Lnet/minecraft/server/dedicated/MinecraftDedicatedServer$1;"))
    private void initServer(CallbackInfoReturnable<Boolean> cir) {
        CottonFramework.getInstance().getApi().pluginManager()
                        .getEventBus().post(new CottonPluginInitialize());
    }
}