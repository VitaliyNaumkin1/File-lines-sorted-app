package ru.naumkin.java.test.sort.file.contents;


import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.naumkin.java.test.sort.file.contents.enums.StatisticMode;
import ru.naumkin.java.test.sort.file.contents.enums.TypeOfLine;
import ru.naumkin.java.test.sort.file.contents.statisticsrecorder.done.FullDoubleNumberStatisticRecorder;
import ru.naumkin.java.test.sort.file.contents.statisticsrecorder.done.FullIntegerNumberStatisticRecorder;
import ru.naumkin.java.test.sort.file.contents.statisticsrecorder.done.FullStringsStatisticRecorder;
import ru.naumkin.java.test.sort.file.contents.statisticsrecorder.done.StatisticsRecorder;
import ru.naumkin.java.test.sort.file.contents.statisticsrecorder.test.Counter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static ru.naumkin.java.test.sort.file.contents.FileLineSorterApp.DEFAULT_DIRECTORY;

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

    private StatisticMode statisticMode;
    private Counter counter = new Counter(StatisticMode.FULL);

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
        this.directoryForSortedFiles = userCommandHandler.getDirectoryForSortedFiles(); // Надо убрать т.к мы можем просто брать директорию из userCommandHandlera
        run();
    }

    /**
     * 1)Статистику собирать. Либо класс отдельный который собирает статистику , либо просто переменные.
     * 2)-s , -
     */

//    public void test(){
//        userCommandHandler.getDirectoryForSortedFiles();
//        userCommandHandler.getOptions().
//    }
    private void run() {
        readLinesFromFiles____SORTING();
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


    private void readLinesFromFiles____SORTING() {
        for (File file : filesToSort) {
            Path path = directoryForSortedFiles.resolve(file.toPath());
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    determineTypeOfString(line);
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    private void sortingLinesFromInputFiles() {

        for (File file : filesToSort) {

        }

    }

    public void test(){

    }
    private String readNextLineFromFile(File file, long position) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            String line = new String(randomAccessFile.readLine().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //??????? СЮда в метод нужно видимо передавтаь либо весь список либо один элемент
            return line;
        } catch (IOException e) {
            logger.error(e);
        }
        return null;
    }

    private void determineTypeOfString(String line) {
        if (NumberUtils.isCreatable(line)) {
            if (Pattern.matches("^-?\\d+$", line)) {
                writeLineIntoFile(line, TypeOfLine.INTEGER);
            } else {
                writeLineIntoFile(line, TypeOfLine.FLOATS);
            }
        } else {
            writeLineIntoFile(line, TypeOfLine.STRING);
        }
    }

    /**
     * походу надо добавить то что бы создавались дирректории для файлов еще , надо посмотреть , как это сделать.
     */

    public void writeLineIntoFile(String line, TypeOfLine typeOfLine) {  ///Тут надо походу добавить отдельный метод который создает имя что бы каждый раз не создавать.для каждого
        Path pathToCreatableFile = null;
        StringBuilder namePrefix = new StringBuilder(userCommandHandler.getNamePrefixForSortedFiles());

        if (typeOfLine.equals(TypeOfLine.STRING)) {
            String fileName = namePrefix.append("strings.txt").toString();
            pathToCreatableFile = directoryForSortedFiles.resolve(fileName);
            createDirectoriesForOutputFiles(pathToCreatableFile);           // зачем несколько раз проверять , можно один раз вызывать этот метдо и всё и больше не вызывать.А файл сам создатся при записи
        }
        if (typeOfLine.equals(TypeOfLine.INTEGER)) {
            String fileName = namePrefix.append("integers.txt").toString();
            pathToCreatableFile = directoryForSortedFiles.resolve(fileName);
            createDirectoriesForOutputFiles(pathToCreatableFile);
        }
        if (typeOfLine.equals(TypeOfLine.FLOATS)) {
            String fileName = namePrefix.append("floats.txt").toString();
            pathToCreatableFile = directoryForSortedFiles.resolve(fileName);
            createDirectoriesForOutputFiles(pathToCreatableFile);
        }

        //и вот это убрать т а в createDirectoriesForOutputFiles добавить создание дефолтной дирректориюю. А убрать т..к потом можно будет что эта переменная будет точно создаани
        if (pathToCreatableFile == null) {
            try {
                throw new FileNotFoundException();
            } catch (FileNotFoundException e) {
                logger.error(e);
                logger.info("не удалось создать директорию для файлов с результатами сортировки. Была выбрана стандартная директория: " + DEFAULT_DIRECTORY.toAbsolutePath());
            }
            pathToCreatableFile = DEFAULT_DIRECTORY;
        }

        try (FileWriter writer = new FileWriter(pathToCreatableFile.toString(), true)) {
            writer.write(line);
            writer.append("\n");
        } catch (IOException e) {
            logger.error(e);
        }

    }

    /*
    EXCEPTION
     */
    public void createDirectoriesForOutputFiles(Path pathToCreatableFile) {
        if (Files.exists(pathToCreatableFile.getParent())) {
            return;
        }

        try {
            Files.createDirectories(pathToCreatableFile.getParent());
        } catch (IOException e) {
            logger.error(e);
            logger.info("не удалось создать директорию для файлов с результатами сортировки. Была выбрана стандартная директория: " + DEFAULT_DIRECTORY.toAbsolutePath());
            directoryForSortedFiles = DEFAULT_DIRECTORY;
        }
    }

    /**
     * думаю тут надо прикрутить еще BigDecimal еще.
     */
//    private void defineTypeOfLine(String line) {
//        if (NumberUtils.isCreatable(line)) {
//            if (Pattern.matches("^-?\\d+$", line)) {    //целое число   /////////////--- может более лучшее есть рег выражение
////                listWithSortedLongs.add(Long.parseLong(line));
//                listWithSortedLongs.add(NumberUtils.createLong(line));
//            } else {
//                listWithSortedDoubles.add(NumberUtils.createDouble(line));
//            }
//        } else {
//            listWithSortedStrings.add(line);
//        }
//    }

//    public void writeLineIntoFile(String line, TypeOfLine type) {
//        try (FileWriter writer = new FileWriter(path.toString(), true)) {
//
//        }
//        if (type.equals(TypeOfLine.STRING)) {
//
//        }
//    }
    private static void WORK_FILE_WRITER() throws IOException {
        Path path = Path.of("input files\\out1.txt");
        try (FileWriter writer = new FileWriter(path.toString(), true)) {
            writer.write("hello java epta nu davay");
            writer.append("\n");
            writer.write("YOUUEEEEEE");
        }
    }

}



