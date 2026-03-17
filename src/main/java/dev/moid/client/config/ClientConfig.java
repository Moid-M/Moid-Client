package dev.moid.client.config;

public class ClientConfig {

    // --- Scale ---
    public static float guiScale           = 0.8f;

    // --- Accent & indicators ---
    public static int accentColor          = 0xFFD966FF;
    public static int enabledDotColor      = 0xFFFF69D9;
    public static int enabledTextColor     = 0xFFFF69D9;
    public static int disabledTextColor    = 0xFFAAAAAA;

    // --- Arraylist (HUD) ---
    public static boolean showArraylist    = true;
    public static int arraylistColor       = 0xFFD966FF;
    public static int arraylistX           = 5;
    public static int arraylistY           = 5;

    // --- GUI ---
    public static boolean blurBackground   = false;
    public static boolean fadeAnimation    = true;
    public static int panelWidth           = 130;
    public static int moduleHeight         = 20;

    // --- Search bar ---
    public static boolean searchAtBottom   = false;

    // --- Panel colors ---
    public static int panelBgColor         = 0xDD0d0d1a;
    public static int panelHeaderColor     = 0xFF1a0a2e;
    public static int panelHeaderHover     = 0xFF2a0a3e;
    public static int moduleBgColor        = 0xDD120d1f;
    public static int moduleHoverColor     = 0xDD1f1035;
    public static int separatorColor       = 0x33FFFFFF;

    public static int toARGB(int a, int r, int g, int b) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
