package dev.moid.client.manager;

import dev.moid.client.MoidClient;
import dev.moid.client.module.Category;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddonLoader {

    private final List<File> scanFolders  = new ArrayList<>();
    private final List<String> loaded     = new ArrayList<>();

    public AddonLoader() {
        File gameDir      = FabricLoader.getInstance().getGameDir().toFile();
        File addonsFolder = new File(gameDir, "addons");
        File modsFolder   = new File(gameDir, "mods");
        if (!addonsFolder.exists()) addonsFolder.mkdirs();
        scanFolders.add(addonsFolder);
        scanFolders.add(modsFolder);
    }

    public void loadAll() {
        for (File folder : scanFolders) {
            File[] jars = folder.listFiles(f -> f.getName().endsWith(".jar"));
            if (jars == null) continue;
            for (File jar : jars) {
                if (AddonNameResolver.shouldSkip(jar)) continue;
                loadAddon(jar);
            }
        }
    }

    private void loadAddon(File jarFile) {
        String addonName     = AddonNameResolver.resolve(jarFile);
        Category addonCat    = Category.getOrCreate(addonName);

        MoidClient.LOGGER.info("Scanning for modules in: " + jarFile.getName());

        List<AddonClassScanner.ScanResult> results =
            AddonClassScanner.scan(jarFile, addonCat);

        for (AddonClassScanner.ScanResult result : results) {
            MoidClient.moduleManager.registerFromAddon(result.clazz, result.category);
        }

        loaded.add(addonName);
        MoidClient.LOGGER.info("Loaded addon: " + addonName
            + " (" + results.size() + " modules)");
    }

    public List<String> getLoadedAddons() { return loaded; }
}
