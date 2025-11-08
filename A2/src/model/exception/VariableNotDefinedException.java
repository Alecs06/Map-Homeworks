package model.exception;

public class VariableNotDefinedException extends RuntimeException {
    public VariableNotDefinedException() {
        super("Variable not defined");
    }
}
