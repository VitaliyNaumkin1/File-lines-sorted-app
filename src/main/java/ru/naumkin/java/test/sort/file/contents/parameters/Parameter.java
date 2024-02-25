package ru.naumkin.java.test.sort.file.contents.parameters;

public class Parameter {
    private String name;
    private String content;

    public Parameter(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public Parameter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
