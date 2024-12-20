/*
---------------------------------------------------------------------------------
File Name : ClassData

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 20.12.2024
Last Modified : 20.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.manager.plugin.transformers.rebuild;

public interface ClassData {
    String getName();
    ClassData getSuperclass();
    ClassData[] getInterfaces();
    boolean isAssignableFrom(ClassData clData);
    boolean isInterface();
    boolean isFinal();
    boolean isPublic();
    boolean isCustom();
}