package net.furryplayplace.cottonframework.api.permissions.v0;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.NotNull;

public interface PermissionCheckEvent {

    Event<PermissionCheckEvent> EVENT = EventFactory.createArrayBacked(PermissionCheckEvent.class, (callbacks) -> (source, permission) -> {
        for (PermissionCheckEvent callback : callbacks) {
            TriState state = callback.onPermissionCheck(source, permission);
            if (state != TriState.DEFAULT) {
                return state;
            }
        }
        return TriState.DEFAULT;
    });

    @NotNull TriState onPermissionCheck(@NotNull CommandSource source, @NotNull String permission);

}
