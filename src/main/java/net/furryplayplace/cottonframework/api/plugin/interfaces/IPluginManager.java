/*
---------------------------------------------------------------------------------
File Name : PluginManager

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 19.12.2024
Last Modified : 19.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.api.plugin.interfaces;

import com.google.common.eventbus.EventBus;
import net.furryplayplace.cottonframework.api.command.AbstractCommand;
import net.furryplayplace.cottonframework.api.exceptions.PluginAlreadyRegisteredExceptions;
import net.furryplayplace.cottonframework.api.exceptions.PluginNotRegisteredExceptions;
import net.furryplayplace.cottonframework.api.plugin.JavaPlugin;
import net.furryplayplace.cottonframework.api.plugin.PluginContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code PluginManager} interface defines methods for managing plugins in the Cotton API.
 * It provides capabilities to register, enable, disable, load, unload, and manage plugin states.
 */
public interface IPluginManager extends ICommandManager {

    /**
     * Registers a plugin with the system.
     *
     * @param plugin the {@code CottonPlugin} to register.
     */
    void registerPlugin(JavaPlugin plugin) throws PluginAlreadyRegisteredExceptions;

    /**
     * Unregisters a plugin from the system.
     *
     * @param plugin the {@code CottonPlugin} to unregister.
     */
    void unregisterPlugin(JavaPlugin plugin) throws PluginNotRegisteredExceptions;

    /**
     * Retrieves a registered plugin by its name.
     *
     * @param name the name of the plugin.
     * @return the {@code CottonPlugin} with the specified name, or {@code null} if not found.
     */
    JavaPlugin getPlugin(String name);

    /**
     * Retrieves all registered plugins.
     *
     * @return an array of all registered {@code CottonPlugin} instances.
     */
    ArrayList<JavaPlugin> getPlugins();

    /**
     * Checks if a plugin is enabled by its name.
     *
     * @param name the name of the plugin.
     * @return {@code true} if the plugin is enabled, otherwise {@code false}.
     */
    boolean isPluginEnabled(String name);

    /**
     * Checks if a specific plugin instance is enabled.
     *
     * @param plugin the {@code CottonPlugin} instance to check.
     * @return {@code true} if the plugin is enabled, otherwise {@code false}.
     */
    boolean isPluginEnabled(JavaPlugin plugin);

    /**
     * Enables a plugin by its name.
     *
     * @param name the name of the plugin to enable.
     */
    void enablePlugin(String name) throws PluginNotRegisteredExceptions;

    /**
     * Disables a plugin by its name.
     *
     * @param name the name of the plugin to disable.
     */
    void disablePlugin(String name) throws PluginNotRegisteredExceptions;

    /**
     * Reloads all registered plugins.
     */
    void reloadPlugins();

    /**
     * Reloads a plugin by its name.
     *
     * @param name the name of the plugin to reload.
     */
    void reloadPlugin(String name);

    /**
     * Reloads a specific plugin instance.
     *
     * @param plugin the {@code CottonPlugin} instance to reload.
     */
    void reloadPlugin(JavaPlugin plugin);

    /**
     * Saves the state of all plugins to persistent storage.
     */
    void savePlugins();

    /**
     * Saves the state of a specific plugin by its name.
     *
     * @param name the name of the plugin to save.
     */
    void savePlugin(String name);

    /**
     * Saves the state of a specific plugin instance.
     *
     * @param plugin the {@code CottonPlugin} instance to save.
     */
    void savePlugin(JavaPlugin plugin);

    /**
     * Loads all plugins into the system.
     */
    void loadPlugins();

    /**
     * Loads a plugin by its name into the system.
     *
     * @param name the name of the plugin to load.
     */
    void loadPlugin(String name) throws PluginNotRegisteredExceptions;

    /**
     * Loads a specific plugin instance into the system.
     *
     * @param plugin the {@code CottonPlugin} instance to load.
     */
    void loadPlugin(JavaPlugin plugin) throws PluginNotRegisteredExceptions;

    /**
     * Unloads all plugins from the system.
     */
    void unloadPlugins();

    /**
     * Unloads a plugin by its name from the system.
     *
     * @param name the name of the plugin to unload.
     */
    void unloadPlugin(String name);

    /**
     * Unloads a specific plugin instance from the system.
     *
     * @param plugin the {@code CottonPlugin} instance to unload.
     */
    void unloadPlugin(JavaPlugin plugin);

    /**
     * Disables all plugins currently registered in the system.
     */
    void disableAllPlugins();

    /**
     * Enables all plugins currently registered in the system.
     */
    void enableAllPlugins();

    EventBus getEventBus();

    List<AbstractCommand> getCommands();

    ArrayList<PluginContainer<JavaPlugin>> getPluginsWithContainer();
}
