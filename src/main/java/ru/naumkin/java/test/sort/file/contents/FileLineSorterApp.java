package ru.naumkin.java.test.sort.file.contents;

import org.w3c.dom.ls.LSOutput;

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

    private File defaultDirForSortedFiles;
    private File dirForSortedFiles;


    public FileLineSorterApp(String[] args) {
        this.rawInputUserParameters = Arrays.asList(args);
        this.inputFiles = new ArrayList<>();
        this.defaultDirForSortedFiles = Path.of("input files\\").toFile();
        this.dirForSortedFiles = defaultDirForSortedFiles;
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
            getFilesSpecifiedByUser();
        } catch (InvalidPathException e) {
            e.printStackTrace();
            System.err.println("Не верный путь к файлу");
            //
        }
        printChoosingFilesForSort();
        setParameters();
        setPathForSortedFiles();

        //&&&&&77 СЛИШКОМ ЛИНЕЙНЫЙ КОД.
    }

    public void getFilesSpecifiedByUser() throws InvalidPathException {
//        int countFiles = 0;
        for (String parameter : rawInputUserParameters) {
            if (parameter.endsWith(".txt")) {
//                Path path = Path.of("input files\\").resolve(parameter).toAbsolutePath();
                File file = Path.of("input files\\").resolve(parameter).toFile();
                inputFiles.add(file);
//                System.out.println(path);
//                inputFiles.add()
//                countFiles++;
            }
//            System.out.println("Вы не указали входящие файлы/файл");
            //какие то файлы не существуют если введены просто так.
        }

        if (inputFiles.isEmpty()) {
            return;
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
                            return;
                        }
                        break;
                    }
                    break;
                }
                //повторно могут ввести тот же файл.
//c:\1\2\2.1.txt
            }
        }
    }

//    public boolean isInputFileExist(File userInputFile) {
//        if (userInputFile.isDirectory()) {
//            System.out.println("Вы ввели путь к директории, а не к файлу");
//            return false;
//        }
//        return userInputFile.exists();
//    }

    public boolean defaultDirIsEmpty() {
        File[] dir = new File("input files\\").listFiles();//defaultDir прописать.
        if (dir.length == 0) {
            System.out.println("В стандартной директории нету файлов, укажите полный путь к своим файлам содержимое которых хотите разделить");/////?????
            return true;
        }
        return false;
    }

    public void printInputFilesFromDefaultDir() {
        File[] dir = new File("input files\\").listFiles();
        if (dir.length == 0) {
            System.out.println("В стандартной директории нету файлов, укажите полный путь к своим файлам содержимое которых хотите разделить");/////?????
        }
        System.out.println("printInputFilesFromDefaultDir()");/////
        System.out.println("Список файлов из стандартной директории:");
        for (File file : dir) {
            System.out.println(file.getName());
        }
    }

    public boolean isParameterExist(String parameter) {
        for (String inputParameter : rawInputUserParameters) {
            if (inputParameter.equals(parameter)) {
                return true;
            }
        }
        return false;
    }


    //nio or io file?
    public List<String> getLinesFromFile(File file) {
        List<String> fileStrings = new ArrayList<>();

        return fileStrings;
    }

    public void printChoosingFilesForSort() {
        System.out.println("Выбранные файлы для сортировки: ");
        for (File file : inputFiles) {
            System.out.println(file.getName());
        }
    }

    public void userParametersHandler() {
        //ПОКА ЧТО ИСПОЛЬЗУЕМ RAW her

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
                    }else {

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
