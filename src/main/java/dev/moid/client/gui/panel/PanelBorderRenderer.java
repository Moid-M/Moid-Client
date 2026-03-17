package dev.moid.client.gui.panel;

import dev.moid.client.gui.theme.Theme;
import net.minecraft.client.gui.DrawContext;

public class PanelBorderRenderer {

    public static void render(DrawContext ctx, Panel panel, int globalAlpha) {
        int x  = panel.x;
        int y  = panel.y;
        int pw = Theme.panelWidth();
        int ph = panel.getPanelHeight();

        ctx.fill(x, y, x + 1, y + ph,
            Theme.applyAlpha(0x88FFFFFF, globalAlpha));
        ctx.fill(x, y, x + pw, y + 1,
            Theme.applyAlpha(0x88FFFFFF, globalAlpha));
        ctx.fill(x + pw - 1, y, x + pw, y + ph,
            Theme.applyAlpha(0x33FFFFFF, globalAlpha));
        ctx.fill(x, y + ph - 1, x + pw, y + ph,
            Theme.applyAlpha(0x33FFFFFF, globalAlpha));
    }
}
