package meteordevelopment.meteorclient.settings;

import java.util.ArrayList;
import java.util.List;

public class Settings {
    private final List<SettingGroup> groups = new ArrayList<>();

    public SettingGroup getDefaultGroup() {
        if (groups.isEmpty()) groups.add(new SettingGroup("General"));
        return groups.get(0);
    }

    public SettingGroup createGroup(String name) {
        SettingGroup group = new SettingGroup(name);
        groups.add(group);
        return group;
    }

    public List<SettingGroup> getGroups() { return groups; }
}
