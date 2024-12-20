/*
---------------------------------------------------------------------------------
File Name : ServerPlayerEntityMixin

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 20.12.2024
Last Modified : 20.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.mixins.player;

import net.furryplayplace.cottonframework.api.CottonAPI;
import net.furryplayplace.cottonframework.api.Location;
import net.furryplayplace.cottonframework.api.events.player.PlayerDropItemEvent;
import net.furryplayplace.cottonframework.api.events.player.PlayerMoveEvent;
import net.furryplayplace.cottonframework.api.events.player.PlayerQuitEvent;
import net.furryplayplace.cottonframework.api.events.player.PlayerTeleportEvent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.EnumSet;
import java.util.Set;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Shadow @Final public MinecraftServer server;

    @Inject(method = "teleport(Lnet/minecraft/server/world/ServerWorld;DDDFF)V", at = @At(value = "HEAD"))
    public void onTeleport(ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player == null) return;

        getPlayerCurrentLoc(targetWorld, player, x, y, z, yaw, pitch);
    }

    @Inject(method = "teleport(Lnet/minecraft/server/world/ServerWorld;DDDLjava/util/Set;FF)Z", at = @At(value = "HEAD"))
    public void onTeleport(ServerWorld world, double destX, double destY, double destZ, Set<PositionFlag> flags, float yaw, float pitch, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player == null) return;

        Set<PositionFlag> modifiableFlags = EnumSet.copyOf(flags);

        double x = modifiableFlags.contains(PositionFlag.X) ? destX : player.getX();
        double y = modifiableFlags.contains(PositionFlag.Y) ? destY : player.getY();
        double z = modifiableFlags.contains(PositionFlag.Z) ? destZ : player.getZ();
        float adjustedYaw = modifiableFlags.contains(PositionFlag.Y_ROT) ? yaw : player.getYaw();
        float adjustedPitch = modifiableFlags.contains(PositionFlag.X_ROT) ? pitch : player.getPitch();

        getPlayerCurrentLoc(world, player, x, y, z, adjustedYaw, adjustedPitch);
    }

    @Unique
    private void getPlayerCurrentLoc(ServerWorld world, PlayerEntity player, double x, double y, double z, float adjustedYaw, float adjustedPitch) {
        Location from = new Location(player.getWorld(), player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
        Location to = new Location(world, x, y, z, adjustedYaw, adjustedPitch);

        CottonAPI.get().pluginManager().getEventBus()
                .post(new PlayerTeleportEvent(player, from, to));
    }


    @Inject(method = "travel", at = @At(value = "HEAD"))
    public void onMovement(Vec3d movementInput, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player == null) return;

        Location from = new Location(player.getWorld(), player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
        Location to = new Location(player.getWorld(), movementInput.x, movementInput.y, movementInput.z);

        PlayerMoveEvent playerMoveEvent = new PlayerMoveEvent(player, from, to);
        playerMoveEvent.setCancelled(ci.isCancelled());

        CottonAPI.get().pluginManager().getEventBus()
                .post(playerMoveEvent);
    }

    @Inject(method = "dropItem", at = @At(value = "HEAD"))
    public void onDropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (player == null) return;

        CottonAPI.get().pluginManager().getEventBus()
                .post(new PlayerDropItemEvent(player, stack.getItem()));
    }

    @Inject(method = "onDisconnect", at = @At(value = "HEAD"))
    public void onDisconnect(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (player == null) return;

        CottonAPI.get().pluginManager().getEventBus()
                .post(new PlayerQuitEvent(player));
    }
}