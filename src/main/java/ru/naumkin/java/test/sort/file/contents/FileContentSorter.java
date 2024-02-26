package ru.naumkin.java.test.sort.file.contents;


import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.naumkin.java.test.sort.file.contents.statisticsrecorder.done.FullDoubleNumberStatisticRecorder;
import ru.naumkin.java.test.sort.file.contents.statisticsrecorder.done.FullIntegerNumberStatisticRecorder;
import ru.naumkin.java.test.sort.file.contents.statisticsrecorder.done.FullStringsStatisticRecorder;
import ru.naumkin.java.test.sort.file.contents.statisticsrecorder.done.StatisticsRecorder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FileContentSorter {
    private final Logger logger = LogManager.getLogger(FileContentSorter.class.getName());

    private List<String> listWithSortedStrings;
    private List<Long> listWithSortedLongs;
    private List<Double> listWithSortedDoubles;
    private List<File> filesToSort;

    private List<String> rawStringsFromFiles;

    private UserCommandHandler userCommandHandler; ///////// ?????? убрать?
    private Path directoryForSortedFiles;

    private Map<String, StatisticsRecorder> counters = new HashMap<>();


    /**
     * ВРЕМЕННО ПУСТЬ БУДЕТ ТУТ
     * А если создать один класс Counter который содержит в себе эти каунтеры;
     */

    StatisticsRecorder stringsStatisticRecorder = new FullStringsStatisticRecorder();
    StatisticsRecorder integerStatisticRecorder = new FullIntegerNumberStatisticRecorder();
    StatisticsRecorder doubleStatisticRecorder = new FullDoubleNumberStatisticRecorder();


    public FileContentSorter(UserCommandHandler userCommandHandler) {
        this.listWithSortedStrings = new ArrayList<>();
        this.listWithSortedLongs = new ArrayList<>();
        this.listWithSortedDoubles = new ArrayList<>();
        this.userCommandHandler = userCommandHandler;
        this.filesToSort = userCommandHandler.getInputFiles();
        this.directoryForSortedFiles = userCommandHandler.getDirectoryForSortedFiles();
        run();
    }

    /**
     * 1)Статистику собирать. Либо класс отдельный который собирает статистику , либо просто переменные.
     * 2)-s , -
     */


    private void run() {
        readLinesFromFiles();
        System.out.println("__________________________");
        System.out.println("ЦЕЛЫЕ:");
        for (Long l : listWithSortedLongs) {
            System.out.println(l);
        }
        System.out.println("__________________________");
        System.out.println("ВЕЩЕСТВЕННЫЕ: ");
        for (Double l : listWithSortedDoubles) {
            System.out.println(l);
        }
        System.out.println("__________________________");
        System.out.println("Строки:");
        for (String l : listWithSortedStrings) {
            System.out.println(l);
        }
    }


    private void readLinesFromFiles() {
        for (File file : filesToSort) {
            Path path = directoryForSortedFiles.resolve(file.toPath());
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sortLineFromFile(line);
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    private void sortLineFromFile(String line) {
        Pattern DOUBLE_PATTERN = Pattern.compile("[-+](Infinity|NaN|(0(x[\\da-fA-F]+|b[01]+)|(\\d+(_\\d+)*)(\\.(\\d+(_\\d+)*))?)([eE][-+]?\\d+)?[fFdD]?)");

//        if (NumberUtils.isCreatable(line)) {
//            System.out.println("line = " + line);
//            listWithSortedDoubles.add(NumberUtils.createDouble(line));
//        }

        if (NumberUtils.isCreatable(line)) {
            if (Pattern.matches("^-?\\d+$", line)) {    //целое число
                listWithSortedLongs.add(Long.parseLong(line));
            } else {
                listWithSortedDoubles.add(NumberUtils.createDouble(line));
            }
        } else {
            listWithSortedStrings.add(line);
        }


//        if (Pattern.matches("^-?\\d+$", line)) {    //целое число
//            listWithSortedLongs.add(Long.parseLong(line));
//        } else if (Pattern.matches("^-?\\d+(\\.\\d+)?$", line)) {   //вещественное число
//            listWithSortedDoubles.add(Double.parseDouble(line));
//        } else {
//            listWithSortedStrings.add(line);
//        }
//    }

//        Pattern DOUBLE_PATTERN = Pattern.compile("[-+](Infinity|NaN|(0(x[\\da-fA-F]+|b[01]+)|(\\d+(_\\d+)*)(\\.(\\d+(_\\d+)*))?)([eE][-+]?\\d+)?[fFdD]?)");

        //         ^[0-9]+([\\,\\.][0-9]+)?$    ////     "^-?\\d+(\\.\\d+)?$"

//    public void readLinesFromFile() {
//        try {
//            Path path = Paths.get("input files\\").resolve("in1.txt");
//            String content = Files.readString(path);
//            rawStringsFromFiles = Arrays.asList(content.split("[\\r\\n]+"));
//            try (BufferedReader reader = Files.newBufferedReader(path)) {
//                reader.readLine();
//            }catch (IOException e){
//
//            }
//
//            System.out.println(content);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    }
}



