package dev.moid.client.gui.panel;

import dev.moid.client.gui.theme.Theme;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class PanelRenderer {

    public static void renderPanel(DrawContext ctx, TextRenderer font,
                                   Panel panel, int mouseX, int mouseY, int globalAlpha) {
        panel.tickAnimation();

        int x  = panel.x;
        int y  = panel.y;
        int pw = Theme.panelWidth();
        int ph = panel.getPanelHeight();

        // Panel background
        ctx.fill(x, y, x + pw, y + ph,
            Theme.applyAlpha(Theme.panelBg(), globalAlpha));

        // Header
        PanelHeaderRenderer.render(ctx, font, panel, mouseX, mouseY, globalAlpha);

        // Module rows
        if (panel.animHeight > 2) {
            ModuleRowRenderer.renderRows(ctx, font, panel, mouseX, mouseY, globalAlpha);
        }

        // Border
        PanelBorderRenderer.render(ctx, panel, globalAlpha);
    }
}
