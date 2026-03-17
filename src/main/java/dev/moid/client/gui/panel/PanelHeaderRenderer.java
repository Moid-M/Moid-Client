package dev.moid.client.gui.panel;

import dev.moid.client.gui.theme.Theme;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class PanelHeaderRenderer {

    public static void render(DrawContext ctx, TextRenderer font,
                              Panel panel, int mouseX, int mouseY, int globalAlpha) {
        int x  = panel.x;
        int y  = panel.y;
        int pw = Theme.panelWidth();
        int hh = Panel.HEADER_HEIGHT;

        boolean hovered = mouseX >= x && mouseX <= x + pw
            && mouseY >= y && mouseY <= y + hh;

        // Header background
        ctx.fill(x, y, x + pw, y + hh,
            Theme.applyAlpha(hovered ? Theme.panelHeaderHover() : Theme.panelHeader(), globalAlpha));

        // Left accent line
        ctx.fill(x, y, x + 2, y + hh,
            Theme.applyAlpha(Theme.accent(), globalAlpha));

        // Category name
        ctx.drawTextWithShadow(font,
            (panel.collapsed ? "§7▶ " : "§7▼ ") + "§d" + panel.getDisplayName(),
            x + 6, y + 5, 0xFFFFFF);

        // Right menu hint
        ctx.drawTextWithShadow(font, "§8≡", x + pw - 12, y + 5, 0xFFFFFF);
    }
}
