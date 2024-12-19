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

package net.furryplayplace.cotton.api.plugin;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public abstract class CottonPlugin {

    private final String name;
    private final String version;
    private final List<String> authors;

    private final Logger logger;

    public CottonPlugin(String name, String version, List<String> authors) {
        this.name = name;
        this.version = version;
        this.authors = authors;

        this.logger = Logger.getLogger(name);
    }

    public abstract void onEnable();
    public abstract void onDisable();

    public String name() { return name; }
    public String version() { return version; }
    public List<String> authors() { return authors; }

    public Optional<String> author() {
        if (authors.isEmpty()) return Optional.empty();
        return Optional.of(authors.getFirst());
    }
}