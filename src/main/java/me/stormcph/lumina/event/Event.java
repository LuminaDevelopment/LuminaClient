package me.stormcph.lumina.event;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Hexeption on 18/12/2016.
 */
public abstract class Event {
    private boolean cancelled;

    public enum State {
        PRE("PRE", 0),
        POST("POST", 1);

        private State(final String string, final int number) {}
    }

    public Event call() {
        this.cancelled = false;
        call(this);
        return this;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    private static void call(final Event event) {
        final List<Data> dataList = EventManager.get(event.getClass());

        if (dataList != null) {
            for (final Data data : dataList) {
                try {
                    data.target().invoke(data.source(), event);
                }
                catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
