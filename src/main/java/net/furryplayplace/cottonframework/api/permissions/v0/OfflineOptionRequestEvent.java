package net.furryplayplace.cottonframework.api.permissions.v0;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface OfflineOptionRequestEvent {

    Event<OfflineOptionRequestEvent> EVENT = EventFactory.createArrayBacked(OfflineOptionRequestEvent.class, (callbacks) -> (uuid, key) -> {
        CompletableFuture<Optional<String>> res = CompletableFuture.completedFuture(null);
        for (OfflineOptionRequestEvent callback : callbacks) {
            res = res.thenCompose(value -> {
                if (value.isPresent()) {
                    return CompletableFuture.completedFuture(value);
                }
                return callback.onOptionRequest(uuid, key);
            });
        }
        return res;
    });

    @NotNull CompletableFuture<Optional<String>> onOptionRequest(@NotNull UUID uuid, @NotNull String key);

}
