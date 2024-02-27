package ru.naumkin.java.test.sort.file.contents.statisticsrecorder.test;

public class StatisticsRecorder3<T>{
    protected int countOfLongElementsWrittenToFile;
    protected int countOfDoubleElementsWrittenToFile;
    protected int countOfStringElementsWrittenToFile;

    protected int getCountOfLongElements() {
        return countOfLongElementsWrittenToFile;
    }

    public int getCountOfDoubleElements() {
        return countOfDoubleElementsWrittenToFile;
    }

    protected int getCountOfStringElements() {
        return countOfStringElementsWrittenToFile;
    }

    protected StatisticsRecorder3() {
        this.countOfLongElementsWrittenToFile = 0;
        this.countOfDoubleElementsWrittenToFile = 0;
        this.countOfStringElementsWrittenToFile = 0;
    }

    protected void increaseCounterOfLongElements() {
        countOfLongElementsWrittenToFile++;
    }

    protected void increaseCounterOfDoubleElements() {
        countOfLongElementsWrittenToFile++;
    }

    protected void increaseOfStringElements() {
        countOfLongElementsWrittenToFile++;
    }



}
