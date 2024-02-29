package ru.naumkin.java.test.sort.file.contents.statisticrecorder.fullstatisticrecorders;

import ru.naumkin.java.test.sort.file.contents.enums.TypeOfData;
import java.math.BigDecimal;

public class FullNumbersStatisticRecorder {
    protected BigDecimal minElement;
    protected BigDecimal maxElement;
    protected BigDecimal sum;
    protected BigDecimal average;
    protected int countOfElementsWrittenToFile;
    protected final TypeOfData typeOfData;

    protected boolean isFirstStatisticAdded = false;

    public FullNumbersStatisticRecorder(TypeOfData typeOfData) {
        this.minElement = new BigDecimal(0);
        this.maxElement = new BigDecimal(0);
        this.average = new BigDecimal(0);
        this.sum = new BigDecimal(0);
        this.typeOfData = typeOfData;
    }
    protected void min(BigDecimal bigDecimal) {
        minElement = minElement.min(bigDecimal);
    }

    protected void max(BigDecimal bigDecimal) {
        maxElement = maxElement.max(bigDecimal);
    }

    protected void sum(BigDecimal bigDecimal) {
        sum = sum.add(bigDecimal);
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

    public TypeOfData getTypeOfData() {
        return typeOfData;
    }

    public void increaseCounter() {
        this.countOfElementsWrittenToFile++;
    }
}
