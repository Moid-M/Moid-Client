package meteordevelopment.meteorclient.utils.misc;

public class Keybind {
    private int key = -1;
    private boolean isButton = false;

    public static Keybind fromKey(int key) {
        Keybind k = new Keybind(); k.key = key; return k;
    }
    public static Keybind fromButton(int button) {
        Keybind k = new Keybind(); k.key = button; k.isButton = true; return k;
    }
    public static Keybind none() { return new Keybind(); }
    public boolean isPressed() { return false; }
    public int getKey() { return key; }
}
