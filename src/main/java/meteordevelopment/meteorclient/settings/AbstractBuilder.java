package meteordevelopment.meteorclient.settings;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public abstract class AbstractBuilder<B extends AbstractBuilder<B, S, T>, S extends Setting<T>, T> {
    protected S instance;

    public Object name(String v)           { instance.name = v; return this; }
    public Object description(String v)    { instance.description = v; return this; }
    public Object defaultValue(Object v)   { instance.value = (T) v; return this; }
    public Object visible(IVisible v)      { return this; }
    public Object visible(BooleanSupplier v){ return this; }
    public Object onChanged(Consumer<T> v) { return this; }
    public B group(SettingGroup group)     { group.add(instance); return (B) this; }
}
