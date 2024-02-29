package ru.naumkin.java.test.sort.file.contents.statisticrecorder;

import org.apache.commons.lang3.math.NumberUtils;
import ru.naumkin.java.test.sort.file.contents.enums.TypeOfData;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FullIntegerStatisticRecorder implements AbstractStatisticRecorder {
    private BigDecimal minElement;
    private BigDecimal maxElement;
    private BigDecimal sum;
    private BigDecimal average;
    private int countOfElementsWrittenToFile;
    private final TypeOfData typeOfData;

    private boolean isFirstStatisticAdded = false;

    public FullIntegerStatisticRecorder(TypeOfData typeOfData) {
        this.minElement = new BigDecimal(0);
        this.maxElement = new BigDecimal(0);
        this.average = new BigDecimal(0);
        this.sum = new BigDecimal(0);
        this.typeOfData = typeOfData;
    }


    @Override
    public void addToStatistic(String line) {
        increaseCounter();
        BigDecimal bigDecimal = BigDecimal.valueOf(NumberUtils.createDouble(line));
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
            average = sum.divide(new BigDecimal(countOfElementsWrittenToFile), 2, RoundingMode.HALF_UP);
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "[File data type = " + typeOfData.toString() +
                ", min element =" + minElement.toString() +
                ", max element =" + maxElement +
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
