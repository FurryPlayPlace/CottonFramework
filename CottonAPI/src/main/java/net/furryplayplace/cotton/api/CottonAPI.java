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

package net.furryplayplace.cotton.api;

import net.furryplayplace.cotton.api.plugin.interfaces.IPluginManager;

public interface CottonAPI {
    CottonAPI get();

    IPluginManager pluginManager();
}
