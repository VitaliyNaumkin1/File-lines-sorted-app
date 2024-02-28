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
import java.util.*;
import java.util.regex.Pattern;

import static ru.naumkin.java.test.sort.file.contents.FileLineSorterApp.DEFAULT_DIRECTORY;

public class FileContentSorter {
    private final Logger logger = LogManager.getLogger(FileContentSorter.class.getName());

    private final List<File> filesToSort;

    private final UserCommandHandler userCommandHandler; ///////// ?????? убрать?
    private Path directoryForSortedFiles;

    private final Map<String, StatisticsRecorder> counters = new HashMap<>();

    private StatisticMode statisticMode;
    private final Counter counter = new Counter(StatisticMode.FULL);
    private final Map<File, Long> fileAndPointerPosition;

    /**
     * ВРЕМЕННО ПУСТЬ БУДЕТ ТУТ
     * А если создать один класс Counter который содержит в себе эти каунтеры;
     */

    StatisticsRecorder stringsStatisticRecorder = new FullStringsStatisticRecorder();
    StatisticsRecorder integerStatisticRecorder = new FullIntegerNumberStatisticRecorder();
    StatisticsRecorder doubleStatisticRecorder = new FullDoubleNumberStatisticRecorder();


    public FileContentSorter(UserCommandHandler userCommandHandler) {
        this.fileAndPointerPosition = new TreeMap<>();
        this.userCommandHandler = userCommandHandler;
        this.filesToSort = userCommandHandler.getInputFiles();
        this.directoryForSortedFiles = userCommandHandler.getDirectoryForSortedFiles(); // Надо убрать т.к мы можем просто брать директорию из userCommandHandlera
        run();
    }

    /**
     * 1)Статистику собирать. Либо класс отдельный который собирает статистику , либо просто переменные.
     * 2)-s , -
     */

    private void run() {
        sortingLinesFromInputFiles();
    }


    private void readLinesFromFiles____SORTING() {
        for (File file : filesToSort) {
            Path path = directoryForSortedFiles.resolve(file.toPath());
            try (BufferedReader reader = Files.newBufferedReader(path)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    determineTypeOfLine(line);
                }
            } catch (IOException e) {
                logger.error(e);
            }
        }
    }

    private void sortingLinesFromInputFiles() {
        //описание сделать толковое что это такое Long
        //Для каждого файла в значении хранится положение указателя с какой строки начать дальше считывать RandomAcsessFile
        fillMapWithPointerPositions();
        int count = 0;
        while (true) {
            if (fileAndPointerPosition.isEmpty()) {
                break;
            }
            for (Map.Entry<File, Long> entry : fileAndPointerPosition.entrySet()) {
                File file = entry.getKey();
                long pointerPosition = entry.getValue();
                readNextLineFromFile(file, pointerPosition);
            }
        }
    }

    //название изменить
    public void setPositionOfPointerToFile() {

    }


    private void readNextLineFromFile(File file, long pointerPositionInFile) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file.toPath().toString(), "wr")) { //FileNotFoundException появляется
            randomAccessFile.seek(pointerPositionInFile);
            String line;
            if ((line = randomAccessFile.readLine()) == null) {
                fileAndPointerPosition.remove(file);
                return;
            }
//             line = new String(randomAccessFile.readLine().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            String lineUtf8 = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //??????? СЮда в метод нужно видимо передавтаь либо весь список либо один элемент
            long pointerPosition = randomAccessFile.getFilePointer();
            setCurrentPointerPositionForFile(file, pointerPosition);
            determineTypeOfLine(lineUtf8);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
        }
    }


    private void determineTypeOfLine(String line) {
        if (NumberUtils.isCreatable(line)) {
            if (Pattern.matches("^-?\\d+$", line)) {
                writeLineInToSortedFile(line, TypeOfLine.INTEGER);
            } else {
                writeLineInToSortedFile(line, TypeOfLine.FLOATS);
            }
        } else {
            writeLineInToSortedFile(line, TypeOfLine.STRING);
        }
    }


    /**
     * походу надо добавить то что бы создавались дирректории для файлов еще , надо посмотреть , как это сделать.
     */

    public void writeLineInToSortedFile(String line, TypeOfLine typeOfLine) {  ///Тут надо походу добавить отдельный метод который создает имя что бы каждый раз не создавать.для каждого
        Path pathToCreatableFile = null;
        StringBuilder namePrefix = new StringBuilder(userCommandHandler.getNamePrefixForSortedFiles());
        createDirectoriesForOutputFiles(directoryForSortedFiles);
        if (typeOfLine.equals(TypeOfLine.STRING)) {
            String fileName = namePrefix.append("strings.txt").toString();
            pathToCreatableFile = directoryForSortedFiles.resolve(fileName);
//            createDirectoriesForOutputFiles(pathToCreatableFile);           // зачем несколько раз проверять , можно один раз вызывать этот метдо и всё и больше не вызывать.А файл сам создатся при записи
        }
        if (typeOfLine.equals(TypeOfLine.INTEGER)) {
            String fileName = namePrefix.append("integers.txt").toString();
            pathToCreatableFile = directoryForSortedFiles.resolve(fileName);  ////может тоже так же вынести
//            createDirectoriesForOutputFiles(pathToCreatableFile);
        }
        if (typeOfLine.equals(TypeOfLine.FLOATS)) {
            String fileName = namePrefix.append("floats.txt").toString();
            pathToCreatableFile = directoryForSortedFiles.resolve(fileName);
//            createDirectoriesForOutputFiles(pathToCreatableFile);
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

//        try (FileWriter writer = new FileWriter(pathToCreatableFile.toString(), true)) {  // надо как то выносить эту штуку
//            writer.write(line);
//            writer.append("\n");
//        } catch (IOException e) {
//            logger.error(e);
//        }

        try (FileWriter writer = new FileWriter(pathToCreatableFile.toString(), true)) {  // надо как то выносить эту штуку
            writer.write(line);
            writer.append("\n");
        } catch (IOException e) {
            logger.error(e);
        }

    }

    /*
    EXCEPTION
     */
    public void createDirectoriesForOutputFiles(Path dirForSortedFiles) {

//        if (Files.exists(dirForSortedFiles.getParent())) {
//            return;
//        }
        if (Files.exists(dirForSortedFiles)) {
            return;
        }

        try {
            Files.createDirectories(dirForSortedFiles.getParent());
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

    private void fillMapWithPointerPositions() {
        for (int i = filesToSort.size() - 1; i >= 0; i--) {
            fileAndPointerPosition.put(filesToSort.get(i), 0L);
        }
    }

    private void setCurrentPointerPositionForFile(File file, long pointerPosition) {
        fileAndPointerPosition.put(file, pointerPosition);///
    }

    public void deletePreviousFilesWithResults() { // можно взять и передавть имя файла в другом методе
        Path directory = directoryForSortedFiles;
        StringBuilder namePrefix = new StringBuilder(userCommandHandler.getNamePrefixForSortedFiles());
        Path filePathSortedIntegers = directory.resolve((namePrefix.append("integers.txt")).toString());
        Path filePathSortedFloats = directory.resolve((namePrefix.append("floats.txt")).toString());
        Path filePathSortedStrings = directory.resolve((namePrefix.append("strings.txt")).toString());
        if (Files.exists(filePathSortedIntegers)) {
            try {
                Files.delete(filePathSortedIntegers);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (Files.exists(filePathSortedFloats)) {
            try {
                Files.delete(filePathSortedFloats);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (Files.exists(filePathSortedStrings)) {
            try {
                Files.delete(filePathSortedStrings);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void test2() {
        if (userCommandHandler.getOptions().contains("-a")) {

        }
    }

}



