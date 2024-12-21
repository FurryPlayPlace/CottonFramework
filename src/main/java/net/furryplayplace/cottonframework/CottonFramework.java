package net.furryplayplace.cottonframework;

import com.google.common.eventbus.Subscribe;
import com.mojang.authlib.GameProfile;
import lombok.Getter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.util.TriState;
import net.fabricmc.loader.api.FabricLoader;
import net.furryplayplace.cottonframework.api.CottonAPI;
import net.furryplayplace.cottonframework.api.events.cotton.CottonPluginShutdown;
import net.furryplayplace.cottonframework.api.permissions.v0.PermissionCheckEvent;
import net.furryplayplace.cottonframework.api.permissions.v1.CottonPermissions;
import net.furryplayplace.cottonframework.api.permissions.v1.Permissible;
import net.furryplayplace.cottonframework.api.permissions.v1.Permission;
import net.furryplayplace.cottonframework.api.permissions.v1.PermissionDefaults;
import net.furryplayplace.cottonframework.configuration.PermissionConfiguration;
import net.furryplayplace.cottonframework.contributors.ContributorManager;
import net.furryplayplace.cottonframework.manager.command.AboutCommand;
import net.furryplayplace.cottonframework.manager.command.PermissionCommand;
import net.furryplayplace.cottonframework.manager.command.PluginsCommand;
import net.furryplayplace.cottonframework.manager.command.VersionCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class CottonFramework implements ModInitializer  {
    @Getter
    private final Logger logger = LogManager.getLogger("CottonFramework");

    @Getter
    private final File dataFolder = new File(FabricLoader.getInstance().getConfigDir().toFile(), ".cotton");

    @Getter
    private final File permissionsFolder = new File(dataFolder, "users");

    @Getter
    private final File groupsFolder = new File(permissionsFolder, "groups");

    public static final String MOD_ID = "cottonframework";
    public static final String MOD_NAME = "Cotton Framework";
    public static final String MOD_VERSION = "${version}";
    public static final String MOD_AUTHOR = "Vakea";

    @Getter
    private static CottonFramework instance;

    @Getter
    public final CottonAPI api = new BaseCottonAPI();

    public static HashMap<String, PermissionConfiguration> groups = new HashMap<>();
    public static HashMap<String, PermissionConfiguration> users = new HashMap<>();

    @Override
    public void onInitialize() {
        instance = this;

        dataFolder.mkdirs();
        permissionsFolder.mkdirs();
        groupsFolder.mkdirs();

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

        PermissionCommand cmd = new PermissionCommand();
        PluginsCommand plugins = new PluginsCommand();
        VersionCommand versionCommand = new VersionCommand();
        AboutCommand aboutCommand = new AboutCommand();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            cmd.register(dispatcher, "perms");
            cmd.register(dispatcher, "cotton-permissions");
            plugins.register(dispatcher, "plugins");
            plugins.register(dispatcher, "pl");
            versionCommand.register(dispatcher, "version");
            aboutCommand.register(dispatcher, "about");

            this.api.pluginManager().getCommands().forEach(command -> {
                command.register(dispatcher, command.getName());
                this.logger.info("Registered command: {}", command.getName());
            });
        });

        for (File f : Objects.requireNonNull(groupsFolder.listFiles())) {
            PermissionConfiguration conf = new PermissionConfiguration(f);
            groups.put(conf.name, conf);
        }

        if (!groups.containsKey("default")) {
            try {
                PermissionConfiguration conf = new PermissionConfiguration("default");
                groups.put(conf.name, conf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (File f : Objects.requireNonNull(permissionsFolder.listFiles())) {
            PermissionConfiguration conf = new PermissionConfiguration(f);
            users.put(conf.uuid, conf);
        }

        PermissionCheckEvent.EVENT.register((source, permission) -> {
            if (source instanceof ServerCommandSource ss) {
                try {
                    ServerPlayerEntity plr = ss.getPlayer();
                    Permissible p = CottonPermissions.getPlayerPermissible(plr);
                    Permission perm = new Permission(permission, "LuckPerms API provided permission", PermissionDefaults.OPERATOR);
                    boolean hass = p.hasPermission(perm);
                    if (hass) {
                        return TriState.TRUE;
                    }
                } catch (Exception ignored) {}
            }

            return TriState.DEFAULT;
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

        System.exit(0);
    }

    public static GameProfile findGameProfile(ServerCommandSource cs, String name) {
        if (name.length() >= 32)
            return cs.getServer().getUserCache().getByUuid(UUID.fromString(name)).get();
        return cs.getServer().getUserCache().findByName(name).get();
    }

    public static PermissionConfiguration getUser(GameProfile profile) {
        String uuid = profile.getId().toString();
        if (users.containsKey(uuid))
            return users.get(uuid);

        try {
            PermissionConfiguration conf = new PermissionConfiguration(profile);
            users.put(conf.uuid, conf);
            return conf;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static PermissionConfiguration getUser(ServerPlayerEntity e) {
        String uuid = e.getUuid().toString();
        if (users.containsKey(uuid))
            return users.get(uuid);

        try {
            PermissionConfiguration conf = new PermissionConfiguration(e);
            users.put(conf.uuid, conf);
            return conf;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
