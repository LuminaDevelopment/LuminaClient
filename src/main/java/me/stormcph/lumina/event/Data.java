package me.stormcph.lumina.event;

import java.lang.reflect.Method;

/**
 * Created by Hexeption on 18/12/2016.
 */
public record Data(Object source, Method method, byte priority) {
}
