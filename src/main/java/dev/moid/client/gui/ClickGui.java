package dev.moid.client.gui;

import dev.moid.client.config.ClientConfig;
import dev.moid.client.gui.animation.FadeAnimation;
import dev.moid.client.gui.animation.WarpAnimation;
import dev.moid.client.gui.header.Header;
import dev.moid.client.gui.panel.Panel;
import dev.moid.client.gui.panel.PanelRenderer;
import dev.moid.client.gui.popout.SettingsPopout;
import dev.moid.client.gui.search.SearchBar;
import dev.moid.client.gui.search.SearchRenderer;
import dev.moid.client.gui.theme.Theme;
import dev.moid.client.module.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ClickGui extends Screen {

    private final List<Panel> panels       = new ArrayList<>();
    private final FadeAnimation fade       = new FadeAnimation();
    private final SettingsPopout popout    = new SettingsPopout();
    private final SearchBar searchBar      = new SearchBar();
    private final WarpAnimation popoutAnim = new WarpAnimation();

    private TextFieldWidget searchWidget;
    private Module expandedModule          = null;
    private boolean popoutVisible          = false;

    private int dragStartX, dragStartY;
    private boolean didDrag                = false;

    public ClickGui() {
        super(Text.literal("Moid Client"));
    }

    @Override
    protected void init() {
        panels.clear();
        fade.reset();
        expandedModule = null;
        popoutVisible  = false;
        popoutAnim.snapTo(0f);
        buildSearchWidget();
        buildPanels();
    }

    private void buildSearchWidget() {
        boolean bottom = Theme.searchBottom();
        int sx = searchBar.getBarX(width);
        int sy = searchBar.getBarY(height, bottom);
        searchWidget = new TextFieldWidget(
            textRenderer, sx, sy, SearchBar.WIDTH, SearchBar.HEIGHT,
            Text.literal("Search...")
        );
        searchWidget.setMaxLength(32);
        searchWidget.setPlaceholder(Text.literal("§7Search modules..."));
        searchWidget.setVisible(searchBar.isVisible());
        searchWidget.setChangedListener(searchBar::setQuery);
        if (searchBar.isVisible()) addDrawableChild(searchWidget);
    }

    private void buildPanels() {
        List<String> seen = new ArrayList<>();
        int x = 5;
        int y = Theme.searchBottom() ? 30 : 52;
        for (Module module : dev.moid.client.MoidClient.moduleManager.getModules()) {
            String cat = module.getCategory().name();
            if (!seen.contains(cat)) {
                seen.add(cat);
                Panel panel = new Panel(cat, x, y);
                panel.collapsed = true;
                panels.add(panel);
                y += Panel.HEADER_HEIGHT + 3;
            }
        }
    }

    private int sw() { return (int)(width  / ClientConfig.guiScale); }
    private int sh() { return (int)(height / ClientConfig.guiScale); }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        int ga    = fade.tick();
        float scl = ClientConfig.guiScale;
        int smx   = (int)(mouseX / scl);
        int smy   = (int)(mouseY / scl);

        ctx.getMatrices().push();
        ctx.getMatrices().scale(scl, scl, 1f);

        // 1. Header
        Header.render(ctx, textRenderer, sw(), ga);

        // 2. Panels
        for (Panel panel : panels) {
            PanelRenderer.renderPanel(ctx, textRenderer, panel, smx, smy, ga);
        }

        ctx.getMatrices().pop();

        // 3. Search widget (unscaled so it renders correctly)
        super.render(ctx, mouseX, mouseY, delta);

        ctx.getMatrices().push();
        ctx.getMatrices().scale(scl, scl, 1f);

        // 4. Search toggle button
        SearchRenderer.renderToggleBtn(ctx, textRenderer, searchBar,
            sw(), sh(), Theme.searchBottom(), smx, smy, ga);

        // 5. Search dropdown
        SearchRenderer.renderDropdown(ctx, textRenderer, searchBar, panels,
            sw(), sh(), Theme.searchBottom(), smx, smy, ga);

        // 6. Popout with warp
        popoutAnim.setTarget(popoutVisible ? 1f : 0f);
        float ps = popoutAnim.tick();
        if (ps > 0.01f && expandedModule != null) {
            ctx.getMatrices().push();
            int ax = popout.getAnchorX();
            int ay = popout.getAnchorY();
            ctx.getMatrices().translate(ax, ay, 0);
            ctx.getMatrices().scale(ps, ps, 1f);
            ctx.getMatrices().translate(-ax, -ay, 0);
            popout.render(ctx, textRenderer, expandedModule, sw(), sh(), ga);
            ctx.getMatrices().pop();
        }

        ctx.getMatrices().pop();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        float scl = ClientConfig.guiScale;
        int mx    = (int)(mouseX / scl);
        int my    = (int)(mouseY / scl);
        boolean bottom = Theme.searchBottom();

        // Search toggle button
        if (searchBar.isBtnHovered(mx, my, sw(), sh(), bottom)) {
            searchBar.toggle();
            clearChildren();
            buildSearchWidget();
            return true;
        }

        // Search dropdown clicks
        List<Module> results = searchBar.getResults();
        int ri = searchBar.getResultAt(mx, my, sw(), sh(), bottom);
        if (ri >= 0 && ri < results.size()) {
            Module module = results.get(ri);
            if (button == 0) {
                module.toggle();
            } else if (button == 1) {
                // Jump to category
                Panel panel = panels.stream()
                    .filter(p -> p.category.equals(module.getCategory().name()))
                    .findFirst().orElse(null);
                if (panel != null) {
                    panel.collapsed = false;
                    searchWidget.setText("");
                    searchBar.setQuery("");
                }
                expandedModule = module;
                popoutVisible  = true;
                popoutAnim.snapTo(0f);
                if (panel != null) popout.setAnchor(panel.x, panel.y + Panel.HEADER_HEIGHT);
            }
            return true;
        }

        // Close popout
        if (expandedModule != null) {
            if (popout.isClickInside(mx, my, sw(), sh(), expandedModule.getSettings().size())) {
                popout.mouseClicked(expandedModule, mx, my, sw(), sh(), button);
                return true;
            }
            expandedModule = null;
            popoutVisible  = false;
            return true;
        }

        // Panel header — RIGHT CLICK only to open/close, LEFT CLICK drags
        for (Panel panel : panels) {
            if (panel.headerHovered(mx, my)) {
                if (button == 0) {
                    dragStartX = mx; dragStartY = my;
                    didDrag    = false;
                    panel.startDrag(mx, my);
                } else if (button == 1) {
                    panel.collapsed = !panel.collapsed;
                }
                return true;
            }

            // Module clicks
            if (!panel.collapsed && panel.animHeight > 2) {
                List<Module> modules = panel.getModules();
                int rowY = panel.y + Panel.HEADER_HEIGHT + 2;
                int maxY = panel.y + panel.getPanelHeight() - 2;
                for (int i = panel.scrollOffset;
                     i < modules.size() && (i - panel.scrollOffset) < Panel.MAX_VISIBLE; i++) {
                    if (rowY + Theme.moduleHeight() > maxY) break;
                    Module module = modules.get(i);
                    if (mx >= panel.x && mx <= panel.x + Theme.panelWidth()
                        && my >= rowY && my <= rowY + Theme.moduleHeight()) {
                        if (button == 0) module.toggle();
                        else if (button == 1) {
                            expandedModule = (expandedModule == module) ? null : module;
                            popoutVisible  = expandedModule != null;
                            if (popoutVisible) {
                                popoutAnim.snapTo(0f);
                                popout.setAnchor(panel.x, rowY);
                            }
                        }
                        return true;
                    }
                    rowY += Theme.moduleHeight();
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dX, double dY) {
        float scl = ClientConfig.guiScale;
        int mx    = (int)(mouseX / scl);
        int my    = (int)(mouseY / scl);
        if (Math.abs(mx - dragStartX) > 3 || Math.abs(my - dragStartY) > 3) didDrag = true;
        if (expandedModule != null) { popout.mouseDragged(mx, sw(), sh(), expandedModule); return true; }
        for (Panel panel : panels) { if (panel.dragging) { panel.drag(mx, my); return true; } }
        return super.mouseDragged(mouseX, mouseY, button, dX, dY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        didDrag = false;
        popout.mouseReleased();
        panels.forEach(Panel::stopDrag);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double h, double v) {
        float scl = ClientConfig.guiScale;
        int mx    = (int)(mouseX / scl);
        int my    = (int)(mouseY / scl);
        for (Panel panel : panels) { if (panel.isHovered(mx, my)) { panel.scroll((int) -v); return true; } }
        if (expandedModule != null) {
            popout.mouseScrolled((int)(mouseX / scl), (int)(mouseY / scl), sw(), sh(), expandedModule.getSettings().size(), v);
        }
        return super.mouseScrolled(mouseX, mouseY, h, v);
    }

    @Override
    public boolean shouldPause() { return false; }
}
