package dev.moid.client.manager;

import java.io.File;

public class AddonNameResolver {

    public static String resolve(File jarFile) {
        String name = jarFile.getName().replaceAll("\\.jar$", "");
        name = name.replaceAll("[-_]?[vV]?\\d+(\\.\\d+)*$", "").trim();
        if (name.isEmpty()) name = jarFile.getName().replaceAll("\\.jar$", "");
        return name;
    }

    public static boolean shouldSkip(File jarFile) {
        String n = jarFile.getName();
        return n.startsWith("moid-client")
            || n.startsWith("fabric-api")
            || n.startsWith("fabric-loader");
    }
}
