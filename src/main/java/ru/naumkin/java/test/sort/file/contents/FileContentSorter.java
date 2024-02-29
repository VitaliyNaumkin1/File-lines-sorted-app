package ru.naumkin.java.test.sort.file.contents;


import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.naumkin.java.test.sort.file.contents.enums.StatisticMode;
import ru.naumkin.java.test.sort.file.contents.enums.TypeOfData;
import ru.naumkin.java.test.sort.file.contents.statisticrecorder.FullNumberTypeShortStatisticRecorder;
import ru.naumkin.java.test.sort.file.contents.statisticrecorder.FullStatisticRecorder;
import ru.naumkin.java.test.sort.file.contents.statisticrecorder.FullStringsStatisticRecorder;
import ru.naumkin.java.test.sort.file.contents.statisticrecorder.ShortStatisticRecorder;

//import ru.naumkin.java.test.sort.file.contents.statisticsrecorder.test.Counter;

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

    private final UserCommandHandler userCommandHandler;
    private Path directoryForSortedFiles;
    private final String namePrefix;

    private final StatisticMode statisticMode;
    private final Map<File, Long> fileAndPointerPosition;

    private final List<ShortStatisticRecorder> shortStatisticRecorderList;
    private final List<FullStatisticRecorder> fullStatisticRecorderList;

    public List<ShortStatisticRecorder> getShortStatisticRecorderList() {
        return shortStatisticRecorderList;
    }

    public List<FullStatisticRecorder> getFullStatisticRecorderList() {
        return fullStatisticRecorderList;
    }

    public StatisticMode getStatisticMode() {
        return statisticMode;
    }

    public FileContentSorter(UserCommandHandler userCommandHandler) {
        this.fileAndPointerPosition = new TreeMap<>();
        this.userCommandHandler = userCommandHandler;
        this.filesToSort = userCommandHandler.getInputFiles();
        this.directoryForSortedFiles = userCommandHandler.getDirectoryForSortedFiles(); // Надо убрать т.к мы можем просто брать директорию из userCommandHandlera
        this.namePrefix = userCommandHandler.getNamePrefixForSortedFiles();
        this.statisticMode = userCommandHandler.getStatisticMode();
        this.shortStatisticRecorderList = new ArrayList<>();
        this.fullStatisticRecorderList = new ArrayList<>();
        run();
    }

    private void run() {
        createStatisticsRecorders();
        sortingLinesFromInputFiles();
    }

    public void createStatisticsRecorders() {
        if (statisticMode.equals(StatisticMode.SHORT)) {
            shortStatisticRecorderList.add(new ShortStatisticRecorder(TypeOfData.STRING));
            shortStatisticRecorderList.add(new ShortStatisticRecorder(TypeOfData.FLOAT));
            shortStatisticRecorderList.add(new ShortStatisticRecorder(TypeOfData.INTEGER));
        } else {
            fullStatisticRecorderList.add(new FullStringsStatisticRecorder(TypeOfData.STRING));
            fullStatisticRecorderList.add(new FullNumberTypeShortStatisticRecorder(TypeOfData.INTEGER));
            fullStatisticRecorderList.add(new FullNumberTypeShortStatisticRecorder(TypeOfData.FLOAT));
        }
    }

    private void sortingLinesFromInputFiles() {
        //Для каждого файла в значении хранится положение указателя с какой строки начать дальше будет считывать RandomAccessFile
        createDirectoriesForOutputFiles(directoryForSortedFiles);
        boolean isOption_A_Exist = userCommandHandler.getOptions().contains("-a");

        if (!isOption_A_Exist) {
            deletePreviousFilesWithResults();
        }
        for (File file : filesToSort) {
            fileAndPointerPosition.put(file, 0L);
        }
        while (!fileAndPointerPosition.isEmpty()) {
            for (Map.Entry<File, Long> entry : fileAndPointerPosition.entrySet()) {
                File file = entry.getKey();
                long pointerPosition = entry.getValue();
                readNextLineFromFile(file, pointerPosition);
            }
        }
    }

    private void readNextLineFromFile(File file, long pointerPositionInFile) {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) { //FileNotFoundException появляется
            randomAccessFile.seek(pointerPositionInFile);
            String line;
            if ((line = randomAccessFile.readLine()) == null) {
                fileAndPointerPosition.remove(file);
                return;
            }
            String lineUtf8 = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            long pointerPosition = randomAccessFile.getFilePointer();
            fileAndPointerPosition.put(file, pointerPosition);
            determineTypeOfLine(lineUtf8);
        } catch (IOException e) {
            e.printStackTrace();
            determineTypeOfLine("");
        }
    }

    private void determineTypeOfLine(String line) {
        if (NumberUtils.isCreatable(line)) {
            if (Pattern.matches("^-?\\d+$", line)) {
                addToStatistic(TypeOfData.INTEGER, line);
                writeLineInToSortedFile(TypeOfData.INTEGER, line);
            } else {
                addToStatistic(TypeOfData.FLOAT, line);
                writeLineInToSortedFile(TypeOfData.FLOAT, line);
            }
        } else {
            addToStatistic(TypeOfData.STRING, line);
            writeLineInToSortedFile(TypeOfData.STRING, line);
        }
    }

    public void addToStatistic(TypeOfData typeOfData, String line) {
        if (statisticMode.equals(StatisticMode.SHORT)) {
            if (typeOfData.equals(TypeOfData.STRING)) {
                for (ShortStatisticRecorder recorder : shortStatisticRecorderList) {
                    if (recorder.getTypeOfData().equals(TypeOfData.STRING)) {
                        recorder.increaseCounter();
                        return;
                    }
                }
            }
            if (typeOfData.equals(TypeOfData.INTEGER)) {
                for (ShortStatisticRecorder recorder : shortStatisticRecorderList) {
                    if (recorder.getTypeOfData().equals(TypeOfData.INTEGER)) {
                        recorder.increaseCounter();
                        return;
                    }
                }
            }
            if (typeOfData.equals(TypeOfData.FLOAT)) {
                for (ShortStatisticRecorder recorder : shortStatisticRecorderList) {
                    if (recorder.getTypeOfData().equals(TypeOfData.FLOAT)) {
                        recorder.increaseCounter();
                        return;
                    }
                }
            }
        } else {
            if (typeOfData.equals(TypeOfData.STRING)) {
                for (FullStatisticRecorder recorder : fullStatisticRecorderList) {
                    if (recorder.getTypeOfData().equals(TypeOfData.STRING)) {
                        recorder.setString(line);
                        recorder.addStatistic();
                        return;
                    }
                }
            }
            if (typeOfData.equals(TypeOfData.INTEGER)) {
                for (FullStatisticRecorder recorder : fullStatisticRecorderList) {
                    try {
                        if (recorder.getTypeOfData().equals(TypeOfData.INTEGER)) {
                            recorder.setString(line);
                            recorder.addStatistic();
                            return;
                        }
                    } catch (RuntimeException e) {
                        e.getMessage();
                    }
                }
            }
            if (typeOfData.equals(TypeOfData.FLOAT)) {
                try {
                    for (FullStatisticRecorder recorder : fullStatisticRecorderList) {
                        if (recorder.getTypeOfData().equals(TypeOfData.FLOAT)) {
                            recorder.setString(line);
                            recorder.addStatistic();
                            return;
                        }
                    }
                } catch (RuntimeException e) {
                    e.getMessage();
                }
            }
        }
    }

    /**
     * походу надо добавить то что бы создавались дирректории для файлов еще , надо посмотреть , как это сделать.
     */

    public void writeLineInToSortedFile(TypeOfData typeOfData, String line) {  ///Тут надо походу добавить отдельный метод который создает имя что бы каждый раз не создавать.для каждого
        Path pathToCreatableFile = null;
        StringBuilder namePrefix = new StringBuilder(userCommandHandler.getNamePrefixForSortedFiles());

        if (typeOfData.equals(TypeOfData.STRING)) {
            String fileName = namePrefix.append("strings.txt").toString();
            pathToCreatableFile = directoryForSortedFiles.resolve(fileName);
        }
        if (typeOfData.equals(TypeOfData.INTEGER)) {
            String fileName = namePrefix.append("integers.txt").toString();
            pathToCreatableFile = directoryForSortedFiles.resolve(fileName);  ////может тоже так же вынести
        }
        if (typeOfData.equals(TypeOfData.FLOAT)) {
            String fileName = namePrefix.append("floats.txt").toString();
            pathToCreatableFile = directoryForSortedFiles.resolve(fileName);
        }

        try (FileWriter writer = new FileWriter(pathToCreatableFile.toString(), true)) {
            writer.write(line + "\n");
        } catch (IOException e) {
            logger.error(e);
        }
    }

    /*
    EXCEPTION
     */
    public void createDirectoriesForOutputFiles(Path dirForSortedFiles) {
        try {
            if (Files.exists(dirForSortedFiles)) {
                return;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.printf("не удалось использовать данную директорию %s. Была выбрана стандартная директория: %s.\n", dirForSortedFiles, DEFAULT_DIRECTORY.toAbsolutePath());
            directoryForSortedFiles = DEFAULT_DIRECTORY;
        }
        try {
            Files.createDirectories(dirForSortedFiles.getParent());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("не удалось создать директорию для файлов с результатами сортировки. Была выбрана стандартная директория: %s\n", DEFAULT_DIRECTORY.toAbsolutePath());
            directoryForSortedFiles = DEFAULT_DIRECTORY;
        }
    }

    private void deletePreviousFilesWithResults() {
        Path directory = directoryForSortedFiles;
        Path filePathSortedIntegers = directory.resolve(namePrefix + "integers.txt");
        Path filePathSortedFloats = directory.resolve(namePrefix + "floats.txt");
        Path filePathSortedStrings = directory.resolve(namePrefix + "strings.txt");

        if (Files.exists(filePathSortedIntegers)) {
            try {
                Files.delete(filePathSortedIntegers);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Files.exists(filePathSortedFloats)) {
            try {
                Files.delete(filePathSortedFloats);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (Files.exists(filePathSortedStrings)) {
            try {
                Files.delete(filePathSortedStrings);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}



