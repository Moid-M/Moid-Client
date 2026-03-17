package dev.moid.client.module;

import java.util.ArrayList;
import java.util.List;

public class Category {

    // Built-in categories
    public static final Category CLIENT   = new Category("CLIENT");
    public static final Category COMBAT   = new Category("COMBAT");
    public static final Category MOVEMENT = new Category("MOVEMENT");
    public static final Category RENDER   = new Category("RENDER");
    public static final Category WORLD    = new Category("WORLD");
    public static final Category MISC     = new Category("MISC");

    private static final List<Category> values = new ArrayList<>();

    static {
        values.add(CLIENT);
        values.add(COMBAT);
        values.add(MOVEMENT);
        values.add(RENDER);
        values.add(WORLD);
        values.add(MISC);
    }

    private final String name;

    public Category(String name) {
        this.name = name;
    }

    public String name() { return name; }

    // Dynamically create or retrieve a category by name
    public static Category getOrCreate(String name) {
        return values.stream()
            .filter(c -> c.name.equalsIgnoreCase(name))
            .findFirst()
            .orElseGet(() -> {
                Category newCat = new Category(name);
                values.add(newCat);
                return newCat;
            });
    }

    public static List<Category> values() { return values; }
}
