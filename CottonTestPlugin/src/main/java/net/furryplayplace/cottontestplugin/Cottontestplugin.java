package net.furryplayplace.cottontestplugin;

import com.google.common.eventbus.Subscribe;
import net.fabricmc.api.ModInitializer;
import net.furryplayplace.cottonframework.api.CottonAPI;
import net.furryplayplace.cottonframework.api.events.player.PlayerChatMessageEvent;
import net.furryplayplace.cottonframework.api.events.player.PlayerCraftRequestEvent;
import net.furryplayplace.cottonframework.api.events.player.PlayerJoinEvent;
import net.furryplayplace.cottonframework.api.plugin.CottonPlugin;
import net.furryplayplace.cottonframework.api.events.block.BlockBreakEvent;
import net.furryplayplace.cottonframework.api.events.block.BlockPlaceEvent;

import net.furryplayplace.cottontestplugin.commands.TestCommand;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class Cottontestplugin extends CottonPlugin  {

    private static Cottontestplugin instance;

    public Cottontestplugin() {
        super("TestPlugin", "1.0.0", List.of("Vakea"));
    }

    @Override
    public void onEnable() {
        instance = this;
        System.out.println("Hello World!");

        CottonAPI.get().pluginManager().getEventBus().register(this);
        CottonAPI.get().pluginManager().registerCommand(this, new TestCommand());
    }

    @Override
    public void onDisable() {



    }

    @Override
    public void onLoad() {}

    @Subscribe
    public void onBlockBreak(BlockBreakEvent event) {
       event.getPlayer().sendMessage(Text.of("You broke a block!"));
       event.setCancelled(true);
    }

    @Subscribe
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(Text.of("Hello, world!"));

        // Test of instantiation
        if (CottonAPI.get().server().getPlayerManager().areCheatsAllowed()) {
            event.getPlayer().sendMessage(Text.of("Cheats is allowed!"));
        } else {
            event.getPlayer().sendMessage(Text.of("Cheats is not allowed!"));
        }
    }

    @Subscribe
    public void onBlockPlace(BlockPlaceEvent event) {
        event.getPlayer().sendMessage(Text.of("You placed a block!"));
        event.setCancelled(true);
    }

    @Subscribe
    public void onChatMessage(PlayerChatMessageEvent event) {
        event.getPlayer().sendMessage(Text.literal( Formatting.GRAY + "Echo: " + event.getTextMessage().getString()));
        event.setCancelled(true);
    }

    @Subscribe
    public void onCraftRequest(PlayerCraftRequestEvent event) {
        if (event.getPlayer().experienceLevel < 10) {
            event.getPlayer().sendMessage(Text.literal(Formatting.RED + "You need at least 10 experience levels to craft!"));
            event.setCancelled(true);
        }
    }

    public static Cottontestplugin getInstance() {
        return instance;
    }
}
