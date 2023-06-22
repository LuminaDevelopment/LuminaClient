package me.stormcph.lumina.utils;

public class RandomizationUtil {
    public static int randomize(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static int randomize(double min, double max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }
}
