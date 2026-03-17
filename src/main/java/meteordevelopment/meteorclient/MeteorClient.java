package meteordevelopment.meteorclient;

import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.orbit.EventBus;

public class MeteorClient {
    public static final MeteorClient INSTANCE = new MeteorClient();
    public static final EventBus EVENT_BUS    = EventBus.INSTANCE;
    public final Modules modules              = new Modules();

    public static MeteorClient getInstance() { return INSTANCE; }
}
