package dev.moid.client.gui.animation;

import dev.moid.client.gui.theme.Theme;

public class FadeAnimation {

    private float alpha = 0f;
    private static final float SPEED = 0.08f;

    public void reset() { alpha = 0f; }

    public int tick() {
        if (Theme.fade()) {
            alpha = Math.min(1f, alpha + SPEED);
        } else {
            alpha = 1f;
        }
        return (int)(alpha * 255);
    }

    public float getAlpha() { return alpha; }
}
