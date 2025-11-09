package model.exception;

public class FileAlreadyOpenException extends RuntimeException {
    public FileAlreadyOpenException() {
        super("File already open");
    }
}
