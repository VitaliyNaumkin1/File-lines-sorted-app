package ru.naumkin.java.test.sort.file.contents.statisticrecorder.test;

import org.apache.commons.lang3.math.NumberUtils;
import ru.naumkin.java.test.sort.file.contents.enums.TypeOfData;
import ru.naumkin.java.test.sort.file.contents.statisticrecorder.FullStatisticRecorder;

import java.math.BigDecimal;

public class FullFloatStatisticRecorder2 implements AbstractStatisticRecorder {
    private BigDecimal minElement;
    private BigDecimal maxElement;
    private BigDecimal sum;
    private BigDecimal average;
    private int countOfElementsWrittenToFile;
    private final TypeOfData typeOfData;

    private String currentLine;

    public FullFloatStatisticRecorder2(TypeOfData typeOfData) {
        this.minElement = new BigDecimal(0);
        this.maxElement = new BigDecimal(0);
        this.average = new BigDecimal(0);
        this.sum = new BigDecimal(0);
        this.typeOfData = typeOfData;
    }


    @Override
    public void addToStatistic(TypeOfData typeOfData, String line) {
        increaseCounter();
        BigDecimal bigDecimal = BigDecimal.valueOf(NumberUtils.createDouble(line));
        max(bigDecimal);
        min(bigDecimal);
        sum(bigDecimal);
        average();
    }

    private void min(BigDecimal bigDecimal) {
        minElement = minElement.min(bigDecimal);
    }

    private void max(BigDecimal bigDecimal) {
        maxElement = maxElement.min(bigDecimal);
    }


    private void sum(BigDecimal bigDecimal) {
        sum = sum.add(bigDecimal);
    }

    private void average() {
        try {
            average = sum.divide(new BigDecimal(countOfElementsWrittenToFile));
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "[File data type = " + typeOfData +
                ", min element =" + minElement.toString() +
                ", max element =" + maxElement +
                ", count of elements =" + countOfElementsWrittenToFile +
                ", sum =" + sum +
                ", average =" + average + "]";
    }

    @Override
    public TypeOfData getTypeOfData() {
        return typeOfData;
    }

    @Override
    public void increaseCounter() {
        this.countOfElementsWrittenToFile++;
    }

    @Override
    public void setString(String string) {
        this.currentLine = string;
    }
}
