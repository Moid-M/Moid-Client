package meteordevelopment.meteorclient.settings;

import java.util.ArrayList;
import java.util.List;

public class SettingGroup {
    public final String name;
    private final List<Setting<?>> settings = new ArrayList<>();

    public SettingGroup(String name) {
        this.name = name;
    }

    public <T> Setting<T> add(Setting<T> setting) {
        settings.add(setting);
        return setting;
    }

    public List<Setting<?>> getSettings() { return settings; }
}
