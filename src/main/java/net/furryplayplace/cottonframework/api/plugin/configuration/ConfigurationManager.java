/*
---------------------------------------------------------------------------------
File Name : ConfigurateManager

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 20.12.2024
Last Modified : 20.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.api.plugin.configuration;

import net.furryplayplace.cottonframework.api.plugin.CottonPlugin;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationManager {
    private final CottonPlugin plugin;
    private final File file;
    private Map<String, Object> data;

    public ConfigurationManager(CottonPlugin plugin, File pluginsDir, @NotNull File configDir) {
        this.plugin = plugin;

        this.file = new File(pluginsDir, configDir.getPath() + File.separator + "config.yml");
    }

    public void load() {
        Yaml yaml = new Yaml(new Constructor(Map.class, new LoaderOptions()));
        try (FileInputStream inputStream = new FileInputStream(file)) {
            Object loadedData = yaml.load(inputStream);
            if (loadedData instanceof Map) {
                data = (Map<String, Object>) loadedData;
            } else {
                data = new HashMap<>();
            }
        } catch (IOException e) {
            this.plugin.getLogger().severe("Unable to load config.yml");
            e.printStackTrace();
        }
    }

    public void save() {
        Yaml yaml = new Yaml();
        try (FileWriter writer = new FileWriter(file)) {
            yaml.dump(data, writer);
        } catch (Exception e) {
            this.plugin.getLogger().severe("Unable to save config.yml");
            e.printStackTrace();
        }
    }

    public String getString(String path) {
        Object value = get(path);
        return value != null ? value.toString() : null;
    }

    public String getString(String path, String defaultValue) {
        Object value = get(path);
        return value != null ? value.toString() : defaultValue;
    }

    public boolean getBoolean(String path) {
        Object value = get(path);
        return value instanceof Boolean ? (Boolean) value : false;
    }

    public int getInt(String path) {
        Object value = get(path);
        return value instanceof Integer ? (Integer) value : 0;
    }

    public double getDouble(String path) {
        Object value = get(path);
        return value instanceof Double ? (Double) value : 0.0D;
    }

    public void set(String path, Object value) {
        String[] keys = path.split("\\.");
        Map<String, Object> currentMap = data;

        for (int i = 0; i < keys.length - 1; i++) {
            String key = keys[i];
            if (!currentMap.containsKey(key) || !(currentMap.get(key) instanceof Map)) {
                currentMap.put(key, new HashMap<>());
            }
            currentMap = (Map<String, Object>) currentMap.get(key);
        }

        currentMap.put(keys[keys.length - 1], value);
    }

    public Object get(String path) {
        String[] keys = path.split("\\.");
        Map<String, Object> currentMap = data;

        for (int i = 0; i < keys.length - 1; i++) {
            String key = keys[i];
            if (currentMap.containsKey(key) && currentMap.get(key) instanceof Map) {
                currentMap = (Map<String, Object>) currentMap.get(key);
            } else {
                return null;
            }
        }

        return currentMap.get(keys[keys.length - 1]);
    }
}
