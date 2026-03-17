package meteordevelopment.meteorclient.settings;

import java.util.ArrayList;
import java.util.List;

public class BlockListSetting extends Setting<List<?>> {
    private BlockListSetting() { super("", "", new ArrayList<>()); }

    public static class Builder extends AbstractBuilder<Builder, BlockListSetting, List<?>> {
        public Builder() { instance = new BlockListSetting(); }
        public Builder defaultValue(List<?> v) { instance.value = v; return this; }
        public BlockListSetting build() { return (BlockListSetting) instance; }
    }
}
