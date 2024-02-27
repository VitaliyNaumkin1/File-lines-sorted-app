package ru.naumkin.java.test.sort.file.contents.statisticsrecorder.test;

import ru.naumkin.java.test.sort.file.contents.enums.StatisticMode;
import ru.naumkin.java.test.sort.file.contents.statisticsrecorder.done.*;

public class Counter {

    StatisticMode statisticMode;
    private StatisticsRecorder integers;
    private StatisticsRecorder floats;
    private StatisticsRecorder strings;

    public Counter(StatisticMode statisticMode) {
        this.statisticMode = statisticMode;
        if (statisticMode.equals(StatisticMode.SHORT)) {
            integers = new ShortStatisticsRecorder();
            floats = new ShortStatisticsRecorder();
            strings = new ShortStatisticsRecorder();
            return;
        }

        if (statisticMode.equals(StatisticMode.FULL)) {
            integers = new FullStringsStatisticRecorder();
            floats = new FullIntegerNumberStatisticRecorder();
            strings = new FullDoubleNumberStatisticRecorder();
        }
    }
}
