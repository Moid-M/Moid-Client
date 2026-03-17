package meteordevelopment.orbit;

import dev.moid.client.MoidClient;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {

    public static final EventBus INSTANCE = new EventBus();

    private final Map<Class<?>, List<Listener>> listeners = new HashMap<>();

    public static class Listener {
        public final Object instance;
        public final Method method;
        public Listener(Object instance, Method method) {
            this.instance = instance;
            this.method   = method;
        }
    }

    public void subscribe(Object obj) {
        for (Method method : getAllMethods(obj.getClass())) {
            // Check by annotation class NAME not identity
            // This handles cross-classloader annotation matching
            if (hasEventHandlerAnnotation(method)) {
                Class<?>[] params = method.getParameterTypes();
                if (params.length == 1) {
                    method.setAccessible(true);
                    Class<?> eventType = params[0];
                    listeners
                        .computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                        .add(new Listener(obj, method));
                    MoidClient.LOGGER.info("  Registered handler: "
                        + obj.getClass().getSimpleName()
                        + "." + method.getName()
                        + "(" + eventType.getSimpleName() + ")");
                }
            }
        }
    }

    public void unsubscribe(Object obj) {
        for (List<Listener> list : listeners.values()) {
            list.removeIf(l -> l.instance == obj);
        }
    }

    public <T> T post(T event) {
        // Match by event class AND superclasses
        List<Listener> toCall = new ArrayList<>();
        Class<?> eventClass = event.getClass();
        for (Map.Entry<Class<?>, List<Listener>> entry : listeners.entrySet()) {
            if (entry.getKey().isAssignableFrom(eventClass)
                || eventClass.getName().equals(entry.getKey().getName())) {
                toCall.addAll(entry.getValue());
            }
        }
        for (Listener listener : toCall) {
            try {
                listener.method.invoke(listener.instance, event);
            } catch (Exception e) {
                MoidClient.LOGGER.error("EventBus error in "
                    + listener.instance.getClass().getSimpleName()
                    + "." + listener.method.getName()
                    + ": " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
            }
        }
        return event;
    }

    // Check if method has ANY annotation named "EventHandler"
    private boolean hasEventHandlerAnnotation(Method method) {
        for (Annotation annotation : method.getAnnotations()) {
            if (annotation.annotationType().getSimpleName().equals("EventHandler")) {
                return true;
            }
        }
        return false;
    }

    private List<Method> getAllMethods(Class<?> clazz) {
        List<Method> methods = new ArrayList<>();
        while (clazz != null) {
            try {
                methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
            } catch (Exception ignored) {}
            clazz = clazz.getSuperclass();
        }
        return methods;
    }
}
