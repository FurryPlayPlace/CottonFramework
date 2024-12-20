/*
---------------------------------------------------------------------------------
File Name : LevelServerMixin

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 19.12.2024
Last Modified : 19.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.mixins;

import net.furryplayplace.cottonframework.api.CottonAPI;
import net.furryplayplace.cottonframework.api.Location;
import net.furryplayplace.cottonframework.api.events.weather.ThunderChangeEvent;
import net.furryplayplace.cottonframework.api.events.weather.WeatherChangeEvent;
import net.furryplayplace.cottonframework.api.events.world.GenericGameEvent;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class LevelServerMixin {

    @Shadow public abstract ServerWorld toServerWorld();

    @Inject(method = "emitGameEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/event/listener/GameEventDispatchManager;dispatch(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/event/GameEvent$Emitter;)V"))
    public void onEmitGameEvent(RegistryEntry<GameEvent> event, Vec3d emitterPos, GameEvent.Emitter emitter, CallbackInfo ci) {
        GameEvent gameEvent = event.value();

        CottonAPI.get().pluginManager().getEventBus().post(new GenericGameEvent(
                gameEvent,
                new Location(emitter.sourceEntity().getWorld(), emitterPos.x, emitterPos.y, emitterPos.z),
                emitter.sourceEntity(),
                gameEvent.notificationRadius(),
                false
        ));
    }

    @Inject(
            method = "setWeather",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ServerWorldProperties;setThunderTime(I)V")
    )
    public void onWeatherChange(int clearDuration, int rainDuration, boolean raining, boolean thundering, CallbackInfo ci) {
        WeatherChangeEvent weatherChangeEvent = new WeatherChangeEvent(
                toServerWorld(),
                raining,
                WeatherChangeEvent.Cause.NATURAL
        );
        weatherChangeEvent.setCancelled(ci.isCancelled());

        CottonAPI.get().pluginManager().getEventBus().post(weatherChangeEvent);

        ThunderChangeEvent thunderChangeEvent = new ThunderChangeEvent(
                toServerWorld(),
                thundering,
                ThunderChangeEvent.Cause.NATURAL
        );
        thunderChangeEvent.setCancelled(ci.isCancelled());

        CottonAPI.get().pluginManager().getEventBus().post(thunderChangeEvent);
    }
}