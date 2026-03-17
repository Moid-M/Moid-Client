package dev.moid.client.gui.animation;

public class WarpAnimation {

    private float value = 0f;
    private float target = 0f;
    private static final float STIFFNESS = 0.25f;
    private static final float DAMPING   = 0.6f;
    private float velocity = 0f;

    public void setTarget(float t) { this.target = t; }

    public float tick() {
        float force = (target - value) * STIFFNESS;
        velocity = velocity * DAMPING + force;
        value += velocity;
        if (Math.abs(target - value) < 0.001f && Math.abs(velocity) < 0.001f) {
            value = target;
            velocity = 0f;
        }
        return value;
    }

    public float getValue() { return value; }
    public void snapTo(float v) { value = v; target = v; velocity = 0f; }
    public boolean isDone() { return value == target && velocity == 0f; }
}
