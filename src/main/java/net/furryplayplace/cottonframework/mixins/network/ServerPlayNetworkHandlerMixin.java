/*
---------------------------------------------------------------------------------
File Name : ServerPlayNetworkHandlerMixin

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 20.12.2024
Last Modified : 20.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.mixins.network;

import net.furryplayplace.cottonframework.CottonFramework;
import net.furryplayplace.cottonframework.api.CottonAPI;
import net.furryplayplace.cottonframework.api.Location;
import net.furryplayplace.cottonframework.api.events.player.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.DisconnectionInfo;
import net.minecraft.network.message.LastSeenMessageList;
import net.minecraft.network.message.MessageChain;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    @Shadow
    private static double clampHorizontal(double d) { return 0; }

    @Shadow
    private static double clampVertical(double d) { return 0; }

    @Shadow public ServerPlayerEntity player;

    @Shadow public abstract ServerPlayerEntity getPlayer();

    @Shadow protected abstract SignedMessage getSignedMessage(ChatMessageC2SPacket packet, LastSeenMessageList lastSeenMessages) throws MessageChain.MessageChainException;

    @Shadow protected abstract Optional<LastSeenMessageList> validateAcknowledgment(LastSeenMessageList.Acknowledgment acknowledgment);

    @Inject(
            method = "onVehicleMove",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onVehicleMove(VehicleMoveC2SPacket packet, CallbackInfo ci) {
        PlayerEntity player = getPlayer();
        if (player == null) return;

        Location from = new Location(player.getWorld(), player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
        Location to = new Location(player.getWorld(), packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch());

        PlayerVehicleMoveEvent playerVehicleMoveEvent = new PlayerVehicleMoveEvent(player, from, to);

        // Avoiding pinging packets to come through
        if (!playerVehicleMoveEvent.hasChangedPosition()) return;

        CottonAPI.get().pluginManager().getEventBus()
                .post(playerVehicleMoveEvent);

        if (playerVehicleMoveEvent.isCancelled()) ci.cancel();
    }

    @Inject(
            method = "onPlayerMove",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    public void onPlayerMove(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        PlayerEntity player = getPlayer();
        if (player == null) return;

        Location from = new Location(player.getWorld(), player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
        Location to = new Location(player.getWorld(),
                packet.getX(player.getX()),
                packet.getY(player.getY()),
                packet.getZ(player.getZ()),
                packet.getYaw(player.getYaw()),
                packet.getPitch(player.getPitch()));

        if (!packet.changesPosition() && !packet.changesLook()) return;

        PlayerMoveEvent moveEvent = new PlayerMoveEvent(player, from, to);
        CottonAPI.get().pluginManager().getEventBus().post(moveEvent);

        if (moveEvent.isCancelled()) {
            if (moveEvent.isAsynchronous()) {
                CompletableFuture.runAsync(() -> {
                    player.teleport(from.getX(), from.getY(), from.getZ(), true);
                    player.getServer().executeSync(ci::cancel);
                });
            } else {
                // Synchronous cancellation
                player.teleport(from.getX(), from.getY(), from.getZ(), true);
                ci.cancel();
            }
        }
    }

    /**
     * @author Vakea
     * @reason Disabling Commands Blocks
     */
    @Overwrite
    public void onUpdateCommandBlock(UpdateCommandBlockC2SPacket packet) {
        PlayerEntity player = getPlayer();
        if (player == null) return;

        player.sendMessage(Text.of("Command blocks are disabled."), true);
    }

    /**
     * @author Vakea
     * @reason Disabling Commands Blocks
     */
    @Overwrite
    public void onUpdateCommandBlockMinecart(UpdateCommandBlockMinecartC2SPacket packet) {
        PlayerEntity player = getPlayer();
        if (player == null) return;

        player.sendMessage(Text.of("Command blocks are disabled."), true);
    }

    @Inject(
            method = "onUpdateBeacon",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getServerWorld()Lnet/minecraft/server/world/ServerWorld;"),
            slice = @Slice(
                    from = @At(value = "HEAD", target = "Lnet/minecraft/screen/ScreenHandler;canUse(Lnet/minecraft/entity/player/PlayerEntity;)Z"),
                    to = @At(value = "TAIL", target = "Lnet/minecraft/screen/BeaconScreenHandler;setEffects(Ljava/util/Optional;Ljava/util/Optional;)V")
            ),
            cancellable = true
    )
    public void onUpdateBeacon(UpdateBeaconC2SPacket packet, CallbackInfo ci) {
        PlayerEntity player = getPlayer();
        if (player == null) return;

        PlayerSetBeaconEffectEvent playerSetBeaconEffectEvent = new PlayerSetBeaconEffectEvent(player, packet.primary(), packet.secondary());

        CottonAPI.get().pluginManager().getEventBus()
                .post(playerSetBeaconEffectEvent);

        if (playerSetBeaconEffectEvent.isCancelled()) ci.cancel();
    }

    @Inject(method = "onDisconnected", at = @At("HEAD"))
    public void onDisconnected(DisconnectionInfo info, CallbackInfo ci) {
        PlayerEntity player = getPlayer();
        if (player == null) return;


        if (Objects.equals(info.reason().getLiteralString(), "Disconnected")) {
            PlayerQuitEvent playerQuitEvent = new PlayerQuitEvent(player);
            CottonAPI.get().pluginManager().getEventBus()
                    .post(playerQuitEvent);
        } else {
            PlayerKickEvent playerKickEvent = new PlayerKickEvent(player);
            CottonAPI.get().pluginManager().getEventBus()
                    .post(playerKickEvent);
        }
    }

    @Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)
    public void onChatMessage(ChatMessageC2SPacket packet, CallbackInfo ci) throws MessageChain.MessageChainException {
        PlayerEntity player = getPlayer();
        if (player == null) return;

        Optional<LastSeenMessageList> optionalCheck = this.validateAcknowledgment(packet.acknowledgment());

        if (optionalCheck.isPresent()) {
            SignedMessage signedMessage = this.getSignedMessage(packet, optionalCheck.get());
            Text textMessage = this.player.server.getMessageDecorator().decorate(this.player, signedMessage.getContent());

            PlayerChatMessageEvent playerChatMessageEvent = new PlayerChatMessageEvent(player, signedMessage, textMessage);

            CottonAPI.get().pluginManager().getEventBus()
                    .post(playerChatMessageEvent);

            if (playerChatMessageEvent.isCancelled()) {
                ci.cancel();
                return;
            }

            player.sendMessage(playerChatMessageEvent.getTextMessage());
        }
    }

    @Inject(method = "onCraftRequest", at = @At("HEAD"), cancellable = true)
    public void onCraftRequest(CraftRequestC2SPacket packet, CallbackInfo ci) {
        PlayerEntity player = getPlayer();
        if (player == null) return;

        PlayerCraftRequestEvent playerCraftRequestEvent = new PlayerCraftRequestEvent(player, packet.getRecipeId(), packet.shouldCraftAll());

        CottonAPI.get().pluginManager().getEventBus()
                .post(playerCraftRequestEvent);

        if (playerCraftRequestEvent.isCancelled()){
            ci.cancel();
        }
    }

    /**
     * This code was originally made for the mod Gotta Go Fast! version 1.1.0 for Minecraft 1.20
     * Repository: https://github.com/oskardotglobal/gottagofast-fabric
     * <p>
     * Has the mod became inactive in terms of Update we decided to copy this implementation and edit it to feat CottonFramework needs.
     *
     * @author Oskardotglobal
     */
    @Inject(
            method = "onPlayerMove(Lnet/minecraft/network/packet/c2s/play/PlayerMoveC2SPacket;)V",
            cancellable = true,
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;[Ljava/lang/Object;)V",
                    shift = At.Shift.BEFORE
            )
    )
    private void onPlayerMoveFix(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        if (!CottonFramework.getInstance().getConfiguration().getBoolean("gotta-go-fast.fix-player-move-spam", true)) {
            ServerPlayNetworkHandler instance = ((ServerPlayNetworkHandler)(Object)this);

            int playerLimit = CottonFramework.getInstance().getConfiguration().getInt("gotta-go-fast.player-limit", 100);
            int playerMoveLimit = CottonFramework.getInstance().getConfiguration().getInt("gotta-go-fast.player-move-limit", 300);

            double x = clampHorizontal(packet.getX(instance.player.getX()));
            double y = clampVertical(packet.getY(instance.player.getY()));
            double z = clampHorizontal(packet.getZ(instance.player.getZ()));

            double distanceXFromLastTick = x - instance.lastTickX;
            double distanceYFromLastTick = y - instance.lastTickY;
            double distanceZFromLastTick = z - instance.lastTickZ;

            double velocity = instance.player.getVelocity().lengthSquared();

            double distance = distanceXFromLastTick * distanceXFromLastTick
                    + distanceYFromLastTick * distanceYFromLastTick
                    + distanceZFromLastTick * distanceZFromLastTick;

            int packetCount = instance.movePacketsCount - instance.lastTickMovePacketsCount;
            float threshold = instance.player.isFallFlying() ? (float) playerMoveLimit : (float) playerLimit;

            if (distance - velocity > (double)(threshold * (float) packetCount) && !instance.isHost()) {
                if (CottonFramework.getInstance().getConfiguration().getBoolean("gotta-go-fast.log-player-move-spam", true)) {
                    CottonFramework.getInstance().getLogger().warn(
                            "[GottaGoFast/CottonFramework] {} moved too quickly! Moved by {},{},{} blocks in x,y,z direction. If you wish to increase the limit to exclude this, increase the limit to {}",
                            instance.player.getName().getString(),
                            distanceXFromLastTick,
                            distanceYFromLastTick,
                            distanceZFromLastTick,
                            (distance - velocity) / packetCount
                    );
                }

                instance.requestTeleport(instance.player.getX(), instance.player.getY(), instance.player.getZ(), instance.player.getYaw(), instance.player.getPitch());
                ci.cancel();
            }
        }
    }
}