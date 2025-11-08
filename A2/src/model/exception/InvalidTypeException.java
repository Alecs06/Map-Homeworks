package model.exception;

public class InvalidTypeException extends RuntimeException {
    public InvalidTypeException() {
        super("Invalid type");
    }
}
