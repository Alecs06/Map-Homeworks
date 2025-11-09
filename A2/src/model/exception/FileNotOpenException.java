package model.exception;

public class FileNotOpenException extends RuntimeException {
    public FileNotOpenException() {
        super("File not open");
    }
}
