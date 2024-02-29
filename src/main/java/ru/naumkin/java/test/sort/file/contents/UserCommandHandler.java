package ru.naumkin.java.test.sort.file.contents;

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
    private final Scanner scanner;
    private final List<String> rawInputUserOptions;
    private final Set<String> options;
    private final List<File> inputFiles;
    private final Path inputFileDirectory;
    private String namePrefixForSortedFiles;
    private Path directoryForSortedFiles;
    private StatisticMode statisticMode;

    public List<File> getInputFiles() {
        return inputFiles;
    }

    public Set<String> getOptions() {
        return options;
    }

    public StatisticMode getStatisticMode() {
        return statisticMode;
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
        this.inputFiles = new LinkedList<>();
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
            e.printStackTrace();
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
        String prefix = (namePrefixForSortedFiles.isEmpty()) ? "без префикса" : namePrefixForSortedFiles;
        System.out.println(">Префикс имени сортированных файлов: " + prefix);
        System.out.println(">Выбрана статистика для файлов: " + statisticMode.getString());
        System.out.println("________________________________________________________");
    }

    private void printChoosingFilesForSort() {
        System.out.println(">Выбранные файлы для сортировки: ");
        for (File file : inputFiles) {
            System.out.printf("[%s] - %s\n", file.getName(), file.getAbsolutePath());
        }
    }

    private void getFilesForSorting() throws InvalidPathException {
        if (getInputFilesFromUserLine()) {
            return;  ///нужно проверить не пустые ли файлы для сортировки, есть ли в них что то ..Отдельный метод. Или же впихнуть  askUserEnterFilesForSorting() в конец выше
        }
        askUserEnterFilesForSorting();
    }


    /**
     * переделать класс. немного коряво работает логика в плане вывода информации
     */
    private void askUserEnterFilesForSorting() throws InvalidPathException {  //--------RunTimeExeption пробросить и выше словить именно InvalidPathException
        while (true) {
            printInputFilesFromDefaultDir();
            if (inputFiles.isEmpty()) {
                System.out.println(">Вы не добавили файлы к сортировке или указанные файлы не найдены.Введите путь к файлу который хотите добавить к сортировке или имя файла из стандартной директории: ");
            }
            String userText = scanner.nextLine();
//            Path path = Paths.get(userText);
//            System.out.println("paht= " + path);
//            if (Files.isDirectory(path)) {
//                logger.info("указанный путь ведёт не к файлу");
//                continue;
//            }

            if (!userText.endsWith(".txt")) {             ///Возможжно это надо вынести в метод т..к уже два раза повторяется
                System.out.printf("Не удалось добавить файл %s. Введите имя файла вместе с его расширением - txt. Например: file1.txt\n", userText);
                continue;
            }
            //если указано только имя файла(int.txt), проверяем есть ли он в текущей папке проекта: input files
            Path pathFileInDefaultDir = inputFileDirectory.resolve(userText);
            if (Files.exists(pathFileInDefaultDir)) {
                System.out.println("Вы добавили файл для сортировки " + pathFileInDefaultDir);
                inputFiles.add(new File(pathFileInDefaultDir.toString()));          ////&&&&&
                continue;
            }
            Path path = Path.of(userText);
            if (userText.startsWith("C:\\")) {
                if (!Files.isDirectory(path)) {
                    inputFiles.add(new File(path.toString()));///может быть повторное добавление одно и того же файла?
                    System.out.println("Вы добавили файл для сортировки: " + path);
                    continue;
                }
                System.out.printf("%s не является файлом \n", path.toAbsolutePath()); /// тут щее надо подумать что может быть итддддд
            }

//            inputFiles.add(new File(path.toString()));
//            logger.info(">Вы добавили файл {} в список для сортировки. ", path.toAbsolutePath());
            while (true) {
                System.out.println(">Введите 1 если хотите добавить еще файл, 0 - если хотите начать сортировку?");
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

    private boolean getInputFilesFromUserLine() throws InvalidPathException {
        int countFiles = 0;
        for (String option : rawInputUserOptions) {
            if (!option.endsWith(".txt")) {
                continue;
            }
            //если указано только имя файла(int.txt), проверяем есть ли он в текущей папке проекта: input files
            Path pathFileInDefaultDir = inputFileDirectory.resolve(option);
            if (Files.exists(pathFileInDefaultDir)) {
                inputFiles.add(new File(pathFileInDefaultDir.toString()));
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
                System.out.printf("%s не является файлом ", path.toAbsolutePath()); /// тут щее надо подумать что может быть итддддд
            }
        }
        return countFiles != 0;
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

    private void getOptionsFromUserInputLine() {

        if (rawInputUserOptions.contains("-a")) {
            options.add("-a");
        }

        if (rawInputUserOptions.contains("-p")) {
            options.add("-p");
        }

        if (rawInputUserOptions.contains("-s")) {
            options.add("-s");
            statisticMode = StatisticMode.SHORT;
        }

        if (rawInputUserOptions.contains("-f")) {
            options.remove("-s");
            options.add("-f");
            statisticMode = StatisticMode.FULL;
        }

        if (rawInputUserOptions.contains("-o")) {
            options.add("-o");
        }
    }

    private void setDirForSortedFiles() throws RuntimeException {
        try {
            if (!Files.exists(DEFAULT_DIRECTORY)) {
                Files.createDirectory(DEFAULT_DIRECTORY);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

                System.out.println(">для опции -o программа не смогла найти указанную директорию либо вы её не указали");
                askUserEnterDirForSortedFiles();
                return;
            }
        }
    }

    private void askUserEnterDirForSortedFiles() {
        while (true) {
            System.out.printf(">Введите путь для сортированных файлов или %s для выбора стандартной директории: ", "\"/default\"");
            String userText = scanner.nextLine();

            if (userText.equals("/default")) {
                System.out.println(">Отсортированные файлы будут храниться в папке: " + DEFAULT_DIRECTORY.toAbsolutePath());
                directoryForSortedFiles = DEFAULT_DIRECTORY;
                return;
            }

            Path path = Paths.get(userText);
            if (Files.isDirectory(path)) {
                directoryForSortedFiles = path;
                System.out.println(">Отсортированные файлы будут храниться в папке: " + directoryForSortedFiles.toAbsolutePath());
                return;
            }

            System.out.printf(">директории %s не существует ", path.toAbsolutePath());
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
                System.out.println("вы не указали префикс для файлов,файлы будут без префикса");
                askUserEnterNamePrefixForSortedFiles();
                return;
            }
            //Пользователь указал опцию -p для указания префикса, но не указал имя для префикса, а следующий элемент в водимой строке это названия файлов.
            String prefix = rawInputUserOptions.get(i + 1);
            if (prefix.endsWith(".txt") && !prefix.equals(".txt")) {
                System.out.println(">Забыли указать префикс для имени сортированных файлов.");
                askUserEnterNamePrefixForSortedFiles();
                return;
            }

            try {
//                Path prefixName = Paths.get(prefix);
//                namePrefixForSortedFiles = prefixName.toString();
                namePrefixForSortedFiles = prefix;
                return;
            } catch (RuntimeException e) {
                e.printStackTrace();
                System.out.println(">Не допустимый префикс для имени файлов.");
            }
            break;
        }
        askUserEnterNamePrefixForSortedFiles();
    }

    private void askUserEnterNamePrefixForSortedFiles() {
        while (true) {
            System.out.printf(">Введите префикс для имен сортированных файлов, или введите %s для того что бы имена файлов были без префикса: \n", "\"/no\"");
            String userText = scanner.nextLine();
            if (userText.equals("/no")) {
                namePrefixForSortedFiles = "";
                System.out.println(">Имена отсортированных файлов будут без префикса!");
                return;
            }
            try {
                Path prefixName = Paths.get(userText);
                namePrefixForSortedFiles = prefixName.toString();
                return;
            } catch (RuntimeException e) {
                e.printStackTrace();
                System.out.println(">Не допустимый префикс для имени файлов, файлы будут без префикса");
                namePrefixForSortedFiles = "";
            }
        }
    }
}
