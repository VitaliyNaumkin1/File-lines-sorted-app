package ru.naumkin.java.test.sort.file.contents.statisticrecorder;

import org.apache.commons.lang3.math.NumberUtils;
import ru.naumkin.java.test.sort.file.contents.enums.TypeOfData;

import java.math.BigDecimal;

public class FullIntegerStatisticRecorder2 implements AbstractStatisticRecorder {
    private BigDecimal minElement;
    private BigDecimal maxElement;
    private BigDecimal sum;
    private BigDecimal average;
    private long countOfElementsWrittenToFile;
    private final TypeOfData typeOfData;
    boolean isFirstStatisticAdded = false;

    public FullIntegerStatisticRecorder2(TypeOfData typeOfData) {
        this.minElement = new BigDecimal(0);
        this.maxElement = new BigDecimal(0);
        this.average = new BigDecimal(0);
        this.sum = new BigDecimal(0);
        this.typeOfData = typeOfData;
    }

    @Override
    public void addToStatistic(String line) {
        increaseCounter();
        BigDecimal bigDecimal = new BigDecimal(NumberUtils.createBigInteger(line));
        if (!isFirstStatisticAdded) {
            minElement = bigDecimal;
            maxElement = bigDecimal;
            isFirstStatisticAdded = true;
        }
        max(bigDecimal);
        min(bigDecimal);
        sum(bigDecimal);
        average();
    }

    private void min(BigDecimal bigDecimal) {
        minElement = minElement.min(bigDecimal);
    }

    private void max(BigDecimal bigDecimal) {
        maxElement = maxElement.max(bigDecimal);
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
                ", max element =" + maxElement.toString() +
                ", count of elements =" + countOfElementsWrittenToFile +
                ", sum =" + sum.toString() +
                ", average =" + average.toString() + "]";
    }

    @Override
    public TypeOfData getTypeOfData() {
        return typeOfData;
    }

    @Override
    public void increaseCounter() {
        this.countOfElementsWrittenToFile++;
    }
}
