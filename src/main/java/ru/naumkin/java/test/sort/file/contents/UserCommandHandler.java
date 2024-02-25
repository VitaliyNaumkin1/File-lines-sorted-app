package ru.naumkin.java.test.sort.file.contents;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static ru.naumkin.java.test.sort.file.contents.FileLineSorterApp.DEFAULT_DIRECTORY;

public class UserCommandHandler {
    private final Logger logger = LogManager.getLogger(UserCommandHandler.class.getName());
    private List<String> rawInputUserOptions;
    private Scanner scanner;
    private List<File> inputFiles;

    private Set<String> options;

    private String namePrefixForSortedFiles;
    private Path directoryForSortedFiles;

    public UserCommandHandler(String[] args) {
        this.rawInputUserOptions = Arrays.asList(args);
        this.scanner = new Scanner(System.in);
        this.inputFiles = new ArrayList<>();
        this.options = new HashSet<>();
    }

    //java -jar util.jar -s -a -p sample- in1.txt in2.txt
    public void run() {
        getOptionsFromUserInputLine();
        setDirForSortedFiles();
        try {
            getFilesForSorting(); /// наверное зря сюда пробросил обработку
        } catch (InvalidPathException e) {
            logger.error(e);
        }
        getNamePrefixForSortedFiles();
        printAllInformation();
    }

    public void printAllInformation() {
        System.out.println("________________________________________________________");
        System.out.println(">>>>>Информация о ввёденных условиях программы<<<<<<");
        printChoosingFilesForSort();
        System.out.println(">Директория для исходящих файлов: " + directoryForSortedFiles.toAbsolutePath());
        System.out.println(">Префикс имени сортированных файлов: " + namePrefixForSortedFiles);
        System.out.println("________________________________________________________");
    }

    public void printChoosingFilesForSort() {
        System.out.println(">Выбранные файлы для сортировки: ");
        for (File file : inputFiles) {
            System.out.println("[" + file.getName() + "]");
        }
    }

    public void getFilesForSorting() throws InvalidPathException {
        if (getInputFilesFromUserLine()) {
            return;  ///нужно проверить не пустые ли файлы для сортировки, есть ли в них что то ..Отдельный метод. Или же впихнуть  askUserEnterFilesForSorting() в конец выше
        }
        askUserEnterFilesForSorting();
    }

    //   C:\1\2\2.1.txt
    public void askUserEnterFilesForSorting() throws InvalidPathException {  //--------RunTimeExeption пробросить и выше словить именно InvalidPathException
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
    public boolean getInputFilesFromUserLine() throws InvalidPathException {
        int countFiles = 0;
        for (String option : rawInputUserOptions) {
            if (option.endsWith(".txt")) {
                Path path = Paths.get(option);
                if (!Files.isDirectory(path)) {
                    inputFiles.add(new File(path.toString()));///может быть повторное добавление одно и того же файла?
                    countFiles++;
                    continue;
                }
                logger.info("{} не является файлом ", path.toAbsolutePath()); /// тут щее надо подумать что может быть итддддд
            }
        }
        return countFiles != 0;
    }

    public void printInputFilesFromDefaultDir() {
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

    public void getOptionsFromUserInputLine() {
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
            count++;
        }

        if (rawInputUserOptions.contains("-f")) {
            options.add("-f");
            count++;
        }

        if (rawInputUserOptions.contains("-o")) {
            options.add("-o");
            count++;
        }
    }

    //    java -jar util.jar -s -a -o c:\1 -p sample- in1.txt in2.txt       +
//    java -jar util.jar -s -a -o -p sample- in1.txt in2.txt            +
    public void setDirForSortedFiles() throws RuntimeException {
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

    public void askUserEnterDirForSortedFiles() {  //--------RunTimeExeption пробросить и выше словить именно InvalidPathException
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
    public void getNamePrefixForSortedFiles() {
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
    public void askUserEnterNamePrefixForSortedFiles() {
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
