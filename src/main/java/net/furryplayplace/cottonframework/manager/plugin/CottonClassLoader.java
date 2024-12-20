/*
---------------------------------------------------------------------------------
File Name : CottonClassLoader

Developer : vakea 
Email     : vakea@fluffici.eu
Real Name : Alex Guy Yann Le Roy

Date Created  : 19.12.2024
Last Modified : 19.12.2024

---------------------------------------------------------------------------------
*/

package net.furryplayplace.cottonframework.manager.plugin;

import net.furryplayplace.cottonframework.CottonFramework;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;


/**
 * {@code CottonClassLoader} provides a secure container for loading plugins from JAR files.
 */
public class CottonClassLoader extends URLClassLoader {

    /**
     * Creates a new {@code CottonClassLoader} for loading plugin classes securely.
     *
     * @param jarFile the JAR file containing the plugin.
     * @param parent  the parent {@code ClassLoader}.
     * @throws IOException if the JAR file cannot be read.
     */
    public CottonClassLoader(File jarFile, ClassLoader parent) throws IOException {
        super(new URL[] { jarFile.toURI().toURL() }, parent);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // Check if the class is already loaded
            Class<?> loadedClass = findLoadedClass(name);
            if (loadedClass == null) {
                try {
                    // Try loading with parent ClassLoader first
                    loadedClass = getParent().loadClass(name);
                } catch (ClassNotFoundException e) {
                    // Parent couldn't load it, try finding the class locally
                    loadedClass = findClass(name);
                }
            }
            if (resolve) {
                resolveClass(loadedClass);
            }
            return loadedClass;
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            String path = name.replace('.', '/').concat(".class");
            URL resource = getResource(path);
            if (resource == null) {
                throw new ClassNotFoundException("Class " + name + " not found in JAR");
            }

            byte[] classBytes = resource.openStream().readAllBytes();

            // Define the class
            return defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Error reading class " + name, e);
        }
    }


    /**
     * Defines a secure environment by restricting unauthorized access.
     *
     * @param jarFile the JAR file containing the plugin.
     * @return a new {@code CottonClassLoader} instance.
     * @throws IOException if an I/O error occurs.
     */
    public static CottonClassLoader createSecureClassLoader(File jarFile) throws IOException {
        return AccessController.doPrivileged((PrivilegedAction<CottonClassLoader>) () -> {
            try {
                return new CottonClassLoader(jarFile, CottonFramework.class.getClassLoader());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create secure class loader", e);
            }
        });
    }
}
