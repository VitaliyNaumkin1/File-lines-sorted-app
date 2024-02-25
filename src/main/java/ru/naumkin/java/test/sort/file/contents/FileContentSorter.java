package ru.naumkin.java.test.sort.file.contents;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FileContentSorter {

    private List<String> listWithSortedStrings;
    private List<Long> listWithSortedLongs;
    private List<Double> listWithSortedDoubles;



    /**
     * 1)Статистику собирать. Либо класс отдельный который собирает статистику , либо просто переменные.
     * 2)-s , -
     */

    public void run() {

    }

    public void readLinesFromFile() {
        try {
            Path path = Paths.get("input files\\").resolve("in1.txt");
            String content = Files.readString(path);
            String[] elements = content.split("[\\r\\n]+");

            System.out.println(Arrays.toString(elements));
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
