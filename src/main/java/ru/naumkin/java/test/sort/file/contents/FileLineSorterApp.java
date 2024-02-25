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
    private UserCommandHandler userCommandHandler;


    public FileLineSorterApp(String[] args) {
        this.rawInputUserParameters = Arrays.asList(args);
        this.inputFiles = new ArrayList<>();
        this.defaultDirForSortedFiles = Path.of("input files\\").toFile();
        this.dirForSortedFiles = defaultDirForSortedFiles;
        this.userCommandHandler = new UserCommandHandler(args);
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
            userCommandHandler.start();
        } catch (InvalidPathException e) {
            e.printStackTrace();
            System.err.println("Не верный путь к файлу");
            //
        }
//        userCommandHandler.printChoosingFilesForSort();
//
//        logger.info("Желаете добавить файлы к сортировке?");
//        userCommandHandler.getOptionsFromUserInputLine();
//        setPathForSortedFiles();




        //&&&&&77 СЛИШКОМ ЛИНЕЙНЫЙ КОД.
    }

    public void printChoosingFilesForSort() {
        System.out.println("Выбранные файлы для сортировки: ");
        for (File file : inputFiles) {
            System.out.println(file.getName());
        }
    }

    //Заменить
    public void setParameters() {
        int count = 0;
        if (rawInputUserParameters.contains(Parameters.A.getStringVersion())) {
            Parameters.A.setExist(true);
            count++;
        }
        if (rawInputUserParameters.contains(Parameters.O.getStringVersion())) {
            Parameters.O.setExist(true);

            count++;
        }

        if (rawInputUserParameters.contains(Parameters.P.getStringVersion())) {
            Parameters.P.setExist(true);
            count++;
        }

        if (rawInputUserParameters.contains(Parameters.S.getStringVersion())) {
            Parameters.S.setExist(true);
            count++;
        }

        if (rawInputUserParameters.contains(Parameters.F.getStringVersion())) {
            Parameters.F.setExist(true);
            count++;
        }

        if (count == 0) {
            Parameters.NO_PARAMETERS.setExist(true);
        }
    }


    public boolean isValidPath(String path) {
        try {
            //Это не файл
            if (new File(path).isFile()) {
                System.out.println("Это файл");
                return false;
            }

            Paths.get(path);
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
        return true;
    }

    public void setPathForSortedFiles() {
        File pathForSortedFiles = null; //**************************************************
//        if (Parameters.NO_PARAMETERS.isExist()) {
//            dirForSortedFiles = defaultDirForSortedFiles; //// возможно лишнее.
//        }

        //Идея харнить состояние переменой в строке.
        if (Parameters.O.isExist()) {
            for (int i = 0; i < rawInputUserParameters.size(); i++) {
                if (Parameters.O.getStringVersion().equals(rawInputUserParameters.get(i))) {
                    pathForSortedFiles = new File(rawInputUserParameters.get(i + 1));
                    if (isValidPath(pathForSortedFiles.getPath())) {
//                        File pathForSortedFiles = Paths.get(rawInputUserParameters.get(i)).toFile();////////
                        dirForSortedFiles = pathForSortedFiles;
                        return;
                    } else {

                    }


                    /**
                     * ТУТ НАДО ВВЕСТИ ПРОВЕРКУ ВАЛИДНОСТИ ПУТИ
                     * ЕСЛИ НЕТ ТО
                     */
                }
            }

            //Если не указали путь при параметре 0-
            /**
             * ///////////////////////////////////////////////// добавить зацикленность что бы проверять что файл точно есть. и проверки
             */
            System.out.println("Не возможно записать файлы ");

        }

        //Если параметр -o не указан, возвращаем стандартную директорию
        dirForSortedFiles = defaultDirForSortedFiles;
    }

}
