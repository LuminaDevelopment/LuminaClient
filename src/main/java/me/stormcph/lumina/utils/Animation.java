package me.stormcph.lumina.utils;

// An Animation Utility class
public class Animation {

    // Values for
    private float end, current;
    private boolean done;

    public Animation(int start, int end) {
        this.end = end;
        this.current = start;
        done = false;
    }

    public void update() {
        update(false, 20);
    }

    public void update(boolean cast) {
        update(cast, 20);
    }

    public void update(boolean cast, float speed) {
        float distance = end - current;
        if (Math.abs(distance) > 0.01f) { // Check if the distance is greater than a small threshold value to prevent floating-point errors
            float increment = distance / speed;
            if (Math.abs(distance) < Math.abs(increment)) {
                current = end;
            } else {
                current += increment;
            }
            if (cast) current = (int) current;
        } else {
            current = end;
            done = true;
        }
    }

    public boolean hasEnded() {
        return done;
    }

    public float getValue() {
        return current;
    }

    public float getEnd() {
        return end;
    }

    public void setValue(float current) {
        this.current = current;
    }

    // Set the end
    public void setEnd(float end) {
        this.end = end;
        done = false;
    }
}
