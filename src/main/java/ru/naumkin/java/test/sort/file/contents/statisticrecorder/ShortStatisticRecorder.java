package ru.naumkin.java.test.sort.file.contents.statisticrecorder;

import ru.naumkin.java.test.sort.file.contents.enums.TypeOfData;

public class ShortStatisticRecorder   {
    protected TypeOfData typeOfData;
    public int countOfElementsWrittenToFile;
    protected String currentLine;

    public ShortStatisticRecorder(TypeOfData typeOfData) {
        this.countOfElementsWrittenToFile = 0;
        this.typeOfData = typeOfData;
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
