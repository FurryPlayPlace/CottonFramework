/*
---------------------------------------------------------------------------------
File Name : ServerHandshakeNetworkHandlerMixin

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 20.12.2024
Last Modified : 20.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.mixins.network;

import net.furryplayplace.cottonframework.api.CottonAPI;
import net.furryplayplace.cottonframework.api.events.player.PlayerLoginEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.server.network.ServerHandshakeNetworkHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerHandshakeNetworkHandler.class)
public class ServerHandshakeNetworkHandlerMixin {

    @Shadow @Final private ClientConnection connection;

    @Inject(method = "login", at = @At("HEAD"))
    public void onLogin(HandshakeC2SPacket packet, boolean transfer, CallbackInfo ci) {
        PlayerLoginEvent playerLoginEvent = new PlayerLoginEvent(connection, packet.address());

        CottonAPI.get().pluginManager().getEventBus()
                .post(playerLoginEvent);
    }

}