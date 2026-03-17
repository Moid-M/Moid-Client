package dev.moid.client.gui.search;

import dev.moid.client.MoidClient;
import dev.moid.client.gui.theme.Theme;
import dev.moid.client.module.Module;

import java.util.List;

public class SearchBar {

    public static final int HEIGHT        = 16;
    public static final int RESULT_HEIGHT = 18;
    public static final int MAX_RESULTS   = 8;
    public static final int WIDTH         = 200;
    // Gap between search bar and toggle button
    public static final int BTN_GAP       = 6;
    public static final int BTN_W         = 16;
    public static final int BTN_H         = 16;

    private boolean visible = true;
    private String query    = "";

    public boolean isVisible()         { return visible; }
    public void setVisible(boolean v)  { visible = v; }
    public void toggle()               { visible = !visible; }
    public String getQuery()           { return visible ? query : ""; }
    public void setQuery(String q)     { query = q; }

    public int getBarX(int screenWidth)  { return screenWidth / 2 - WIDTH / 2; }
    public int getBarY(int screenHeight, boolean bottom) {
        return bottom ? screenHeight - 30 : 28;
    }
    // Toggle button sits to the RIGHT of the search bar with a gap
    public int getBtnX(int screenWidth)  { return getBarX(screenWidth) + WIDTH + BTN_GAP; }
    public int getBtnY(int screenHeight, boolean bottom) {
        return getBarY(screenHeight, bottom);
    }

    public int getDropdownY(int screenHeight, boolean bottom, List<Module> results) {
        return bottom
            ? screenHeight - 30 - results.size() * RESULT_HEIGHT - 4
            : getBarY(screenHeight, bottom) + HEIGHT + 2;
    }

    public List<Module> getResults() {
        if (!visible || query.isEmpty()) return List.of();
        String q = query.toLowerCase().trim();
        return MoidClient.moduleManager.getModules().stream()
            .filter(m -> m.getName().toLowerCase().contains(q))
            .limit(MAX_RESULTS)
            .toList();
    }

    public boolean isBtnHovered(int mx, int my, int screenWidth, int screenHeight, boolean bottom) {
        int bx = getBtnX(screenWidth);
        int by = getBtnY(screenHeight, bottom);
        return mx >= bx && mx <= bx + BTN_W && my >= by && my <= by + BTN_H;
    }

    public int getResultAt(int mx, int my, int screenWidth, int screenHeight, boolean bottom) {
        List<Module> results = getResults();
        if (results.isEmpty()) return -1;
        int dropX = getBarX(screenWidth);
        int dropY = getDropdownY(screenHeight, bottom, results);
        int ry    = dropY + 2;
        for (int i = 0; i < results.size(); i++) {
            if (mx >= dropX && mx <= dropX + WIDTH && my >= ry && my <= ry + RESULT_HEIGHT) return i;
            ry += RESULT_HEIGHT;
        }
        return -1;
    }
}
