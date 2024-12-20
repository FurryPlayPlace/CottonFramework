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

package net.furryplayplace.cottonframework.manager;

import net.furryplayplace.cottonframework.api.plugin.CottonPlugin;
import net.furryplayplace.cottonframework.api.plugin.PluginContainer;
import net.furryplayplace.cottonframework.api.plugin.PluginState;
import com.google.common.eventbus.*;
import net.furryplayplace.cottonframework.api.command.AbstractCommand;
import net.furryplayplace.cottonframework.api.exceptions.PluginAlreadyRegisteredExceptions;
import net.furryplayplace.cottonframework.api.exceptions.PluginNotRegisteredExceptions;
import net.furryplayplace.cottonframework.api.plugin.interfaces.IPluginManager;
import net.furryplayplace.cottonframework.manager.plugin.CottonClassLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.File;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import static com.google.common.util.concurrent.MoreExecutors.directExecutor;

public class PluginManager implements IPluginManager {
    private final Logger logger = LogManager.getLogger("PluginManager");
    private final HashMap<String, PluginContainer<CottonPlugin>> plugins = new HashMap<>();
    private final HashMap<CottonPlugin, AbstractCommand> commands = new HashMap<>();

    private final AsyncEventBus eventBus = new AsyncEventBus("CottonEventBus", directExecutor());

    private final File pluginsDir = new File("plugins");

    public PluginManager() {
        if (!pluginsDir.exists()) {
            pluginsDir.mkdirs();
            this.logger.info("Created plugins directory.");
        }

        this.logger.info("EventBus: {} with Dispatcher {}", this.eventBus.identifier(), (this.eventBus.getDispatcher() instanceof Dispatcher.ImmediateDispatcher ? "Immediate Mode" : "perThread mode") );
    }

    /**
     * Registers a plugin with the system.
     *
     * @param plugin the {@code CottonPlugin} to register.
     */
    @Override
    public void registerPlugin(CottonPlugin plugin) throws PluginAlreadyRegisteredExceptions {
        PluginContainer<CottonPlugin> currentPlugin = this.plugins.getOrDefault(plugin.name(), null);
        if (currentPlugin != null)
            throw new PluginAlreadyRegisteredExceptions(plugin.name() + " is already registered and cannot be registered again.");

        File pluginFile = new File(this.pluginsDir, plugin.name());
        if (!pluginFile.exists()) {
            pluginFile.mkdirs();
            this.logger.info("Created plugin directory for {}.", plugin.name());
        }



        this.plugins.put(plugin.name(), new PluginContainer<>(plugin, PluginState.LOADED));
        this.logger.info("Registered plugin {} ({}) by {}.", plugin.name(), plugin.version(), String.join(", ", plugin.authors()));
    }

    /**
     * Unregisters a plugin from the system.
     *
     * @param plugin the {@code CottonPlugin} to unregister.
     */
    @Override
    public void unregisterPlugin(CottonPlugin plugin) throws PluginNotRegisteredExceptions {
        PluginContainer<CottonPlugin> currentPlugin = this.plugins.getOrDefault(plugin.name(), null);
        if (currentPlugin == null)
            throw new PluginNotRegisteredExceptions(plugin.name() + " is not registered.");

        currentPlugin.setState(PluginState.DISABLED);
        currentPlugin.getPlugin().onDisable();
        this.plugins.remove(plugin.name());
        this.logger.info("Unregistered plugin {} ({}) by {}.", plugin.name(), plugin.version(), String.join(", ", plugin.authors()));
    }

    /**
     * Retrieves a registered plugin by its name.
     *
     * @param name the name of the plugin.
     * @return the {@code CottonPlugin} with the specified name, or {@code null} if not found.
     */
    @Override
    public CottonPlugin getPlugin(String name) {
        return this.plugins.getOrDefault(name, null)
                .getPlugin();
    }

    /**
     * Retrieves all registered plugins.
     *
     * @return an array of all registered {@code CottonPlugin} instances.
     */
    @Override
    public ArrayList<CottonPlugin> getPlugins() {
        // Anti Concurrent modification list
        return new ArrayList<>(this.plugins.values().stream().map(PluginContainer::getPlugin).toList());
    }

    @Override
    public ArrayList<PluginContainer<CottonPlugin>> getPluginsWithContainer() {
        return new ArrayList<>(this.plugins.values().stream().toList());
    }

    /**
     * Checks if a plugin is enabled by its name.
     *
     * @param name the name of the plugin.
     * @return {@code true} if the plugin is enabled, otherwise {@code false}.
     */
    @Override
    public boolean isPluginEnabled(String name) {
        PluginContainer<CottonPlugin> currentPlugin = this.plugins.getOrDefault(name, null);
        if (currentPlugin == null)
            return false;
        return currentPlugin.getState() == PluginState.ENABLED;
    }

    /**
     * Checks if a specific plugin instance is enabled.
     *
     * @param plugin the {@code CottonPlugin} instance to check.
     * @return {@code true} if the plugin is enabled, otherwise {@code false}.
     */
    @Override
    public boolean isPluginEnabled(CottonPlugin plugin) {
        PluginContainer<CottonPlugin> currentPlugin = this.plugins.getOrDefault(plugin.name(), null);
        if (currentPlugin == null)
            return false;
        return currentPlugin.getState() == PluginState.ENABLED;
    }

    /**
     * Enables a plugin by its name.
     *
     * @param name the name of the plugin to enable.
     */
    @Override
    public void enablePlugin(String name) throws PluginNotRegisteredExceptions {
        PluginContainer<CottonPlugin> currentPlugin = this.plugins.getOrDefault(name, null);
        if (currentPlugin == null)
            throw new PluginNotRegisteredExceptions(name + " is not registered.");

        currentPlugin.setState(PluginState.ENABLED);
        currentPlugin.getPlugin().onLoad();
        currentPlugin.getPlugin().onEnable();

        this.logger.info("Enabled plugin {} ({}) by {}.", currentPlugin.getPlugin().name(), currentPlugin.getPlugin().version(), String.join(", ", currentPlugin.getPlugin().authors()));
    }

    /**
     * Disables a plugin by its name.
     *
     * @param name the name of the plugin to disable.
     */
    @Override
    public void disablePlugin(String name) throws PluginNotRegisteredExceptions {
        PluginContainer<CottonPlugin> currentPlugin = this.plugins.getOrDefault(name, null);
        if (currentPlugin == null)
            throw new PluginNotRegisteredExceptions(name + " is not registered.");

        currentPlugin.setState(PluginState.DISABLED);
        currentPlugin.getPlugin().onDisable();

        this.logger.info("Disabled plugin {} ({}) by {}.", currentPlugin.getPlugin().name(), currentPlugin.getPlugin().version(), String.join(", ", currentPlugin.getPlugin().authors()));
    }

    /**
     * Reloads all registered plugins.
     */
    @Override
    public void reloadPlugins() {
        throw new IllegalStateException("Not yet implemented.");
    }

    /**
     * Reloads a plugin by its name.
     *
     * @param name the name of the plugin to reload.
     */
    @Override
    public void reloadPlugin(String name) {
        throw new IllegalStateException("Not yet implemented.");
    }

    /**
     * Reloads a specific plugin instance.
     *
     * @param plugin the {@code CottonPlugin} instance to reload.
     */
    @Override
    public void reloadPlugin(CottonPlugin plugin) {
        throw new IllegalStateException("Not yet implemented.");
    }

    /**
     * Saves the state of all plugins to persistent storage.
     */
    @Override
    public void savePlugins() {
        throw new IllegalStateException("Not yet implemented.");
    }

    /**
     * Saves the state of a specific plugin by its name.
     *
     * @param name the name of the plugin to save.
     */
    @Override
    public void savePlugin(String name) {
        throw new IllegalStateException("Not yet implemented.");
    }

    /**
     * Saves the state of a specific plugin instance.
     *
     * @param plugin the {@code CottonPlugin} instance to save.
     */
    @Override
    public void savePlugin(CottonPlugin plugin) {
        throw new IllegalStateException("Not yet implemented.");
    }

    /**
     * Loads all plugins into the system.
     */
    @Override
    public void loadPlugins() {
        for (File jarFile : Objects.requireNonNull(this.pluginsDir.listFiles())) {
            if (!jarFile.getName().endsWith(".jar")) continue;

            this.logger.info("Loading plugin {}...", jarFile.getName());

            try (JarFile jar = new JarFile(jarFile)) {
                Manifest manifest = jar.getManifest();
                if (manifest == null) {
                    throw new SecurityException("Missing PLUGIN.manifest in: " + jarFile.getName());
                }

                String mainClass = manifest.getMainAttributes().getValue("Plugin-Main-Class");
                if (mainClass == null || mainClass.isBlank()) {
                    throw new SecurityException("Plugin-Main-Class not specified in Manifest: " + jarFile.getName());
                }

                String pluginName = manifest.getMainAttributes().getValue("Plugin-Name");
                if (pluginName == null || pluginName.isBlank()) {
                    throw new SecurityException("Plugin-Name not specified in Manifest: " + jarFile.getName());
                }

                for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements(); ) {
                    JarEntry entry = entries.nextElement();
                    if (entry.isDirectory()) continue;

                    String name = entry.getName();
                    if (name.contains("..") || name.startsWith("/") || name.startsWith("\\")) {
                        throw new SecurityException("Invalid JAR entry detected: " + name);
                    }
                }

                CottonClassLoader pluginClassLoader = CottonClassLoader.createSecureClassLoader(jarFile);

                PluginContainer<CottonPlugin> currentPlugin = this.plugins.get(pluginName);
                if (currentPlugin != null) {
                    throw new PluginAlreadyRegisteredExceptions("Plugin " + pluginName + " is already registered.");
                }

                Class<?> pluginClass = pluginClassLoader.loadClass(mainClass);
                if (!CottonPlugin.class.isAssignableFrom(pluginClass)) {
                    throw new ClassCastException("Class " + mainClass + " does not implement CottonPlugin interface.");
                }

                CottonPlugin pluginInstance = (CottonPlugin) pluginClass.getDeclaredConstructor().newInstance();
                this.registerPlugin(pluginInstance);
                this.logger.info("Successfully loaded plugin {} version {} by {}.",
                        pluginInstance.name(),
                        pluginInstance.version(),
                        String.join(", ", pluginInstance.authors()));
            } catch (Exception | PluginAlreadyRegisteredExceptions e) {
                this.logger.error("Failed to load plugin {}.", jarFile.getName(), e);
            }
        }
    }

    /**
     * Loads a plugin by its name into the system.
     *
     * @param name the name of the plugin to load.
     */
    @Override
    public void loadPlugin(String name) throws PluginNotRegisteredExceptions {
        PluginContainer<CottonPlugin> pluginContainer = this.plugins.get(name);
        if (pluginContainer == null) {
            this.logger.error("Plugin with name {} is not registered.", name);
            throw new PluginNotRegisteredExceptions("Plugin " + name + " is not registered.");
        }

        try {
            if (pluginContainer.getState() == PluginState.LOADED) {
                pluginContainer.setState(PluginState.ENABLED);
                pluginContainer.getPlugin().onEnable();
                this.logger.info("Loaded plugin {} ({}) by {}.",
                        pluginContainer.getPlugin().name(),
                        pluginContainer.getPlugin().version(),
                        String.join(", ", pluginContainer.getPlugin().authors()));
            } else {
                this.logger.warn("Plugin {} is already enabled or in an invalid state.", name);
            }
        } catch (Exception e) {
            this.logger.error("Failed to load plugin {}: {}", name, e.getMessage(), e);
        }
    }

    /**
     * Loads a specific plugin instance into the system.
     *
     * @param plugin the {@code CottonPlugin} instance to load.
     */
    @Override
    public void loadPlugin(CottonPlugin plugin) throws PluginNotRegisteredExceptions {
        if (plugin == null) {
            this.logger.error("Cannot load a null plugin instance.");
            throw new IllegalArgumentException("Plugin instance cannot be null.");
        }

        String pluginName = plugin.name();
        PluginContainer<CottonPlugin> pluginContainer = this.plugins.get(pluginName);

        if (pluginContainer == null) {
            this.logger.error("Plugin instance {} is not registered.", pluginName);
            throw new PluginNotRegisteredExceptions("Plugin " + pluginName + " is not registered.");
        }

        try {
            if (pluginContainer.getState() == PluginState.LOADED) {
                pluginContainer.setState(PluginState.ENABLED);
                plugin.onEnable();
                this.logger.info("Loaded plugin {} ({}) by {}.",
                        plugin.name(),
                        plugin.version(),
                        String.join(", ", plugin.authors()));
            } else {
                this.logger.warn("Plugin {} is already enabled or in an invalid state.", pluginName);
            }
        } catch (Exception e) {
            this.logger.error("Failed to load plugin {}: {}", pluginName, e.getMessage(), e);
        }
    }

    /**
     * Unloads all plugins from the system.
     */
    @Override
    public void unloadPlugins() {
        for (String pluginName : this.plugins.keySet()) {
            PluginContainer<CottonPlugin> pluginContainer = this.plugins.get(pluginName);

            if (pluginContainer != null) {
                try {
                    if (pluginContainer.getState() == PluginState.ENABLED) {
                        pluginContainer.getPlugin().onDisable();
                        pluginContainer.setState(PluginState.DISABLED);
                        this.logger.info("Disabled plugin {}.", pluginName);
                    }

                    unloadPluginClasses(pluginContainer);

                    this.plugins.remove(pluginName);
                    this.logger.info("Unloaded and removed plugin {} from memory.", pluginName);

                } catch (Exception e) {
                    this.logger.error("Failed to unload plugin {}: {}", pluginName, e.getMessage(), e);
                }
            }
        }
    }

    /**
     * Unloads the plugin's classes from memory, including clearing loaded bytecodes.
     *
     * @param pluginContainer the container of the plugin to unload.
     */
    private void unloadPluginClasses(PluginContainer<CottonPlugin> pluginContainer) {
        try {
            ClassLoader pluginClassLoader = pluginContainer.getPlugin().getClass().getClassLoader();

            if (pluginClassLoader instanceof CottonClassLoader) {
                ((Closeable) pluginClassLoader).close();

                this.logger.info("Cleared bytecodes for plugin {}.", pluginContainer.getPlugin().name());
            }
        } catch (Exception e) {
            this.logger.error("Failed to unload classes for plugin {}: {}",
                    pluginContainer.getPlugin().name(), e.getMessage(), e);
        }
    }

    /**
     * Uses ASM to remove the class definition from the JVM.
     *
     * @param clazz the class to unload.
     */
    private void unloadClass(Class<?> clazz) {
        this.logger.debug("Unloading class {}.", clazz.getName());
    }

    /**
     * Unloads a plugin by its name from the system.
     *
     * @param name the name of the plugin to unload.
     */
    @Override
    public void unloadPlugin(String name) {

    }

    /**
     * Unloads a specific plugin instance from the system.
     *
     * @param plugin the {@code CottonPlugin} instance to unload.
     */
    @Override
    public void unloadPlugin(CottonPlugin plugin) {

    }

    /**
     * Disables all plugins currently registered in the system.
     */
    @Override
    public void disableAllPlugins() {
        this.plugins.values().forEach(plugin -> {
            plugin.setState(PluginState.DISABLED);
            plugin.getPlugin().onDisable();
            this.logger.info("Disabled plugin {} ({}) by {}.", plugin.getPlugin().name(), plugin.getPlugin().version(), String.join(", ", plugin.getPlugin().authors()));
        });
    }

    /**
     * Enables all plugins currently registered in the system.
     */
    @Override
    public void enableAllPlugins() {
        this.plugins.values().forEach(plugin -> {
            plugin.setState(PluginState.ENABLED);
            plugin.getPlugin().onEnable();
            this.logger.info("Enabled plugin {} ({}) by {}.", plugin.getPlugin().name(), plugin.getPlugin().version(), String.join(", ", plugin.getPlugin().authors()));
        });
    }

    @Override
    public EventBus getEventBus() {
        return this.eventBus;
    }

    @Override
    public void registerCommand(CottonPlugin plugin, AbstractCommand command) {
        this.commands.put(plugin, command);
        this.logger.info("Registered command {} plugin {}", command.getName(), plugin.name());
    }

    @Override
    public void unregisterCommand(CottonPlugin plugin, AbstractCommand command) {
        this.commands.remove(plugin, command);
        this.logger.info("Unregistered command {} plugin {}", command.getName(), plugin.name());
    }

    @Override
    public void unregisterAllCommands() {
        this.commands.clear();
        this.logger.info("Unregistered all commands.");
    }

    @Override
    public List<AbstractCommand> getCommands() {
        return new ArrayList<>(this.commands.values());
    }
}