package meteordevelopment.meteorclient.events.entity;

import net.minecraft.entity.Entity;

public class EntityRemovedEvent {
    public final Entity entity;
    public EntityRemovedEvent(Entity entity) { this.entity = entity; }
}
