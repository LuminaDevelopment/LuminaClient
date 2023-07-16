package me.stormcph.lumina.event;

/**
 * Created by Hexeption on 18/12/2016.
 */
public class Priority {

    public static final byte FIRST = 0, SECOND = 1, THIRD = 2, FOURTH = 3, FIFTH = 4;

    public static final byte[] VALUE_ARRAY;

    static {
        VALUE_ARRAY = new byte[]{0, 1, 2, 3, 4};
    }

}
