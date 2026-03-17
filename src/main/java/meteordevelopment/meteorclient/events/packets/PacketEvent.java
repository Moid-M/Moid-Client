package meteordevelopment.meteorclient.events.packets;

import net.minecraft.network.packet.Packet;

public class PacketEvent {
    public Packet<?> packet;
    public boolean cancelled = false;

    public PacketEvent(Packet<?> packet) { this.packet = packet; }
    public void cancel() { cancelled = true; }

    public static class Send    extends PacketEvent { public Send(Packet<?> p)    { super(p); } }
    public static class Receive extends PacketEvent { public Receive(Packet<?> p) { super(p); } }
}
