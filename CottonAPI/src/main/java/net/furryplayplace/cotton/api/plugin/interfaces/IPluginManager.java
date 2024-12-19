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

package net.furryplayplace.cotton.api.plugin.interfaces;

import com.google.common.eventbus.EventBus;
import net.furryplayplace.cotton.api.command.AbstractCommand;
import net.furryplayplace.cotton.api.exceptions.PluginAlreadyRegisteredExceptions;
import net.furryplayplace.cotton.api.exceptions.PluginNotRegisteredExceptions;
import net.furryplayplace.cotton.api.plugin.CottonPlugin;
import net.furryplayplace.cotton.api.plugin.PluginContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code PluginManager} interface defines methods for managing plugins in the Cotton API.
 * It provides capabilities to register, enable, disable, load, unload, and manage plugin states.
 */
public interface IPluginManager {

    /**
     * Registers a plugin with the system.
     *
     * @param plugin the {@code CottonPlugin} to register.
     */
    void registerPlugin(CottonPlugin plugin) throws PluginAlreadyRegisteredExceptions;

    /**
     * Unregisters a plugin from the system.
     *
     * @param plugin the {@code CottonPlugin} to unregister.
     */
    void unregisterPlugin(CottonPlugin plugin) throws PluginNotRegisteredExceptions;

    /**
     * Retrieves a registered plugin by its name.
     *
     * @param name the name of the plugin.
     * @return the {@code CottonPlugin} with the specified name, or {@code null} if not found.
     */
    CottonPlugin getPlugin(String name);

    /**
     * Retrieves all registered plugins.
     *
     * @return an array of all registered {@code CottonPlugin} instances.
     */
    ArrayList<CottonPlugin> getPlugins();

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
    boolean isPluginEnabled(CottonPlugin plugin);

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
    void reloadPlugin(CottonPlugin plugin);

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
    void savePlugin(CottonPlugin plugin);

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
    void loadPlugin(CottonPlugin plugin) throws PluginNotRegisteredExceptions;

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
    void unloadPlugin(CottonPlugin plugin);

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

    ArrayList<PluginContainer<CottonPlugin>> getPluginsWithContainer();
}
