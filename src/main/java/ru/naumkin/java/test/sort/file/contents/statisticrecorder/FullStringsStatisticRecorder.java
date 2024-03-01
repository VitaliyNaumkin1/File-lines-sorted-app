package ru.naumkin.java.test.sort.file.contents.statisticrecorder;

import ru.naumkin.java.test.sort.file.contents.enums.TypeOfData;

public class FullStringsStatisticRecorder implements StatisticRecorder {
    private long minLength;
    private long maxLength;
    private long countOfElementsWrittenToFile;
    private final TypeOfData typeOfData;
    private boolean isFirstStatisticAdded = false;

    public FullStringsStatisticRecorder(TypeOfData typeOfData) {
        countOfElementsWrittenToFile = 0;
        minLength = 0;
        maxLength = 0;
        this.typeOfData = typeOfData;
    }

    @Override
    public void addToStatistic(String string) {
        increaseCounter();
        if (!isFirstStatisticAdded) {
            minLength = string.length();
            maxLength = string.length();
            isFirstStatisticAdded = true;
        }
        max(string);
        min(string);
    }

    public TypeOfData getTypeOfData() {
        return this.typeOfData;
    }

    @Override
    public void increaseCounter() {
        countOfElementsWrittenToFile++;
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
