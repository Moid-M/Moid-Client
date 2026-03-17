package meteordevelopment.meteorclient.settings;

import java.util.ArrayList;
import java.util.List;

public class EntityTypeListSetting extends Setting<List<?>> {
    private EntityTypeListSetting() { super("", "", new ArrayList<>()); }

    public static class Builder extends AbstractBuilder<Builder, EntityTypeListSetting, List<?>> {
        public Builder() { instance = new EntityTypeListSetting(); }
        public Builder defaultValue(List<?> v) { instance.value = v; return this; }
        public EntityTypeListSetting build() { return (EntityTypeListSetting) instance; }
    }
}
