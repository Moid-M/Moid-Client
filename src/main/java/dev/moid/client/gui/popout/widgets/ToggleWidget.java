package dev.moid.client.gui.popout.widgets;

import dev.moid.client.gui.theme.Theme;
import dev.moid.client.module.setting.BooleanSetting;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class ToggleWidget {

    public static final int HEIGHT = 24;

    public static void render(DrawContext ctx, TextRenderer font,
                              BooleanSetting bs, int px, int sy,
                              int width, int globalAlpha) {
        ctx.drawTextWithShadow(font, "§7" + bs.getName(), px + 6, sy + 8, 0xFFFFFF);

        boolean val   = bs.getValue();
        int pillX     = px + width - 40;
        int pillY     = sy + 7;

        // Pill background
        ctx.fill(pillX, pillY, pillX + 32, pillY + 10,
            Theme.applyAlpha(val ? Theme.enabledBar() : 0xFF555566, globalAlpha));
        // Knob
        int knobX = val ? pillX + 21 : pillX + 2;
        ctx.fill(knobX, pillY + 1, knobX + 9, pillY + 9,
            Theme.applyAlpha(0xFFFFFFFF, globalAlpha));
    }
}
