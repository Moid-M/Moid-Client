package dev.moid.client.gui.panel;

import dev.moid.client.MoidClient;
import dev.moid.client.gui.animation.WarpAnimation;
import dev.moid.client.gui.theme.Theme;
import dev.moid.client.module.Module;

import java.util.List;
import java.util.function.Supplier;

public class Panel {

    public static final int HEADER_HEIGHT = 18;
    public static final int MAX_VISIBLE   = 10;

    public String category;
    public int x, y;
    public boolean collapsed = true;
    public int scrollOffset  = 0;

    // Warp animation for open/close
    private final WarpAnimation anim = new WarpAnimation();
    public float animHeight = 0f;

    // Drag
    public boolean dragging  = false;
    public int dragOffsetX, dragOffsetY;

    // Search supplier
    public Supplier<String> querySupplier = () -> "";

    public Panel(String category, int x, int y) {
        this.category = category;
        this.x = x;
        this.y = y;
        // Start snapped to 0 since collapsed by default
        anim.snapTo(0f);
    }

    public List<Module> getModules() {
        String query = querySupplier.get().toLowerCase().trim();
        return MoidClient.moduleManager.getModules().stream()
            .filter(m -> m.getCategory().name().equals(category))
            .filter(m -> query.isEmpty() || m.getName().toLowerCase().contains(query))
            .toList();
    }

    public int getTargetHeight() {
        if (collapsed) return 0;
        return Math.min(getModules().size(), MAX_VISIBLE) * Theme.moduleHeight() + 4;
    }

    public void tickAnimation() {
        anim.setTarget(getTargetHeight());
        animHeight = anim.tick();
    }

    public int getPanelHeight() {
        return HEADER_HEIGHT + (int) animHeight;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + Theme.panelWidth()
            && mouseY >= y && mouseY <= y + getPanelHeight();
    }

    public void scroll(int dir) {
        int max = Math.max(0, getModules().size() - MAX_VISIBLE);
        scrollOffset = Math.max(0, Math.min(max, scrollOffset + dir));
    }

    public void startDrag(int mouseX, int mouseY) {
        dragging    = true;
        dragOffsetX = mouseX - x;
        dragOffsetY = mouseY - y;
    }

    public void drag(int mouseX, int mouseY) {
        if (dragging) {
            x = mouseX - dragOffsetX;
            y = mouseY - dragOffsetY;
        }
    }

    public void stopDrag() { dragging = false; }

    public boolean headerHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + Theme.panelWidth()
            && mouseY >= y && mouseY <= y + HEADER_HEIGHT;
    }

    public Module getModuleAt(int mouseX, int mouseY) {
        if (animHeight < 2) return null;
        List<Module> modules = getModules();
        int my = y + HEADER_HEIGHT + 2;
        int maxY = y + getPanelHeight() - 2;
        for (int i = scrollOffset; i < modules.size() && (i - scrollOffset) < MAX_VISIBLE; i++) {
            if (my + Theme.moduleHeight() > maxY) break;
            if (mouseX >= x && mouseX <= x + Theme.panelWidth()
                && mouseY >= my && mouseY <= my + Theme.moduleHeight()) {
                return modules.get(i);
            }
            my += Theme.moduleHeight();
        }
        return null;
    }

    // Format category name to title case e.g. "MOID-ADDON" -> "Moid-Addon"
    public String getDisplayName() {
        String[] parts = category.replace("_", "-").split("-");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                sb.append(Character.toUpperCase(part.charAt(0)));
                if (part.length() > 1) sb.append(part.substring(1).toLowerCase());
                sb.append("-");
            }
        }
        String result = sb.toString();
        if (result.endsWith("-")) result = result.substring(0, result.length() - 1);
        return result;
    }
}
