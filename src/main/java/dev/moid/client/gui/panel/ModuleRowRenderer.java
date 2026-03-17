package dev.moid.client.gui.panel;

import dev.moid.client.gui.theme.Theme;
import dev.moid.client.module.Module;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class ModuleRowRenderer {

    public static void renderRows(DrawContext ctx, TextRenderer font,
                                  Panel panel, int mouseX, int mouseY, int globalAlpha) {
        List<Module> modules = panel.getModules();
        int x    = panel.x;
        int mh   = Theme.moduleHeight();
        int pw   = Theme.panelWidth();
        int my   = panel.y + Panel.HEADER_HEIGHT + 2;
        int maxY = panel.y + panel.getPanelHeight() - 2;

        for (int i = panel.scrollOffset;
             i < modules.size() && (i - panel.scrollOffset) < Panel.MAX_VISIBLE; i++) {
            if (my + mh > maxY) break;
            Module module = modules.get(i);
            module.tickFlash();

            boolean hovered = mouseX >= x && mouseX <= x + pw
                && mouseY >= my && mouseY <= my + mh;

            // Row background
            ctx.fill(x, my, x + pw, my + mh,
                Theme.applyAlpha(hovered ? Theme.moduleHover() : Theme.moduleBg(), globalAlpha));

            // Toggle flash overlay
            if (module.toggleFlash > 0f) {
                int flashAlpha = (int)(module.toggleFlash * 60);
                int flashColor = module.isEnabled()
                    ? (flashAlpha << 24) | (Theme.enabledBar() & 0x00FFFFFF)
                    : (flashAlpha << 24) | 0x00FFFFFF;
                ctx.fill(x, my, x + pw, my + mh, flashColor);
            }

            // Enabled indicator bar
            if (module.isEnabled()) {
                ctx.fill(x, my, x + 2, my + mh,
                    Theme.applyAlpha(Theme.enabledBar(), globalAlpha));
            }

            // Separator
            ctx.fill(x + 4, my + mh - 1, x + pw - 4, my + mh,
                Theme.applyAlpha(Theme.separator(), globalAlpha));

            // Module name
            int nameColor = module.isEnabled()
                ? Theme.enabledText() : Theme.disabledText();
            ctx.drawTextWithShadow(font, module.getName(), x + 8, my + 6,
                Theme.applyAlpha(nameColor, globalAlpha));

            // Enabled dot
            if (module.isEnabled()) {
                ctx.drawTextWithShadow(font, "●", x + pw - 12, my + 6,
                    Theme.applyAlpha(Theme.enabledBar(), globalAlpha));
            }

            my += mh;
        }

        // Scroll hint
        if (modules.size() > Panel.MAX_VISIBLE) {
            ctx.drawTextWithShadow(font, "§8▲▼",
                x + pw - 16, panel.y + Panel.HEADER_HEIGHT + 4, 0xFFFFFF);
        }
    }
}
