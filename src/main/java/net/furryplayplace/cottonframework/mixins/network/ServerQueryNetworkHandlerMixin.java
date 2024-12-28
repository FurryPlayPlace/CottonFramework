/*
---------------------------------------------------------------------------------
File Name : ServerQueryNetworkHandlerMixin

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 20.12.2024
Last Modified : 20.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.mixins.network;

import net.furryplayplace.cottonframework.api.CottonAPI;
import net.furryplayplace.cottonframework.api.events.server.ServerListPingEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.query.QueryRequestC2SPacket;
import net.minecraft.server.ServerMetadata;
import net.minecraft.server.network.ServerQueryNetworkHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Mixin(ServerQueryNetworkHandler.class)
public class ServerQueryNetworkHandlerMixin {

    @Shadow @Final private ServerMetadata metadata;

    @Shadow @Final private ClientConnection connection;

    @Inject(method = "onRequest", at = @At("HEAD"))
    public void onRequest(QueryRequestC2SPacket packet, CallbackInfo ci) {
        ServerMetadata metadata = this.metadata;
        ClientConnection connection = this.connection;
        SocketAddress socketAddress = connection.getAddress();

        if (socketAddress instanceof InetSocketAddress inetSocketAddress) {
            InetAddress inetAddress = inetSocketAddress.getAddress();

            ServerListPingEvent serverListPingEvent = new ServerListPingEvent(
                    connection.getAddressAsString(true),
                    inetAddress,
                    metadata.players().get().online(),
                    metadata.players().get().max()
            );

            CottonAPI.get().pluginManager().getEventBus()
                    .post(serverListPingEvent);
        }
    }
}