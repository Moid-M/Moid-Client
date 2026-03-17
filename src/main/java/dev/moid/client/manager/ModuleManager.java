package dev.moid.client.manager;

import dev.moid.client.MoidClient;
import dev.moid.client.module.Category;
import dev.moid.client.module.Module;
import dev.moid.client.module.MeteorModuleWrapper;
import dev.moid.client.module.modules.FullBright;
import dev.moid.client.module.modules.client.*;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<>();

    public void init() {
        register(new AccentColorSetting());
        register(new IndicatorColorSetting());
        register(new ArraylistSetting());
        register(new GuiSetting());
        register(new PanelColorSetting());
        register(new FullBright());
    }

    public void register(Module module) { modules.add(module); }

    public void registerFromAddon(Class<?> clazz, Category addonCategory) {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            MeteorModuleWrapper wrapper = new MeteorModuleWrapper(instance, addonCategory);
            modules.add(wrapper);
            MoidClient.LOGGER.info("Registered: " + wrapper.getName() + " -> " + addonCategory.name());
        } catch (Throwable e) {
            // Changed to Throwable and full stack trace so we see exactly what fails
            MoidClient.LOGGER.error("Failed to register: " + clazz.getName());
            MoidClient.LOGGER.error("Reason: " + e.getMessage());
            if (e.getCause() != null) {
                MoidClient.LOGGER.error("Cause: " + e.getCause().getMessage());
                for (StackTraceElement el : e.getCause().getStackTrace()) {
                    MoidClient.LOGGER.error("  at " + el.toString());
                    // Only print first 5 lines
                    if (e.getCause().getStackTrace()[4] == el) break;
                }
            }
        }
    }

    public void tickAll() {
        modules.stream().filter(Module::isEnabled).forEach(Module::onTick);
    }

    public List<Module> getModules() { return modules; }

    public Module getModuleByName(String name) {
        return modules.stream()
            .filter(m -> m.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
}
