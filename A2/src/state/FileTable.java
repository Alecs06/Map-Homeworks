package state;

import java.io.BufferedReader;

public interface FileTable {
    boolean isOpened(String fileName);

    void add(String fileName, BufferedReader bufferedReader);

    BufferedReader getFile(String fileName);

    void close(String fileName);
}
