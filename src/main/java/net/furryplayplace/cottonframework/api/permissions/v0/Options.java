package net.furryplayplace.cottonframework.api.permissions.v0;

import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface Options {

    static @NotNull Optional<String> get(@NotNull CommandSource source, @NotNull String key) {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(key, "key");
        return OptionRequestEvent.EVENT.invoker().onOptionRequest(source, key);
    }

    @Contract("_, _, !null -> !null")
    static String get(@NotNull CommandSource source, @NotNull String key, String defaultValue) {
        return get(source, key).orElse(defaultValue);
    }

    static <T> @NotNull Optional<T> get(@NotNull CommandSource source, @NotNull String key, @NotNull Function<String, ? extends T> valueTransformer) {
        return get(source, key).flatMap(value -> {
            try {
                return Optional.ofNullable(valueTransformer.apply(value));
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        });
    }

    @Contract("_, _, !null, _ -> !null")
    static <T> T get(@NotNull CommandSource source, @NotNull String key, T defaultValue, @NotNull Function<String, ? extends T> valueTransformer) {
        return Options.<T>get(source, key, valueTransformer).orElse(defaultValue);
    }

    static @NotNull Optional<String> get(@NotNull Entity entity, @NotNull String key) {
        Objects.requireNonNull(entity, "entity");
        return get(Util.commandSourceFromEntity(entity), key);
    }

    @Contract("_, _, !null -> !null")
    static String get(@NotNull Entity entity, @NotNull String key, String defaultValue) {
        Objects.requireNonNull(entity, "entity");
        return get(Util.commandSourceFromEntity(entity), key, defaultValue);
    }

    static <T> @NotNull Optional<T> get(@NotNull Entity entity, @NotNull String key, @NotNull Function<String, ? extends T> valueTransformer) {
        Objects.requireNonNull(entity, "entity");
        return get(Util.commandSourceFromEntity(entity), key, valueTransformer);
    }

    @Contract("_, _, !null, _ -> !null")
    static <T> T get(@NotNull Entity entity, @NotNull String key, T defaultValue, @NotNull Function<String, ? extends T> valueTransformer) {
        Objects.requireNonNull(entity, "entity");
        return get(Util.commandSourceFromEntity(entity), key, defaultValue, valueTransformer);
    }

    static @NotNull CompletableFuture<Optional<String>> get(@NotNull UUID uuid, @NotNull String key) {
        Objects.requireNonNull(uuid, "uuid");
        Objects.requireNonNull(key, "key");
        return OfflineOptionRequestEvent.EVENT.invoker().onOptionRequest(uuid, key);
    }

    @Contract("_, _, !null -> !null")
    static CompletableFuture<String> get(@NotNull UUID uuid, @NotNull String key, String defaultValue) {
        return get(uuid, key).thenApply(opt -> opt.orElse(defaultValue));
    }

    static <T> @NotNull CompletableFuture<Optional<T>> get(@NotNull UUID uuid, @NotNull String key, @NotNull Function<String, ? extends T> valueTransformer) {
        return get(uuid, key).thenApply(opt -> opt.flatMap(value -> {
            try {
                return Optional.ofNullable(valueTransformer.apply(value));
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        }));
    }

    @Contract("_, _, !null, _ -> !null")
    static <T> CompletableFuture<T> get(@NotNull UUID uuid, @NotNull String key, T defaultValue, @NotNull Function<String, ? extends T> valueTransformer) {
        return Options.<T>get(uuid, key, valueTransformer).thenApply(opt -> opt.orElse(defaultValue));
    }

    static @NotNull CompletableFuture<Optional<String>> get(@NotNull GameProfile profile, @NotNull String key) {
        Objects.requireNonNull(profile, "profile");
        return get(profile.getId(), key);
    }

    @Contract("_, _, !null -> !null")
    static CompletableFuture<String> get(@NotNull GameProfile profile, @NotNull String key, String defaultValue) {
        Objects.requireNonNull(profile, "profile");
        return get(profile.getId(), key, defaultValue);
    }

    static <T> @NotNull CompletableFuture<Optional<T>> get(@NotNull GameProfile profile, @NotNull String key, @NotNull Function<String, ? extends T> valueTransformer) {
        Objects.requireNonNull(profile, "profile");
        return get(profile.getId(), key, valueTransformer);
    }

    @Contract("_, _, !null, _ -> !null")
    static <T> CompletableFuture<T> get(@NotNull GameProfile profile, @NotNull String key, T defaultValue, @NotNull Function<String, ? extends T> valueTransformer) {
        Objects.requireNonNull(profile, "profile");
        return get(profile.getId(), key, defaultValue, valueTransformer);
    }
}
