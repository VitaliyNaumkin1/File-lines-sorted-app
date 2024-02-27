package ru.naumkin.java.test.sort.file.contents.statisticsrecorder.done;

public class FullIntegerNumberStatisticRecorder extends StatisticsRecorder {
    private long minElement;
    private long maxElement;

    public FullIntegerNumberStatisticRecorder() {
        super();
        minElement = 0;
        maxElement = 0;
    }

    public long getMinElement() {
        return minElement;
    }

    public long getMaxElement() {
        return maxElement;
    }

    public void min(long number) {
        if (minElement > number) {
            minElement = number;
        }
    }

    public void max(long number) {
        if (maxElement < number) {
            maxElement = number;
        }
    }

    @Override
    public String toString() {
        return "FullIntegerNumberStatisticRecorder{" +
                "minElement=" + minElement +
                ", maxElement=" + maxElement +
                ", countOfElementsWrittenToFile=" + countOfElementsWrittenToFile +
                '}';
    }
}
