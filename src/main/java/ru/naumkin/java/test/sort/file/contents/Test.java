package ru.naumkin.java.test.sort.file.contents;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
//        test1();

//        test2();
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
