package net.furryplayplace.cottonframework;

import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.furryplayplace.cottonframework.api.CottonAPI;
import net.furryplayplace.cottonframework.api.events.server.ServerLoadEvent;
import net.furryplayplace.cottonframework.api.events.cotton.CottonPluginShutdown;
import net.furryplayplace.cottonframework.contributors.ContributorManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;

public class CottonFramework implements ModInitializer  {
    private final Logger logger = LogManager.getLogger("CottonFramework");

    public static final String MOD_ID = "cottonframework";
    public static final String MOD_NAME = "Cotton Framework";
    public static final String MOD_VERSION = "${version}";
    public static final String MOD_AUTHOR = "Vakea";

    @Getter
    private static CottonFramework instance;

    @Getter
    public final CottonAPI api = new BaseCottonAPI();

    @Override
    public void onInitialize() {
        instance = this;

        this.logger.info("   ******    *******   ********** **********   *******   ****     **");
        this.logger.info("  **////**  **/////** /////**/// /////**///   **/////** /**/**   /**");
        this.logger.info(" **    //  **     //**    /**        /**     **     //**/**//**  /**");
        this.logger.info("/**       /**      /**    /**        /**    /**      /**/** //** /**");
        this.logger.info("/**       /**      /**    /**        /**    /**      /**/**  //**/**");
        this.logger.info("//**    **//**     **     /**        /**    //**     ** /**   //****");
        this.logger.info(" //******  //*******      /**        /**     //*******  /**    //***");
        this.logger.info("  //////    ///////       //         //       ///////   //      ///");
        this.logger.info("                 FurryPlayPlace - CottonFramework                  ");

        this.logger.info("Framework Information: ");
        this.logger.info(" - ID: {}", MOD_ID);
        this.logger.info(" - Name: {}", MOD_NAME);
        this.logger.info(" - Version: {}", MOD_VERSION);
        this.logger.info(" - Author: {}", MOD_AUTHOR);

        ContributorManager
                .getContributors()
                .ifPresent(contributorList -> this.logger.info(" - Contributors: {}", String.join(", ", contributorList.stream().map(ContributorManager.Contributor::getLogin).toList())));

        this.logger.info("No support will be provided for this framework.");
        this.logger.info("If you want to contribute, please visit https://github.com/FurryPlayPlace/CottonFramework");
        this.logger.info("If you want to report a bug, please visit https://github.com/FurryPlayPlace/CottonFramework/issues");
        this.logger.info("If you want to request a new feature, please visit https://github.com/FurryPlayPlace/CottonFramework/issues");

        this.api.pluginManager().getEventBus().register(this);

        this.logger.info("Initializing all plugins...");

        this.api.pluginManager().loadPlugins();
        CottonAPI.get().pluginManager().enableAllPlugins();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            if (environment.dedicated) {
                dispatcher.register(literal("plugins").executes(commandContext -> {
                    List<String> pluginNames = this.api.pluginManager().getPluginsWithContainer().stream()
                            .map(cottonPlugin -> switch (cottonPlugin.getState()) {
                                case FAILURE, DISABLED, UNKNOWN -> Formatting.RED + cottonPlugin.getPlugin().name();
                                case ENABLED -> Formatting.GREEN + cottonPlugin.getPlugin().name();
                                case LOADED -> Formatting.GRAY + cottonPlugin.getPlugin().name();
                            }).toList();

                    commandContext.getSource().sendFeedback(() -> Text.literal("Loaded plugins: " + String.join(", ", pluginNames)), false);
                    return 0;
                }));

                dispatcher.register(literal("about-cotton").executes(commandContext -> {
                    commandContext.getSource().sendFeedback(() -> Text.literal("CottonFramework v" + MOD_VERSION), false);
                    commandContext.getSource().sendFeedback(() -> Text.literal("Developed by " + MOD_AUTHOR), false);
                    commandContext.getSource().sendFeedback(() -> Text.literal("Visit https://github.com/FurryPlayPlace/CottonFramework for more information"), false);
                    return 0;
                }));

                dispatcher.register(literal("reload").executes(commandContext -> {
                    this.api.pluginManager().unloadPlugins();
                    this.api.pluginManager().loadPlugins();
                    CottonAPI.get().pluginManager().enableAllPlugins();

                    ServerLoadEvent serverLoadEvent = new ServerLoadEvent(ServerLoadEvent.LoadType.RELOAD);
                    CottonFramework.getInstance().getApi().pluginManager()
                            .getEventBus().post(serverLoadEvent);

                    commandContext.getSource().sendFeedback(() -> Text.literal(Formatting.GRAY + "Plugins have been reloaded."), true);

                    return 1;
                }));

                this.api.pluginManager().getCommands().forEach(command -> {
                    dispatcher.register(literal(command.getName()).executes(commandContext -> command.execute(commandContext, commandContext.getSource().getPlayer())));
                    this.logger.info("Registered command: {}", command.getName());
                });
            }
        });

        this.logger.info("All plugins have been initialized.");
        this.logger.info("CottonFramework is ready to use.");
    }

    @Subscribe
    public void onCottonShutdown(CottonPluginShutdown event) {
        this.logger.info("Shutting down all plugins...");

        this.api.pluginManager().disableAllPlugins();
        this.api.pluginManager().unloadPlugins();

        this.logger.info("All plugins have been unloaded.");
    }
}
