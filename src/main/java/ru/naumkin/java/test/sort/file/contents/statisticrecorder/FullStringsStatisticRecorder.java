package ru.naumkin.java.test.sort.file.contents.statisticrecorder;

import ru.naumkin.java.test.sort.file.contents.enums.TypeOfData;

public class FullStringsStatisticRecorder extends FullStatisticRecorder {
    private int minLength;
    private int maxLength;
    private int countOfElementsWrittenToFile;
    private final TypeOfData typeOfData;

    private String currentLine;


    public FullStringsStatisticRecorder(TypeOfData typeOfData) {
        countOfElementsWrittenToFile = 0;
        minLength = 0;
        maxLength = 0;
        this.typeOfData = typeOfData;
    }


    public TypeOfData getTypeOfData() {
        return this.typeOfData;
    }

    @Override
    protected void increaseCounter() {
        countOfElementsWrittenToFile++;
    }

    @Override
    public void setString(String string) {
        this.currentLine = string;
    }

    public void addStatistic() {
        increaseCounter();
        max(currentLine);
        min(currentLine);

    }

    private void min(String string) {
        minLength = Math.min(minLength, string.length());
    }

    private void max(String string) {
        maxLength = Math.max(minLength, string.length());
    }


    @Override
    public String toString() {
        return "[File data type = " + typeOfData +
                ", min element = " + minLength +
                ", max element = " + maxLength +
                ", count of elements = " + countOfElementsWrittenToFile + "]";
    }
}
