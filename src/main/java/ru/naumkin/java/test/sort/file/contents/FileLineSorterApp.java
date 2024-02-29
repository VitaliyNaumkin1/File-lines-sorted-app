package ru.naumkin.java.test.sort.file.contents;

import ru.naumkin.java.test.sort.file.contents.enums.StatisticMode;
import ru.naumkin.java.test.sort.file.contents.statisticrecorder.ShortStatisticRecorder;
import ru.naumkin.java.test.sort.file.contents.statisticrecorder.FullStatisticRecorder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * ///////////////////////Сменить имя на ProcessManger , a Main   FileLinesSorterApp
 */
public class FileLineSorterApp {

    static final Path DEFAULT_DIRECTORY = Paths.get("input files\\");

    FileContentSorter fileContentSorter;
    private final UserCommandHandler userCommandHandler;


    public FileLineSorterApp(String[] args) {

        this.userCommandHandler = new UserCommandHandler(args);
        this.fileContentSorter = new FileContentSorter(userCommandHandler);
    }

    //java -jar util.jar -s -a -p sample- in1.txt in2.txt


    /**
     * ДОБАВИТЬ ЛОГГЕР И ВКЛЮЧТИЬ В ЭТО В СПИСОК
     *
     * Возможно все таки тут основные поля инициализировать и дальше передавать их, а не передавть в контруктор друг друга объекты
     */


    /**
     * 1)ввели строку выше , что нам нужно впервую очередь?
     * - проверить есть ли файлы , если нету то надо попросить ввести пользователя файлы.
     * <p>
     * 2)Надо отчисттиь параметры от лишнего
     * <p>
     * 3)задать путь для файлов исходящих.
     */
    public void start() {
        List<ShortStatisticRecorder> shortStatisticRecorderList = fileContentSorter.getShortStatisticRecorderList();
        List<FullStatisticRecorder> fullStatisticRecorderList = fileContentSorter.getFullStatisticRecorderList();
        StatisticMode statisticMode = fileContentSorter.getStatisticMode();
        if (statisticMode.equals(StatisticMode.SHORT)) {
            System.out.println("Короткая статистика: ");
            System.out.println(shortStatisticRecorderList.size());
            for (ShortStatisticRecorder recorder : shortStatisticRecorderList) {
                System.out.println( recorder.toString());
            }
        }else {
            System.out.println("Подробная статистика статистика: ");
            for (FullStatisticRecorder recorder : fullStatisticRecorderList) {
                System.out.println(recorder.toString());
            }
        }
    }
}