/*
---------------------------------------------------------------------------------
File Name : ServerPlayerInteractionManagerMixin

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 21.12.2024
Last Modified : 21.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.mixins.network;

import net.furryplayplace.cottonframework.CottonFramework;
import net.furryplayplace.cottonframework.api.CottonAPI;
import net.furryplayplace.cottonframework.api.events.block.BlockBreakEvent;
import net.furryplayplace.cottonframework.api.events.player.PlayerGameModeChangeEvent;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {

    @Shadow @Final protected ServerPlayerEntity player;

    @Shadow protected abstract void onBlockBreakingAction(BlockPos pos, boolean success, int sequence, String reason);

    @Shadow protected ServerWorld world;

    @Shadow private GameMode gameMode;

    @Shadow public abstract boolean isCreative();

    @Shadow public abstract void finishMining(BlockPos pos, int sequence, String reason);

    @Shadow private int startMiningTime;

    @Shadow private int tickCounter;

    @Shadow private boolean mining;

    @Shadow private BlockPos miningPos;

    @Shadow private int blockBreakingProgress;

    @Shadow private boolean failedToMine;

    @Shadow private BlockPos failedMiningPos;

    @Shadow private int failedStartMiningTime;

    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "changeGameMode", at = @At("HEAD"))
    public void changeGameMode(GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
        PlayerGameModeChangeEvent playerGameModeChangeEvent = new PlayerGameModeChangeEvent(this.player, gameMode);

        CottonAPI.get().pluginManager().getEventBus().post(playerGameModeChangeEvent);

        if (playerGameModeChangeEvent.isCancelled()) cir.cancel();
    }

    /**
     * Is this the best way to do that?..
     *
     * @author vakea
     * @reason Hooking block events for players
     */
    @Overwrite
    public void processBlockBreakingAction(BlockPos pos, PlayerActionC2SPacket.Action action, Direction direction, int worldHeight, int sequence) {
        if (!this.player.canInteractWithBlockAt(pos, 1.0F)) {
            this.onBlockBreakingAction(pos, false, sequence, "too far");
        } else if (pos.getY() >= worldHeight) {
            this.player.networkHandler.sendPacket(new BlockUpdateS2CPacket(pos, this.world.getBlockState(pos)));
            this.onBlockBreakingAction(pos, false, sequence, "too high");
        } else {
            if (action == net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action.START_DESTROY_BLOCK) {
                if (!this.world.canPlayerModifyAt(this.player, pos)) {
                    this.player.networkHandler.sendPacket(new BlockUpdateS2CPacket(pos, this.world.getBlockState(pos)));
                    this.onBlockBreakingAction(pos, false, sequence, "may not interact");
                    return;
                }

                if (this.isCreative()) {
                    BlockBreakEvent blockBreakEvent = new BlockBreakEvent(this.world.getBlockState(pos).getBlock(), player);

                    CottonFramework.getInstance().getApi()
                            .pluginManager().getEventBus().post(blockBreakEvent);

                    if (blockBreakEvent.isCancelled()) {
                        this.world.setBlockBreakingInfo(this.player.getId(), this.miningPos, -1);
                        this.onBlockBreakingAction(pos, true, sequence, "aborted mismatched destroying");
                        return;
                    }

                    this.finishMining(pos, sequence, "creative destroy");
                    return;
                }

                if (this.player.isBlockBreakingRestricted(this.world, pos, this.gameMode)) {
                    this.player.networkHandler.sendPacket(new BlockUpdateS2CPacket(pos, this.world.getBlockState(pos)));
                    this.onBlockBreakingAction(pos, false, sequence, "block action restricted");
                    return;
                }

                this.startMiningTime = this.tickCounter;
                float f = 1.0F;
                BlockState blockState = this.world.getBlockState(pos);
                if (!blockState.isAir()) {
                    EnchantmentHelper.onHitBlock(this.world, this.player.getMainHandStack(), this.player, this.player, EquipmentSlot.MAINHAND, Vec3d.ofCenter(pos), blockState, (item) -> this.player.sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND));
                    blockState.onBlockBreakStart(this.world, pos, this.player);
                    f = blockState.calcBlockBreakingDelta(this.player, this.player.getWorld(), pos);
                }

                if (!blockState.isAir() && f >= 1.0F) {
                    BlockBreakEvent blockBreakEvent = new BlockBreakEvent(blockState.getBlock(), player);

                    CottonFramework.getInstance().getApi()
                            .pluginManager().getEventBus().post(blockBreakEvent);

                    if (blockBreakEvent.isCancelled()) {
                        this.world.setBlockBreakingInfo(this.player.getId(), this.miningPos, -1);
                        this.onBlockBreakingAction(pos, true, sequence, "aborted mismatched destroying");
                        return;
                    }

                    this.finishMining(pos, sequence, "insta mine");
                } else {
                    if (this.mining) {
                        this.player.networkHandler.sendPacket(new BlockUpdateS2CPacket(this.miningPos, this.world.getBlockState(this.miningPos)));
                        this.onBlockBreakingAction(pos, false, sequence, "abort destroying since another started (client insta mine, server disagreed)");
                    }

                    this.mining = true;
                    this.miningPos = pos.toImmutable();
                    int i = (int)(f * 10.0F);
                    this.world.setBlockBreakingInfo(this.player.getId(), pos, i);
                    this.onBlockBreakingAction(pos, true, sequence, "actual start of destroying");
                    this.blockBreakingProgress = i;
                }
            } else if (action == net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK) {
                if (pos.equals(this.miningPos)) {
                    int j = this.tickCounter - this.startMiningTime;
                    BlockState blockState = this.world.getBlockState(pos);
                    if (!blockState.isAir()) {
                        float g = blockState.calcBlockBreakingDelta(this.player, this.player.getWorld(), pos) * (float)(j + 1);
                        if (g >= 0.7F) {
                            this.mining = false;
                            this.world.setBlockBreakingInfo(this.player.getId(), pos, -1);

                            BlockBreakEvent blockBreakEvent = new BlockBreakEvent(blockState.getBlock(), player);

                            CottonFramework.getInstance().getApi()
                                    .pluginManager().getEventBus().post(blockBreakEvent);

                            if (blockBreakEvent.isCancelled()) {
                                this.world.setBlockBreakingInfo(this.player.getId(), this.miningPos, -1);
                                this.onBlockBreakingAction(pos, true, sequence, "aborted mismatched destroying");
                                return;
                            }

                            this.finishMining(pos, sequence, "destroyed");
                            return;
                        }

                        if (!this.failedToMine) {
                            this.mining = false;
                            this.failedToMine = true;
                            this.failedMiningPos = pos;
                            this.failedStartMiningTime = this.startMiningTime;
                        }
                    }
                }

                this.onBlockBreakingAction(pos, true, sequence, "stopped destroying");
            } else if (action == net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK) {
                this.mining = false;
                if (!Objects.equals(this.miningPos, pos)) {
                    LOGGER.warn("Mismatch in destroy block pos: {} {}", this.miningPos, pos);
                    this.world.setBlockBreakingInfo(this.player.getId(), this.miningPos, -1);
                    this.onBlockBreakingAction(pos, true, sequence, "aborted mismatched destroying");
                }

                this.world.setBlockBreakingInfo(this.player.getId(), pos, -1);
                this.onBlockBreakingAction(pos, true, sequence, "aborted destroying");
            }

        }
    }
}