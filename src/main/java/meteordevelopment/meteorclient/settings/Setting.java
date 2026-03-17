package meteordevelopment.meteorclient.settings;

public abstract class Setting<T> {
    public String name;
    public String description;
    public T value;
    protected final T defaultValue;

    public Setting(String name, String description, T defaultValue) {
        this.name = name;
        this.description = description;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
    }

    public T get() { return value; }
    public void set(T value) { this.value = value; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public T getValue() { return value; }
}
