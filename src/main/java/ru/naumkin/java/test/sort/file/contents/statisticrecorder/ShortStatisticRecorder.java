package ru.naumkin.java.test.sort.file.contents.statisticrecorder;

import ru.naumkin.java.test.sort.file.contents.enums.TypeOfData;

public class ShortStatisticRecorder implements StatisticRecorder {
    protected TypeOfData typeOfData;
    public int countOfElementsWrittenToFile;

    public ShortStatisticRecorder(TypeOfData typeOfData) {
        this.countOfElementsWrittenToFile = 0;
        this.typeOfData = typeOfData;
    }

    @Override
    public void addToStatistic( String string) {
        increaseCounter();
    }

    public TypeOfData getTypeOfData() {
        return typeOfData;
    }

    public void increaseCounter() {
        this.countOfElementsWrittenToFile++;
    }

    @Override
    public String toString() {
        return "тип данных: " + typeOfData + " ,количество записанных элементов: " + countOfElementsWrittenToFile;
    }
}
