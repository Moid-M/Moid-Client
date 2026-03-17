package meteordevelopment.meteorclient.systems.modules;

public class Category {
    public final String name;

    public Category(String name) {
        this.name = name;
    }

    // Common Meteor categories
    public static final Category COMBAT = new Category("Combat");
    public static final Category MOVEMENT = new Category("Movement");
    public static final Category RENDER = new Category("Render");
    public static final Category WORLD = new Category("World");
    public static final Category MISC = new Category("Misc");
    public static final Category PLAYER = new Category("Player");
}
