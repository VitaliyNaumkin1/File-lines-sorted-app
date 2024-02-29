package ru.naumkin.java.test.sort.file.contents.statisticrecorder;

import ru.naumkin.java.test.sort.file.contents.enums.TypeOfData;

public abstract class FullStatisticRecorder {

    public abstract TypeOfData getTypeOfData();

    protected abstract void increaseCounter();
    public abstract void setString(String string);
    public abstract void addStatistic();


}
