/*
---------------------------------------------------------------------------------
File Name : PluginContainer

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 19.12.2024
Last Modified : 19.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cotton.api.plugin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PluginContainer<T> {
    private final T plugin;
    private PluginState state;
    private CottonPluginInfo pluginInfo;

    public PluginContainer(T plugin, PluginState state) {
        this.plugin = plugin;
        this.state = state;
    }

    public PluginContainer(T plugin, CottonPluginInfo pluginInfo) {
        this(plugin, PluginState.ENABLED);
        this.pluginInfo = pluginInfo;
    }
}