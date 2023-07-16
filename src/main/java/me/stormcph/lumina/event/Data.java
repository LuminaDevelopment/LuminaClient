package me.stormcph.lumina.event;

import java.lang.reflect.Method;

/**
 * Created by Hexeption on 18/12/2016.
 */
public class Data {

    public final Object source;

    public final Method target;

    public final byte priority;

    Data(Object source, Method target, byte priority) {

        this.source = source;
        this.target = target;
        this.priority = priority;
    }

}
