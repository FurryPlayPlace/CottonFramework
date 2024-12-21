/*
---------------------------------------------------------------------------------
File Name : CottonServer

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 20.12.2024
Last Modified : 20.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.api;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.*;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;

@Getter
@Setter
public class CottonServer {
    private PlayerManager playerManager;
    private MinecraftServer server;
    private MinecraftDedicatedServer dedicatedServer;
}