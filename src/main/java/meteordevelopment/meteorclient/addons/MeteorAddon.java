package meteordevelopment.meteorclient.addons;

public abstract class MeteorAddon {
    public String name = "";
    public String[] authors = {};
    public abstract void onInitialize();
    public void onRegisterCategories() {}
    public String getPackage() { return ""; }
}
