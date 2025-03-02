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

public class PluginFileException extends Throwable {
    public PluginFileException(String message) {
        super(message);
    }
    public PluginFileException(String message, Throwable cause) {
        super(message, cause);
    }
    public PluginFileException(Throwable cause) {
        super(cause);
    }
    public PluginFileException() {
        super();
    }
}