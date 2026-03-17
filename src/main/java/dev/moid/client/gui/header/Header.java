package dev.moid.client.gui.header;

import dev.moid.client.MoidClient;
import dev.moid.client.gui.theme.Theme;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class Header {

    public static final int HEIGHT = 22;

    public static void render(DrawContext ctx, TextRenderer font, int screenWidth, int globalAlpha) {
        ctx.fill(0, 0, screenWidth, HEIGHT, Theme.applyAlpha(0xFF0d0d1a, globalAlpha));
        ctx.fill(0, HEIGHT, screenWidth, HEIGHT + 1, Theme.applyAlpha(Theme.accent(), globalAlpha));
        ctx.drawCenteredTextWithShadow(font,
            "§dMoid §5Client §8| §7v" + MoidClient.MOD_VERSION,
            screenWidth / 2, 7, Theme.applyAlpha(0xFFFFFF, globalAlpha));
    }
}
