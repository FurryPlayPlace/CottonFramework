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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

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
        validateJar(jarFile);
    }

    /**
     * Validates the contents of the JAR file to ensure security.
     *
     * @param jarFile the JAR file to validate.
     * @throws IOException if validation fails or the JAR file cannot be read.
     */
    private void validateJar(File jarFile) throws IOException {
        try (JarFile jar = new JarFile(jarFile)) {
            Manifest manifest = jar.getManifest();
            if (manifest == null) {
                throw new SecurityException("Missing PLUGIN.manifest in: " + jarFile.getName());
            }

            String mainClass = manifest.getMainAttributes().getValue("Main-Class");
            if (mainClass == null || mainClass.isBlank()) {
                throw new SecurityException("Main-Class not specified in PLUGIN.manifest: " + jarFile.getName());
            }

            for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements(); ) {
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory()) continue;

                String name = entry.getName();
                if (name.contains("..") || name.startsWith("/") || name.startsWith("\\")) {
                    throw new SecurityException("Invalid JAR entry detected: " + name);
                }
            }
        }
    }

    /**
     * Loads a class with security restrictions applied.
     *
     * @param name the name of the class to load.
     * @param resolve whether to resolve the class after loading.
     * @return the loaded {@code Class}.
     * @throws ClassNotFoundException if the class cannot be found or loaded.
     */
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            if (name.startsWith("net.furryplayplace")) {
                throw new SecurityException("Access to package net.furryplayplace is forbidden: " + name);
            }

            Class<?> loadedClass = findLoadedClass(name);
            if (loadedClass == null) {
                try {
                    loadedClass = getParent().loadClass(name);
                } catch (ClassNotFoundException e) {
                    throw new ClassNotFoundException("Class not found: " + name, e);
                }
            }
            if (resolve) {
                resolveClass(loadedClass);
            }
            return loadedClass;
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
                return new CottonClassLoader(jarFile, ClassLoader.getSystemClassLoader());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create secure class loader", e);
            }
        });
    }
}
