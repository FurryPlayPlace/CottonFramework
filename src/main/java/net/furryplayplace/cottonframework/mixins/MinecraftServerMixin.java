/*
---------------------------------------------------------------------------------
File Name : MinecraftServerMixin

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 19.12.2024
Last Modified : 19.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.mixins;

import net.furryplayplace.cottonframework.api.CottonAPI;
import net.furryplayplace.cottonframework.api.events.cotton.CottonPluginShutdown;
import net.furryplayplace.cottonframework.CottonFramework;
import net.furryplayplace.cottonframework.api.events.world.WorldInitEvent;
import net.furryplayplace.cottonframework.api.events.world.WorldLoadEvent;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.RandomSequencesState;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow public abstract ServerWorld getOverworld();

    @Inject(method = "stop", at = @At(value = "HEAD"))
    public void stopServer(CallbackInfo ci) {
        CottonFramework.getInstance().getApi().pluginManager()
                .getEventBus().post(new CottonPluginShutdown());
    }

    @Redirect(method = "createWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;<init>(Lnet/minecraft/server/MinecraftServer;Ljava/util/concurrent/Executor;Lnet/minecraft/world/level/storage/LevelStorage$Session;Lnet/minecraft/world/level/ServerWorldProperties;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/world/dimension/DimensionOptions;Lnet/minecraft/server/WorldGenerationProgressListener;ZJLjava/util/List;ZLnet/minecraft/util/math/random/RandomSequencesState;)V"))
    private void redirectServerLevelConstructor(ServerWorld instance, MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey worldKey, DimensionOptions dimensionOptions, WorldGenerationProgressListener worldGenerationProgressListener, boolean debugWorld, long seed, List spawners, boolean shouldTickTime, RandomSequencesState randomSequencesState) {
        WorldInitEvent worldInitEvent = new WorldInitEvent(instance);
        CottonAPI.get().pluginManager().getEventBus().post(worldInitEvent);
    }

    @Inject(method = "prepareStartRegion", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;getOverworld()Lnet/minecraft/server/world/ServerWorld;"))
    public void prepareStartRegion(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci) {
        CottonFramework.getInstance().getApi().pluginManager()
                .getEventBus().post(new WorldLoadEvent(this.getOverworld()));
    }


}
