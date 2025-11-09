package state;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapFileTable implements FileTable{
    private final Map<String, BufferedReader> fileTable;

    public MapFileTable() {
        this.fileTable = new HashMap<>();
    }

    @Override
    public boolean isOpened(String fileName) {
        return  fileTable.containsKey(fileName);
    }

    @Override
    public void add(String fileName, BufferedReader bufferedReader) {
        fileTable.put(fileName, bufferedReader);
    }

    @Override
    public BufferedReader getFile(String fileName) {
        return fileTable.get(fileName);
    }

    @Override
    public void close(String fileName) {
        try {
            fileTable.remove(fileName).close();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
