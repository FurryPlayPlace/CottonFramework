/*
---------------------------------------------------------------------------------
File Name : MonsterEntityMixin

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 20.12.2024
Last Modified : 20.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.mixins.entity;

import net.furryplayplace.cottonframework.CottonFramework;
import net.furryplayplace.cottonframework.api.Location;
import net.furryplayplace.cottonframework.api.events.entity.EntityExplodeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class ExplosionWorldMixin {

    @Inject(method = "createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/World$ExplosionSourceType;)Lnet/minecraft/world/explosion/Explosion;", at = @At("RETURN"))
    public void createExplosion(Entity entity, double x, double y, double z, float power, World.ExplosionSourceType explosionSourceType, CallbackInfoReturnable<Explosion> cir) {
        EntityExplodeEvent entityExplodeEvent = new EntityExplodeEvent(entity, new Location(entity.getWorld(), x, y, z), cir.getReturnValue().getAffectedBlocks(), power);
        entityExplodeEvent.setCancelled(cir.isCancelled());

        if (entityExplodeEvent.isCancelled())
            return;

        CottonFramework.getInstance().getApi()
                .pluginManager().getEventBus().post(entityExplodeEvent);
    }

    @Inject(method = "createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/World$ExplosionSourceType;)Lnet/minecraft/world/explosion/Explosion;", at = @At("RETURN"))
    public void createExplosion(Entity entity, DamageSource damageSource, ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, CallbackInfoReturnable<Explosion> cir) {
        EntityExplodeEvent entityExplodeEvent = new EntityExplodeEvent(entity, new Location(entity.getWorld(), x, y, z), cir.getReturnValue().getAffectedBlocks(), power);
        entityExplodeEvent.setCancelled(cir.isCancelled());

        if (entityExplodeEvent.isCancelled())
            return;

        CottonFramework.getInstance().getApi()
                .pluginManager().getEventBus().post(entityExplodeEvent);
    }

    @Inject(method = "createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/World$ExplosionSourceType;Lnet/minecraft/particle/ParticleEffect;Lnet/minecraft/particle/ParticleEffect;Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/world/explosion/Explosion;", at = @At("RETURN"))
    public void createExplosion(Entity entity, DamageSource damageSource, ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, ParticleEffect particle, ParticleEffect emitterParticle, RegistryEntry<SoundEvent> soundEvent, CallbackInfoReturnable<Explosion> cir) {
        EntityExplodeEvent entityExplodeEvent = new EntityExplodeEvent(entity, new Location(entity.getWorld(), x, y, z), cir.getReturnValue().getAffectedBlocks(), power);
        entityExplodeEvent.setCancelled(cir.isCancelled());

        if (entityExplodeEvent.isCancelled())
            return;

        CottonFramework.getInstance().getApi()
                .pluginManager().getEventBus().post(entityExplodeEvent);
    }

    @Inject(method = "createExplosion(Lnet/minecraft/entity/Entity;DDDFZLnet/minecraft/world/World$ExplosionSourceType;)Lnet/minecraft/world/explosion/Explosion;", at = @At("RETURN"))
    public void createExplosion(Entity entity, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, CallbackInfoReturnable<Explosion> cir) {
        EntityExplodeEvent entityExplodeEvent = new EntityExplodeEvent(entity, new Location(entity.getWorld(), x, y, z), cir.getReturnValue().getAffectedBlocks(), power);
        entityExplodeEvent.setCancelled(cir.isCancelled());

        if (entityExplodeEvent.isCancelled())
            return;

        CottonFramework.getInstance().getApi()
                .pluginManager().getEventBus().post(entityExplodeEvent);
    }

    @Inject(method = "createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;DDDFZLnet/minecraft/world/World$ExplosionSourceType;ZLnet/minecraft/particle/ParticleEffect;Lnet/minecraft/particle/ParticleEffect;Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/world/explosion/Explosion;", at = @At("RETURN"))
    public void createExplosion(Entity entity, DamageSource damageSource, ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, boolean particles, ParticleEffect particle, ParticleEffect emitterParticle, RegistryEntry<SoundEvent> soundEvent, CallbackInfoReturnable<Explosion> cir) {
        EntityExplodeEvent entityExplodeEvent = new EntityExplodeEvent(entity, new Location(entity.getWorld(), x, y, z), cir.getReturnValue().getAffectedBlocks(), power);
        entityExplodeEvent.setCancelled(cir.isCancelled());

        if (entityExplodeEvent.isCancelled())
            return;

        CottonFramework.getInstance().getApi()
                .pluginManager().getEventBus().post(entityExplodeEvent);
    }

    @Inject(method = "createExplosion(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;Lnet/minecraft/world/explosion/ExplosionBehavior;Lnet/minecraft/util/math/Vec3d;FZLnet/minecraft/world/World$ExplosionSourceType;)Lnet/minecraft/world/explosion/Explosion;", at = @At("RETURN"))
    public void createExplosion(Entity entity, DamageSource damageSource, ExplosionBehavior behavior, Vec3d pos, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, CallbackInfoReturnable<Explosion> cir) {
        EntityExplodeEvent entityExplodeEvent = new EntityExplodeEvent(entity, new Location(entity.getWorld(), pos.x, pos.y, pos.z), cir.getReturnValue().getAffectedBlocks(), power);
        entityExplodeEvent.setCancelled(cir.isCancelled());

        if (entityExplodeEvent.isCancelled())
            return;

        CottonFramework.getInstance().getApi()
                .pluginManager().getEventBus().post(entityExplodeEvent);
    }

}