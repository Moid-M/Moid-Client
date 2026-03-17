package dev.moid.client.gui.popout;

import dev.moid.client.config.ClientConfig;
import dev.moid.client.gui.popout.widgets.*;
import dev.moid.client.gui.theme.Theme;
import dev.moid.client.module.Module;
import dev.moid.client.module.setting.*;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.List;

public class SettingsPopout {

    private static final int WIDTH        = 220;
    private static final int TITLE_HEIGHT = 18;
    private static final int PADDING      = 6;
    private static final int MAX_VISIBLE  = 10;

    private int anchorX, anchorY;
    private int scrollOffset   = 0;
    private int draggingIndex  = -1;
    private boolean isDragging = false;
    private int expandedIndex  = -1;

    // Store raw screen mouseX for drag updates
    private int rawDragMouseX  = 0;

    public void setAnchor(int x, int y) {
        anchorX       = x;
        anchorY       = y;
        scrollOffset  = 0;
        expandedIndex = -1;
        draggingIndex = -1;
        isDragging    = false;
    }

    public int getAnchorX() { return anchorX; }
    public int getAnchorY() { return anchorY; }

    private int getTotalHeight(int count) {
        int visible = Math.min(count, MAX_VISIBLE);
        int base = TITLE_HEIGHT
            + Math.max(1, visible) * SettingRowRenderer.ROW_HEIGHT
            + PADDING;
        if (expandedIndex >= 0) base += ColorPickerWidget.HEIGHT;
        return base;
    }

    private int getPx(int screenWidth) {
        int px = anchorX + Theme.panelWidth() + 4;
        if (px + WIDTH > screenWidth) px = anchorX - WIDTH - 4;
        return Math.max(2, px);
    }

    private int getPy(int screenHeight, int count) {
        int py = anchorY;
        int h  = getTotalHeight(count);
        if (py + h > screenHeight) py = screenHeight - h - 4;
        return Math.max(4, py);
    }

    public void render(DrawContext ctx, TextRenderer font,
                       Module module, int screenWidth, int screenHeight,
                       int globalAlpha) {
        if (module == null) return;
        List<Setting<?>> settings = module.getSettings();
        int count  = settings.size();
        int px     = getPx(screenWidth);
        int py     = getPy(screenHeight, count);
        int height = getTotalHeight(count);

        // Background + borders
        ctx.fill(px, py, px + WIDTH, py + height,
            Theme.applyAlpha(0xEE0d0d1a, globalAlpha));
        ctx.fill(px, py, px + WIDTH, py + 2,
            Theme.applyAlpha(Theme.accent(), globalAlpha));
        ctx.fill(px, py, px + 1, py + height,
            Theme.applyAlpha(Theme.accent(), globalAlpha));
        ctx.fill(px + WIDTH - 1, py, px + WIDTH, py + height,
            Theme.applyAlpha(0x44FFFFFF, globalAlpha));
        ctx.fill(px, py + height - 1, px + WIDTH, py + height,
            Theme.applyAlpha(0x44FFFFFF, globalAlpha));

        // Connector line
        ctx.fill(anchorX + Theme.panelWidth(),
            anchorY + Theme.moduleHeight() / 2,
            px, anchorY + Theme.moduleHeight() / 2 + 1,
            Theme.applyAlpha(Theme.accent(), globalAlpha));

        // Title
        String title = "§d" + module.getName();
        if (count > MAX_VISIBLE)
            title += " §8(" + (scrollOffset + 1) + "-"
                + Math.min(scrollOffset + MAX_VISIBLE, count)
                + "/" + count + ")";
        ctx.drawTextWithShadow(font, title, px + 6, py + 4, 0xFFFFFF);

        if (settings.isEmpty()) {
            ctx.drawCenteredTextWithShadow(font, "§7No settings",
                px + WIDTH / 2, py + TITLE_HEIGHT + 8, 0xFFFFFF);
            return;
        }

        int sy = py + TITLE_HEIGHT;
        for (int i = scrollOffset;
             i < settings.size() && (i - scrollOffset) < MAX_VISIBLE; i++) {
            Setting<?> setting = settings.get(i);
            SettingRowRenderer.renderRow(ctx, font, setting, i, px, sy,
                WIDTH, expandedIndex == i, draggingIndex, isDragging, globalAlpha);
            sy += SettingRowRenderer.ROW_HEIGHT;

            if (expandedIndex == i && setting instanceof BridgedStringSetting bss) {
                ColorPickerWidget.render(ctx, font, bss, px, sy, WIDTH, globalAlpha);
                sy += ColorPickerWidget.HEIGHT;
            }
        }

        if (count > MAX_VISIBLE) {
            ctx.drawCenteredTextWithShadow(font, "§8▲▼",
                px + WIDTH - 10, py + 6, 0xFFFFFF);
        }
    }

    // mouseX and mouseY here are already in SCALED space
    public boolean mouseClicked(Module module, int mouseX, int mouseY,
                                int screenWidth, int screenHeight, int button) {
        if (module == null) return false;
        List<Setting<?>> settings = module.getSettings();
        int px = getPx(screenWidth);
        int py = getPy(screenHeight, settings.size());
        int sy = py + TITLE_HEIGHT;

        for (int i = scrollOffset;
             i < settings.size() && (i - scrollOffset) < MAX_VISIBLE; i++) {
            Setting<?> setting = settings.get(i);

            if (mouseX >= px && mouseX <= px + WIDTH
                && mouseY >= sy && mouseY <= sy + SettingRowRenderer.ROW_HEIGHT) {
                handleRowClick(setting, i, mouseX, mouseY, px, sy, button, null);
                return true;
            }
            sy += SettingRowRenderer.ROW_HEIGHT;

            if (expandedIndex == i && setting instanceof BridgedStringSetting) {
                if (mouseY >= sy && mouseY <= sy + ColorPickerWidget.HEIGHT) {
                    ColorPickerWidget.mouseClicked(mouseX, mouseY, px, sy, WIDTH);
                    return true;
                }
                sy += ColorPickerWidget.HEIGHT;
            }
        }
        return false;
    }

    private void handleRowClick(Setting<?> setting, int index,
                                int mouseX, int mouseY,
                                int px, int sy, int button,
                                TextRenderer font) {
        if (setting instanceof BooleanSetting bs && button == 0) {
            bs.toggle();
        } else if (setting instanceof SliderSetting ss && button == 0) {
            draggingIndex = index;
            isDragging    = true;
            // mouseX is already scaled here
            SliderWidget.updateValue(ss, mouseX, px, WIDTH);
        } else if (setting instanceof BridgedEnumSetting es && button == 0) {
            EnumWidget.handleClick(es, mouseX, mouseY, px, sy, WIDTH, font);
        } else if (setting instanceof BridgedStringSetting && button == 0) {
            expandedIndex = (expandedIndex == index) ? -1 : index;
        }
    }

    // rawScreenMouseX is the UNSCALED mouse position from the OS
    public void mouseDragged(int rawScreenMouseX, int screenWidth,
                             int screenHeight, Module module) {
        if (!isDragging || draggingIndex < 0 || module == null) return;
        List<Setting<?>> settings = module.getSettings();
        if (draggingIndex >= settings.size()) return;

        // Convert screen coords to scaled coords
        int scaledMouseX = (int)(rawScreenMouseX / ClientConfig.guiScale);
        int px           = getPx(screenWidth);

        Setting<?> setting = settings.get(draggingIndex);
        if (setting instanceof SliderSetting ss) {
            SliderWidget.updateValue(ss, scaledMouseX, px, WIDTH);
        }
        ColorPickerWidget.mouseDragged(scaledMouseX, px, WIDTH);
    }

    public void mouseReleased() {
        isDragging    = false;
        draggingIndex = -1;
        ColorPickerWidget.mouseReleased();
    }

    public void mouseScrolled(int mouseX, int mouseY, int screenWidth,
                              int screenHeight, int settingsCount, double v) {
        if (!isClickInside(mouseX, mouseY, screenWidth, screenHeight, settingsCount))
            return;
        int max = Math.max(0, settingsCount - MAX_VISIBLE);
        scrollOffset = (int) Math.max(0, Math.min(max, scrollOffset - v));
    }

    public boolean isClickInside(int mouseX, int mouseY,
                                 int screenWidth, int screenHeight,
                                 int settingsCount) {
        int height = getTotalHeight(settingsCount);
        int px     = getPx(screenWidth);
        int py     = getPy(screenHeight, settingsCount);
        return mouseX >= px && mouseX <= px + WIDTH
            && mouseY >= py && mouseY <= py + height;
    }
}
