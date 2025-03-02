package net.furryplayplace.cottonframework.manager.plugin.transformers.rebuild;

/**
 * This class was made by Fox2Code for KibblePatcher
 *
 * Thank you a lots, this class is very useful
 */

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