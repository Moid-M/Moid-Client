package meteordevelopment.meteorclient.systems.modules;

import java.util.ArrayList;
import java.util.List;

public class Modules {
    private static Modules INSTANCE = new Modules();
    private final List<Module> modules = new ArrayList<>();

    public static Modules get() { return INSTANCE; }
    public void add(Module module) { modules.add(module); }
    public <T extends Module> T get(Class<T> clazz) { return null; }
    public boolean isActive(Class<? extends Module> clazz) { return false; }
}
