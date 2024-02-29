package ru.naumkin.java.test.sort.file.contents.statisticrecorder.test;

import ru.naumkin.java.test.sort.file.contents.enums.TypeOfData;

public interface FullStatisticRecorder2 {

    public abstract TypeOfData getTypeOfData();

    public abstract void increaseCounter();

    public abstract void setString(String string);

    public abstract void addStatistic();


}
