package net.furryplayplace.cottonframework.api.permissions.v0;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.command.CommandSource;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface OptionRequestEvent {

    Event<OptionRequestEvent> EVENT = EventFactory.createArrayBacked(OptionRequestEvent.class, (callbacks) -> (source, key) -> {
        for (OptionRequestEvent callback : callbacks) {
            Optional<String> value = callback.onOptionRequest(source, key);
            if (value.isPresent()) {
                return value;
            }
        }
        return Optional.empty();
    });

    @NotNull Optional<String> onOptionRequest(@NotNull CommandSource source, @NotNull String key);
}
