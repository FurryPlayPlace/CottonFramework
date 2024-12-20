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
import net.furryplayplace.cottonframework.api.events.player.*;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
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
public abstract class ServerPlayerEntityMixin {

    @Shadow @Final public MinecraftServer server;

    @Shadow protected abstract boolean isBedObstructed(BlockPos pos, Direction direction);

    @Inject(method = "teleportTo", at = @At(value = "HEAD"))
    public void onTeleportTo(TeleportTarget teleportTarget, CallbackInfoReturnable<Entity> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player == null) return;

        Location from = new Location(player.getWorld(), player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
        Location to = new Location(teleportTarget.world(), teleportTarget.pos().x, teleportTarget.pos().y, teleportTarget.pos().z, teleportTarget.yaw(), teleportTarget.pitch());

        PlayerPortalEvent playerPortalEvent = new PlayerPortalEvent(player, from, to);
        if (playerPortalEvent.isCancelled())
            return;

        CottonAPI.get().pluginManager().getEventBus()
                .post(playerPortalEvent);
    }


    @Inject(method = "teleport(Lnet/minecraft/server/world/ServerWorld;DDDFF)V", at = @At(value = "HEAD"))
    public void onTeleport(ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player == null) return;

        if (ci.isCancelled())
            return;

        getPlayerCurrentLoc(targetWorld, player, x, y, z, yaw, pitch);
    }

    @Inject(method = "teleport(Lnet/minecraft/server/world/ServerWorld;DDDLjava/util/Set;FF)Z", at = @At(value = "HEAD"))
    public void onTeleport(ServerWorld world, double destX, double destY, double destZ, Set<PositionFlag> flags, float yaw, float pitch, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player == null) return;

        if (cir.isCancelled())
            return;

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

        PlayerTeleportEvent playerTeleportEvent = new PlayerTeleportEvent(player, from, to);
        if (playerTeleportEvent.isCancelled())
            return;

        CottonAPI.get().pluginManager().getEventBus()
                .post(playerTeleportEvent);
    }


    @Inject(method = "attack", at = @At(value = "RETURN"))
    public void onAttack(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player == null) return;

        PlayerInteractEntityEvent playerInteractEntityEvent = new PlayerInteractEntityEvent(player, target);
        playerInteractEntityEvent.setCancelled(ci.isCancelled());

        if (playerInteractEntityEvent.isCancelled())
            return;

        CottonAPI.get().pluginManager().getEventBus()
                .post(playerInteractEntityEvent);
    }


    @Inject(method = "sleep", at = @At(value = "HEAD"))
    public void onSleep(BlockPos pos, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player == null) return;

        PlayerBedEnterEvent.BedEnterResult bedEnterResult = PlayerBedEnterEvent.BedEnterResult.NOT_POSSIBLE_NOW;

        Block bed = player.getWorld().getBlockState(pos).getBlock();
        if (bed == null) return;
        if (bed instanceof BedBlock bedBlock) {
            if (this.isBedObstructed(pos, Direction.UP) || this.isBedObstructed(pos, Direction.DOWN)
               || this.isBedObstructed(pos.up(), Direction.EAST) || this.isBedObstructed(pos.down(), Direction.NORTH)
               || this.isBedObstructed(pos.up(), Direction.WEST) || this.isBedObstructed(pos.down(), Direction.SOUTH)) {
                bedEnterResult = PlayerBedEnterEvent.BedEnterResult.OBSTRUCTED;
            }

            PlayerBedEnterEvent playerBedEnterEvent = new PlayerBedEnterEvent(player, bedBlock, bedEnterResult);
            playerBedEnterEvent.setCancelled(bedEnterResult != PlayerBedEnterEvent.BedEnterResult.OK);

            CottonAPI.get().pluginManager().getEventBus()
                    .post(playerBedEnterEvent);
        }
    }

    @Inject(method = "wakeUp", at = @At("RETURN"))
    public void onWakeUp(boolean skipSleepTimer, boolean updateSleepingPlayers, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player == null) return;

        PlayerBedLeaveEvent playerBedLeaveEvent = new PlayerBedLeaveEvent(player, true);
        if (playerBedLeaveEvent.isCancelled())
            return;

        CottonAPI.get().pluginManager().getEventBus()
                .post(playerBedLeaveEvent);
    }

    @Inject(method = "dropItem", at = @At(value = "HEAD"))
    public void onDropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (player == null) return;

        PlayerDropItemEvent playerDropItemEvent = new PlayerDropItemEvent(player, stack.getItem());

        if (playerDropItemEvent.isCancelled())
            return;

        CottonAPI.get().pluginManager().getEventBus()
                .post(playerDropItemEvent);
    }

    @Inject(method = "onDisconnect", at = @At(value = "HEAD"))
    public void onDisconnect(CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (player == null) return;

        CottonAPI.get().pluginManager().getEventBus()
                .post(new PlayerQuitEvent(player));
    }
}