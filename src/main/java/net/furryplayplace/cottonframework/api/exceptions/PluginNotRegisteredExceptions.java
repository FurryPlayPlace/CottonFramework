/*
---------------------------------------------------------------------------------
File Name : PluginAlreadyRegisteredExceptions

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 19.12.2024
Last Modified : 19.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.api.exceptions;

public class PluginNotRegisteredExceptions extends Throwable {
    public PluginNotRegisteredExceptions(String message) {
        super(message);
    }
    public PluginNotRegisteredExceptions(String message, Throwable cause) {
        super(message, cause);
    }
    public PluginNotRegisteredExceptions(Throwable cause) {
        super(cause);
    }
    public PluginNotRegisteredExceptions() {
        super();
    }
}