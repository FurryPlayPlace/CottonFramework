/*
---------------------------------------------------------------------------------
File Name : CottonPlugin

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 19.12.2024
Last Modified : 19.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.api.plugin;

import lombok.Getter;
import net.furryplayplace.cottonframework.api.plugin.configuration.ConfigurationManager;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public abstract class CottonPlugin  {

    private final String name;
    private final String version;
    private final List<String> authors;

    @Getter
    private final ConfigurationManager config;

    @Getter
    private final Logger logger;

    public CottonPlugin(String name, String version, List<String> authors) {
        this.name = name;
        this.version = version;
        this.authors = authors;

        this.config = new ConfigurationManager(this, new File("plugins"), new File(name));
        this.logger = Logger.getLogger(name);
    }

    public abstract void onEnable();
    public abstract void onDisable();

    public abstract void onLoad();

    public void saveConfig() {
        this.config.save();
    }

    public String name() { return name; }
    public String version() { return version; }
    public List<String> authors() { return authors; }

    public Optional<String> author() {
        if (authors.isEmpty()) return Optional.empty();
        return Optional.of(authors.getFirst());
    }
}