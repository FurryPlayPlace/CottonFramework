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

import net.furryplayplace.cottonframework.api.CottonAPI;
import net.furryplayplace.cottonframework.api.events.cotton.CottonPluginInitialize;
import net.furryplayplace.cottonframework.CottonFramework;
import net.furryplayplace.cottonframework.api.events.server.ServerLoadEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftDedicatedServer.class)
public abstract class DedicatedServerMixin {
    @Shadow public abstract PlayerManager getPlayerManager();

    @Inject(
            method = "setupServer",
            at = @At(value = "RETURN")
    )
    private void initServer(CallbackInfoReturnable<Boolean> cir) {
        CottonFramework.getInstance().getApi().pluginManager()
                        .getEventBus().post(new CottonPluginInitialize());

        ServerLoadEvent serverLoadEvent = new ServerLoadEvent(ServerLoadEvent.LoadType.STARTUP);
        CottonFramework.getInstance().getApi().pluginManager()
                        .getEventBus().post(serverLoadEvent);

        CottonAPI.get().server().setPlayerManager(getPlayerManager());
        CottonAPI.get().server().setServer((MinecraftServer) (Object) this);
        CottonAPI.get().server().setDedicatedServer((MinecraftDedicatedServer) (Object) this);
    }
}