package me.stormcph.lumina.cape;

public abstract class Cape {

    private final String name, fileName;

    public Cape(String name, String fileName) {
        this.name = name;
        this.fileName = fileName;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }
}
