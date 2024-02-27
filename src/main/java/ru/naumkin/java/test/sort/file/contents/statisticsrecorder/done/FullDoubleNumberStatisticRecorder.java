package ru.naumkin.java.test.sort.file.contents.statisticsrecorder.done;

public class FullDoubleNumberStatisticRecorder extends StatisticsRecorder {
    private double minElement;
    private double maxElement;

    public FullDoubleNumberStatisticRecorder() {
        super();
        minElement = 0;
        maxElement = 0;
    }

    public double getMinElement() {
        return minElement;
    }

    public double getMaxElement() {
        return maxElement;
    }

    public void min(double number) {
        if (minElement > number) {
            minElement = number;
        }
    }

    public void max(double number) {
        if (maxElement < number) {
            maxElement = number;
        }
    }

    @Override
    public String toString() {
        return "FullDoubleNumberStatisticRecorder{" +
                "minElement=" + minElement +
                ", maxElement=" + maxElement +
                ", countOfElementsWrittenToFile=" + countOfElementsWrittenToFile +
                '}';
    }
}
