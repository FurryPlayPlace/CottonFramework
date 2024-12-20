package net.furryplayplace.cottontestplugin;

import com.google.common.eventbus.Subscribe;
import net.fabricmc.api.ModInitializer;
import net.furryplayplace.cottonframework.api.CottonAPI;
import net.furryplayplace.cottonframework.api.events.player.PlayerJoinEvent;
import net.furryplayplace.cottonframework.api.plugin.CottonPlugin;
import net.furryplayplace.cottonframework.api.events.block.BlockBreakEvent;
import net.furryplayplace.cottonframework.api.events.block.BlockPlaceEvent;

import net.furryplayplace.cottontestplugin.commands.TestCommand;
import net.minecraft.text.Text;

import java.util.List;

public class Cottontestplugin extends CottonPlugin implements ModInitializer  {

    public Cottontestplugin() {
        super("TestPlugin", "1.0.0", List.of("Vakea"));
    }

    @Override
    public void onInitialize() {}

    @Override
    public void onEnable() {
        System.out.println("Hello World!");

        CottonAPI.get().pluginManager().getEventBus().register(this);
        CottonAPI.get().pluginManager().registerCommand(this, new TestCommand());
    }

    @Override
    public void onDisable() {

    }

    @Subscribe
    public void onBlockBreak(BlockBreakEvent event) {
       event.getPlayer().sendMessage(Text.of("You broke a block!"));
    }

    @Subscribe
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(Text.of("Hello, world!"));
    }

    @Subscribe
    public void onBlockPlace(BlockPlaceEvent event) {
        event.getPlayer().sendMessage(Text.of("You placed a block!"));
    }
}
