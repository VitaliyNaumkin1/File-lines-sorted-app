package ru.naumkin.java.test.sort.file.contents;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 * ///////////////////////Сменить имя на ProcessManger , a Main   FileLinesSorterApp
 */
public class FileLineSorterApp {
    //        private Map<String, Boolean> parameters;
    Scanner scanner = new Scanner(System.in);
    //    private List<String> rawInputUserParameters;
    private List<String> rawInputUserParameters;
    private List<String> userParameters;
    private List<File> inputFiles;

    private final Logger logger = LogManager.getLogger(FileLineSorterApp.class.getName());

    static final Path DEFAULT_DIRECTORY = Paths.get("default directory");

    private File defaultDirForSortedFiles;
    private File dirForSortedFiles;
    FileContentSorter fileContentSorter;
    private UserCommandHandler userCommandHandler;


    public FileLineSorterApp(String[] args) {
        this.rawInputUserParameters = Arrays.asList(args);
        this.inputFiles = new ArrayList<>();
        this.defaultDirForSortedFiles = Path.of("input files\\").toFile();
        this.dirForSortedFiles = defaultDirForSortedFiles;
        this.userCommandHandler = new UserCommandHandler(args);
        this.fileContentSorter = new FileContentSorter();
    }

    //java -jar util.jar -s -a -p sample- in1.txt in2.txt


    /**
     * ДОБАВИТЬ ЛОГГЕР И ВКЛЮЧТИЬ В ЭТО В СПИСОК
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
        try {
//            userCommandHandler.getFilesForSorting(); /// обработку кинуть в юзер хендлер
            userCommandHandler.run();
        } catch (InvalidPathException e) {
            e.printStackTrace();
            System.err.println("Не верный путь к файлу");
            //
        }
        fileContentSorter.run();


//        userCommandHandler.printChoosingFilesForSort();
//
//        logger.info("Желаете добавить файлы к сортировке?");
//        userCommandHandler.getOptionsFromUserInputLine();
//        setPathForSortedFiles();


//    public boolean isValidPath(String path) {
//        try {
//            //Это не файл
//            if (new File(path).isFile()) {
//                System.out.println("Это файл");
//                return false;
//            }
//
//            Paths.get(path);
//        } catch (InvalidPathException | NullPointerException ex) {
//            return false;
//        }
//        return true;
//    }

    }
}
