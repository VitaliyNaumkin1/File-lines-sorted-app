package ru.naumkin.java.test.sort.file.contents.statisticsrecorder.done;

public class FullStringsStatisticRecorder extends StatisticsRecorder {
    private int shorLineSize;
    private int longestLineSize;

    public int getShorLineSize() {
        return shorLineSize;
    }

    public int getLongestLineSize() {
        return longestLineSize;
    }

    public FullStringsStatisticRecorder() {
        shorLineSize = 0;
        longestLineSize = 0;
    }

    public void shortestLine(String string) {
        if (shorLineSize > string.length()) {
            shorLineSize = string.length();
        }
    }

    public void longestLine(String string) {
        if (longestLineSize < string.length()) {
            longestLineSize = string.length();
        }
    }

    @Override
    public String toString() {
        return "FullStringsStatisticRecorder{" +
                "shorLineSize=" + shorLineSize +
                ", longestLineSize=" + longestLineSize +
                ", countOfElementsWrittenToFile=" + countOfElementsWrittenToFile +
                '}';
    }
}
