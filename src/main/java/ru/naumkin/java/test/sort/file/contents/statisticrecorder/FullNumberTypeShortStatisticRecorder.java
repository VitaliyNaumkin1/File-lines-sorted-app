package ru.naumkin.java.test.sort.file.contents.statisticrecorder;

import org.apache.commons.lang3.math.NumberUtils;
import ru.naumkin.java.test.sort.file.contents.enums.TypeOfData;

public class FullNumberTypeShortStatisticRecorder extends FullStatisticRecorder {
    private Number minElement;
    private Number maxElement;
    private Number sum;
    private Number average;

    private int countOfElementsWrittenToFile;
    private final TypeOfData typeOfData;

    private String currentLine;

    public FullNumberTypeShortStatisticRecorder(TypeOfData typeOfData) {
        this.minElement = 0;
        this.maxElement = 0;
        this.average = 0;
        this.sum = 0;
        this.typeOfData = typeOfData;
    }


    public void addStatistic() {
        increaseCounter();
        Number number;
        if (typeOfData.equals(TypeOfData.INTEGER)) {

            number = NumberUtils.createInteger(currentLine);
        } else {
            number = NumberUtils.createFloat(currentLine);
        }
        max(number);
        min(number);
        sum(number);
        average();
    }

    private void min(Number number) {
        if (typeOfData.equals(TypeOfData.INTEGER)) {
            minElement = Integer.min((Integer) number, (Integer) minElement);
        }
        if (typeOfData.equals(TypeOfData.FLOAT)) {
            minElement = Float.min((Float) number, (Float) minElement);
        }
    }

    private void max(Number number) {
        if (typeOfData.equals(TypeOfData.INTEGER)) {
            maxElement = Integer.max((Integer) number, (Integer) maxElement);
        }
        if (typeOfData.equals(TypeOfData.FLOAT)) {
            maxElement = Float.max((Float) number, (Float) maxElement);
        }
    }


    private void sum(Number number) {
        if (typeOfData.equals(TypeOfData.INTEGER)) {
            sum = Integer.sum((Integer) sum, (Integer) number);
        }
        if (typeOfData.equals(TypeOfData.FLOAT)) {
            minElement = Float.sum(number.floatValue(), maxElement.floatValue());
        }
    }

    private void average() {
        average = (Integer) sum / (Integer) countOfElementsWrittenToFile;
    }

    @Override
    public String toString() {
        return "[File data type = " + typeOfData +
                ", min element =" + minElement +
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
    protected void increaseCounter() {
        this.countOfElementsWrittenToFile++;
    }

    @Override
    public void setString(String string) {
        this.currentLine = string;
    }
}
