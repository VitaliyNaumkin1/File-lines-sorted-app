package ru.naumkin.java.test.sort.file.contents;


import org.apache.commons.lang3.math.NumberUtils;
import ru.naumkin.java.test.sort.file.contents.enums.StatisticMode;
import ru.naumkin.java.test.sort.file.contents.enums.TypeOfData;
import ru.naumkin.java.test.sort.file.contents.statisticrecorder.*;
import ru.naumkin.java.test.sort.file.contents.statisticrecorder.fullstatisticrecorders.FullFloatStatisticRecorder;
import ru.naumkin.java.test.sort.file.contents.statisticrecorder.fullstatisticrecorders.FullIntegerStatisticRecorder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

import static ru.naumkin.java.test.sort.file.contents.FileLineSorterApp.DEFAULT_DIRECTORY;

public class FileContentSorter {
    private final List<File> filesToSort;

    private final UserCommandHandler userCommandHandler;
    private Path directoryForSortedFiles;
    private final String namePrefix;

    private final StatisticMode statisticMode;
    private final Map<File, Long> fileAndPointerPosition;

    private final List<StatisticRecorder> statisticRecorderList;

    public List<StatisticRecorder> getAbstractStatisticRecorderList() {
        return statisticRecorderList;
    }

    public FileContentSorter(UserCommandHandler userCommandHandler) {
        this.fileAndPointerPosition = new TreeMap<>();
        this.userCommandHandler = userCommandHandler;
        this.filesToSort = userCommandHandler.getInputFiles();
        this.directoryForSortedFiles = userCommandHandler.getDirectoryForSortedFiles();
        this.namePrefix = userCommandHandler.getNamePrefixForSortedFiles();
        this.statisticMode = userCommandHandler.getStatisticMode();
        this.statisticRecorderList = new ArrayList<>();
        run();
    }

    private void run() {
        createStatisticsRecorders();
        sortingLinesFromInputFiles();
    }


    public void createStatisticsRecorders() {
        if (statisticMode.equals(StatisticMode.SHORT)) {
            statisticRecorderList.add(new ShortStatisticRecorder(TypeOfData.STRING));
            statisticRecorderList.add(new ShortStatisticRecorder(TypeOfData.FLOAT));
            statisticRecorderList.add(new ShortStatisticRecorder(TypeOfData.INTEGER));
        } else {
            statisticRecorderList.add(new FullStringsStatisticRecorder(TypeOfData.STRING));
            statisticRecorderList.add(new FullIntegerStatisticRecorder(TypeOfData.INTEGER));
            statisticRecorderList.add(new FullFloatStatisticRecorder(TypeOfData.FLOAT));
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
                addToStatistic2(TypeOfData.INTEGER, line);
                writeLineInToSortedFile(TypeOfData.INTEGER, line);
            } else {
                addToStatistic2(TypeOfData.FLOAT, line);
                writeLineInToSortedFile(TypeOfData.FLOAT, line);
            }
        } else {
            addToStatistic2(TypeOfData.STRING, line);
            writeLineInToSortedFile(TypeOfData.STRING, line);
        }
    }

    public void addToStatistic2(TypeOfData typeOfData, String line) {
        for (StatisticRecorder recorder : statisticRecorderList) {
            if (recorder.getTypeOfData().equals(typeOfData)) {
                recorder.addToStatistic(line);
            }
        }
    }

    public void writeLineInToSortedFile(TypeOfData typeOfData, String line) {
        Path pathToCreatableFile = null;
        StringBuilder namePrefix = new StringBuilder(userCommandHandler.getNamePrefixForSortedFiles());

        if (typeOfData.equals(TypeOfData.STRING)) {
            String fileName = namePrefix.append("strings.txt").toString();
            pathToCreatableFile = directoryForSortedFiles.resolve(fileName);
        }
        if (typeOfData.equals(TypeOfData.INTEGER)) {
            String fileName = namePrefix.append("integers.txt").toString();
            pathToCreatableFile = directoryForSortedFiles.resolve(fileName);
        }
        if (typeOfData.equals(TypeOfData.FLOAT)) {
            String fileName = namePrefix.append("floats.txt").toString();
            pathToCreatableFile = directoryForSortedFiles.resolve(fileName);
        }

        try (FileWriter writer = new FileWriter(pathToCreatableFile.toString(), true)) {
            writer.write(line + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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



