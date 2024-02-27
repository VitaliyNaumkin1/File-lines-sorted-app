package ru.naumkin.java.test.sort.file.contents.statisticsrecorder.done;

public class StatisticsRecorder {
    protected int countOfElementsWrittenToFile;

    public StatisticsRecorder() {
        this.countOfElementsWrittenToFile = 0;
    }

    protected void increaseCounter() {
        countOfElementsWrittenToFile++;
    }

    @Override
    public String toString() {
        return "StatisticsRecorder2{" +
                "countOfElementsWrittenToFile=" + countOfElementsWrittenToFile +
                '}';
    }
}
