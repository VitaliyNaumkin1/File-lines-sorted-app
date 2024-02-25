package ru.naumkin.java.test.sort.file.contents;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Test {
    private static final Logger logger = LogManager.getLogger(Test.class.getName());

    public static void main(String[] args) throws IOException {
//        test1();
//        test2();
//        test3();
//        testPath();
//        testPath2();
//        exceptionsTest();
//        catchExcep();
//        test4();
        Path path = Paths.get("C:\\Users\\User\\Desktop").resolve("fsdfsdffile.txt");
        System.out.println("path = " + path);
        System.out.println(Files.isDirectory(path));
        logger.info("указанный файл {} является дирректорией", "hello");
        logger.info("указанный файл {} не является файлом ", path.toAbsolutePath());
    }

    private static void test4() {
        Path path = Paths.get("input files").resolve("default input files").resolve("fi22.txt");
        System.out.println(!Files.isDirectory(path));
        System.out.println(path);
        System.out.println(path.toString().endsWith(".txt"));
    }

    private static void catchExcep() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String stringPath = scanner.nextLine();

        try {
            Path path = Paths.get(stringPath);
            Files.createDirectories(path);
        } catch (RuntimeException e) {
            logger.error(e);
            catchExcep();
        }

        System.out.println("eeeeeeee");
    }

    private static void exceptionsTest() {
        try {
            throw new InvalidPathException("yefdssf", "sdfsdfsdf");
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("catch");
        }
        System.out.println("yes");
    }

    private static void testPath2() throws IOException {
        Path path1 = Paths.get("c:\\1\\2\\2.1.txt");
        System.out.println(Files.isDirectory(path1));
        Path path = Paths.get("\\\\\\HOOOOOOO").resolve("yeeeeess");
        Files.createDirectories(path);
    }

    private static void testPath() throws IOException {
        final String DEFAULT_DIRECTORY = "input files\\Hello";
        Path path = Paths.get("input files\\hello");
        System.out.println("path " + path);
        System.out.println("abs " + path.toAbsolutePath());
        System.out.println("path.t " + Paths.get(path.toString()));
        System.out.println(" is dir = " + Files.isDirectory(path));
        File file = new File(DEFAULT_DIRECTORY);
        Files.createDirectories(path);
    }

    private static void test3() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Хотите добавить еще файл который хотите отсортировать?\n" +
                    "Если да введите цифру - 1 ,если нет введите цифру - 0 ");
            int userChoice;
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

                System.out.println("Вы добавили файл");
                break;
            }
        }
    }

    private static void test2() {
        while (true) {
            System.out.println(1);
            int count = 0;
            while (true) {
                System.out.println("count = " + count);
                if (count == 4) {
                    return;
                }
                count++;
                System.out.println(2);
            }
        }
    }


    private static void test1() {
        String stringPath = "C:\\1\\2";
//        File file = new File();
        Path pathForSortedFiles = Paths.get(stringPath);
        System.out.println(pathForSortedFiles);
    }
}
