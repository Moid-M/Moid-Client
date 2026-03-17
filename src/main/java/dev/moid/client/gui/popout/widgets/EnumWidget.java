package dev.moid.client.gui.popout.widgets;

import dev.moid.client.gui.theme.Theme;
import dev.moid.client.module.setting.BridgedEnumSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class EnumWidget {

    public static final int HEIGHT = 24;

    public static void render(DrawContext ctx, TextRenderer font,
                              BridgedEnumSetting es, int px, int sy,
                              int width, int globalAlpha) {
        ctx.drawTextWithShadow(font, "§7" + es.getName(), px + 6, sy + 8, 0xFFFFFF);

        if (es.getEnumValues() != null) {
            int tabX = px + 6 + font.getWidth("§7" + es.getName()) + 8;
            for (Object enumVal : es.getEnumValues()) {
                String label     = enumVal.toString();
                boolean selected = enumVal.equals(es.getValue());
                int tabW         = font.getWidth(label) + 6;
                if (tabX + tabW > px + width - 4) break;

                ctx.fill(tabX, sy + 5, tabX + tabW, sy + HEIGHT - 5,
                    Theme.applyAlpha(selected ? Theme.accent() : Theme.panelHeader(), globalAlpha));
                ctx.drawTextWithShadow(font,
                    selected ? "§d" + label : "§8" + label,
                    tabX + 3, sy + 8, 0xFFFFFF);
                tabX += tabW + 3;
            }
        } else {
            String valStr = es.getValue() != null ? es.getValue().toString() : "??";
            int btnW = Math.min(font.getWidth(valStr) + 10, 80);
            int btnX = px + width - btnW - 4;
            ctx.fill(btnX, sy + 4, btnX + btnW, sy + HEIGHT - 4,
                Theme.applyAlpha(Theme.panelHeader(), globalAlpha));
            ctx.fill(btnX, sy + 4, btnX + btnW, sy + 5,
                Theme.applyAlpha(Theme.accent(), globalAlpha));
            ctx.drawCenteredTextWithShadow(font, "§d" + valStr,
                btnX + btnW / 2, sy + 8, 0xFFFFFF);
        }
    }

    public static boolean handleClick(BridgedEnumSetting es, int mouseX, int mouseY,
                                      int px, int sy, int width, TextRenderer font) {
        // Use Minecraft's font renderer as fallback if null
        TextRenderer f = (font != null)
            ? font
            : MinecraftClient.getInstance().textRenderer;

        if (es.getEnumValues() == null) {
            es.cycleNext();
            return true;
        }

        int tabX = px + 6 + f.getWidth("§7" + es.getName()) + 8;
        for (Object enumVal : es.getEnumValues()) {
            String label = enumVal.toString();
            int tabW     = f.getWidth(label) + 6;
            if (mouseX >= tabX && mouseX <= tabX + tabW
                && mouseY >= sy + 5 && mouseY <= sy + HEIGHT - 5) {
                while (!es.getValue().equals(enumVal)) es.cycleNext();
                return true;
            }
            tabX += tabW + 3;
        }
        return false;
    }
}
