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

public class PluginIncompatibleApiVersionException extends Throwable {
    public PluginIncompatibleApiVersionException(String message) {
        super(message);
    }
    public PluginIncompatibleApiVersionException(String message, Throwable cause) {
        super(message, cause);
    }
    public PluginIncompatibleApiVersionException(Throwable cause) {
        super(cause);
    }
    public PluginIncompatibleApiVersionException() {
        super();
    }
}