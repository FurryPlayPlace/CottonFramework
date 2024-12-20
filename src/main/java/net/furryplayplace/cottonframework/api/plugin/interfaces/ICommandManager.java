/*
---------------------------------------------------------------------------------
File Name : ICommandManager

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 19.12.2024
Last Modified : 19.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.api.plugin.interfaces;

import net.furryplayplace.cottonframework.api.command.AbstractCommand;
import net.furryplayplace.cottonframework.api.plugin.CottonPlugin;

public interface ICommandManager {
    void registerCommand(CottonPlugin plugin, AbstractCommand command);
    void unregisterCommand(CottonPlugin plugin, AbstractCommand command);
    void unregisterAllCommands();
}
