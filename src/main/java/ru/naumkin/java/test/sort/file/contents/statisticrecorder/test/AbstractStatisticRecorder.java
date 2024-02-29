package ru.naumkin.java.test.sort.file.contents.statisticrecorder.test;

import ru.naumkin.java.test.sort.file.contents.enums.TypeOfData;

public interface AbstractStatisticRecorder {
    abstract void addToStatistic(TypeOfData typeOfData, String string);
//    abstract void addToStatistic(TypeOfData typeOfData, String string);
public abstract TypeOfData getTypeOfData();

    public abstract void increaseCounter();

    public abstract void setString(String string);


}
