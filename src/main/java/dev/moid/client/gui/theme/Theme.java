package dev.moid.client.gui.theme;

import dev.moid.client.config.ClientConfig;

/**
 * All visual values read from ClientConfig.
 * To add a new theme value: add to ClientConfig, reference here.
 * Nothing else needs changing.
 */
public class Theme {

    // --- Accent ---
    public static int accent()          { return ClientConfig.accentColor; }
    public static int enabledBar()      { return ClientConfig.enabledDotColor; }
    public static int enabledText()     { return ClientConfig.enabledTextColor; }
    public static int disabledText()    { return ClientConfig.disabledTextColor; }

    // --- Panel ---
    public static int panelBg()         { return ClientConfig.panelBgColor; }
    public static int panelHeader()     { return ClientConfig.panelHeaderColor; }
    public static int panelHeaderHover(){ return ClientConfig.panelHeaderHover; }
    public static int panelWidth()      { return ClientConfig.panelWidth; }
    public static int moduleHeight()    { return ClientConfig.moduleHeight; }

    // --- Module rows ---
    public static int moduleBg()        { return ClientConfig.moduleBgColor; }
    public static int moduleHover()     { return ClientConfig.moduleHoverColor; }
    public static int separator()       { return ClientConfig.separatorColor; }

    // --- HUD ---
    public static int arraylistColor()  { return ClientConfig.arraylistColor; }
    public static int arraylistX()      { return ClientConfig.arraylistX; }
    public static int arraylistY()      { return ClientConfig.arraylistY; }
    public static boolean showHud()     { return ClientConfig.showArraylist; }

    // --- Misc ---
    public static boolean blur()        { return ClientConfig.blurBackground; }
    public static boolean fade()        { return ClientConfig.fadeAnimation; }
    public static boolean searchBottom(){ return ClientConfig.searchAtBottom; }

    // Utility — apply global fade alpha to any color
    public static int applyAlpha(int color, int globalAlpha) {
        int a = (color >> 24) & 0xFF;
        a = (int)(a * (globalAlpha / 255f));
        return (color & 0x00FFFFFF) | (a << 24);
    }
}
