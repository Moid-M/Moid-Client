package dev.moid.client.manager;

import dev.moid.client.MoidClient;
import dev.moid.client.module.Category;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AddonClassScanner {

    public static class ScanResult {
        public final Class<?> clazz;
        public final Category category;

        public ScanResult(Class<?> clazz, Category category) {
            this.clazz    = clazz;
            this.category = category;
        }
    }

    public static List<ScanResult> scan(File jarFile, Category addonCategory) {
        List<ScanResult> results = new ArrayList<>();
        try {
            URLClassLoader classLoader = new URLClassLoader(
                new URL[]{jarFile.toURI().toURL()},
                Thread.currentThread().getContextClassLoader()
            );
            JarFile jar = new JarFile(jarFile);
            jar.stream()
                .filter(e -> e.getName().endsWith(".class"))
                .forEach(e -> {
                    Class<?> found = tryLoad(e, classLoader);
                    if (found != null) {
                        MoidClient.LOGGER.info("  Found meteor module: "
                            + found.getSimpleName());
                        results.add(new ScanResult(found, addonCategory));
                    }
                });
            jar.close();
        } catch (Exception e) {
            MoidClient.LOGGER.error("Failed to scan jar: "
                + jarFile.getName() + " - " + e.getMessage());
        }
        return results;
    }

    private static Class<?> tryLoad(JarEntry entry, URLClassLoader loader) {
        String className = entry.getName()
            .replace("/", ".")
            .replace(".class", "");
        try {
            Class<?> clazz = loader.loadClass(className);
            if (isMeteorModule(clazz)) return clazz;
        } catch (Throwable ignored) {}
        return null;
    }

    private static boolean isMeteorModule(Class<?> clazz) {
        try {
            Class<?> c = clazz.getSuperclass();
            while (c != null) {
                if (c.getName().equals(
                    "meteordevelopment.meteorclient.systems.modules.Module"))
                    return true;
                c = c.getSuperclass();
            }
        } catch (Throwable ignored) {}
        return false;
    }
}
