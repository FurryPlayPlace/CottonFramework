/*
---------------------------------------------------------------------------------
File Name : ServerLoginNetworkHandlerMixin

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 20.12.2024
Last Modified : 20.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.mixins.network;

import com.mojang.authlib.GameProfile;
import net.furryplayplace.cottonframework.api.CottonAPI;
import net.furryplayplace.cottonframework.api.events.player.PlayerJoinEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLoginNetworkHandler.class)
public class ServerLoginNetworkHandlerMixin {

    @Shadow @Final MinecraftServer server;

    @Inject(method = "sendSuccessPacket", at = @At("RETURN"))
    public void onHello(GameProfile profile, CallbackInfo ci) {
        ServerPlayerEntity player = this.server.getPlayerManager().getPlayer(profile.getId());
        if (player == null) return;

        PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(player);

        CottonAPI.get().pluginManager().getEventBus()
                .post(playerJoinEvent);
    }
}