package dev.moid.client.gui.popout;

import dev.moid.client.gui.popout.widgets.*;
import dev.moid.client.gui.theme.Theme;
import dev.moid.client.module.setting.*;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class SettingRowRenderer {

    public static final int ROW_HEIGHT = 24;

    public static void renderRow(DrawContext ctx, TextRenderer font,
                                 Setting<?> setting, int index,
                                 int px, int sy, int width,
                                 boolean expanded, int draggingIndex,
                                 boolean isDragging, int globalAlpha) {
        // Row background
        ctx.fill(px, sy, px + width, sy + ROW_HEIGHT,
            Theme.applyAlpha(expanded ? Theme.moduleHover() : Theme.moduleBg(), globalAlpha));
        // Separator
        ctx.fill(px + 4, sy + ROW_HEIGHT - 1, px + width - 4, sy + ROW_HEIGHT,
            Theme.applyAlpha(Theme.separator(), globalAlpha));

        if (setting instanceof BooleanSetting bs) {
            ToggleWidget.render(ctx, font, bs, px, sy, width, globalAlpha);

        } else if (setting instanceof SliderSetting ss) {
            SliderWidget.render(ctx, font, ss, index, draggingIndex,
                isDragging, px, sy, width, globalAlpha);

        } else if (setting instanceof BridgedEnumSetting es) {
            EnumWidget.render(ctx, font, es, px, sy, width, globalAlpha);

        } else if (setting instanceof BridgedStringSetting bss) {
            renderStringRow(ctx, font, bss, px, sy, width, expanded, globalAlpha);

        } else {
            // Generic fallback
            String valStr = setting.getValue() != null ? setting.getValue().toString() : "??";
            if (valStr.length() > 24) valStr = valStr.substring(0, 24) + "...";
            ctx.drawTextWithShadow(font,
                "§7" + setting.getName() + ": §f" + valStr,
                px + 6, sy + 8, 0xFFFFFF);
        }
    }

    private static void renderStringRow(DrawContext ctx, TextRenderer font,
                                        BridgedStringSetting bss,
                                        int px, int sy, int width,
                                        boolean expanded, int globalAlpha) {
        String valStr = bss.getValue() != null ? bss.getValue().toString() : "";
        boolean isColor = valStr.contains("SettingColor")
            || bss.getName().toLowerCase().contains("color");

        if (isColor) {
            ctx.drawTextWithShadow(font, "§7" + bss.getName(), px + 6, sy + 8, 0xFFFFFF);
            // Color swatch
            int swatchX = px + width - 36;
            int swatchY = sy + 4;
            int color   = parseSettingColor(bss.getValue());
            ctx.fill(swatchX, swatchY, swatchX + 28, swatchY + ROW_HEIGHT - 8, color);
            ctx.fill(swatchX, swatchY, swatchX + 28, swatchY + 1,
                Theme.applyAlpha(0xFFFFFFFF, globalAlpha));
            ctx.fill(swatchX, swatchY, swatchX + 1, swatchY + ROW_HEIGHT - 8,
                Theme.applyAlpha(0xFFFFFFFF, globalAlpha));
            ctx.drawTextWithShadow(font,
                expanded ? "§d▲" : "§7▼",
                swatchX - 12, sy + 8, 0xFFFFFF);
        } else {
            String display = valStr.length() > 22 ? valStr.substring(0, 22) + "..." : valStr;
            ctx.drawTextWithShadow(font,
                "§7" + bss.getName() + ": §f" + display,
                px + 6, sy + 8, 0xFFFFFF);
        }
    }

    private static int parseSettingColor(Object val) {
        if (val == null) return 0xFFFF69D9;
        try {
            Object r = val.getClass().getField("r").get(val);
            Object g = val.getClass().getField("g").get(val);
            Object b = val.getClass().getField("b").get(val);
            Object a = val.getClass().getField("a").get(val);
            return ((int)(Integer)a << 24) | ((int)(Integer)r << 16)
                | ((int)(Integer)g << 8) | (int)(Integer)b;
        } catch (Exception ignored) {}
        return 0xFFFF69D9;
    }
}
