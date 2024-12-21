package net.furryplayplace.cottonframework.api.permissions.v0;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.util.TriState;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Simple permissions check event for (potentially) offline players.
 */
public interface OfflinePermissionCheckEvent {
    Event<OfflinePermissionCheckEvent> EVENT = EventFactory.createArrayBacked(OfflinePermissionCheckEvent.class, (callbacks) -> (uuid, permission) -> {
        CompletableFuture<TriState> res = CompletableFuture.completedFuture(TriState.DEFAULT);
        for (OfflinePermissionCheckEvent callback : callbacks) {
            res = res.thenCompose(triState -> {
                if (triState != TriState.DEFAULT) {
                    return CompletableFuture.completedFuture(triState);
                }
                return callback.onPermissionCheck(uuid, permission);
            });
        }
        return res;
    });

    @NotNull CompletableFuture<TriState> onPermissionCheck(@NotNull UUID uuid, @NotNull String permission);
}
