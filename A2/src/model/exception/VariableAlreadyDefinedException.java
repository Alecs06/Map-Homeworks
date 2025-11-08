package model.exception;

public class VariableAlreadyDefinedException extends RuntimeException {
    public VariableAlreadyDefinedException() {
        super("Variable already defined");
    }
}
