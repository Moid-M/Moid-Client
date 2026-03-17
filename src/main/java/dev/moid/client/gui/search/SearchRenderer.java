package dev.moid.client.gui.search;

import dev.moid.client.gui.panel.Panel;
import dev.moid.client.gui.theme.Theme;
import dev.moid.client.module.Module;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class SearchRenderer {

    public static void renderToggleBtn(DrawContext ctx, TextRenderer font,
                                       SearchBar bar, int screenWidth, int screenHeight,
                                       boolean bottom, int mouseX, int mouseY, int globalAlpha) {
        int bx = bar.getBtnX(screenWidth);
        int by = bar.getBtnY(screenHeight, bottom);
        boolean hovered = bar.isBtnHovered(mouseX, mouseY, screenWidth, screenHeight, bottom);

        // Button background
        ctx.fill(bx, by, bx + SearchBar.BTN_W, by + SearchBar.BTN_H,
            Theme.applyAlpha(hovered ? Theme.panelHeaderHover() : Theme.panelHeader(), globalAlpha));
        // Top accent line
        ctx.fill(bx, by, bx + SearchBar.BTN_W, by + 1,
            Theme.applyAlpha(Theme.accent(), globalAlpha));
        // Left border
        ctx.fill(bx, by, bx + 1, by + SearchBar.BTN_H,
            Theme.applyAlpha(Theme.accent(), globalAlpha));
        // Right border
        ctx.fill(bx + SearchBar.BTN_W - 1, by, bx + SearchBar.BTN_W, by + SearchBar.BTN_H,
            Theme.applyAlpha(0x44FFFFFF, globalAlpha));
        // Bottom border
        ctx.fill(bx, by + SearchBar.BTN_H - 1, bx + SearchBar.BTN_W, by + SearchBar.BTN_H,
            Theme.applyAlpha(0x44FFFFFF, globalAlpha));

        // Icon
        ctx.drawCenteredTextWithShadow(font,
            bar.isVisible() ? "§d✕" : "§7⌕",
            bx + SearchBar.BTN_W / 2, by + 4, 0xFFFFFF);
    }

    public static void renderDropdown(DrawContext ctx, TextRenderer font,
                                      SearchBar bar, List<Panel> panels,
                                      int screenWidth, int screenHeight,
                                      boolean bottom, int mouseX, int mouseY, int globalAlpha) {
        List<Module> results = bar.getResults();
        if (results.isEmpty()) return;

        int dropX  = bar.getBarX(screenWidth);
        int dropY  = bar.getDropdownY(screenHeight, bottom, results);
        int dropW  = SearchBar.WIDTH;
        int totalH = results.size() * SearchBar.RESULT_HEIGHT + 4;

        // Background
        ctx.fill(dropX, dropY, dropX + dropW, dropY + totalH,
            Theme.applyAlpha(0xEE0d0d1a, globalAlpha));
        ctx.fill(dropX, dropY, dropX + dropW, dropY + 1,
            Theme.applyAlpha(Theme.accent(), globalAlpha));
        ctx.fill(dropX, dropY, dropX + 1, dropY + totalH,
            Theme.applyAlpha(Theme.accent(), globalAlpha));
        ctx.fill(dropX + dropW - 1, dropY, dropX + dropW, dropY + totalH,
            Theme.applyAlpha(0x44FFFFFF, globalAlpha));
        ctx.fill(dropX, dropY + totalH - 1, dropX + dropW, dropY + totalH,
            Theme.applyAlpha(0x44FFFFFF, globalAlpha));

        int ry = dropY + 2;
        for (Module module : results) {
            boolean hovered = mouseX >= dropX && mouseX <= dropX + dropW
                && mouseY >= ry && mouseY <= ry + SearchBar.RESULT_HEIGHT;

            ctx.fill(dropX, ry, dropX + dropW, ry + SearchBar.RESULT_HEIGHT,
                Theme.applyAlpha(hovered ? Theme.moduleHover() : Theme.moduleBg(), globalAlpha));

            if (module.isEnabled()) {
                ctx.fill(dropX, ry, dropX + 2, ry + SearchBar.RESULT_HEIGHT,
                    Theme.applyAlpha(Theme.enabledBar(), globalAlpha));
            }

            int nameColor = module.isEnabled() ? Theme.enabledText() : Theme.disabledText();
            ctx.drawTextWithShadow(font, module.getName(),
                dropX + 6, ry + 5, Theme.applyAlpha(nameColor, globalAlpha));

            // Category hint
            Panel panel = panels.stream()
                .filter(p -> p.category.equals(module.getCategory().name()))
                .findFirst().orElse(null);
            if (panel != null) {
                String hint = "§8" + panel.getDisplayName();
                int hw = font.getWidth(hint);
                ctx.drawTextWithShadow(font, hint, dropX + dropW - hw - 6, ry + 5, 0xFFFFFF);
            }

            ctx.fill(dropX + 4, ry + SearchBar.RESULT_HEIGHT - 1,
                dropX + dropW - 4, ry + SearchBar.RESULT_HEIGHT,
                Theme.applyAlpha(Theme.separator(), globalAlpha));

            ry += SearchBar.RESULT_HEIGHT;
        }
    }
}
