package ru.naumkin.java.test.sort.file.contents.files;

import ru.naumkin.java.test.sort.file.contents.files.FileContentReader;
import ru.naumkin.java.test.sort.file.contents.files.FileContentWriter;

import java.util.ArrayList;
import java.util.List;

public class FilesHandler {

    FileContentReader fileContentReader;
    FileContentWriter fileContentWriter;
    private List<String> listWithStrings;
    private List<Long> listWithLongs;
    private List<Double> listWithDoubles;

    public FilesHandler() {
        this.fileContentReader = new FileContentReader();
        this.fileContentWriter = new FileContentWriter();
        this.listWithStrings = new ArrayList<>();
        this.listWithLongs = new ArrayList<>();
        this.listWithDoubles = new ArrayList<>();
    }
}
