package me.stormcph.lumina.event;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Hexeption on 18/12/2016.
 */
public class EventManager {
    private static final Map<Class<? extends Event>, List<Data>> REGISTRY_MAP = new HashMap<>();

    public static void register(final Object o) {
        for (final Method method : o.getClass().getDeclaredMethods()) {
            if (!isMethodBad(method)) {
                register(method, o);
            }
        }
    }

    public static void register(final Object o, final Class<? extends Event> clazz) {
        for (final Method method : o.getClass().getDeclaredMethods()) {
            if (!isMethodBad(method, clazz)) {
                register(method, o);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void register(final Method method, final Object o) {
        final Class<?> clazz = method.getParameterTypes()[0];
        final Data methodData = new Data(o, method, method.getAnnotation(EventTarget.class).priority());

        if (!methodData.target().canAccess(o)) {
            methodData.target().setAccessible(true);
        }

        List<Data> eventData = REGISTRY_MAP.computeIfAbsent((Class<? extends Event>) clazz, k -> new ArrayList<>());
        if (!eventData.contains(methodData)) {
            eventData.add(methodData);
            sortListValue((Class<? extends Event>) clazz);
        }
    }

    public static void unregister(final Object o) {
        boolean removed = false;
        for (var eventData : REGISTRY_MAP.values()) {
            removed |= eventData.removeIf(methodData -> methodData.source() == o);
        }

        if (removed) cleanMap(true);
    }

    public static void unregister(final Object o, final Class<? extends Event> clazz) {
        List<Data> eventData = REGISTRY_MAP.get(clazz);
        if (eventData != null && eventData.removeIf(methodData -> methodData.source() == o)) cleanMap(true);
    }


    public static void cleanMap(final boolean b) {
        if (!b) REGISTRY_MAP.clear();
        else REGISTRY_MAP.values().removeIf(List::isEmpty);
    }

    public static void removeEntry(final Class<? extends Event> clazz) {
        REGISTRY_MAP.remove(clazz);
    }

    private static void sortListValue(final Class<? extends Event> clazz) {
        REGISTRY_MAP.get(clazz).sort(Comparator.comparingInt(Data::priority));
    }

    private static boolean isMethodBad(final Method method) {
        return method.getParameterCount() != 1 || method.getParameterTypes()[0].isAssignableFrom(Event.class) || !method.isAnnotationPresent(EventTarget.class);
    }

    private static boolean isMethodBad(final Method method, final Class<? extends Event> clazz) {
        return isMethodBad(method) || method.getParameterTypes()[0].equals(clazz);
    }

    public static List<Data> get(final Class<? extends Event> clazz) {
        return EventManager.REGISTRY_MAP.get(clazz);
    }
}
