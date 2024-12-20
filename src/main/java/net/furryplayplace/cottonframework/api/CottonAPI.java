/*
---------------------------------------------------------------------------------
File Name : CottonAPI

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 19.12.2024
Last Modified : 19.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.api;

import net.furryplayplace.cottonframework.api.plugin.interfaces.IPluginManager;

public abstract class CottonAPI {
    private static CottonAPI instance;

    public CottonAPI() {
        instance = this;
    }

    public abstract IPluginManager pluginManager();

    public static CottonAPI get() {
        return instance;
    }
}
