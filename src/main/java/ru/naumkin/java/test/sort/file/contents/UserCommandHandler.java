package ru.naumkin.java.test.sort.file.contents;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.naumkin.java.test.sort.file.contents.enums.StatisticMode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static ru.naumkin.java.test.sort.file.contents.FileLineSorterApp.DEFAULT_DIRECTORY;

public class UserCommandHandler {
    private Scanner scanner;
    private final Logger logger = LogManager.getLogger(UserCommandHandler.class.getName());
    private List<String> rawInputUserOptions;
    private List<File> inputFiles;

    private Set<String> options;
    private StatisticMode statisticMode;

    private String namePrefixForSortedFiles;
    private Path directoryForSortedFiles;
    private Path inputFileDirectory;

    public Path getInputFileDirectory() {
        return inputFileDirectory;
    }

    public List<File> getInputFiles() {
        return inputFiles;
    }

    public Set<String> getOptions() {
        return options;
    }

    public Path getDirectoryForSortedFiles() {
        return directoryForSortedFiles;
    }

    public String getNamePrefixForSortedFiles() {
        return namePrefixForSortedFiles;
    }

    public UserCommandHandler(String[] args) {
        this.inputFileDirectory = DEFAULT_DIRECTORY;
        this.rawInputUserOptions = Arrays.asList(args);
        this.scanner = new Scanner(System.in);
        this.inputFiles = new ArrayList<>();
        this.options = new HashSet<>();
        run();
    }

    //java -jar util.jar -s -a -p sample- in1.txt in2.txt
    private void run() {
        getOptionsFromUserInputLine();
        setDirForSortedFiles();
        try {
            getFilesForSorting(); /// наверное зря сюда пробросил обработку
        } catch (InvalidPathException e) {
            logger.error(e);
        }
        getNamePrefixForSortedFilesFromLine();
        printAllInformation();
    }

    /**
     *
     */
    private void printAllInformation() {
        System.out.println("________________________________________________________");
        System.out.println(">>>>>Информация о ввёденных условиях программы<<<<<<");
        printChoosingFilesForSort();
        System.out.println(">Директория для исходящих файлов: " + directoryForSortedFiles.toAbsolutePath());
        System.out.println(">Префикс имени сортированных файлов: " + namePrefixForSortedFiles);
        System.out.println(">Выбрана статистика для файлов : ");
        System.out.println("________________________________________________________");

        for (File inputFile : inputFiles) {
            System.out.println(inputFile.getAbsolutePath());
        }
    }

    private void printChoosingFilesForSort() {
        System.out.println(">Выбранные файлы для сортировки: ");
        for (File file : inputFiles) {
            System.out.println("[" + file.getName() + "]");
        }
    }

    private void getFilesForSorting() throws InvalidPathException {
        if (getInputFilesFromUserLine()) {
            return;  ///нужно проверить не пустые ли файлы для сортировки, есть ли в них что то ..Отдельный метод. Или же впихнуть  askUserEnterFilesForSorting() в конец выше
        }
        askUserEnterFilesForSorting();
    }

    //   C:\1\2\2.1.txt
    private void askUserEnterFilesForSorting() throws InvalidPathException {  //--------RunTimeExeption пробросить и выше словить именно InvalidPathException
        while (true) {
            printInputFilesFromDefaultDir();
            logger.info(">Введите путь к файлу который хотите добавить к сортировке или имя файла из стандартной директории: ");
            String userText = scanner.nextLine();
            Path path = Paths.get(userText);
            if (Files.isDirectory(path)) {
                logger.info("указанный путь ведёт не к файлу");
                continue;
            }

            inputFiles.add(new File(path.toString()));
            logger.info(">Вы добавили файл {} в список для сортировки. ", path.toAbsolutePath());
            while (true) {
                logger.info(">Введите 1 если хотите добавить еще файл, 0 - если хотите начать сортировку?");
                String userNumber = scanner.nextLine();
                if (!userNumber.equals("0") && !userNumber.equals("1")) {
                    continue;
                }
                if (userNumber.equals("0")) {
                    return;
                }
                break;
            }
        }
    }

    //java -jar util.jar -s -a -p sample- in1.txt in2.txt

    /**
     * Здесь может быть такое что могут просто так написать где угодно файл , а не в конце и что это будет тогда значить.
     */
//    private boolean getInputFilesFromUserLine() throws InvalidPathException {
//        int countFiles = 0;
//        for (String option : rawInputUserOptions) {
//            if (option.endsWith(".txt")) {
//                Path path = inputFileDirectory.resolve(option);
//                if (!Files.isDirectory(path)) {
//                    inputFiles.add(new File(path.toString()));///может быть повторное добавление одно и того же файла?
//                    countFiles++;
//                    continue;
//                }
//                logger.info("{} не является файлом ", path.toAbsolutePath()); /// тут щее надо подумать что может быть итддддд
//            }
//        }
//        return countFiles != 0;
//    }

    /**
     * Надо подумать над этоим как правильно тут выцепить файлы логику продумать.
     */
    private boolean getInputFilesFromUserLine() throws InvalidPathException {
        int countFiles = 0;
        for (String option : rawInputUserOptions) {
            if (!option.endsWith(".txt")) {
                continue;
            }
            //если указано только имя файла(int.txt), проверяем есть ли он в текущей папке проекта: input files
            Path pathFileInDefaultDir = inputFileDirectory.resolve(option);
            if (Files.exists(pathFileInDefaultDir)) {
                inputFiles.add(new File(pathFileInDefaultDir.toString()));          ////&&&&&
                countFiles++;
                continue;
            }
            Path path = Path.of(option);
            if (option.startsWith("C:\\")) {
                if (!Files.isDirectory(path)) {
                    inputFiles.add(new File(path.toString()));///может быть повторное добавление одно и того же файла?
                    countFiles++;
                    continue;
                }
                logger.info("{} не является файлом ", path.toAbsolutePath()); /// тут щее надо подумать что может быть итддддд
            }
//            else {
//                logger.info("не возможно определить нахождение файла {}, он не будет добавлен к сортировке ", option);
//            }

            //если указано только имя файла(int.txt), проверяем есть ли он в текущей папке проекта: input files

        }
        return countFiles != 0;
    }


    public void defineInputFileDirectory() {

    }

    private void printInputFilesFromDefaultDir() {
        System.out.println("_______________________________________");
        File[] dir = new File("input files\\").listFiles();
        if (dir.length == 0) {
            System.out.println("В стандартной директории нету файлов, укажите полный путь к своим файлам содержимое которых хотите разделить");/////?????
        }
        System.out.println("Список файлов из стандартной директории:");
        for (File file : dir) {
            System.out.println("[" + file.getName() + "]");
        }
        System.out.println("_______________________________________");
    }

    /**
     * ДОПОЛНЕНИЕ: если указаны две опции для сортировки файлов -s и -f, то выбирается полная сортировка.
     */
    private void getOptionsFromUserInputLine() {
        /**
         * может быть несколько одинаковых параметров в 1 строке.
         */

        int count = 0;
        if (rawInputUserOptions.contains("-a")) {
            options.add("-a");
            count++;
        }

        if (rawInputUserOptions.contains("-p")) {
            options.add("-p");
            count++;
        }

        if (rawInputUserOptions.contains("-s")) {
            options.add("-s");
            statisticMode = StatisticMode.SHORT;
            count++;
        }

        if (rawInputUserOptions.contains("-f")) {
            options.remove("-s"); ///// Оставить или убрать?
            options.add("-f");
            statisticMode = StatisticMode.FULL;
            count++;
        }

        if (rawInputUserOptions.contains("-o")) {
            options.add("-o"); /// тут можно доавбить вызова метода setDirForSortedFiles(); но тогда нужно будет установить дефолтную дирректорию.
            count++;
        }
    }

    //    java -jar util.jar -s -a -o c:\1 -p sample- in1.txt in2.txt       +
//    java -jar util.jar -s -a -o -p sample- in1.txt in2.txt            +
    private void setDirForSortedFiles() throws RuntimeException {
        try {
            if (!Files.exists(DEFAULT_DIRECTORY)) {
                Files.createDirectory(DEFAULT_DIRECTORY); //---------- возможно это надо наверх прокинуть.Тут это не обработать логично.
            }
        } catch (IOException e) {
            logger.error(e); //LOGGER
        }

        if (!options.contains("-o")) {
            directoryForSortedFiles = DEFAULT_DIRECTORY;
            return;
        }

        //если указана опция -о
        for (int i = 0; i < rawInputUserOptions.size(); i++) {
            if (rawInputUserOptions.get(i).equals("-o")) {
                Path path = Paths.get(rawInputUserOptions.get(i + 1));
                if (Files.isDirectory(path)) {
                    directoryForSortedFiles = path;
                    return;
                }
                logger.info(">для опции -o программа не смогла найти указанную директорию либо вы её не указали");
                askUserEnterDirForSortedFiles();
                return;
            }
        }

        /**
         * МОжет быть такая возомжность что указын параметры -o -a
         * или -о C:\\1 -a -o C:\\2\2.1
         */
    }

    private void askUserEnterDirForSortedFiles() {  //--------RunTimeExeption пробросить и выше словить именно InvalidPathException
        while (true) {
            logger.info(">Введите путь для сортированных файлов или {} для выбора стандартной директории: ", "\"/default\"");
            String userText = scanner.nextLine();

            if (userText.equals("/default")) {
                logger.info(">Отсортированные файлы будут храниться в папке: " + DEFAULT_DIRECTORY.toAbsolutePath());
                directoryForSortedFiles = DEFAULT_DIRECTORY;
                return;
            }

            Path path = Paths.get(userText);
            if (Files.isDirectory(path)) {
                directoryForSortedFiles = path;
                logger.info(">Отсортированные файлы будут храниться в папке: " + directoryForSortedFiles.toAbsolutePath());
                return;
            }

            logger.info(">директории {} не существует ", path.toAbsolutePath());

//            try {
////                Files.createDirectories(path);///////// ------------- нужно ли это?
////                logger.info(">Отсортированные файлы будут храниться в папке: " + DEFAULT_DIRECTORY.toAbsolutePath());
//            } catch (IOException | RuntimeException e) {
//                logger.error(e);
//                logger.info(">Не удалось создать папку для отсортированных файлов");
//                askUserEnterDirForSortedFiles(); ///// ------ опасно ли ?
//            }
        }
    }

    //      java -jar util.jar -s -a -o -p sample- in1.txt in2.txt          +
    //      java -jar util.jar -s -a -o -p sample- in1.txt in2.txt          +
    private void getNamePrefixForSortedFilesFromLine() {
        if (!options.contains("-p")) {
            return;
        }
        for (int i = 0; i < rawInputUserOptions.size(); i++) {
            if (!rawInputUserOptions.get(i).equals("-p")) {
                continue;
            }
            //нашли префикс в списке и проверяем не находится ли он на последнем элементе иначе ArrayIndexOutOfBoundsException
            if (i == rawInputUserOptions.size() - 1) {  /// NULLLLL
                return;
            }
            //Пользователь указал опцию -p для указания префикса, но не указал имя префикса, а следующий элемент в водимой строке это названия файлов.
            String prefix = rawInputUserOptions.get(i + 1);
            if (prefix.endsWith(".txt") && !prefix.equals(".txt")) {
                logger.info(">Забыли указать префикс для имени сортированных файлов.");
                askUserEnterNamePrefixForSortedFiles();
                return;
            }
            try {
                Path prefixName = Paths.get(prefix);
                namePrefixForSortedFiles = prefixName.toString();
                return;
            } catch (RuntimeException e) {
                logger.error(e);
                logger.info(">Не допустимый префикс для имени файлов.");
            }
            break;
        }
        askUserEnterNamePrefixForSortedFiles();
    }

    //      java -jar util.jar -s -a -o -p in1.txt in2.txt
    private void askUserEnterNamePrefixForSortedFiles() {
        while (true) {
            logger.info(">Введите префикс для имен сортированных файлов, или введите {} для того что бы имена файлов были без префикса: ", "\"/no\"");
            String userText = scanner.nextLine();
            if (userText.equals("/no")) {
                logger.info(">Имена отсортированных файлов будут без префикса!");
                return;
            }
            try {
                Path prefixName = Paths.get(userText);
                namePrefixForSortedFiles = prefixName.toString();
                return;
            } catch (RuntimeException e) {
                logger.error(e);
                logger.info(">Не допустимый префикс для имени файлов");
            }
        }
    }
}
