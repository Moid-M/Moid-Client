package dev.moid.client.gui.popout.widgets;

import dev.moid.client.gui.theme.Theme;
import dev.moid.client.module.setting.BridgedStringSetting;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class ColorPickerWidget {

    public static final int HEIGHT     = 84;
    public static final int ROW_HEIGHT = 24;
    public static final int BAR_H      = 12;
    public static final int BAR_GAP    = 14;

    // Stored HSV state per widget — simple static for now
    private static float hue        = 0.8f;
    private static float saturation = 1.0f;
    private static float brightness = 1.0f;
    private static int draggingBar  = -1; // 0=hue, 1=sat, 2=val

    public static void render(DrawContext ctx, TextRenderer font,
                              BridgedStringSetting setting,
                              int px, int sy, int width, int globalAlpha) {
        int pickerW = width - 16;
        int pickerX = px + 8;

        // Hue bar
        for (int i = 0; i < pickerW; i++) {
            float h = (float) i / pickerW;
            ctx.fill(pickerX + i, sy, pickerX + i + 1, sy + BAR_H,
                Theme.applyAlpha(hsvToRgb(h, 1f, 1f) | 0xFF000000, globalAlpha));
        }
        // Hue cursor
        int hueX = pickerX + (int)(hue * pickerW) - 1;
        ctx.fill(hueX, sy - 2, hueX + 3, sy + BAR_H + 2,
            Theme.applyAlpha(0xFFFFFFFF, globalAlpha));
        ctx.drawTextWithShadow(font, "§8H", pickerX, sy + BAR_H + 2, 0xFFFFFF);

        int satY = sy + BAR_GAP + BAR_H;
        // Saturation bar
        for (int i = 0; i < pickerW; i++) {
            float s = (float) i / pickerW;
            ctx.fill(pickerX + i, satY, pickerX + i + 1, satY + BAR_H,
                Theme.applyAlpha(hsvToRgb(hue, s, brightness) | 0xFF000000, globalAlpha));
        }
        int satX = pickerX + (int)(saturation * pickerW) - 1;
        ctx.fill(satX, satY - 2, satX + 3, satY + BAR_H + 2,
            Theme.applyAlpha(0xFFFFFFFF, globalAlpha));
        ctx.drawTextWithShadow(font, "§8S", pickerX, satY + BAR_H + 2, 0xFFFFFF);

        int valY = satY + BAR_GAP + BAR_H;
        // Value/brightness bar
        for (int i = 0; i < pickerW; i++) {
            float v = (float) i / pickerW;
            ctx.fill(pickerX + i, valY, pickerX + i + 1, valY + BAR_H,
                Theme.applyAlpha(hsvToRgb(hue, saturation, v) | 0xFF000000, globalAlpha));
        }
        int valX = pickerX + (int)(brightness * pickerW) - 1;
        ctx.fill(valX, valY - 2, valX + 3, valY + BAR_H + 2,
            Theme.applyAlpha(0xFFFFFFFF, globalAlpha));
        ctx.drawTextWithShadow(font, "§8V", pickerX, valY + BAR_H + 2, 0xFFFFFF);

        // Preview swatch
        int previewColor = hsvToRgb(hue, saturation, brightness) | 0xFF000000;
        ctx.fill(px + width - 20, sy, px + width - 4, sy + HEIGHT - 4,
            Theme.applyAlpha(previewColor, globalAlpha));
    }

    public static boolean mouseClicked(int mouseX, int mouseY,
                                       int px, int sy, int width) {
        int pickerW = width - 16;
        int pickerX = px + 8;

        if (mouseX >= pickerX && mouseX <= pickerX + pickerW) {
            if (mouseY >= sy && mouseY <= sy + BAR_H) {
                draggingBar = 0;
                hue = (float)(mouseX - pickerX) / pickerW;
                return true;
            }
            int satY = sy + BAR_GAP + BAR_H;
            if (mouseY >= satY && mouseY <= satY + BAR_H) {
                draggingBar = 1;
                saturation = (float)(mouseX - pickerX) / pickerW;
                return true;
            }
            int valY = satY + BAR_GAP + BAR_H;
            if (mouseY >= valY && mouseY <= valY + BAR_H) {
                draggingBar = 2;
                brightness = (float)(mouseX - pickerX) / pickerW;
                return true;
            }
        }
        return false;
    }

    public static void mouseDragged(int mouseX, int px, int width) {
        if (draggingBar < 0) return;
        int pickerW = width - 16;
        int pickerX = px + 8;
        float pct = Math.max(0f, Math.min(1f, (float)(mouseX - pickerX) / pickerW));
        switch (draggingBar) {
            case 0 -> hue        = pct;
            case 1 -> saturation = pct;
            case 2 -> brightness = pct;
        }
    }

    public static void mouseReleased() { draggingBar = -1; }

    public static int getCurrentColor() {
        return hsvToRgb(hue, saturation, brightness) | 0xFF000000;
    }

    public static int hsvToRgb(float h, float s, float v) {
        int i = (int)(h * 6);
        float f = h * 6 - i;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);
        float r, g, b;
        switch (i % 6) {
            case 0  -> { r = v; g = t; b = p; }
            case 1  -> { r = q; g = v; b = p; }
            case 2  -> { r = p; g = v; b = t; }
            case 3  -> { r = p; g = q; b = v; }
            case 4  -> { r = t; g = p; b = v; }
            default -> { r = v; g = p; b = q; }
        }
        return ((int)(r * 255) << 16) | ((int)(g * 255) << 8) | (int)(b * 255);
    }
}
