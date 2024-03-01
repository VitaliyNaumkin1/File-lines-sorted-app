package ru.naumkin.java.test.sort.file.contents.statisticrecorder;

import ru.naumkin.java.test.sort.file.contents.enums.TypeOfData;

public interface StatisticRecorder {
    abstract void addToStatistic( String string);

    public TypeOfData getTypeOfData();

    public void increaseCounter();
}
