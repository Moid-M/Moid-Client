package meteordevelopment.meteorclient.utils.render.color;

public class Color {
    public int r, g, b, a;
    public Color() { this(255, 255, 255, 255); }
    public Color(int r, int g, int b) { this(r, g, b, 255); }
    public Color(int r, int g, int b, int a) {
        this.r = r; this.g = g; this.b = b; this.a = a;
    }
    public Color set(int r, int g, int b, int a) {
        this.r = r; this.g = g; this.b = b; this.a = a;
        return this;
    }
    public int getPacked() { return (a << 24) | (r << 16) | (g << 8) | b; }
}
