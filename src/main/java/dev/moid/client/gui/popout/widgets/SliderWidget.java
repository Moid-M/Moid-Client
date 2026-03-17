package dev.moid.client.gui.popout.widgets;

import dev.moid.client.config.ClientConfig;
import dev.moid.client.gui.theme.Theme;
import dev.moid.client.module.setting.SliderSetting;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class SliderWidget {

    public static final int HEIGHT = 24;

    public static void render(DrawContext ctx, TextRenderer font,
                              SliderSetting ss, int index, int draggingIndex,
                              boolean isDragging, int px, int sy,
                              int width, int globalAlpha) {
        double val = ss.getValue();
        double min = ss.getMin();
        double max = ss.getMax();
        float pct  = (max > min)
            ? (float) Math.max(0, Math.min(1, (val - min) / (max - min)))
            : 0f;
        int barW   = width - 16;
        int barX   = px + 8;
        int barY   = sy + 16;

        String valStr = (ss.getStep() >= 1.0)
            ? String.valueOf((int) val)
            : String.format("%.2f", val);

        ctx.drawTextWithShadow(font,
            "§7" + ss.getName() + "  §d" + valStr,
            px + 6, sy + 4, 0xFFFFFF);

        // Track
        ctx.fill(barX, barY, barX + barW, barY + 3,
            Theme.applyAlpha(0x44FFFFFF, globalAlpha));
        // Fill
        ctx.fill(barX, barY, barX + (int)(barW * pct), barY + 3,
            Theme.applyAlpha(Theme.accent(), globalAlpha));
        // Handle
        int hx = barX + (int)(barW * pct) - 3;
        boolean active = isDragging && draggingIndex == index;
        ctx.fill(hx, barY - 3, hx + 6, barY + 6,
            Theme.applyAlpha(active ? Theme.accent() : 0xFFFFFFFF, globalAlpha));
    }

    // mouseX here must already be in SCALED space
    public static void updateValue(SliderSetting ss, int scaledMouseX, int px, int width) {
        int barW  = width - 16;
        int barX  = px + 8;
        float pct = Math.max(0f, Math.min(1f,
            (float)(scaledMouseX - barX) / barW));
        double raw     = ss.getMin() + pct * (ss.getMax() - ss.getMin());
        double stepped = (ss.getStep() > 0)
            ? Math.round(raw / ss.getStep()) * ss.getStep()
            : raw;
        ss.setValue(Math.max(ss.getMin(), Math.min(ss.getMax(), stepped)));
    }

    // Call this with raw screen mouseX — converts to scaled space internally
    public static void updateValueFromScreen(SliderSetting ss, int screenMouseX,
                                             int px, int width) {
        int scaledMouseX = (int)(screenMouseX / ClientConfig.guiScale);
        updateValue(ss, scaledMouseX, px, width);
    }
}
