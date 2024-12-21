package net.furryplayplace.cottonframework.api.permissions.v0;

import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

class Util {

    static ServerCommandSource commandSourceFromEntity(Entity entity) {
        if (entity instanceof ServerPlayerEntity) {
            return (entity).getCommandSource();
        }
        World world = entity.getWorld();
        if (world instanceof ServerWorld) {
            return entity.getCommandSource();
        } else {
            throw new IllegalArgumentException("Entity '" + entity + "' is not a server entity. Try passing a CommandSource directly instead.");
        }
    }

}
