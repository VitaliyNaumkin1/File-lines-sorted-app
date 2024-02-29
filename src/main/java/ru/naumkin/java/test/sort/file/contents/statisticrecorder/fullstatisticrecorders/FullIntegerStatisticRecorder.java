package ru.naumkin.java.test.sort.file.contents.statisticrecorder.fullstatisticrecorders;

import org.apache.commons.lang3.math.NumberUtils;
import ru.naumkin.java.test.sort.file.contents.enums.TypeOfData;
import ru.naumkin.java.test.sort.file.contents.statisticrecorder.AbstractStatisticRecorder;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FullIntegerStatisticRecorder extends FullNumbersStatisticRecorder implements AbstractStatisticRecorder {
    public FullIntegerStatisticRecorder(TypeOfData typeOfData) {
        super(typeOfData);
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

    private void average() {
        try {
            average = sum.divide(new BigDecimal(countOfElementsWrittenToFile), 2, RoundingMode.HALF_UP);
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
    }

}
