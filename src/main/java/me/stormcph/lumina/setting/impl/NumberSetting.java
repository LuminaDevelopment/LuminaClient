package me.stormcph.lumina.setting.impl;

import com.google.gson.JsonObject;
import me.stormcph.lumina.setting.Setting;

public class NumberSetting extends Setting {

    private double min, max, increment;
    private double value, defaultValue;

    public NumberSetting(String name, double min, double max, double defaultValue, double increment) {
        super(name);
        this.min = min;
        this.max = max;
        this.value = defaultValue;
        this.increment = increment;
        this.defaultValue = defaultValue;
    }

    public double clamp(double value, double min, double max) {
        value = Math.max(min, value);
        value = Math.min(max, value);
        return value;
    }

    public double getValue() {
        return value;
    }
    
    public int getIntValue() {
        return (int) value;
    }

    public float getFloatValue() {
        return (float) value;
    }

    public double getDefaultValue() {
        return defaultValue;
    }

    public void setValue(double value) {
        value = clamp(value,this.min, this.max);
        value = Math.round(value * (1.0 / this.increment)) / (1.0 / this.increment);
        this.value = value;
    }

    public void increment(boolean positive) {
        if(positive) {
            setValue(getValue() + getIncrement());
        }
        else {
            setValue(getFloatValue() - getIncrement());
        }
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getIncrement() {
        return increment;
    }

    @Override
    public void save(JsonObject object) {
        object.addProperty(getName(), value);
    }

    @Override
    public void load(JsonObject object) {
        value = object.get(getName()).getAsDouble();
    }
}
