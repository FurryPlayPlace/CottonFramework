package net.furryplayplace.cottonframework.api.permissions.v0;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public interface Permissions {

    static @NotNull TriState getPermissionValue(@NotNull CommandSource source, @NotNull String permission) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(permission, "permission");
        return PermissionCheckEvent.EVENT.invoker().onPermissionCheck(source, permission);
    }

    static boolean check(@NotNull CommandSource source, @NotNull String permission, boolean defaultValue) {
        return getPermissionValue(source, permission).orElse(defaultValue);
    }

    static boolean check(@NotNull CommandSource source, @NotNull String permission, int defaultRequiredLevel) {
        return getPermissionValue(source, permission).orElseGet(() -> source.hasPermissionLevel(defaultRequiredLevel));
    }

    static boolean check(@NotNull CommandSource source, @NotNull String permission) {
        return getPermissionValue(source, permission).orElse(false);
    }

    static @NotNull Predicate<ServerCommandSource> require(@NotNull String permission, boolean defaultValue) {
        Objects.requireNonNull(permission, "permission");
        return player -> check(player, permission, defaultValue);
    }

    static @NotNull Predicate<ServerCommandSource> require(@NotNull String permission, int defaultRequiredLevel) {
        Objects.requireNonNull(permission, "permission");
        return player -> check(player, permission, defaultRequiredLevel);
    }

    static @NotNull Predicate<ServerCommandSource> require(@NotNull String permission) {
        Objects.requireNonNull(permission, "permission");
        return player -> check(player, permission);
    }

    static @NotNull TriState getPermissionValue(@NotNull Entity entity, @NotNull String permission) {
        Objects.requireNonNull(entity, "entity");
        return getPermissionValue(Util.commandSourceFromEntity(entity), permission);
    }

    static boolean check(@NotNull Entity entity, @NotNull String permission, boolean defaultValue) {
        Objects.requireNonNull(entity, "entity");
        return check(Util.commandSourceFromEntity(entity), permission, defaultValue);
    }

    static boolean check(@NotNull Entity entity, @NotNull String permission, int defaultRequiredLevel) {
        Objects.requireNonNull(entity, "entity");
        return check(Util.commandSourceFromEntity(entity), permission, defaultRequiredLevel);
    }

    static boolean check(@NotNull Entity entity, @NotNull String permission) {
        Objects.requireNonNull(entity, "entity");
        return check(Util.commandSourceFromEntity(entity), permission);
    }

    static @NotNull CompletableFuture<TriState> getPermissionValue(@NotNull UUID uuid, @NotNull String permission) {
        Objects.requireNonNull(uuid, "uuid");
        Objects.requireNonNull(permission, "permission");
        return OfflinePermissionCheckEvent.EVENT.invoker().onPermissionCheck(uuid, permission);
    }

    static CompletableFuture<Boolean> check(@NotNull UUID uuid, @NotNull String permission, boolean defaultValue) {
        return getPermissionValue(uuid, permission).thenApplyAsync(state -> state.orElse(defaultValue));
    }

    static CompletableFuture<Boolean> check(@NotNull UUID uuid, @NotNull String permission) {
        return getPermissionValue(uuid, permission).thenApplyAsync(state -> state.orElse(false));
    }

    static CompletableFuture<Boolean> check(@NotNull GameProfile profile, @NotNull String permission, boolean defaultValue) {
        Objects.requireNonNull(profile, "profile");
        return check(profile.getId(), permission, defaultValue);
    }

    static CompletableFuture<Boolean> check(@NotNull GameProfile profile, @NotNull String permission) {
        Objects.requireNonNull(profile, "profile");
        return check(profile.getId(), permission);
    }

    static CompletableFuture<Boolean> check(@NotNull GameProfile profile, @NotNull String permission, int defaultRequiredLevel, @NotNull MinecraftServer server) {
        Objects.requireNonNull(profile, "profile");
        Objects.requireNonNull(server, "server");
        BooleanSupplier permissionLevelCheck = () -> server.getPermissionLevel(profile) >= defaultRequiredLevel;
        return getPermissionValue(profile.getId(), permission).thenApplyAsync(state -> state.orElseGet(permissionLevelCheck));
    }

}
