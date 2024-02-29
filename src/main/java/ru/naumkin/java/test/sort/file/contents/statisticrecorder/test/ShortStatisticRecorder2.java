package ru.naumkin.java.test.sort.file.contents.statisticrecorder.test;

import ru.naumkin.java.test.sort.file.contents.enums.TypeOfData;

public class ShortStatisticRecorder2 implements AbstractStatisticRecorder {
    protected TypeOfData typeOfData;
    public int countOfElementsWrittenToFile;
    protected String currentLine;

    public ShortStatisticRecorder2(TypeOfData typeOfData) {
        this.countOfElementsWrittenToFile = 0;
        this.typeOfData = typeOfData;
    }

    @Override
    public void addToStatistic(TypeOfData typeOfData, String string) {
        increaseCounter();
    }

    public TypeOfData getTypeOfData() {
        return typeOfData;
    }

    public void increaseCounter() {
        this.countOfElementsWrittenToFile++;
    }

    @Override
    public void setString(String line) {
        this.currentLine = line;
    }


    @Override
    public String toString() {
        return "тип данных: " + typeOfData + " ,количество записанных элементов: " + countOfElementsWrittenToFile;
    }


}
