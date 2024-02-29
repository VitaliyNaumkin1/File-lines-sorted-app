package ru.naumkin.java.test.sort.file.contents.enums;

public enum StatisticMode {
    SHORT("-r"), FULL("-r");
    private final String string;

    StatisticMode(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
