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
//    private Map<String,String> parameters;

    private File defaultDirForSortedFiles;
    private Path directoryForSortedFiles;

//    private Parameters parameters2 = Parameters.P; //В класс лучше с наследниками сделать?

    public UserCommandHandler(String[] args) {
        this.rawInputUserOptions = Arrays.asList(args);
        this.scanner = new Scanner(System.in);
        this.inputFiles = new ArrayList<>();
        this.options = new HashSet<>();
//        dirForSortedFiles = Path.of("input files\\");
    }

    //java -jar util.jar -s -a -p sample- in1.txt in2.txt
    public void start() {
        getOptionsFromUserInputLine();
        setDirForSortedFiles();
        getFilesForSorting();
        printAllInformation();
    }

    public void printAllInformation() {
        System.out.println("________________________________________________________");
        System.out.println(">>>>>Информация о ввёденных условиях программы<<<<<<");
        printChoosingFilesForSort();
        System.out.println(">Директория для исходящих файлов: " + directoryForSortedFiles.toAbsolutePath());
        System.out.println("________________________________________________________");
    }

    public void printChoosingFilesForSort() {
        System.out.println(">Выбранные файлы для сортировки: ");
        for (File file : inputFiles) {
            System.out.println("[" + file.getName() + "]");
        }
    }

    public void getFilesForSorting() {
        if (getInputFilesFromUserLine()) {
            return;  ///нужно проверить не пустые ли файлы для сортировки, есть ли в них что то ..Отдельный метод. Или же впихнуть  askUserEnterFilesForSorting() в конец выше
        }
        askUserEnterFilesForSorting();
    }

    //   C:\1\2\2.1.txt
    public void askUserEnterFilesForSorting() {  //--------RunTimeExeption пробросить и выше словить именно InvalidPathException
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

    public boolean getInputFilesFromUserLine2() throws InvalidPathException {
        for (String option : rawInputUserOptions) {
            if (option.endsWith(".txt")) {
//                Path path = Path.of("input files\\").resolve(option).toAbsolutePath();
                File file = Path.of("input files\\").resolve(option).toFile();
                inputFiles.add(file);
                return true;

            }

            //какие то файлы не существуют если введены просто так.
        }

        if (inputFiles.isEmpty()) {
//            return;
        }

        System.out.println("Вы не указали  файлы, содержимое которых будет сортироваться. Введите название файла из списка ниже или укажите полный путь к файлу");//logger
        printInputFilesFromDefaultDir();
        if (defaultDirIsEmpty()) {
            while (true) {
                System.out.println("Введите полный путь к файлу: ");
                String userFilePath = scanner.nextLine();
                File userInputFile = Path.of(userFilePath).toFile();

                if (!userInputFile.exists()) {
                    System.out.println("такого файла не существует");
                    continue;
                }

                if (!userInputFile.isFile()) {
                    System.out.println("Укажите путь к файлу,а не к директории ");
                    continue;
                }

                System.out.println("Вы добавили файл: " + userInputFile.getName());
                inputFiles.add(userInputFile);

                int userChoice;
                while (true) {
                    System.out.println("Хотите добавить еще файл который хотите отсортировать?\n" +
                            "Если да введите цифру - 1 ,если нет введите цифру - 0 ");
                    while (true) {
                        System.out.println("Введите цифру:");
                        userChoice = scanner.nextInt();
                        if (userChoice != 1 && userChoice != 0) {
                            System.out.println("Не правильный ввод, должна быть цифра 1 или 0");
                            continue;
                        }
                        if (userChoice == 0) {
//                            return;//////////////////////////////////////////////////
                        }
                        break;
                    }
                    break;
                }
                //повторно могут ввести тот же файл.
                //c:\1\2\2.1.txt
            }
        }
        return false;/////////////////////////////////////////////////////
    }

    public boolean defaultDirIsEmpty() {
        File[] dir = new File("input files\\").listFiles();//defaultDir прописать.
        if (dir.length == 0) {
            System.out.println("В стандартной директории нету файлов, укажите полный путь к своим файлам содержимое которых хотите разделить");/////?????
            return true;
        }
        return false;
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

//    java -jar util.jar -s -a -o c:\1 -p sample- in1.txt in2.txt
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
//
                logger.info("для опции -o программа не смогла найти указанную директорию либо вы её не указали");
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
            logger.info("Введите путь для сортированных файлов или введите цифру 1 для выбора стандартной директории: ");
            String userText = scanner.nextLine();

            if (userText.equals("1")) {
                logger.info("Отсортированные файлы будут храниться в папке: " + DEFAULT_DIRECTORY.toAbsolutePath());
                directoryForSortedFiles = DEFAULT_DIRECTORY;
                return;
            }

            Path path = Paths.get(userText);
            if (Files.isDirectory(path)) {
                directoryForSortedFiles = path;
                return;
            }

            try {
                Files.createDirectories(path);
                logger.info("Отсортированные файлы будут храниться в папке: " + DEFAULT_DIRECTORY.toAbsolutePath());
            } catch (IOException | RuntimeException e) {
                logger.error(e);
                logger.info("Не удалось создать папку для отсортированных файлов");
                askUserEnterDirForSortedFiles(); ///// ------ опасно ли ?
            }

        }
    }

}
