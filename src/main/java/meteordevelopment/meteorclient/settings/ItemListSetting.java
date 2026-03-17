package meteordevelopment.meteorclient.settings;

import java.util.ArrayList;
import java.util.List;

public class ItemListSetting extends Setting<List<?>> {
    private ItemListSetting() { super("", "", new ArrayList<>()); }

    public static class Builder extends AbstractBuilder<Builder, ItemListSetting, List<?>> {
        public Builder() { instance = new ItemListSetting(); }
        public Builder defaultValue(List<?> v) { instance.value = v; return this; }
        public ItemListSetting build() { return (ItemListSetting) instance; }
    }
}
