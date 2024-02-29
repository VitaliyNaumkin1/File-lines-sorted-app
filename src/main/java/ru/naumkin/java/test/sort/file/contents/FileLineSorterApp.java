package ru.naumkin.java.test.sort.file.contents;

import ru.naumkin.java.test.sort.file.contents.enums.StatisticMode;
import ru.naumkin.java.test.sort.file.contents.statisticrecorder.AbstractStatisticRecorder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileLineSorterApp {

    static final Path DEFAULT_DIRECTORY = Paths.get("input files\\");

    FileContentSorter fileContentSorter;
    private final UserCommandHandler userCommandHandler;

    public FileLineSorterApp(String[] args) {
        this.userCommandHandler = new UserCommandHandler(args);
        this.fileContentSorter = new FileContentSorter(userCommandHandler);
    }

    public void start() {
        if (userCommandHandler.getStatisticMode().equals(StatisticMode.SHORT)) {
            System.out.println("Короткая информация о сортированных файлах: ");
        } else {
            System.out.println("Полная информация о сортированных файлах: ");
        }
        List<AbstractStatisticRecorder> recorders = fileContentSorter.getAbstractStatisticRecorderList();
        for (AbstractStatisticRecorder recorder : recorders) {
            System.out.println(recorder.toString());
        }
    }
}