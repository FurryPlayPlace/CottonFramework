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

public class PluginFileNotFoundException extends Throwable {
    public PluginFileNotFoundException(String message) {
        super(message);
    }
    public PluginFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public PluginFileNotFoundException(Throwable cause) {
        super(cause);
    }
    public PluginFileNotFoundException() {
        super();
    }
}