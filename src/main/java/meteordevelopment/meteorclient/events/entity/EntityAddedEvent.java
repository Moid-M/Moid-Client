package meteordevelopment.meteorclient.events.entity;

import net.minecraft.entity.Entity;

public class EntityAddedEvent {
    public final Entity entity;
    public EntityAddedEvent(Entity entity) { this.entity = entity; }
}
