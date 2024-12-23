package net.furryplayplace.cottonframework.api.permissions.v1;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class CottonPermissions {

    public static Permissible getPermissible(Object obj) {
        if (!(obj instanceof Permissible)) {
            if (obj instanceof ServerCommandSource) {
                ServerCommandSource cs = (ServerCommandSource)obj;
                if (null == cs.getEntity()) {
                    return (Permissible) cs.getServer();
                } else {
                    return cs.getEntity() instanceof Permissible ? (Permissible) cs.getEntity() : null;
                }
            }
            return null;
        }
        return (Permissible) obj;
    }

    public static Permissible getServerPermissible(MinecraftServer server) {
        return (Permissible) server;
    }

    public static Permissible getPlayerPermissible(ServerPlayerEntity plr) {
        return (Permissible) plr;
    }

}