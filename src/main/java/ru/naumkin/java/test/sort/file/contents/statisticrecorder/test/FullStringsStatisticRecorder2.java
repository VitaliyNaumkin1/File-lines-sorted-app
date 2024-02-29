package ru.naumkin.java.test.sort.file.contents.statisticrecorder.test;

import ru.naumkin.java.test.sort.file.contents.enums.TypeOfData;
import ru.naumkin.java.test.sort.file.contents.statisticrecorder.FullStatisticRecorder;

public class FullStringsStatisticRecorder2 extends FullStatisticRecorder {
    private long minLength;
    private long maxLength;
    private long countOfElementsWrittenToFile;
    private final TypeOfData typeOfData;

    private String currentLine;


    public FullStringsStatisticRecorder2(TypeOfData typeOfData) {
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
