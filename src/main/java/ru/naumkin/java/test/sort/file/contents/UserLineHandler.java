package ru.naumkin.java.test.sort.file.contents;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserLineHandler {
    private List<String> rawInputUserParameters;
    private Scanner scanner;
    private List<File> inputFiles;

    private Parameters parameters = Parameters.P ; //В класс лучше с наследниками сделать?


    public UserLineHandler(List<String> rawInputUserParameters) {
        this.rawInputUserParameters = rawInputUserParameters;
        this.scanner = new Scanner(System.in);
        this.inputFiles = new ArrayList<>();
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

        if (!inputFiles.isEmpty()) {
            return;
        }

        System.out.println("Вы не указали  файлы, содержимое которых будет сортироваться. Введите название файла из списка ниже или укажите полный путь к файлу");
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
                while (true) {
                    System.out.println("Хотите добавить еще файл который хотите отсортировать?\n" +
                            "Если да введите цифру - 1 ,если нет введите цифру - 0 ");
                    int choice;
                    do {
                        choice = scanner.nextInt();
                        System.out.println(1);
                    } while (choice != 0 && choice != 1); ///////////////////////Пофиксить это дерьмо.
                }
            }
        }
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

}
