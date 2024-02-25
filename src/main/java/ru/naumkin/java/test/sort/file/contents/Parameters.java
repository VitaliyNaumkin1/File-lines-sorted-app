package ru.naumkin.java.test.sort.file.contents;

public enum Parameters {
    A(false, "-a", ""),
    O(false, "-o", ""),
    P(false, "-p", ""),
    S(false, "-s", ""),
    F(false, "-f", ""),
    NO_PARAMETERS(false, "", "");
    private boolean isExist;
    private final String stringVersion;
    private String content;


    public void setExist(boolean exist) {
        isExist = exist;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isExist() {
        return isExist;
    }

    public String getStringVersion() {
        return stringVersion;
    }

    Parameters(boolean isExist, String stringVersion, String content) {
        this.isExist = isExist;
        this.stringVersion = stringVersion;
        this.content = content;
    }
}
