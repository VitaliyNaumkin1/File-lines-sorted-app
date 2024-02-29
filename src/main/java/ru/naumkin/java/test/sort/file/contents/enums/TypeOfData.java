package ru.naumkin.java.test.sort.file.contents.enums;

public enum TypeOfData {
    STRING("строка"), INTEGER("целое число"), FLOAT("вещественное число");

    private String rusName;

    TypeOfData(String rusName) {
        this.rusName = rusName;
    }

    public String getRusName() {
        return rusName;
    }
}
