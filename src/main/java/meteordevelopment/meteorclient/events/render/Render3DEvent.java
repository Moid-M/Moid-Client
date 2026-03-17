package meteordevelopment.meteorclient.events.render;

import net.minecraft.client.util.math.MatrixStack;

public class Render3DEvent {
    public final MatrixStack matrices;
    public final float tickDelta;

    public Render3DEvent(MatrixStack matrices, float tickDelta) {
        this.matrices  = matrices;
        this.tickDelta = tickDelta;
    }
}
